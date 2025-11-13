package k1.z5_minmax;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    T min;
    T max;
    int tmpMinCounter = 1;
    int tmpMaxCounter = 1;
    int counter = 0;

    MinMax(){
        min = null;
        max = null;
    }

    void update(T element){
        if (min == null) {
            // if min is null, max is also null
            min = element;
            max = element;
            return;
        }
        int diffMin = element.compareTo(min);
        int diffMax = element.compareTo(max);
        boolean flag = false;
        if (diffMin == 0){
            tmpMinCounter++;
            flag = true;
        }
        if (diffMax == 0){
            tmpMaxCounter++;
            flag = true;
        }
        if (flag) return;
        if (diffMin < 0) {
            if (max != min) counter += tmpMinCounter;
            tmpMinCounter = 1;
            min = element;
        }
        else if (diffMax > 0){
            if (max != min) counter += tmpMaxCounter;
            tmpMaxCounter = 1;
            max = element;
        }
        else counter++;
    }

    @Override
    public String toString(){
        return String.format("%s %s %s\n", min, max, counter);
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}