/*
 ***
 * 各种单例模式的构建方法和安全性问题；
 * 包括 饿汉模式，懒汉模式，双重锁懒汉模式，volatile双锁懒汉模式，静态内部类模式
 */
public class SingletonTest {
    private SingletonTest() {
    }

    public static SingletonTest getInstance() {
        return SingleHolder.Instance;
    }

    //静态内部类的单例模式，懒加载，确保线程安全和单例唯一
    private static class SingleHolder {
        private static SingletonTest Instance = new SingletonTest();
    }
}

class Singleton_E {
    private static Singleton_E Instance = new Singleton_E();

    //饿汉单例模式,类初始化时即已经创建对象，线程安全，以空间换时间
    private Singleton_E() {
    }

    public static Singleton_E getInstance() {
        return Instance;
    }
}

class Singleton_L {
    private static Singleton_L Instance = null;

    private Singleton_L() {
    }

    //懒汉单例模式，方法调用后才创建对象，存在风险
    public static Singleton_L getInstance() {
        if (Instance == null) {
            Instance = new Singleton_L();
        }
        return Instance;
    }
}

class Singleton_DoubleCheck {
    private static Singleton_DoubleCheck Instance = null;

    private Singleton_DoubleCheck() {
    }

    //双重锁懒汉模式
    public static Singleton_DoubleCheck getInstance() {
        if (Instance == null) {
            synchronized (Singleton_DoubleCheck.class) {
                if (Instance == null) {
                    Instance = new Singleton_DoubleCheck();
                }
            }
        }
        return Instance;
    }
}

class Singleton_DoubleCheck2 {

    private volatile static Singleton_DoubleCheck2 uniqueInstance;

    //volatile关键字型实现单例双重校验锁，线程安全
    private Singleton_DoubleCheck2() {
    }

    public static Singleton_DoubleCheck2 getUniqueInstance() {
        //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {
            //类对象加锁
            synchronized (Singleton_DoubleCheck2.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton_DoubleCheck2();
                }
            }
        }
        return uniqueInstance;
    }
}