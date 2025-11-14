package k1.z37_airports;

import java.time.LocalTime;
import java.util.*;

class Airport {
    private final String name;
    private final String country;
    private final String code;
    private final int passengers;
    private final Map<String, Set<Flight>> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    public void addFlight(String from, String to, int time, int duration){
        flights.putIfAbsent(to, new TreeSet<>());
        flights.get(to).add(new Flight(from, to, time, duration));
    }

    public Map<String, Set<Flight>> getFlights() {
        return flights;
    }
}

class Flight implements Comparable<Flight> {
    private final String from;
    private final String to;
    private final int time;
    private final int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }


    @Override
    public int compareTo(Flight o) {
        int diff = Integer.compare(this.time, o.time);
        return diff != 0 ? diff : this.from.compareTo(o.from);
    }

    @Override
    public String toString(){
        int hours = duration / 60;
        int mins = duration - (duration / 60) * 60;
        String minsStr = mins > 10 ? String.valueOf(mins) : "0" + mins;
        String totDuration = hours + "h" + minsStr + "m";
        LocalTime fromTime = LocalTime.ofSecondOfDay(time * 60L);
        LocalTime toTime = fromTime.plusMinutes(duration);
        return String.format(
                "%s-%s %s-%s %s%s",
                from, to, fromTime, toTime, toTime.isBefore(fromTime) ? "+1d " : "",totDuration
        );
    }

    public String getTo() {
        return to;
    }
}

class Airports {
    Map<String, Airport> airports;
    public Airports() {
        this.airports = new HashMap<>();

    }

    public void addAirport(String name, String country, String code, int passengers){
        airports.put(code, new Airport(name, country, code, passengers));
    }
    public void addFlights(String from, String to, int time, int duration){
        airports.get(from).addFlight(from, to, time, duration);
    }

    public void showFlightsFromAirport(String code){
        Airport airport = airports.get(code);
        System.out.printf("%s (%s)%n", airport.getName(), airport.getCode());
        System.out.println(airport.getCountry());
        System.out.println(airport.getPassengers());
        int i = 1;
        for (String to : airport.getFlights().keySet()){
            Set<Flight> flights = airport.getFlights().get(to);
            for (Flight f : flights) {
                System.out.printf("%d. %s%n", i++, f);
            }
        }
    }
    public void showDirectFlightsFromTo(String from, String to){
        Airport origin = airports.get(from);
        Set<Flight> flights = origin.getFlights().get(to);
        if (flights == null){
            System.out.println("No flights from " + from + " to " + to);
        } else {
            for (Flight f : flights){
                System.out.println(f);
            }
        }
    }
    public void showDirectFlightsTo(String to){
        Set<Flight> flights = new TreeSet<>();
        for (Airport airport : airports.values()){
            Set<Flight> flightsTo = airport.getFlights().get(to);
            if (flightsTo != null){
                flights.addAll(flightsTo);
            }
        }
        for (Flight f : flights){
            System.out.println(f);
        }
    }

}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


