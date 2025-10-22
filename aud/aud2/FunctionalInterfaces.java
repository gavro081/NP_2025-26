package aud.aud2;

import java.util.function.*;

public class FunctionalInterfaces {
    public static void main(String[] args) {
        Function<String, Integer> f = (str) -> str.length();
        // or
//        Function<String, Integer> f1 = String::length;
        System.out.println("Testing function for string length of \"hello\": "
                + f.apply("hello"));
        System.out.println();

        BiFunction<Integer, Integer, Integer> bf =
                (num1, num2) -> num1 + num2;
        // or
//        BiFunction<Integer, Integer, Integer> bf1 = Integer::sum;
        System.out.println("Testing bifunction for sum of 2 and 3: " + bf.apply(2,3));
        System.out.println();

        Predicate<Integer> p = (num) -> num % 2 == 0;
        System.out.println("Testing predicate for even numbers with param 3: " + p.test(3));
        System.out.println("Testing predicate for even numbers with param 2: " + p.test(2));
        System.out.println();

        Consumer<String> c = str -> System.out.println(str);
        // or
//        Consumer<String> c1 = System.out::println;
        c.accept("Testing consumer for printing a string: " + "hello from consumer");
        System.out.println();

        Supplier<Long> s = () -> System.currentTimeMillis();
        // or
//        Supplier<Long> s1 = System::currentTimeMillis;

        System.out.println("Testing supplier for getting time in millis: " + s.get());
    }
}
