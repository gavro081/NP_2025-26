package aud.aud2;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class Student {
    private final String index;
    private String name;
    private int grade;
    private int attendance;

    public Student(String index, String name, int grade, int attendance) {
        this.index = index;
        this.name = name;
        this.grade = grade;
        this.attendance = attendance;
    }

    public int getGrade() {
        return grade;
    }

    public int getAttendance() {
        return attendance;
    }

    @Override
    public String toString() {
        return "Student{" +
                "index='" + index + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", attendance=" + attendance + "%" +
                '}';
    }

    public void setGrade(int grade) {
        if (grade >= 5 && grade <= 10)
            this.grade = grade;
    }

}

class Course {
    private final String title;
    private final Student []students;
    private int studentCount;

    public Course(String title, int capacity) {
        this.title = title;
        students = new Student[capacity];
        studentCount = 0;
    }

    public boolean enroll(Supplier<Student> supplier){
        if (studentCount < students.length){
            students[studentCount++] = supplier.get();
            return true;
        }
        return false;
    }

    void forEach(Consumer<Student> action){
        for (int i = 0; i < studentCount; i++) {
            action.accept(students[i]);
        }
    }

    int count(Predicate<Student> condition){
        int count = 0;
        for (int i = 0; i < studentCount; i++) {
            if (condition.test(students[i])) count++;
        }
        return count;
    }

    Student findFirst(Predicate<Student> condition){
        for (int i = 0; i < studentCount; i++) {
            if (condition.test(students[i])) return students[i];
        }
        return null;
    }

    Student[] filter(Predicate<Student> condition){
        int matches = count(condition);
        Student[] filtered = new Student[matches];
        int ctr = 0;
        for (int i = 0; i < studentCount; i++) {
            if (condition.test(students[i])) filtered[ctr++] = students[i];
        }
        return filtered;
    }

    String[] mapToLabels(Function<Student, String> mapper){
        String[] mappedValues = new String[studentCount];
        for (int i = 0; i < studentCount; i++) {
            mappedValues[i] = mapper.apply(students[i]);
        }
        return mappedValues;
    }

    void mutate(Consumer<Student> mutator){
        for (int i = 0; i < studentCount; i++) {
            mutator.accept(students[i]);
        }
    }

    void conditionalMutate(Predicate<Student> condition,
                           Consumer<Student> mutator) {
        for (int i = 0; i < studentCount; i++) {
            if (condition.test(students[i])) mutator.accept(students[i]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < studentCount; i++) {
            sb.append(students[i]).append('\n');
        }
        return "Course{" +
                "title='" + title + '\'' +
                ", students=\n" + sb +
                ", studentCount=" + studentCount +
                '}';
    }
}

public class CourseDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Course course = new Course("Test Course", 10);

        int n  = Integer.parseInt(scanner.nextLine());

        Supplier<Student> studentSupplier = () -> {
            String index = scanner.nextLine();
            String name = scanner.nextLine();
            int grade = Integer.parseInt(scanner.nextLine());
            int attendance = Integer.parseInt(scanner.nextLine());
            return new Student(index, name, grade, attendance);
        };

        System.out.println("enter student details\n1.index\n2.name\n3.grade\n4.attendance");
        for (int i = 0; i < n; i++) {
            course.enroll(studentSupplier);
        }
        scanner.close();

        System.out.println("enrolled students:");
        course.forEach(System.out::println);
        System.out.println();

        System.out.println("students with passing grade and good attendance");
        Predicate<Student> isPassed = student -> student.getGrade() >= 6;
        Predicate<Student> hasGoodAttendance =
                student -> student.getAttendance() >= 70;
        Student[] passed = course.filter(isPassed.and(hasGoodAttendance));
        for (Student s : passed) System.out.println(s);
        System.out.println();

        System.out.println("first student with grade >= 9");
        Student first = course.findFirst(s -> s.getGrade() >= 9);
        System.out.println(first != null ? first : "No such student");
        System.out.println();


        System.out.println("incrementing all grades by 1");
        course.mutate(s -> s.setGrade(s.getGrade() + 1));
        course.forEach(System.out::println);
        System.out.println();


        System.out.println("incrementing grades by 1 if attendance is above 90%");
        course.conditionalMutate(
                s -> s.getAttendance() > 90,
                s -> s.setGrade(s.getGrade() + 1)
        );

        String[] labels = course.mapToLabels(Student::toString);
        for (String s : labels) System.out.println(s);
    }
}

