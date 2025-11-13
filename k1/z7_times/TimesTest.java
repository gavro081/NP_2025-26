package k1.z7_times;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

class UnsupportedFormatException extends Exception{
    UnsupportedFormatException(String message){
        super(message);
    }
}
class InvalidTimeException extends Exception{
    InvalidTimeException(String message){
        super(message);
    }
}

class Time implements Comparable<Time>{
    int hours;
    int minutes;
    String suffix = "";

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public Time(String time)
            throws UnsupportedFormatException,InvalidTimeException {
        String []parts;
        if (time.contains(".")){
            parts = time.split("\\.");
        } else if (time.contains(":")){
            parts = time.split(":");
        } else throw new UnsupportedFormatException(time);
        int hour = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        if (hour < 0 || hour > 23 || minutes < 0 || minutes > 59)
            throw new InvalidTimeException(time);
        this.hours = hour;
        this.minutes = minutes;
    }

    @Override
    public int compareTo(Time o) {
        int diff = hours - o.hours;
        return diff != 0 ? diff : minutes - o.minutes;
    }

    @Override
    public String toString() {
        return String.format("%2s:%2s", hours, formatMinutes());
    }

    private String formatMinutes() {
        return (this.minutes >= 10)
                ? String.valueOf(this.minutes)
                : "0" + this.minutes;
    }

    public String mapToAMPM(){
        if (hours == 0) {
            this.hours = 12;
            suffix = "AM";
        } else if (hours >= 1 && hours <= 11)
            suffix = "AM";
        else if (hours == 12){
            suffix = "PM";
        } else {
            hours -= 12;
            suffix = "PM";
        }
        return String.format("%2s:%2s %s", hours, formatMinutes(), suffix);
    }
}

class TimeTable {
    ArrayList<Time> times = new ArrayList<>();
    TimeTable(){}

    public void readTimes(InputStream in) 
    throws InvalidTimeException, UnsupportedFormatException, IOException {
        BufferedReader sc = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = sc.readLine()) != null){
            String []parts = line.split(" ");
            for (String part : parts){
                times.add(new Time(part));
            }
        }
    }

    public void writeTimes(PrintStream out, TimeFormat timeFormat) {
        if (timeFormat == TimeFormat.FORMAT_24){
            times.stream()
                    .sorted()
                    .forEach(out::println);
        } else {
            times.stream()
                    .sorted()
                    .map(Time::mapToAMPM)
                    .forEach(out::println);
        }
    }
}

public class TimesTest {

    public static void main(String[] args) throws IOException {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
