package k1.Shapes2;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

class InvalidCanvasException extends Exception{
    InvalidCanvasException(String message){
        super(message);
    }
}

abstract class Shape implements Comparable<Shape>{
    double s;
    Shape(double s){
        this.s = s;
    }
    abstract double getArea();

    @Override
    public int compareTo(Shape o) {
        return Double.compare(getArea(), o.getArea());
    }
}

class Square extends Shape {
    public Square(double s) {
        super(s);
    }

    @Override
    public double getArea() {
        return s*s;
    }
}

class Circle extends Shape {
    public Circle(double s) {
        super(s);
    }

    @Override
    public double getArea() {
        return Math.PI * s * s;
    }
}

class Canvas {
    private final String id;
    private final List<Shape> shapes;

    public Canvas(String id) {
        this.id = id;
        this.shapes = new ArrayList<>();
    }

    public double getTotalArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .sum();
    }

    public void addCanvas(String sign, String size, double maxArea) throws InvalidCanvasException {
        Shape shape;
        if ("C".equals(sign)) {
            shape = new Circle(Double.parseDouble(size));
        }
        else {
            shape = new Square(Double.parseDouble(size));
        }
        if (shape.getArea() > maxArea)
            throw new InvalidCanvasException(
                    String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea)
            );
        shapes.add(shape);
    }

    public double averageArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .average()
                .getAsDouble();
    }

    public long totalCircles(){
        return shapes.stream()
                .filter(s -> s instanceof Circle)
                .count();
    }

    public long totalSquares(){
        return shapes.stream()
                .filter(s -> s instanceof Square)
                .count();
    }

    public double getMin(){
        return Collections.min(shapes).getArea();
    }

    public double getMax(){
        return Collections.max(shapes).getArea();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %.2f %.2f %.2f",
                id, shapes.size(), totalCircles(), totalSquares(), getMin(), getMax(), averageArea());
    }
}

class ShapesApplication {
    List<Canvas> canvases;
    double maxArea;

    ShapesApplication(double maxArea){
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }

    public void readCanvases(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()){
            String []parts = sc.nextLine().split("\\s++");
            Canvas canvas = new Canvas(parts[0]);
            try{
                for (int i = 1; i < parts.length; i+=2)
                    canvas.addCanvas(parts[i], parts[i + 1], maxArea);
                canvases.add(canvas);
            } catch (InvalidCanvasException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void printCanvases(PrintStream out) {
        canvases.stream()
                .sorted(Comparator.comparingDouble(Canvas::getTotalArea).reversed())
                .forEach(out::println);
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}