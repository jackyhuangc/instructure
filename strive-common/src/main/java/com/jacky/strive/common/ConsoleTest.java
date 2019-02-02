/*
 * 包命名规则
 * com.公司名.项目名.模块名...
 * Java的包名都有小写单词组成，类名首字母大写；包的路径符合所开发的 系统模块的 定义
 * 公司名可用域名代替，可避免重名
 */
package com.jacky.strive.common;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 测试工具类
 *
 * @author Jacky
 * @version 2018.10.11
 * @since 1.8
 */

//class Test implements Serializable {
//    private static final long serialVersionUID = -4246665266031558401L;
//}

public class ConsoleTest {

    public static void main(String[] args) {

        List<String> stringList = new ArrayList<>();

        boolean test1 = StringUtils.isBlank("");
        boolean test2 = StringUtils.isBlank(null);

        List<String> todayHasFailChannelNames = stringList.stream().collect(toList());
        String str = todayHasFailChannelNames.stream().collect(Collectors.joining("|"));
        stringList.add("cbiz_qnn");
        boolean rettt = stringList.contains("biz");

        Map<String, String> testMap = new HashMap<>();
        String test = testMap.get("123");

        String ret123 = "订单已存在";
        String ret1234 = "已存在";
        String ret12345 = "订单重复";
        String ret123456 = "xxxxx";

        int r1 = ret123.indexOf("已存在");
        int r3 = ret1234.indexOf("已存在");
        int r2 = ret12345.indexOf("重复");
        int r4 = ret123456.indexOf("已存在");

        //易宝返回码可能为空同步数据会出现错误 如果为空把状态给code
        if (StringUtils.isBlank("xxx")) {
            LogUtil.info("xxx");
        } else {
            LogUtil.info("xxx");
        }

        String failMsg = "";

        String[] tmp = "单笔限额超限.交易金额[3],单笔限额[1]|单笔限额超限.交易金额[3],单笔限额[1]||".split("\\|");
        List<String> listMsg = Arrays.asList("|||".split("\\|"));
        listMsg = listMsg.stream().filter(s -> !s.isEmpty()).map(String::toString).collect(toList());

        // 取最后一条数据
        if (listMsg.size() > 0) {
            failMsg = listMsg.get(listMsg.size() - 1);
        }
//        new Thread(() -> {
//
//        }).start();

        // stream parallelStream

        // reduce规约和Collector收集器

        // Function、Predicate、Consumer、Supplier接口
        // https://www.jianshu.com/p/5b35158a0365
        // 在Java 8中，接口可以包含带有实现代码的方法，这些方法称为default方法

        // <? extends T>和<? super T>

        function();
    }

    public static void stream_parallelStream() {

        /**
         * https://www.cnblogs.com/CarpenterLee/p/6675568.html
         * 所以，如果出于性能考虑，1. 对于简单操作推荐使用外部迭代手动实现，2. 对于复杂操作，推荐使用Stream API， 3. 在多核情况下，推荐使用并行Stream API来发挥多核优势，4.单核情况下不建议使用并行Stream API。
         *
         * 如果出于代码简洁性考虑，使用Stream API能够写出更短的代码。即使是从性能方面说，尽可能的使用Stream API也另外一个优势，那就是只要Java Stream类库做了升级优化，代码不用做任何修改就能享受到升级带来的好处。
         */

        String path = "pku_training.utf8";
        try {
            List<String> list = null;//IOUtil.readFile2List(path, "utf-8");

            long start = System.currentTimeMillis();

            // 串行，单线程
            list.stream().
                    filter(e -> StringUtils.isNotBlank(e)).
                    map(e -> getIdiom(e)).
                    collect(Collectors.toList());
            System.out.println("stream : " + (System.currentTimeMillis() - start) + "ms");

            start = System.currentTimeMillis();

            // 并行，多线程
            list.parallelStream().
                    filter(e -> StringUtils.isNotBlank(e)).
                    map(e -> getIdiom(e)).
                    collect(Collectors.toList());
            System.out.println("parallelStream : " + (System.currentTimeMillis() - start) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reduce_collect() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);
//        Map<String, Integer> routeFilterMapSort = routeFilterMap
//                .entrySet()
//                .stream()
//                .sorted(comparingByValue())
//                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
//                        LinkedHashMap::new));

        map = map.entrySet().stream().sorted((K1, K2) -> K2.getValue() - K1.getValue()).
                collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        assert null != map;

        Stream<String> stream = Stream.of("I", "love", "you", "too");
        stream.sorted((str1, str2) -> str1.length() - str2.length())
                .forEach(str -> System.out.println(str));

        // reduce 规约，类似max(),sum()方法
        Optional<Map.Entry<String, Integer>> max = map.entrySet().stream().reduce((s1, s2) -> s1.getValue() >= s2.getValue() ? s1 : s2);

        // 一个 Stream 只可以使用一次
        Stream<String> stream1 = Stream.of("I", "love", "you", "too");
        Optional<String> longest = stream1.reduce((s1, s2) -> s1.length() >= s2.length() ? s1 : s2);

//       一个 Stream 只可以使用一次
        Stream<String> stream2 = Stream.of("I", "love", "you", "too");
        Map<String, Integer> map1 = stream2.collect(Collectors.toMap(Function.identity(), String::length)); // (3)

        //
        eval(map, n -> n.getValue() == 3 || n.getValue() == 5);

        // 原始收集器
        // Stream<String> stream4 = Stream.of("I", "love", "you", "too");
        // HashSet<String> list = stream.collect(HashSet::new, HashSet::add, HashSet::addAll);// 方式１

        // 收集器工具类

        //一个 Stream 只可以使用一次
        Stream<String> stream3 = Stream.of("I", "love", "you", "too");
        List<String> map2 = stream3.collect(Collectors.toList()); // (3)

        // 使用collect()生成Collection
        // 使用toCollection()指定规约容器的类型
        //ArrayList<String> arrayList = stream.collect(Collectors.toCollection(ArrayList::new));// (3)
        //HashSet<String> hashSet = stream.collect(Collectors.toCollection(HashSet::new));// (4)


        // 使用collect()生成Map,主要用于分区分组
        // 分组
//        Map<Boolean, List<Student>> passingFailing = students.stream()
//                .collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD));
//        情况3：使用groupingBy()生成的收集器，这是比较灵活的一种情况。跟SQL中的group by语句类似，这里的groupingBy()也是按照某个属性对数据进行分组，属性相同的元素会被对应到Map的同一个key上。下列代码展示将员工按照部门进行分组：
//
//// Group employees by department
//        Map<Department, List<Employee>> byDept = employees.stream()
//                .collect(Collectors.groupingBy(Employee::getDepartment));


//        // 按照部门对员工分布组，并只保留员工的名字
//        Map<Department, List<String>> byDept = employees.stream()
//                .collect(Collectors.groupingBy(Employee::getDepartment,
//                        Collectors.mapping(Employee::getName,// 下游收集器
//                                Collectors.toList())));// 更下游的收集器

//        // 使用Collectors.joining()拼接字符串
//        Stream<String> stream = Stream.of("I", "love", "you");
////String joined = stream.collect(Collectors.joining());// "Iloveyou"
////String joined = stream.collect(Collectors.joining(","));// "I,love,you"
//        String joined = stream.collect(Collectors.joining(",", "{", "}"));// "{I,love,you}"
    }

    // 函数式编程
    public static void function() {
        // Function、Predicate、Consumer、Supplier接口
        // https://www.jianshu.com/p/5b35158a0365
        // 在Java 8中，接口可以包含带有实现代码的方法，这些方法称为default方法
        /**
         * Function：接受一个参数，返回一个参数。
         * Predicate：用于测试是否符合条件。
         * Consumer：接受一个参数，不返回参数。
         * Supplier：无输入参数，返回一个结果。
         *
         * boolean	anyMatch() allMatch() noneMatch()
         * Optional	findFirst() findAny()
         * 归约结果	reduce() collect()
         * 数组	toArray()
         */

        // 有返回值的写法1，接近于C#风格
        Function<String, Boolean> f = (n) -> {
            return n.equals("接受一个参数，返回一个参数");
        };
        System.out.println(f.apply("接受一个参数，返回一个参数"));

        Predicate<String> p = (n) -> n.equals("用于测试是否符合条件");
        System.out.println(p.test("用于测试是否符合条件"));

        Consumer<String> c = (n) -> System.out.println(n);
        c.accept("无输入参数，返回一个结果");

        // 有返回值的写法2
        Supplier<String> s = () -> "无输入参数，返回一个结果";
        System.out.println(s.get());
    }

    private static void eval(Map<String, Integer> list, Predicate<Map.Entry<String, Integer>> predicate) {

        list.entrySet().stream().filter(predicate).forEach(System.out::println);
    }

    private static List<String> getIdiom(String string) {
        String[] array = string.split("\\s+");
        List<String> list = Arrays.asList(array);

        return list.stream().filter(e -> e.length() == 4).collect(Collectors.toList());
    }

    // https://blog.csdn.net/qq_36372507/article/details/78757811
    public void testFunctional() {
        // static方法必须通过接口类调用
        JDK8Interface.staticMethod();

        //default方法必须通过实现类的对象调用
        new JDK8InterfaceImpl().defaultMethod();

        JDK8Interface jdk8Interface = s -> {

        };
    }
}


//@FunctionalInterface标记在接口上，“函数式接口”是指仅仅只包含一个抽象方法的接口。
@FunctionalInterface
interface JDK8Interface {
    void test(String str);

    // static修饰符定义静态方法
    static void staticMethod() {
        System.out.println("接口中的静态方法");
    }

    // default修饰符定义默认方法
    default void defaultMethod() {
        System.out.println("接口中的默认方法");
    }
}

// https://blog.csdn.net/aitangyong/article/details/54134385
class JDK8InterfaceImpl implements JDK8Interface {

    @Override
    public void test(String str) {

    }

    //实现接口后，因为默认方法不是抽象方法，所以可以不重写，但是如果开发需要，也可以重写
//    @Override
//    public void defaultMethod() {
//
//    }
}