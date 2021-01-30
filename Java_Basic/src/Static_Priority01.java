/*
 ***
 * 用于比较静态代码块，静态方法，构造方法之间的加载顺序；
 * 一般来说加载顺序满足 静态代码块 > 代码块 > 构造方法
 ***
 */
public class Static_Priority01 {
    static {             //静态代码块
        System.out.println("test static");
        Pmethod();
    }

    PersonA person = new PersonA("Test");   //构造函数调用

    public Static_Priority01() {
        System.out.println("test constructor");
    }       //构造函数

    private static void Pmethod() {
        System.out.println("private print");
    }   //静态方法

    public static void main(String[] args) {
        new MyClass();
    }       //启动器
}

class PersonA {
    static {
        System.out.println("person static");
    }

    public PersonA(String str) {
        System.out.println("person " + str);
    }
}

class MyClass extends Static_Priority01 {
    static {
        System.out.println("myclass static");
    }

    PersonA person = new PersonA("MyClass");

    public MyClass() {
        System.out.println("myclass constructor");
    }
}

