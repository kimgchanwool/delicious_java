#!/usr/bin/env python
# encoding: utf-8
import time
import sys
import threading
import tornado.web
import tornado.ioloop
import tornado.gen
from functools import partial
from tornado.log import app_log as log
from tornado.options import define, options

import os
import re
import tempfile
import uuid
import subprocess
import construct

MPEGTSHeader = construct.Struct(
    "MPEGTSHeader",
    construct.Magic('G'),
    construct.EmbeddedBitStruct(
        construct.Flag('transport_error_indicator'),
        construct.Flag('payload_unit_start'),
        construct.Flag('transport_priority'),
        construct.Bits('pid', 13),
        construct.Bits('scrambling', 2),
        construct.Bits("adaptation_field_control", 2),
        construct.Bits("continuity_counter", 4),
    )
)


define('url', type=str, default="")
define('protocol', default='tcp')
define('port', default=8888, type=int)
define('address', default='127.0.0.1')
define('no_audio', default=False, type=bool)


class HTTPHandler(tornado.web.RequestHandler):
    CLIENTS = set([])
    INFO = []

    def initialize(self):
        self.CLIENTS.add(self)
        self.lock = True
        self.alive = True
        self.initiated = False

    def finish(self, *args, **kwargs):
        self.alive = False
        self.CLIENTS.remove(self)
        return super(HTTPHandler, self).finish(*args, **kwargs)

    def on_connection_close(self, *args, **kwargs):
        self.finish()
        return super(HTTPHandler, self).on_connection_close(*args, **kwargs)

    @tornado.gen.coroutine
    def write_media(self, is_pat, frame):
        if not self.lock and self.alive:
            if self.initiated:
                self.write(frame)
                yield self.flush()
            elif not self.initiated and is_pat:
                self.initiated = True
                self.write(frame)

    @tornado.web.asynchronous
    def get(self):
        codecs = ",".join("{0[codec]}.{0[pid]}".format(i) for i in self.INFO)
        self.set_header('Content-Type', 'video/mp2t;codecs="%s"' % codecs)
        self.flush()
        self.lock = False


def Pusher():
    ioloop = tornado.ioloop.IOLoop.instance()
    while True:
        is_pat, frame = yield
        try:
            for client in HTTPHandler.CLIENTS:
                ioloop.add_callback(partial(client.write_media, is_pat, frame))
        except Exception as e:
            log.exception(e)


if __name__ == '__main__':
    options.parse_command_line()

    stream_parser = re.compile(r'\s*Stream\s#\d+\:(?P<pid>\d+)\:\s\w+\:\s(?P<codec>\S+)')

    def worker():
        env = dict()
        env['PATH'] = os.environ.get('PATH', '')
        env['TERM'] = 'vt100'

        while True:
            cmd = ("avconv", "-rtsp_transport", options.protocol, "-i", str(options.url))
            cmd += ("-c:v", "copy")
            if options.no_audio:
                cmd += ("-map", "0:0")
                cmd += ("-f", "mpegts", '-streamid', '0:0', "-")
            else:
                cmd += ("-map", "0", "-c:a", "copy")
                cmd += ("-f", "mpegts", '-streamid', '0:0', '-streamid', '-muxrate', '100', '1:1', "-")

            process = subprocess.Popen(
                cmd,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                shell=False,
                env=env,
                cwd=tempfile.gettempdir()
            )

            log.info('Capturing running')

            def get_info(process):
                info = ''
                payload = False
                data = process.stderr.readline()
                out = list()

                while data:
                    if "Output " in data:
                        break

                    info += data
                    data = process.stderr.readline()

                buf = ''
                for line in info.split('\n'):
                    if 'Audio' in line and options.no_audio:
                        continue

                    if 'Stream ' in line:
                        m = stream_parser.match(line)
                        if m is not None:
                            out.append(m.groupdict())

                return out

            HTTPHandler.INFO = get_info(process)

            pusher = Pusher()
            pusher.next()
            ioloop = tornado.ioloop.IOLoop.instance()
            retcode = process.poll()
            while retcode is None:
                retcode = process.poll()

                # MPEG-TS has fixed packet size
                chunk = process.stdout.read(188)

                if not chunk:
                    continue

                try:
                    hdr = MPEGTSHeader.parse(chunk[0:5])
                    pusher.send((hdr.pid == 0, chunk))
                except Exception as e:
                    log.error("%r", chunk[0:5])
                    log.exception(e)

            log.info('Capturing stoped')

    t = threading.Thread(target=worker)
    t.daemon = True
    t.start()

    application = tornado.web.Application([
        ("/", HTTPHandler),
    ])

    application.listen(options.port, address=options.address)
    tornado.ioloop.IOLoop.instance().start()