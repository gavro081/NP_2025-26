//package k1.z50_faculty;

import java.util.*;

class OperationNotAllowedException extends Exception{
    OperationNotAllowedException(String message){
        super(message);
    }
}

class CourseInfo implements Comparable<CourseInfo>{
    private final String courseName;
    private int studentCount;
    private int totalGrades;

    public CourseInfo(String courseName) {
        this.courseName = courseName;
        this.studentCount = 0;
        this.totalGrades = 0;
    }

    public void addGrade(int grade){
        studentCount++;
        totalGrades+=grade;
    }

    private double averageGrade(){
        return (double) totalGrades / studentCount;
    }

    @Override
    public int compareTo(CourseInfo o) {
        int diff = Integer.compare(studentCount, o.studentCount);
        if (diff != 0) return diff;
        diff = Double.compare(averageGrade(), o.averageGrade());
        return diff != 0 ? diff : this.courseName.compareTo(o.courseName);
    }

    @Override
    public String toString(){
        return String.format("%s %d %.2f", courseName, studentCount, averageGrade());
    }
}

class Course {
    private final String courseName;
    private final int grade;

    public Course(String courseName, int grade) {
        this.courseName = courseName;
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public String getCourseName() {
        return courseName;
    }
}

class Student implements Comparable<Student>{
    private final String id;
    private final int yearOfStudies;
    private List<Set<Course>> gradesBySemester;
    private Set<String> allCourses;

    public Student(String id, int yearOfStudies) {
        this.id = id;
        this.yearOfStudies = yearOfStudies;
        this.gradesBySemester = new ArrayList<>();
        this.allCourses = new TreeSet<>();
        for (int i = 0; i < yearOfStudies * 2; i++) {
            gradesBySemester.add(new HashSet<>());
        }
    }

    private int getPassedSubjects(){
        return gradesBySemester.stream().mapToInt(Set::size).sum();
    }

    public double getAverageGrade(){
        int passed = getPassedSubjects();
        if (passed == 0) return 5d;
        return gradesBySemester.stream()
                .flatMap(Set::stream)
                .mapToInt(Course::getGrade)
                .sum() / (double) passed;
    }

    public int getYearOfStudies() {
        return yearOfStudies;
    }

    private List<String> allCourses() {
        return new ArrayList<>(allCourses);
    }

    public boolean isGraduated(){
        if (yearOfStudies == 3) return getPassedSubjects() == 18;
        if (yearOfStudies == 4) return getPassedSubjects() == 24;
        return false;
    }

    private double averageGradeForTerm(int term){
        Set<Course> courses = gradesBySemester.get(term);
        int size = courses.size();
        if (size == 0) return 5d;
        return (double) courses.stream().mapToInt(Course::getGrade).sum() / size;
    }

    @Override
    public int compareTo(Student o) {
        int diff = -Integer.compare(this.getPassedSubjects(), o.getPassedSubjects());
        if (diff != 0) return diff;
        diff = Double.compare(o.getAverageGrade(), this.getAverageGrade());
        return diff != 0 ? diff : -id.compareTo(o.id);
    }

    @Override
    public String toString(){
        return String.format("Student: %s Courses passed: %d Average grade: %.2f",
                id, getPassedSubjects(),getAverageGrade());
    }

    public String report(){
        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(id).append("\n");
        for (int i = 0; i < gradesBySemester.size(); i++) {
            Set<Course> courses = gradesBySemester.get(i);
            sb.append("Term ").append(i + 1).append("\n");
            sb.append("Courses: ").append(courses.size()).append("\n");
            sb.append("Average grade for term: ").append(String.format("%.2f",averageGradeForTerm(i))).append("\n");
        }
        sb.append("Average grade: ").append(String.format("%.2f",getAverageGrade())).append("\n");
        sb.append("Courses attended: ");
        String courses = allCourses.toString();
        courses = courses.replaceAll(" ","");
        sb.append(courses, 1, courses.length() -1);
//        ArrayList<String> courses = new ArrayList<>(allCourses);
//        for (int i = 0; i < courses.size(); i++) {
//            String course = courses.get(i);
//            if (i != courses.size() - 1){
//                sb.append(course).append(",");
//            } else sb.append(course);
//        }
        return sb.toString();
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        int maxTerms = yearOfStudies * 2;
        if (term >= maxTerms)
            throw new OperationNotAllowedException
                    ("Term "+ (term+1) +" is not possible for student with ID " + id);
        Set<Course> courses = gradesBySemester.get(term);
        if (courses.size() == 3)
            throw new OperationNotAllowedException
                    ("Student " + id + " already has 3 grades in term " + (term+1));
        courses.add(new Course(courseName, grade));
        allCourses.add(courseName);
    }
}

class Faculty {
    private ArrayList<String> logs;
    private Map<String, Student> students;
    private Set<Student> sortedStudents;
    private Set<CourseInfo> allCourseInfos;
    private Map<String, CourseInfo> nameToCourseInfo;

    public Faculty() {
        logs = new ArrayList<>();
        students = new HashMap<>();
        sortedStudents = new TreeSet<>();
        allCourseInfos = new TreeSet<>();
        nameToCourseInfo = new HashMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        Student s = new Student(id, yearsOfStudies);
        students.put(id, s);
        sortedStudents.add(s);
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student student = students.get(studentId);

        nameToCourseInfo.putIfAbsent(courseName, new CourseInfo(courseName));
        CourseInfo courseInfo = nameToCourseInfo.get(courseName);

        sortedStudents.remove(student);
        allCourseInfos.remove(courseInfo);
        try{
            student.addGrade(term -1, courseName, grade);
            courseInfo.addGrade(grade);

            if (student.isGraduated()){
                students.remove(studentId);
                logs.add(
                        String.format("Student with ID %s graduated with average grade %.2f in %d years.",
                                studentId, student.getAverageGrade(), student.getYearOfStudies()));
            } else {
                sortedStudents.add(student);
            }
            allCourseInfos.add(courseInfo);
        } catch (OperationNotAllowedException e){
            sortedStudents.add(student);
            allCourseInfos.add(courseInfo);
            throw e;
        }
    }

    String getFacultyLogs() {
        return String.join("\n", logs);
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).report();
    }

    void printFirstNStudents(int n) {
        sortedStudents.stream()
                .limit(n)
                .forEach(System.out::println);
    }

    void printCourses() {
        allCourseInfos.forEach(System.out::println);
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}


