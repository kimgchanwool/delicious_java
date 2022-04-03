package javacore.basic;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestProxy {


    /**
     * 대리인. 외리지날 프록시
     * @param args
     */

    public static void main(String[] args) {

//        Animal whale = new WhaleProxy(new Whale());
//        whale.sound();



        Animal whale = (Animal) Proxy.newProxyInstance(Animal.class.getClassLoader(), new Class[]{Animal.class}, new InvocationHandler() {
            final Animal whale = new Whale();

            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                long startTime = System.nanoTime();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object invoke = method.invoke(whale, args);
                System.out.println(System.nanoTime() - startTime);
                return invoke;
            }
        });

        whale.sound();
        whale.death();


//        Whale whale = new Whale();
//        whale.sound();
//        whale.birthBaby();

        //reflection

    }




    public static class Whale implements Animal{
        //Whale sound
        @Override
        public void sound() {

            System.out.println("우워어엉");
        }

        @Override
        public void birthBaby() {

            System.out.println("애낳기");
        }

        @Override
        public void death() {
            System.out.println("죽음");
        }

    }




    interface Animal {

        void sound();

        void birthBaby();


        void death();

    }


//    public static class WhaleProxy implements Animal{
//
//        private Whale whale;
//
//        public WhaleProxy(Whale whale) {
//            this.whale = whale;
//        }
//
//        @Override
//        public void sound() {
//            System.out.println("proxy start");
//            whale.sound();
//            System.out.println("proxy end");
//        }
//    }




//    public interface Animal {
//
//        void bark();
//    }


//    public static class DogProxy implements Animal {
//        private  dog;
//
//        public DogProxy( dog) {
//            this.dog = dog;
//        }
//
//        @Override
//        public void bark() {
//
//            System.out.println("servlet.proxy object start");
//            dog.bark();
//            System.out.println("servlet.proxy object end");
//
//        }
//    }


}
