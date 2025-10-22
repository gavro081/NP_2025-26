package aud.aud2;

import java.util.Scanner;

class UnknownOperatorException extends Exception{
}

interface CalculatorOperation{
    double apply(double result, double value);
}

class OperationFactory {
    private static final char PLUS_SIGN = '+';
    private static final char MINUS_SIGN = '-';
    private static final char MULTIPLY_SIGN = '*';
    private static final char DIVISION_SIGN = '/';

    private static final CalculatorOperation ADD = (r, v) -> r + v;
    private static final CalculatorOperation SUBTRACT = (r, v) -> r - v;
    private static final CalculatorOperation MULTIPLY = (r, v) -> r * v;
    private static final CalculatorOperation DIVIDE = (r, v) -> r / v;

    public static CalculatorOperation getOperation(char operator)
            throws UnknownOperatorException{
        return switch (operator) {
            case PLUS_SIGN -> ADD;
            case MINUS_SIGN -> SUBTRACT;
            case MULTIPLY_SIGN -> MULTIPLY;
            case DIVISION_SIGN -> DIVIDE;
            default -> throw new UnknownOperatorException();
        };
    }
}

class Calculator {
    private double result;

    public Calculator() {
        this.result = 0.0;
    }

    private void performOperation(char sign, double val) throws UnknownOperatorException{
        CalculatorOperation op = OperationFactory.getOperation(sign);
        result = op.apply(result, val);
        System.out.println("result " + sign + " " + val + " = " + result);
        System.out.println("updated result = " + result);
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("calculator is on.");
        System.out.println("result = " + result);
        while (true){
            String line = scanner.nextLine();
            if ("r".equalsIgnoreCase(line)){
                System.out.println("Final result = " + result);
                System.out.println("Again? (y/n)");
                String ans = scanner.nextLine();
                if (!"yes".equalsIgnoreCase(ans) && !"y".equalsIgnoreCase(ans)){
                    System.out.println("End of Program");
                    break;
                }
                else {
                    result = 0.0;
                    System.out.println("result = " + result);
                }
            }
            else {
                // will throw errors for bad inputs but not important for now
                char sign = line.charAt(0);
                double val = Double.parseDouble(line.substring(1));
                try {
                    performOperation(sign, val);
                } catch (UnknownOperatorException e) {
                    System.out.println(sign + " is an unknown operation.");
                    System.out.println("Reenter, your last line:");
                }
            }
        }
    }
}

public class CalculatorExample{
    public static void main(String[] args) {
        Calculator c = new Calculator();
        c.start();
    }
}
