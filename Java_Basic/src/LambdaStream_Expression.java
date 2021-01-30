/*
 ***
 * 关于JDK8版本中的lambda表达式的部分使用方法总结
 * 包括接口抽象，匿名表达式，匿名内部类，流处理
 * 语法形式为 () -> {}，其中 () 用来描述参数列表，{} 用来描述方法体，-> 为 lambda运算符 ，读作(goes to)；
 * 方法归属者::方法名 静态方法的归属者为类名，普通方法归属者为对象，如=对象类::new;=对象::方法
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
interface NoReturnMultiParam {
    void method(int a, int b);
}

/**
 * 无参无返回值
 */
@FunctionalInterface
interface NoReturnNoParam {
    void method();
}

/**
 * 一个参数无返回
 */
@FunctionalInterface
interface NoReturnOneParam {
    void method(int a);
}

/**
 * 多个参数有返回值
 */
@FunctionalInterface
interface ReturnMultiParam {
    int method(int a, int b);
}

/*** 无参有返回*/
@FunctionalInterface
interface ReturnNoParam {
    int method();
}

/**
 * 一个参数有返回值
 */
@FunctionalInterface
interface ReturnOneParam {
    int method(int a);
}

public class LambdaStream_Expression {
    public static void main(String[] args) {
        new LambdaStream_Expression().lambda01();
        new LambdaStream_Expression().lambda02();
        new LambdaStream_Expression().Stream02_Use();
        new LambdaStream_Expression().Stream02();
        new LambdaStream_Expression().LambdaInterface();
    }

    /**
     * 要求
     * 1.参数数量和类型要与接口中定义的一致
     * 2.返回值类型要与接口中定义的一致
     */
    public static int doubleNum(int a) {
        return a * 2;
    }

    public void lambda01() {
        System.out.println("Lambda01_________Start");
        /**
         *注意：
         *   1.lambda体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致！
         *   2.若lambda参数列表中的第一个参数是实例方法的调用者，而第二个参数是实例方法的参数时，可以使用ClassName::method
         *
         */
        Consumer<Integer> con = (x) -> System.out.println(x);
        con.accept(100);

        // 方法引用-对象::实例方法
        Consumer<Integer> con2 = System.out::println;
        con2.accept(200);

        // 方法引用-类名::静态方法名
        BiFunction<Integer, Integer, Integer> biFun = (x, y) -> Integer.compare(x, y);
        BiFunction<Integer, Integer, Integer> biFun2 = Integer::compare;
        Integer result = biFun2.apply(100, 200);
        System.out.println(result);

        // 方法引用-类名::实例方法名
        BiFunction<String, String, Boolean> fun1 = (str1, str2) -> str1.equals(str2);
        BiFunction<String, String, Boolean> fun2 = String::equals;
        Boolean result2 = fun2.apply("hello", "world");

        System.out.println(result2);
        System.out.println("Lambda01_________Over");
    }

    public void lambda02() {
        System.out.println("Lambda02_________Start");
        // 构造方法引用  类名::new
        Supplier<Employee> sup = () -> new Employee();
        System.out.println(sup.get());
        Supplier<Employee> sup2 = Employee::new;
        System.out.println(sup2.get());

        // 构造方法引用 类名::new （带一个参数）
        Function<String, Employee> fun = (x) -> new Employee(x);
        Function<String, Employee> fun2 = Employee::new;
        System.out.println(fun2.apply("Jerry"));

        //构造方法引用，带两参数的
        BiFunction<String, Integer, Employee> bifun = Employee::new;
        System.out.println(bifun.apply("rock", 31));
        System.out.println("Lambda02_________Over");
    }

    public void Stream01_Create() {
        System.out.println("Stream01_________Start");
        // 1，校验通过Collection 系列集合提供的stream()或者paralleStream()
        List<String> list = new ArrayList<>();
        //获取ArrayList流的方法
        Stream<String> stream1 = list.stream();
        // 2.通过Arrays的静态方法stream()获取数组流
        String[] str = new String[10];
        Stream<String> stream2 = Arrays.stream(str);
        // 3.通过Stream类中的静态方法of
        Stream<String> stream3 = Stream.of("aa", "bb", "cc");
        // 4.创建无限流
        // 迭代
        Stream<Integer> stream4 = Stream.iterate(0, (x) -> x + 2);
        //生成
        Stream.generate(() -> Math.random());
        System.out.println("Stream01_________Over");
    }

