package k1.z6_Shapes;

import java.util.*;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}

interface Stackable{
    double weight();
}

abstract class Shape implements Scalable, Stackable, Comparable<Shape>{
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public int compareTo(Shape o) {
        int diff = Double.compare(o.weight(), this.weight());
        return diff != 0 ? diff : this.id.compareTo(o.id);
    }
}

class Circle extends Shape implements Scalable, Stackable {
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }

    @Override
    public double weight() {
        return Math.PI * radius * radius;
    }

    @Override
    public String toString() {
        return String.format(
                "C: %-4s %-10s %9.2f\n",
                id, color, weight()
        );
    }
}

class Rectangle extends Shape implements Scalable, Stackable {
    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public double weight() {
        return width * height;
    }


    @Override
    public String toString() {
        return String.format(
                "R: %-4s %-10s %9.2f\n",
                id, color, weight()
        );
    }
}


class Canvas {
    TreeSet<Shape> shapes = new TreeSet<>();

    void add(String id, Color color, float radius){
        Circle circle = new Circle(id, color, radius);
        shapes.add(circle);
    }

    void add(String id, Color color, float width, float height){
        Rectangle rectangle = new Rectangle(id, color, width, height);
        shapes.add(rectangle);
    }

    void scale(String id, float scaleFactor){
        Shape shape = shapes.stream()
                .filter(s -> s.id.equals(id))
                .findFirst()
                .get();
        shapes.remove(shape);
        shape.scale(scaleFactor);
        shapes.add(shape);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape s : shapes){
            sb.append(s);
        }
        return sb.toString();
    }
}


public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

