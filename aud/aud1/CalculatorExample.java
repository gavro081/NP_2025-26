package aud.aud1;

import java.util.Scanner;

class UnknownOperatorException extends Exception{
}

class Calculator {
    private double result;

    public Calculator() {
        this.result = 0.0;
    }

    private void performOperation(char sign, double val) throws UnknownOperatorException{
        switch (sign) {
            case '+':
                result += val; break;
            case '-':
                result -= val; break;
            case '*':
                result *= val; break;
            case '/':
                if (val == 0) {
                    System.out.println("cannot divide by zero");
                    break;
                }
                result /= val; break;
            default:
                throw new UnknownOperatorException();
            }
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
