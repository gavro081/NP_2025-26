package k1.z1_Shapes1;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;


class Window{
    String id;
    List<Integer> squareSizes;

    public Window(String id) {
        this.id = id;
        squareSizes = new ArrayList<>();
    }
    public void addSquare(int s) {squareSizes.add(s);}
    public int getSize(){return squareSizes.size();}
    public int getPerimeter(){
        return squareSizes.stream()
                .mapToInt(n -> 4 * n)
                .sum();
    }
}

class ShapesApplication {
    List<Window> windows;
    ShapesApplication(){
        windows = new ArrayList<>();
    }
    int readCanvases (InputStream in) {
        Scanner sc = new Scanner(in);
        int count = 0;
        while (sc.hasNextLine()){
            String []parts = sc.nextLine().split("\\s++");
            count += parts.length - 1;
            Window w = new Window(parts[0]);
            Arrays.stream(parts)
                    .skip(1)
                    .mapToInt(Integer::parseInt)
                    .forEach(w::addSquare);
            windows.add(w);
        }
//        return windows.stream().mapToInt(Window::getSize).sum();
        return count;
    }

    public void printLargestCanvasTo(PrintStream out) {
        Window window = windows.stream().max(Comparator.comparingInt(Window::getPerimeter)).get();
        out.println(
                String.format("%s %s %s", window.id, window.getSize(), window.getPerimeter())
        );
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
