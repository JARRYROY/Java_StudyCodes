/*
 ***
 * 主要为父类与子类中静态代码块，代码块和构造方法等执行顺序；
 * 其执行顺序为  父类静态代码块——>子类静态代码块——>父类代码块——>父类构造方法——>子类代码块——>子类构造方法
 * 可联系Java运行时内存分配和类加载，对象加载过程；
 */
public class Static_Priority02 extends TestTwo {
    static {
        System.out.println("子类静态代码块");
    }

    {
        System.out.println("子类代码块");
    }

    public Static_Priority02() {
        System.out.println("子类构造方法");
    }

    public static void main(String[] args) {
        new Static_Priority02();
    }

}

class TestTwo {
    static {
        System.out.println("父类静态代码块");
    }

    {
        System.out.println("父类代码块");
    }

    public TestTwo() {
        System.out.println("父类构造方法");
    }

    public static void find() {
        System.out.println("静态方法");
    }
}