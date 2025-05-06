import java.sql.*;
import java.util.Scanner;

public class StudentCRUD {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n=== Student Management System ===");
            System.out.println("[1] Insert Student");
            System.out.println("[2] Retrieve Student");
            System.out.println("[3] Update Student");
            System.out.println("[4] Delete Student");
            System.out.println("[5] Exit");
            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> insertStudent();
                case 2 -> retrieveStudent();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> System.out.println("Exiting program...");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 5);
    }

    private static void insertStudent() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            System.out.print("Enter student course: ");
            String course = scanner.nextLine();

            String sql = "INSERT INTO students (name, course) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, course);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Student inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveStudent() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter student ID to retrieve: ");
            int id = Integer.parseInt(scanner.nextLine());

            String sql = "SELECT name, course FROM students WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Course: " + rs.getString("course"));
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateStudent() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter student ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            String checkSql = "SELECT * FROM students WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Current Name: " + rs.getString("name"));
                System.out.println("Current Course: " + rs.getString("course"));

                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();

                System.out.print("Enter new course: ");
                String newCourse = scanner.nextLine();

                String updateSql = "UPDATE students SET name = ?, course = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newName);
                updateStmt.setString(2, newCourse);
                updateStmt.setInt(3, id);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Student updated successfully.");
                }
            } else {
                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteStudent() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.print("Enter student ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            String checkSql = "SELECT name FROM students WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.print("Are you sure you want to delete this student? (yes/no): ");
                String confirm = scanner.nextLine();

                if (confirm.equalsIgnoreCase("yes")) {
                    String deleteSql = "DELETE FROM students WHERE id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                    deleteStmt.setInt(1, id);

                    int rowsDeleted = deleteStmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Student deleted successfully.");
                    }
                } else {
                    System.out.println("Deletion cancelled.");
                }
            } else {
                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}