    public void Stream02_Use() {
        /**
         * 筛选 过滤  去重
         */
        System.out.println("Stream_Use_________Start");
        List<Employee> emps = new ArrayList<>();
        emps.add(new Employee("tom", 21));
        emps.add(new Employee("jack", 25));
        emps.add(new Employee("betty", 18));
        emps.add(new Employee("cory", 23));
        emps.add(new Employee("franck", 27));
        emps.stream()
                .filter(e -> e.getAge() > 22)   //filter为流的过滤选项，单项择条件过滤
                .limit(4)       //limit用于获取指定数量的流，对应条件过滤后自上而下的数据列数
                .skip(2)        //skip(n)表示为跳过前n个元素
                // distinct主要完成列表的去重，若为引用对象则需要流中对象元素重写hashCode和equals方法
                .distinct()
                .forEach(System.out::println);

        /**
         *  生成新的流 通过map映射
         */
        emps.stream()
                .map((e) -> e.getAge())
                .forEach(System.out::println);

        /**
         *  自然排序  定制排序
         */
        emps.stream()
                .sorted((e1, e2) -> {
                    if (e1.getAge().equals(e2.getAge())) {
                        return e1.getName().compareTo(e2.getName());
                    } else {
                        return e1.getAge().compareTo(e2.getAge());
                    }
                })
                .forEach(System.out::println);
        System.out.println("Stream_Use_________Over");
    }

    public void Stream02() {
        System.out.println("Stream02_________Start");
        List<String> stringList = new ArrayList<>();
        stringList.add("ddd2");
        stringList.add("aaa2");
        stringList.add("bbb1");
        stringList.add("aaa1");
        stringList.add("bbb3");
        stringList.add("ccc");
        stringList.add("bbb2");
        stringList.add("ddd1");
        System.out.println("Origin___Data_Before");
        System.out.println(stringList);
        // 测试 Filter(过滤)
        stringList
                .stream()
                .filter((s) -> s.startsWith("a"))   //此处过滤以a开头的字符串
                .forEach(System.out::println);//aaa2 aaa1

        System.out.println("Sort________");
        stringList
                .stream()
                .sorted()       //先排序后过滤
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println);// aaa1 aaa2

        //需要注意的是，排序只创建了一个排列好后的Stream，而不会影响原有的数据源，排序之后原数据stringCollection是不会被修改的
        System.out.println("Origin___Data_later");
        System.out.println(stringList);

        System.out.println("another_________");
        List<Integer> list = new ArrayList();
        list.add(3);
        list.add(2);
        list.add(1);
        list.add(4);
        list.sort(Comparator.reverseOrder());
        list.stream().filter((x) -> x > 2).forEach(System.out::println);
        System.out.println("Stream02_________Over");
    }

    public void LambdaInterface() {
        ReturnOneParam lambda1 = a -> doubleNum(a);
        System.out.println(lambda1.method(3));

        //lambda2 引用了已经实现的 ShowInput 方法
        ReturnOneParam lambda2 = Employee::ShowInput;
        System.out.println(lambda2.method(3));

        Employee exe = new Employee();

        //lambda4 引用了已经实现的 addTwo 方法
        ReturnOneParam lambda4 = exe::addTwo;
        System.out.println(lambda4.method(2));
    }

    public int addTwo(int a) {
        return a + 2;
    }
}

class Employee {
    static int Uid = 0;
    int id = -1;
    String name;
    int age = -1;

    public Employee() {
        Uid += 1;
        this.id = Uid;
    }

    public Employee(String name) {
        Uid += 1;
        this.id = Uid;
        this.name = name;
    }

    public Employee(String name, int age) {
        Uid += 1;
        this.id = Uid;
        this.name = name;
        this.age = age;
    }

    public static int ShowInput(int x) {
        return x;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id_in) {
        id = id_in;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int addTwo(int x) {
        return x + 2;
    }

    @Override
    public String toString() {
        return "[Employee] id = " + id + " , name = " + name + " , age = " + age + " ;";
    }
}
