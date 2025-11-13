package k1.z20_subtitles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Element{
    private int seqNum;
    private LocalTime start;
    private LocalTime end;
    private String text;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");


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
    public String toString() {
        return String.format("%d%n%s --> %s%n%s%n",
                seqNum,
                start.format(FORMATTER),
                end.format(FORMATTER),
                text.trim());
    }
}

class Subtitles {
    List<Element> elements;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    Subtitles(){
        elements = new ArrayList<>();
    }
    public int loadSubtitles(InputStream inputStream) throws IOException{
        BufferedReader sc = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = sc.readLine()) != null){
            int seqNum = Integer.parseInt(line);
            String[] parts = sc.readLine().split(" --> ");
            LocalTime start = LocalTime.parse(parts[0], formatter);
            LocalTime end = LocalTime.parse(parts[1], formatter);
            StringBuilder sb = new StringBuilder();
            while ((line = sc.readLine()) != null){
                if (line.trim().isEmpty()) break;
                sb.append(line).append("\n");
            }
            elements.add(new Element(seqNum, start, end, sb.toString()));
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
    public static void main(String[] args)throws IOException {
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

