import java.util.ArrayList;

public class Student {
    public String studentId;
    private String firstName;
    private String lastName;
    private ArrayList<Assignment> assignments = new ArrayList<>();

    public Student(String id, String first, String last) {
        this.studentId = id;
        this.firstName = first;
        this.lastName = last;
    }

    public void addAssignment(Assignment a) {
        assignments.add(a);
    }

    public double getFinalGrade() { // final grade of students
        double earned = 0, max = 0;
        for (Assignment a : assignments) {
            earned += a.getEarnedPoints();
            max += a.getMaxPoints();
        }
        return max == 0 ? 0 : (earned / max) * 100;
    }

    public void printReport() { // will display in order for the assignments and students
        System.out.println(lastName + " , " + firstName + ":");
        for (Assignment a : assignments) {
            System.out.println(" Assn" + a.getNumber() + ":" + a.getEarnedPoints() + "/" + a.getMaxPoints());
        }
        System.out.printf(" Final Grade: %.2f%%\n", getFinalGrade());
    }

}
