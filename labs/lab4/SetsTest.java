package labs.lab4;

import java.util.*;
import java.util.stream.Collectors;

class AverageGradeComparator implements Comparator<Student>{
    @Override
    public int compare(Student o1, Student o2) {
        int diff = Double.compare(o2.averageGrade(), o1.averageGrade());
        if (diff != 0) return diff;
        diff = Integer.compare(o2.getGradeCount(), o1.getGradeCount());
        return diff != 0 ? diff : o1.getId().compareTo(o2.getId());
    }
}

class GradeCountComparator implements Comparator<Student>{
    @Override
    public int compare(Student o1, Student o2) {
        int diff = Integer.compare(o2.getGradeCount(), o1.getGradeCount());
        if (diff != 0) return diff;
        diff = Double.compare(o2.averageGrade(), o1.averageGrade());
        return diff != 0 ? diff : o1.getId().compareTo(o2.getId());
    }
}

class Student {
    private final String id;
    private final List<Integer> grades;

    Student(String id, List<Integer> grades){
        this.id = id;
        this.grades = grades;
    }

    public void addGrade(int grade){
        grades.add(grade);
    }

    public double averageGrade(){
        return grades
                .stream()
                .mapToDouble(Integer::doubleValue)
                .average().getAsDouble();
    }

    public int getGradeCount(){
        return grades.size();
    }

    public String getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

class Faculty {
    Map<String, Student> students;

    public Faculty() {
        this.students = new HashMap<>();
    }

    public void addStudent(String id, List<Integer> grades) throws Exception{
        if (students.containsKey(id)) throw new Exception("Student with ID " + id + " already exists");
        students.put(id, new Student(id, grades));
    }

    public void addGrade(String id, int grade){
        students.get(id).addGrade(grade);
    }

    Set<Student> getStudentsSortedByAverageGrade(){
        return students.values().stream()
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(new AverageGradeComparator())));
    }
    Set<Student> getStudentsSortedByCoursesPassed(){
        return students.values().stream()
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(new GradeCountComparator())));
    }

}

public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}

