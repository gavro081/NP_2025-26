package k1.z44_event_calendar;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Event{
    private final String name;
    private final String location;
    private final LocalDateTime time;
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm");
    public static final DateTimeFormatter EXCEPTION_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'UTC' yyyy", Locale.ENGLISH);

    public Event(String name, String location, LocalDateTime time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString(){
        return String.format(
                "%s at %s, %s",
                DATE_TIME_FORMATTER.format(time), location, name
        );
    }
}

class EventCalendar{
    Set<Event> events;
    int year;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new TreeSet<>(Comparator
                .comparing(Event::getTime)
                .thenComparing(Event::getName)
                .thenComparing(Event::getLocation));
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if (date.getYear() != (year)) {
            ZonedDateTime dateTime = date.atZone(ZoneId.of("UTC"));
            String formatted = dateTime.format(Event.EXCEPTION_TIME_FORMATTER);
            throw new WrongDateException("Wrong date: " + formatted);
        }
        events.add(new Event(name, location, date));
    }

    public void listEvents(LocalDateTime date){
        List<Event> eventList = events.stream()
                .filter(e -> {
                    LocalDateTime time = e.getTime();
                    return (time.getYear() == date.getYear() &&
                            time.getDayOfYear() == date.getDayOfYear()
                    );
                })
                .collect(Collectors.toList());
        if (eventList.isEmpty()){
            System.out.println("No events on this day!");
        } else {
            eventList.forEach(System.out::println);
        }
    }

    public void listByMonth(){
        IntStream.range(1,13).forEach(i -> {
            long count = events.stream()
                    .filter(e -> e.getTime().getMonth().equals(Month.of(i)))
                    .count();
            System.out.printf("%d : %d%n", i, count);
        });
    }
}

class WrongDateException extends Exception{
    WrongDateException(String message){
        super(message);
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2], df);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(), df);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde
