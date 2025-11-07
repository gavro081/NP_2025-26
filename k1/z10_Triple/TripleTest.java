package k1.z10_Triple;

import java.util.*;
import java.util.stream.Collectors;

class Triple<T extends Number>{
    List<T> items;

    public Triple(T i1, T i2, T i3) {
        this.items = List.of(i1, i2, i3);
    }

    double max(){
        return items.stream().mapToDouble(Number::doubleValue).max().getAsDouble();
    }

    double average(){
        return items.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
    }

    void sort(){
        this.items = items.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString(){
        return String.format(
                "%.2f %.2f %.2f",
                items.get(0).doubleValue(),
                items.get(1).doubleValue(),
                items.get(2).doubleValue()
                );
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple



