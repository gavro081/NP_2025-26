package aud.aud3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Box<T>{
    private final List<T> items;

    public Box(List<T> items) {
        this.items = items;
    }

    public void add(T item){
        items.add(item);
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public T drawItem(){
        if (isEmpty()) return null;
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<>(new ArrayList<>(List.of(1, 2, 3, 5)));
        Box<String> stringBox = new Box<>(new ArrayList<>(List.of("abc", "fgh", "xyz")));
        Box<Double> doubleBox = new Box<>(new ArrayList<>(List.of(1.2)));
        doubleBox.add(3.4);
        doubleBox.add(3d);
        doubleBox.add(6.4);
        Box<Integer> emptyBox = new Box<>(new ArrayList<>());
        System.out.println(integerBox.drawItem());
        System.out.println(stringBox.drawItem());
        System.out.println(doubleBox.drawItem());
        System.out.println(emptyBox.drawItem());
    }
}
