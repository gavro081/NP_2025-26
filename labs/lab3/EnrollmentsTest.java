package labs.lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

// TODO: Add classes and implement methods
class Applicant implements Comparable<Applicant>{
    private int id;
    private String name;
    private double gpa;
    private List<SubjectWithGrade> subjectsWithGrade;
    private StudyProgramme studyProgramme;

    public Applicant(int id, String name, double gpa, StudyProgramme studyProgramme) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.studyProgramme = studyProgramme;
        this.subjectsWithGrade = new ArrayList<>();
    }

    public void addSubjectAndGrade(String subject, int grade){
        subjectsWithGrade.add(new SubjectWithGrade(subject, grade));
    }

    public double calculatePoints(){
        double points = 0;
        for (SubjectWithGrade s: subjectsWithGrade){
            if (studyProgramme.getFaculty().isSubjectAppropriate(s.getSubject())){
                points += 2 * s.getGrade();
            } else points += 1.2 * s.getGrade();
        }
        points += gpa * 12;
        return points;
    }

    @Override
    public int compareTo(Applicant o) {
        return Double.compare(calculatePoints(), o.calculatePoints());
    }

    @Override
    public String toString(){
        return "Id: " + id + ", Name: " + name + ", GPA: " +
                gpa + " - " + calculatePoints();
    }
}

class StudyProgramme{
    private final String code;
    private final String name;
    private final int numPublicQuota;
    private final int numPrivateQuota;
    private int enrolledInPublicQuota;
    private int enrolledInPrivateQuota;
    private List<Applicant> applicants;
    private final Faculty faculty;

    public StudyProgramme(String code, String name, Faculty f, int numPublicQuota, int numPrivateQuota) {
        this.code = code;
        this.name = name;
        this.numPublicQuota = numPublicQuota;
        this.numPrivateQuota = numPrivateQuota;
        this.enrolledInPublicQuota = 0;
        this.enrolledInPrivateQuota = 0;
        faculty = f;
        applicants = new ArrayList<>();
    }

    public Faculty getFaculty(){
        return this.faculty;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void addApplicant(Applicant a){
        applicants.add(a);
        if (enrolledInPublicQuota < numPublicQuota)
            enrolledInPublicQuota++;
        else if (enrolledInPrivateQuota < numPrivateQuota)
            enrolledInPrivateQuota++;

    }

    public int getEnrolledInPublicQuota() {
        return enrolledInPublicQuota;
    }

    public int getEnrolledInPrivateQuota() {
        return enrolledInPrivateQuota;
    }

    public double accepted(){
        return ((double)(enrolledInPrivateQuota + enrolledInPublicQuota) /
                (numPrivateQuota + numPublicQuota)) * 100;
    }

    void calculateEnrollmentNumbers(){
    }



    @Override
    public String toString(){
        applicants.sort(Comparator.reverseOrder());
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(name).append("\n");
        sb.append("Public Quota:\n");
        for (int i = 0; i < Math.min(applicants.size(), numPublicQuota); i++) {
            sb.append(this.applicants.get(i)).append("\n");
        }
        sb.append("Private Quota:\n");
        for (int i = numPublicQuota; i < Math.min(applicants.size(), numPrivateQuota + numPublicQuota); i++) {
            sb.append(this.applicants.get(i)).append("\n");
        }
        sb.append("Rejected: \n");
        for (int i = numPublicQuota + numPrivateQuota; i < applicants.size(); i++) {
            sb.append(this.applicants.get(i)).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

}

class Faculty {
    private String shortName;
    private List<String> appropriateSubjects;
    private List<StudyProgramme> studyProgrammes;

    Faculty(String s){
        this.shortName = s;
        appropriateSubjects = new ArrayList<>();
        studyProgrammes = new ArrayList<>();
    }

    public boolean isSubjectAppropriate(String subj){
        return appropriateSubjects.stream().anyMatch(s -> s.equals(subj));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Faculty: ").append(this.shortName).append("\n");
        sb.append("Subjects: ");
        sb.append(appropriateSubjects);
        sb.append("\n");
        sb.append("Study Programmes: \n");
        studyProgrammes.stream()
                .sorted(Comparator
                        .comparing(StudyProgramme::accepted)
                        .reversed())
                .forEach(sb::append);
        return sb.toString();
    }

    public void addSubject(String s){
        appropriateSubjects.add(s);
    }

    public void addStudyProgramme(StudyProgramme si) {
        studyProgrammes.add(si);
    }
}

class SubjectWithGrade
{
    private String subject;
    private int grade;
    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public int getGrade() {
        return grade;
    }
}

class EnrollmentsIO {
    public static void printRanked(List<Faculty> faculties) {
        faculties.forEach(System.out::print);
    }

    public static void readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while (true){
            String line = br.readLine();
            if (line == null || line.trim().isEmpty() ) break;
            String []parts = line.split(";");
            StudyProgramme sp = studyProgrammes.stream()
                    .filter(s ->
                            s.getCode().equals(parts[parts.length - 1]))
                    .findFirst().get();
            Applicant a = new Applicant(Integer.parseInt(parts[0]), parts[1],Double.parseDouble(parts[2]), sp);
            for (int i = 3; i < parts.length - 1; i+=2) {
                String subject = parts[i];
                int grade = Integer.parseInt(parts[i + 1]);
                a.addSubjectAndGrade(subject, grade);
            }
            sp.addApplicant(a);
        }
    }
}

public class EnrollmentsTest {

    public static void main(String[] args) throws IOException {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        EnrollmentsIO.readEnrollments(allProgrammes, System.in);

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);

    }


}
