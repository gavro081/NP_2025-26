package k1.z12_generic_fraction;

import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    ZeroDenominatorException(){
        super("Denominator cannot be zero");
    }
}

class GenericFraction<T extends Number, U extends Number> {
    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator)
    throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0) throw new ZeroDenominatorException();
        this.numerator = numerator;
        this.denominator = denominator;
    }
    public GenericFraction<Double, Double> add(
            GenericFraction<? extends Number, ? extends Number> gf
            ) throws ZeroDenominatorException {
        double numeratorAns =
                this.numerator.doubleValue() * gf.denominator.doubleValue() +
                gf.numerator.doubleValue() * this.denominator.doubleValue();
        return new GenericFraction<>(numeratorAns, gf.denominator.doubleValue() * this.denominator.doubleValue());
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public GenericFraction<Double, Double> normalize(
            GenericFraction<? extends Number, ? extends Number> gf
    ) throws ZeroDenominatorException {
        int i = 2;
        double num = numerator.doubleValue();
        double den = denominator.doubleValue();
        while (i < num && i < den){
            if (num % i == 0 && den % i == 0){
                num /= i;
                den /= i;
            } else i++;
        }
        return new GenericFraction<>(num, den);
    }

    @Override
    public String toString(){
        try {
            GenericFraction<Double, Double> normalized = normalize(this);
            return String.format("%.2f / %.2f", normalized.numerator, normalized.denominator);
        } catch (ZeroDenominatorException e) {
            return "";
        }
    }

}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
