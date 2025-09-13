import java.sql.*;
import java.util.*;

public class StudentDAO {
    private Connection conn;
    private Statement stmt;

    public StudentDAO() {
        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:classDB.db");
            if (conn != null) {
                stmt = conn.createStatement();

            } else {

            }
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    // Display all students
    public void displayAllStudents() {
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM studentTable")) {
            while (rs.next()) {
                System.out.println("Id:" + rs.getString("student_id") + "-" + rs.getString("last_name")
                        + " ," + rs.getString("first_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error searching all students:");
            e.printStackTrace();
        }
    }

    // Display all students with their grades
    public void displayAllStudentsWithGrades() {
        try (PreparedStatement psStudents = conn.prepareStatement("SELECT * FROM studentTable");
                ResultSet rs = psStudents.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("student_id");
                System.out.println(rs.getString("last_name") + ", " + rs.getString("first_name") + ":");
                try (ResultSet grades = stmt.executeQuery("SELECT * FROM assnTable WHERE student_id = '" + id + "'")) {
                    while (grades.next()) {
                        System.out.println(
                                "  Assn " + grades.getInt("assn_num") + ": " + grades.getInt("earned_points") + "/"
                                        + grades.getInt("max_points"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching students with grades:");
            e.printStackTrace();
        }
    }

    // Display grades for a specific student based on their name or ID
    public void displaySpecificStudent(Scanner scanner) {
        try {
            System.out.print("Search by (1) Name or (2) ID: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            if (option == 1) {
                System.out.print("Enter last name: ");
                String last = scanner.nextLine();
                System.out.print("Enter first name: ");
                String first = scanner.nextLine();
                try (PreparedStatement ps = conn
                        .prepareStatement("SELECT * FROM studentTable WHERE first_name = ? AND last_name = ?")) {
                    ps.setString(1, first);
                    ps.setString(2, last);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            showStudentWithGrades(rs.getString("student_id"));
                        } else {
                            System.out.println("No student found with the given name.");
                        }
                    }
                }
            } else if (option == 2) {
                System.out.print("Enter student ID: ");
                String id = scanner.nextLine();
                showStudentWithGrades(id);
            } else {
                System.out.println("Invlaid number");
            }
        } catch (SQLException e) {
            System.err.println("Error searching specific student:");
            e.printStackTrace();
        }
    }

    // Show student with their grades
    private void showStudentWithGrades(String studentId) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM studentTable WHERE student_id = ?")) {
            ps.setString(1, studentId);
            try (ResultSet student = ps.executeQuery()) {
                if (student.next()) {
                    System.out.println(student.getString("last_name") + ", " + student.getString("first_name") + ":");
                }
            }

            try (PreparedStatement psGrades = conn.prepareStatement("SELECT * FROM assnTable WHERE student_id = ?")) {
                psGrades.setString(1, studentId);
                try (ResultSet rs = psGrades.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("  Assn " + rs.getInt("assn_num") + ": " + rs.getInt("earned_points") + "/"
                                + rs.getInt("max_points"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching grades for student with ID: " + studentId);
            e.printStackTrace();
        }
    }

    // Search students by last name (using LIKE) of the prefix of last name
    public void searchByLastName(Scanner scanner) {
        try {
            System.out.print("Enter last name prefix: ");
            String prefix = scanner.nextLine();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM studentTable WHERE last_name LIKE ?")) {
                ps.setString(1, prefix + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        showStudentWithGrades(rs.getString("student_id"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching students by last name:");
            e.printStackTrace();
        }
    }

    // Calculate average score for a specific assignment
    public void calculateAssignmentAverage(Scanner scanner) {
        try {
            System.out.print("Enter assignment number: ");
            int num = scanner.nextInt();
            try (PreparedStatement ps = conn
                    .prepareStatement("SELECT AVG(earned_points) AS avg_score FROM assnTable WHERE assn_num = ?")) {
                ps.setInt(1, num);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Average score for assignment " + num + ": " + rs.getDouble("avg_score"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error calculating assignment average:");
            e.printStackTrace();
        }
    }

    // Calculate final grades for each student
    public void calculateFinalGrades() {
        try {
            Map<String, Student> students = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM studentTable")) {
                while (rs.next()) {
                    Student s = new Student(rs.getString("student_id"), rs.getString("first_name"),
                            rs.getString("last_name"));
                    students.put(s.studentId, s);
                }
            }

            try (ResultSet rs = stmt.executeQuery("SELECT * FROM assnTable")) {
                while (rs.next()) {
                    String studentId = rs.getString("student_id");
                    Student student = students.get(studentId);
                    if (student != null) {
                        Assignment a = new Assignment(rs.getInt("assn_num"), rs.getInt("max_points"),
                                rs.getInt("earned_points"));
                        student.addAssignment(a);
                    }
                }
            }

            // Print the final report for each student
            for (Student s : students.values()) {
                s.printReport();
            }
        } catch (SQLException e) {
            System.err.println("Error calculating final grades:");
            e.printStackTrace();
        }
    }

}
