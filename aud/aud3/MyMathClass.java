package aud.aud3;

import java.util.ArrayList;
import java.util.Arrays;

public class MyMathClass {
    static double standardDeviation(ArrayList<? extends Number> numbers){
        // koren
        // 1/n * suma ( xi - 1/n * suma xi) ^2
        double sum = 0;
        for (Number n : numbers) sum += n.doubleValue();
        int size = numbers.size();
        double avg = sum / size;
        sum = 0;
        for (Number n : numbers) sum += Math.pow(avg - n.doubleValue(), 2);
        return Math.sqrt(sum / size);
    }

    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(3);
        integers.add(6);
        integers.add(30);
        System.out.println(String.format("std %.2f", standardDeviation(integers)));
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(5.9);
        doubles.add(6.1);
        System.out.println(String.format("std %.2f", standardDeviation(doubles)));
    }
}
