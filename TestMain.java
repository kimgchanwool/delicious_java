package javacore.basic;

public class TestMain {


    public static void main(String[] args) {
        
        //다형성

        //객체와 객체간의 관계를 맺기 위해서는 is a, has a

//        달리기선수 민범기 = new 단거리달리기선수();
//        달리기선수 이봉주 = new 장거리달리기선수();
//        SuperControl 이봉주2 = new 장거리달리기선수();
//        달리기선수 김찬울 = new 중거리달리기선수();
//
//        SuperControl 이찬울 = new 높이뛰기선수();
//        supereee(이찬울);
//
//        run(김찬울);

        결제서비스 결제서비스 = new FactoryPattern().결제서비스();
        결제서비스.결제();


    }

    private static void run(달리기선수 달리기선수) {
        달리기선수.run();
    }

    private static void supereee(SuperControl superControl) {
        superControl.존나잘함();
    }
}


/**
 * 클래스를 통한 추상화 //요거는
 * 인터페이스를 통한 추상화
 */



class 높이뛰기선수 implements Control, SuperControl{

    /**
     * 얘는 쓸모없는 run 을 가지게 되요.
     * @param hp
     */
    public void jump(){
        streching();
        System.out.println("높이 뛴다.");
    }

    @Override
    public void 존나잘함() {

    }
}



interface DBConnector {
    void connent();
}


class MysqlConnector implements DBConnector {
    /**
     * 여름에는 mysql을 쓰고 겨울에는 oracle을 쓴다.
     */

    @Override
    public void connent(){
        System.out.println("Mysql DB에 연결되었습니다.");
    }
}


class OracleConnector implements DBConnector {
    /**
     * 여름에는 mysql을 쓰고 겨울에는 oracle을 쓴다.
     */
    @Override
    public void connent(){
        System.out.println("oracle DB에 연결되었습니다.");
    }
}






class 결제서비스  {

    /**
     * 로그인 체크, 권한 수정, 정산
     */

    //강한 결합.
    //DB
    private DBConnector dbConnecter;

    public 결제서비스(DBConnector dbConnecter) { //의존성 주입
        this.dbConnecter = dbConnecter;
    }

    public void 결제(){
        dbConnecter.connent();
        System.out.println("블라블라");
    }

}




interface Control {
    default void streching (){
        System.out.println("스트레칭한다.");
    }
}


interface SuperControl {
    void 존나잘함();
}

class 달리기선수 {

    int hp; //요렇게는 잘 안 써여.

    public 달리기선수(int hp) {
        this.hp = hp;
    }

    void run(){
        System.out.println("달린다.");
    }



}


//인터페이스
class 단거리달리기선수 extends 달리기선수 implements Control{

    public 단거리달리기선수() {
        super(10);
    }


    @Override
    void run() {
        streching();
        System.out.print("빨리 ");
        super.run();
    }


}

class 장거리달리기선수 extends 달리기선수 implements Control,  SuperControl{
    public 장거리달리기선수() {
        super(10);
    }
    @Override
    void run() {
        streching();
        System.out.print("천천히 ");
        super.run();
    }

    @Override
    public void 존나잘함() {

    }
}

class 중거리달리기선수 extends 달리기선수 implements Control{
    public 중거리달리기선수() {
        super(10);
    }

    @Override
    void run() {
        streching();
        System.out.print("적당히 ");
        super.run();
    }

}
