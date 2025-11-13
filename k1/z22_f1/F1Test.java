package k1.z22_f1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class F1Test {

    public static void main(String[] args)throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {
    // vashiot kod ovde
    static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("m:ss:SSS")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter();
    static DateTimeFormatter outputFormatter = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.MINUTE_OF_HOUR)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MILLI_OF_SECOND, 3)
            .toFormatter();
    List<DriverInfo> infos;
    // polesno ke bese samo racno da parsiram min, s i ms ama nema veze

    static class DriverInfo implements Comparable<DriverInfo>{
        String name;
        LocalTime lap1;
        LocalTime lap2;
        LocalTime lap3;

        public DriverInfo(String name, LocalTime lap1, LocalTime lap2, LocalTime lap3) {
            this.name = name;
            this.lap1 = lap1;
            this.lap2 = lap2;
            this.lap3 = lap3;
        }

        LocalTime getMin(){
            LocalTime min = lap1.isBefore(lap2) ? lap1 : lap2;
            min = lap3.isBefore(min) ? lap3 : min;
            return min;
        }

        @Override
        public int compareTo(DriverInfo o) {
            if (getMin().equals(o.getMin())) return 0;
            return getMin().isBefore(o.getMin()) ? -1 : 1;
        }
    }

    public F1Race() {
        this.infos = new ArrayList<>();
    }

    public void readResults(InputStream in)throws IOException {
        BufferedReader sc = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = sc.readLine()) != null){
            String []parts = line.split("\\s++");
            String name = parts[0];
            LocalTime lap1 = LocalTime.parse(parts[1], formatter);
            LocalTime lap2 = LocalTime.parse(parts[2], formatter);
            LocalTime lap3 = LocalTime.parse(parts[3], formatter);
            infos.add(new DriverInfo(name, lap1, lap2, lap3));
        }
    }

    public void printSorted(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        AtomicInteger i = new AtomicInteger(1);
        infos.stream()
                .sorted()
                .map(info -> String.format("%s. %-10s%10s",
                        i.getAndIncrement(), info.name, info.getMin().format(outputFormatter)))
                .forEach(pw::println);
        pw.flush();
    }


}
