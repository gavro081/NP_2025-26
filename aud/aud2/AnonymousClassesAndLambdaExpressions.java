package aud.aud2;

interface Operation{
    int apply(int num1, int num2);
}

interface MessageProvider {
    String getMessage();
}

class Addition implements Operation {
    @Override
    public int apply(int num1, int num2) {
        return num1 + num2;
    }
}

class ExampleMessage implements MessageProvider {
    @Override
    public String getMessage() {
        return "Hello from ExampleMessage";
    }
}

public class AnonymousClassesAndLambdaExpressions {
    public static void main(String[] args) {
        // 1. class that implements interface
        Operation op1 = new Addition();
        System.out.println("add: " + op1.apply(2,3));

        MessageProvider mp1 = new ExampleMessage();
        System.out.println(mp1.getMessage());

        // 2. anonymous class
        Operation op2 = new Operation() {
            @Override
            public int apply(int num1, int num2) {
                return num1 * num2;
            }
        };
        System.out.println("multiply: " + op2.apply(2,3));

        MessageProvider mp2 = new MessageProvider() {
            @Override
            public String getMessage() {
                return "Hello from anonymous class";
            }
        };
        System.out.println(mp2.getMessage());

        // 3. lambda expression
        Operation op3 = ((num1, num2) -> num1 - num2);
        System.out.println("subtract: " + op3.apply(2,3));

        MessageProvider mp3 = () -> "Hello from lambda";
        System.out.println(mp3.getMessage());

    }
}
