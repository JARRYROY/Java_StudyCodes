/*
 ***
 * 反射机制测试,获取Class对象的四种方法：
 * 1.知道具体类->Class cls = TargetObject.class;
 * 2.知道类路径->Class cls = Class.forName("com.springframework.xxx");
 * 3.拥有对象实例->Object o = new Object(); Class cls = o.getClass();
 * 4.类加载器传入类路径->Class cls = ClassLoader.loadClass("com.springframework.xxx");
 * 通过类加载器获取的Class对象不会进行初始化，不进行包括初始化等一些列步骤，静态块和静态对象不会得到执行；
 * 关于反射机制的应用，可联系Spring框架,JDBC连接，SpringIoc动态加载管理Bean以及类与对象加载过程；
 * 反射机制优缺点：
 * 优点：运行期类型的判断，动态加载类，提高代码灵活度；
 * 缺点：
 * 1.性能瓶颈：反射相当于一系列解释操作，通知 JVM 要做的事情，性能比直接的 java 代码要慢很多；
 * 2.安全问题，让我们可以动态操作改变类的属性同时也增加了类的安全隐患。
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectInvoke {

    public static void main(String[] args) throws Exception {
        Customer customer = new Customer("tom", 20);
        customer.setID(1L);

        //利用反射完成对象拷贝
        ReflectInvoke tester = new ReflectInvoke();
        Customer customer1 = (Customer) tester.ObjectCopy(customer);
        System.out.println(customer1);

        //利用反射完成通过类路径的对象创建
        Customer customer2 = (Customer) tester.ClassCopy("Customer");
        customer2.setName("jetty");
        customer2.setAge(25);
        System.out.println(customer2);

        //通过反射完成类中方法调用
        tester.MethodInvoke("Customer");

        //通过反射方法将泛型放入指定类型集合中（不推荐这样做，将拥有隐形风险
        tester.GenericAdd();
    }

    public Object ObjectCopy(Object object) throws Exception {
        Class<?> classType = object.getClass();         //直接从目标对象获取Class类
        Object objectCopy = classType.getConstructor(new Class[]{}).newInstance();  //调用类中构造函数获取新实例；
        Field[] fields = classType.getDeclaredFields();     //获取类中值对象的对象头和对象类，以Field形式保存
        for (Field field : fields) {
            String name = field.getName();      //获取值对象的名称，如ID,name或age
            String firstLetter = name.substring(0, 1).toUpperCase();    //对应get与set方法需要的名称首字母大写并完成衔接
            //将属性的首字母转换为大写
            String getMethodName = "get" + firstLetter + name.substring(1);     //substring为对字符串的剪切函数
            String setMethodName = "set" + firstLetter + name.substring(1);
            Method getMethod = classType.getMethod(getMethodName, new Class[]{});   //此处形成对值的操作方法,如getID,setID;
            Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
            Object value = getMethod.invoke(object, new Object[]{});    //使用Method.invoke(object)完成对object对象中该方法的调用
            setMethod.invoke(objectCopy, new Object[]{value});      //使用Method.invoke完成方法的调用
        }
        return objectCopy;
    }

    public Object ClassCopy(String path) throws Exception {     //和上一个相同，不同点是使用forname完成类对象的加载创建
        Class<?> classType = Class.forName(path);       //
        Object objectCopy = classType.getConstructor(new Class[]{}).newInstance();
        Field[] fields = classType.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String firstLetter = name.substring(0, 1).toUpperCase();
            //将属性的首字母转换为大写
            String getMethodName = "get" + firstLetter + name.substring(1);
            String setMethodName = "set" + firstLetter + name.substring(1);
            Method getMethod = classType.getMethod(getMethodName, new Class[]{});
            Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});
            Object value = getMethod.invoke(objectCopy, new Object[]{});
            setMethod.invoke(objectCopy, new Object[]{value});
        }
        return objectCopy;
    }

    public void MethodInvoke(String path) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        /**
         * 获取TargetObject类的Class对象并且创建TargetObject类实例
         */
        Class<?> tagetClass = Class.forName(path);
        Customer targetObject = (Customer) tagetClass.getConstructor().newInstance();
        /**
         * 获取所有类中所有定义的方法
         */
        Method[] methods = tagetClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        /**
         * 获取指定方法并调用
         */
        Method publicMethod = tagetClass.getDeclaredMethod("PrintMethod",
                String.class);

        publicMethod.invoke(targetObject, "Method_Invoke_Success");
        /**
         * 获取指定参数并对参数进行修改
         */
        Field field = tagetClass.getDeclaredField("name");
        //为了对类中的参数进行修改我们取消安全检查
        field.setAccessible(true);
        field.set(targetObject, "Field_Set_Success");
        /**
         * 调用 private 方法
         */
        Method privateMethod = tagetClass.getDeclaredMethod("PrintMethod1");
        //为了调用private方法我们取消安全检查
        privateMethod.setAccessible(true);
        privateMethod.invoke(targetObject);
    }

    public void GenericAdd() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Integer> list = new ArrayList<>();     //此处为申请的Integer类型列表
        list.add(12);       //列表指定规则为存放Integer
        //list.add("a");    //此处若不进行注释则报错，不过检
        Class<? extends List> clazz = list.getClass();      //获取list的类，即ArrayList
        Method add = clazz.getDeclaredMethod("add", Object.class);      //直接获取Arraylist类中add方法
        add.invoke(list, "kl");      //利用反射规避安全检测特性，即泛型类型擦除完成list列表元素加入
        System.out.println(list);
        for (Object object : list) {
            System.out.println(object.getClass().getTypeName());
        }
    }
}

class Customer {
    private float ID;
    private String name;
    private int age;

    public Customer() {
        this.ID = 1L;
        this.name = "A";
        this.age = 1;
    }

    public Customer(String name, int age) {
        this();
        this.name = name;
        this.age = age;
    }

    public float getID() {
        return ID;
    }

    public void setID(float ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void PrintMethod(String s) {
        System.out.println(s);
    }

    private void PrintMethod1() {
        System.out.println("PrivateMethod_Invoke_Success");
    }

    @Override
    public String toString() {
        return "Customer{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}