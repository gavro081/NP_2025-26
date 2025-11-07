package k1.z2_Subtitles;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Element{
    private int seqNum;
    private LocalTime start;
    private LocalTime end;
    private String text;

    public Element(int seqNum, LocalTime start, LocalTime end, String text) {
        this.seqNum = seqNum;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public void transform(int ms){
        start = start.plusNanos(ms * 1_000_000L);
        end = end.plusNanos(ms * 1_000_000L);
    }

    @Override
    public String toString(){
        return seqNum + "\n"
                + start.toString().replaceAll("\\.", ",")
                + " --> "
                + end.toString().replaceAll("\\.", ",") + "\n"
                + text;
    }
}

class Subtitles {
    List<Element> elements;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    Subtitles(){
        elements = new ArrayList<>();
    }
    public int loadSubtitles(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while (true) {
            int seqNum = Integer.parseInt(sc.nextLine());
            String[] parts = sc.nextLine().split(" --> ");
            LocalTime start = LocalTime.parse(parts[0], formatter);
            LocalTime end = LocalTime.parse(parts[1], formatter);
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                if (line.isBlank() || line.isEmpty()) break;
                sb.append(line);
                if (!line.endsWith("\n")) sb.append("\n");
            }
            elements.add(new Element(seqNum, start, end, sb.toString()));
            if (sc.hasNextLine())
            {
//                sc.nextLine();
            }
            else break;
        }
        return elements.size();
    }

    public void print() {
        elements.forEach(System.out::println);
    }

    public void shift(int ms){
        elements.forEach(el -> el.transform(ms));
    }

}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

