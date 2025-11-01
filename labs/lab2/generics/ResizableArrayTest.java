package labs.lab2.generics;
import java.util.*;

class ResizableArray<T> {
    private T []items;
    private int size = 0;

    @SuppressWarnings("unchecked")
    ResizableArray(){
        items = (T[]) new Object[10];
    }

    @SuppressWarnings("unchecked")
    void addElement(T element){
        if (items.length == size){
            T []newItems = (T[]) new Object[size * 2];
            for (int i = 0; i < size; i++) {
                newItems[i] = items[i];
            }
            items = newItems;
        }
        items[size++] = element;
    }

    boolean removeElement(T element){
        for (int i = 0; i < size; i++) {
            if (items[i].equals(element)){
                int j;
                for (j = i; j < size - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[j] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    boolean contains(T element){
        for (int i = 0; i < size; i++){
            if (items[i].equals(element)) return true;
        }
        return false;
    }

    Object[] toArray(){
        return items;
    }

    boolean isEmpty(){
        return size == 0;
    }

    int count(){
        return size;
    }

    T elementAt(int index){
        if (index > size) throw new ArrayIndexOutOfBoundsException();
        return items[index];
    }

    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        T[] items = src.items;
        int size = src.size;
        for (int i = 0; i < size; i++) {
            dest.addElement(items[i]);
        }
    }
}

class IntegerArray extends ResizableArray<Integer> {
    double sum(){
        return Arrays.stream(toArray())
                .filter(Objects::nonNull)
                .mapToInt(o -> (Integer) o)
                .sum();
    }

    double mean(){
        return sum() / count();
    }

    int countNonZero(){
        return (int) Arrays.stream(toArray())
                .filter(Objects::nonNull)
                .map(o -> (Integer) o)
                .filter(i -> i != 0)
                .count();
    }

    IntegerArray distinct(){
        IntegerArray newIntegerArray = new IntegerArray();
        Arrays.stream(Arrays.stream(toArray()).toArray())
                .filter(Objects::nonNull)
                .distinct()
                .map(i -> (Integer) i)
                .forEach(newIntegerArray::addElement);
        return newIntegerArray;
    }

    IntegerArray increment(int offset){
        IntegerArray newIntegerArray = new IntegerArray();
        Arrays.stream(Arrays.stream(toArray()).toArray())
                .filter(Objects::nonNull)
                .map(i -> (Integer) i + offset)
                .forEach(newIntegerArray::addElement);
        return newIntegerArray;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}