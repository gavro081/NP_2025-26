package k1.z23_Audition;

import java.util.*;

class Candidate implements Comparable<Candidate>{
    private final String code;
    private final String name;
    private final int age;
    private final String city;

    public Candidate(String code, String name, int age, String city) {
        this.code = code;
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Candidate o) {
        int diff = this.name.compareTo(o.name);
        if (diff != 0) return diff;
        diff = this.age - o.age;
        return diff != 0 ? diff : this.code.compareTo(o.code);
    }

    public String getCity() {
        return city;
    }

    public String toString(){
        return code + " " + name + " " + age;
    }
}

class Audition {
    ArrayList<Candidate> candidates;
    HashMap<String, HashSet<String>> codes;

    Audition(){
        candidates = new ArrayList<>();
        codes = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age){
        codes.putIfAbsent(city, new HashSet<>());
        HashSet<String> cityCodes = codes.get(city);
        if (cityCodes.contains(code)) return;
        candidates.add(new Candidate(code, name, age, city));
        cityCodes.add(code);
    }

    public void listByCity(String city){
        candidates.stream().
                filter(s -> s.getCity().equals(city))
                .sorted()
                .forEach(System.out::println);

    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
