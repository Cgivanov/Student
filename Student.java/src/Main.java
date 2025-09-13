/*
 * Assignment Title : Student Gradebook JDBC assignment
 * Course : CPI 221
 * Name : Christian Ivanov
 * Date: April 17 2025
 * Description: This program managed a system for tracking the students and their assignment grades
 * Connects to SQLite database and performs the students grades adn finasl grades
 * Users can view all students by searching name or ID and Calc final grades
 * The program uses JDBC and preaperd statemnts for the database connection
 */

import java.util.Scanner;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        StudentDAO dao = new StudentDAO();
        while (true) { // user needs to choice a option
            System.out.println("\nTHE STUDENTS GRADE SYSTEM");
            System.out.println("1. Display all students");
            System.out.println("2. Display all students and grades");
            System.out.println("3. Display grades for specific student");
            System.out.println("4. Search students by last name (LIKE)"); // id and name will pop up user will choice
                                                                          // either or
            System.out.println("5. Class average for assignment");
            System.out.println("6. Calculate and display final grades for each of the students");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            scanner.nextLine(); // newline

            switch (choice) {
                case 1 -> dao.displayAllStudents();
                case 2 -> dao.displayAllStudentsWithGrades();
                case 3 -> dao.displaySpecificStudent(scanner);
                case 4 -> dao.searchByLastName(scanner);
                case 5 -> dao.calculateAssignmentAverage(scanner);
                case 6 -> dao.calculateFinalGrades();
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
