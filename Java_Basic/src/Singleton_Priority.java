/*
 ***
 * 主要测试饿汉单例模式中方法和代码块的加载顺序过程；
 */
public class Singleton_Priority {
    //优点：饿汉模式天生是线程安全的，使用时没有延迟。
    //缺点：启动时即创建实例，启动慢，有可能造成资源浪费。
    private static Singleton_Priority instance = new Singleton_Priority();

    static {
        System.out.println("instance ok");
    }

    private Singleton_Priority() {
        System.out.println("constructor ok");
    }

    public static Singleton_Priority getInstance() {
        System.out.println("instance return");
        return instance;
    }

    public static void main(String[] args) {
        Singleton_Priority.getInstance();
    }
}
