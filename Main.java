class Animal {
    public int age;
    public String name;
    //public void bark();
    public Animal (int age, String name) {
        
        this.age = age;
        this.name = name;
    }
}


class Dog extends Animal
{
    // public int age = Animal.age;
    // public String name = Animal.name;
    

    public Dog (int age, String name ){

        // System.out.println("nice to meet you ");
        // System.out.println(age +""+ name);
        super(age, name);
        System.out.println("nice to meet you ");

        // this.age = old;
        // this.name = my;
    }

    public void bark() {
        System.out.println("wal wal");
    }
}

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog(9, "Sammy");
        dog.bark();

    }
}

    // class Cat implements Animal{


    // }

