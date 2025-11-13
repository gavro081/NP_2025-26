package k1.z14_component;

import java.util.*;

class InvalidPositionException extends Exception{
    InvalidPositionException(int pos){
        super("Invalid position " + pos + ", alredy taken!");
    }
}

class Component {
    private String color;
    private int weight;
    private TreeSet<Component> components;

    public Component(String color, int weight) {
        this.weight = weight;
        this.color = color;
        components = new TreeSet<>(
                Comparator.comparingInt((Component o) -> o.weight)
                        .thenComparing(o -> o.color)
        );
    }

    void addComponent(Component component){
        components.add(component);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void changeColorHelper(int weight, String color){
        if (this.weight < weight) this.color = color;
        for (Component c : components){
            c.changeColorHelper(weight, color);
        }
    }

    public int getWeight() {
        return weight;
    }

    public TreeSet<Component> getComponents() {
        return components;
    }
}

class Window{
    private String name;
    private TreeMap<Integer, Component> componentsMap;

    public Window(String name) {
        this.name = name;
        componentsMap = new TreeMap<>();
    }

    public void addComponent(int position, Component component)
    throws InvalidPositionException {
        if (componentsMap.get(position) != null) throw new InvalidPositionException(position);
        componentsMap.put(position, component);
    }

    public String toStringHelper(Component c, int depth, TreeMap<Integer, Component> componentsMap){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("---");
        }
        sb.append(c.getWeight()).append(":").append(c.getColor()).append("\n");
        for (Component child : c.getComponents()){
            sb.append(toStringHelper(child, depth + 1, componentsMap));
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(this.name).append('\n');
        for (Map.Entry<Integer, Component> entry : componentsMap.entrySet()){
            sb
                    .append(entry.getKey())
                    .append(":")
                    .append(toStringHelper(entry.getValue(), 0, componentsMap));
        }
        return sb.toString();
    }

    public void changeColor(int weight, String color){
        componentsMap.values()
                .forEach(c -> c.changeColorHelper(weight, color));
    }

    void swichComponents(int pos1, int pos2){
        Component comp1 = componentsMap.remove(pos1);
        Component comp2 = componentsMap.remove(pos2);
        componentsMap.put(pos2, comp1);
        componentsMap.put(pos1, comp2);
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
