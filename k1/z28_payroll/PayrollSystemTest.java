package k1.z28_payroll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

abstract class Employee implements Comparable<Employee>{
    private final String id;
    private final String level;
    private final double rate;

    public Employee(String id, String level, double rate) {
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    abstract double getSalary();

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public int compareTo(Employee o) {
        int diff = Double.compare(o.getSalary(), this.getSalary());
        if (diff != 0) return diff;
        diff = this.getLevel().compareTo(o.getLevel());
        if (diff != 0) return diff;
        return this.getId().compareTo(o.getId());
    }
}

class HourlyEmployee extends Employee{
    double hours;

    public HourlyEmployee(String id, String level,double rate, double hours) {
        super(id, level, rate);
        this.hours = hours;
    }

    @Override
    double getSalary() {
        return getRate() * Math.min(40, hours) +
                getRate() * 1.5 * Math.max(hours - 40, 0);
    }

    @Override
    public String toString(){
        return String.format(
                "Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                    getId(), getLevel(), getSalary(), Math.min(hours, 40), Math.max(0, hours - 40)
                );
    }
}
class FreelanceEmployee extends Employee{
    List<Integer> ticketPoints;

    public FreelanceEmployee(String id, String level,double rate, List<Integer> ticketPoints) {
        super(id, level,rate);
        this.ticketPoints = ticketPoints;
    }

    @Override
    double getSalary() {
        return ticketPoints.stream()
                .mapToDouble(i -> (double) i)
                .sum() * getRate();
    }

    @Override
    public String toString(){
        return String.format(
                "Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                getId(), getLevel(), getSalary(),
                ticketPoints.size(),
                (int)ticketPoints.stream().
                        mapToDouble(s -> (double)s).sum()
        );
    }
}

class PayrollSystem{
    List<Employee> employees;
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;

    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<>();
    }
    void readEmployees (InputStream is) throws IOException{
        BufferedReader sc = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = sc.readLine()) != null){
            String []parts = line.split(";");
            if (parts[0].equals("H")){
                employees.add(new HourlyEmployee(parts[1], parts[2], hourlyRateByLevel.get(parts[2]), Double.parseDouble(parts[3])));
            } else if (parts[0].equals("F")) {
                ArrayList<Integer> points = new ArrayList<>();
                for (int i = 3; i < parts.length; i++) {
                    points.add(Integer.parseInt(parts[i]));
                }
                employees.add(new FreelanceEmployee(parts[1], parts[2], ticketRateByLevel.get(parts[2]), points));
            } else break;
        }
    }

    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        Map<String, Set<Employee>> ans = new TreeMap<>();
        employees.stream()
                .filter(e -> levels.contains(e.getLevel()))
                .forEach(s -> {
                    ans.putIfAbsent(s.getLevel(), new TreeSet<>());
                    ans.get(s.getLevel()).add(s);
                });
        return ans;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) throws IOException{

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}