package aud.aud3;

import java.util.ArrayList;

class Pair<T> {
    public T value;
    public int priority;

    public Pair(T value, int priority) {
        this.value = value;
        this.priority = priority;
    }
}

public class PriorityQueue<T> {
    ArrayList<Pair<T>> values;

    public PriorityQueue() {
        this.values = new ArrayList<>();
    }

    public void add(T item, int priority){
        int i = 0;
        for (i = 0; i < values.size(); i++) {
            if (values.get(i).priority < priority){
                break;
            }
        }
        values.add(i, new Pair<>(item, priority));
    }

    public T remove(){
        if (values.isEmpty()) return null;
        return values.removeFirst().value;
    }

    public static void main(String[] args) {
        PriorityQueue<String> pq = new PriorityQueue<>();
        pq.add("filip", 10);
        pq.add("ace", 7);
        pq.add("dimi", 15);
        System.out.println(pq.remove());
        System.out.println(pq.remove());
        System.out.println(pq.remove());
        System.out.println(pq.remove());

    }
}
