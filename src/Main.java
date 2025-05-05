import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {
    private static int editingId = -1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Student Information Form");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField contactField = new JTextField();

        frame.add(new JLabel("Name:"));
        frame.add(nameField);
        frame.add(new JLabel("Age:"));
        frame.add(ageField);
        frame.add(new JLabel("Course:"));
        frame.add(courseField);
        frame.add(new JLabel("Year:"));
        frame.add(yearField);
        frame.add(new JLabel("Contact:"));
        frame.add(contactField);

        JButton submitButton = new JButton("Submit");
        JButton updateButton = new JButton("Update");
        JButton viewButton = new JButton("View");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        updateButton.setEnabled(false);

        frame.add(submitButton);
        frame.add(updateButton);
        frame.add(viewButton);
        frame.add(editButton);
        frame.add(deleteButton);

        frame.setVisible(true);

        submitButton.addActionListener(_ -> {
            String name = nameField.getText();
            String age = ageField.getText();
            String course = courseField.getText();
            String year = yearField.getText();
            String contact = contactField.getText();

            if (name.isEmpty() || age.isEmpty() || course.isEmpty() || year.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO students (name, age, course, year, contact) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, name);
                stmt.setInt(2, Integer.parseInt(age));
                stmt.setString(3, course);
                stmt.setString(4, year);
                stmt.setString(5, contact);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Record added successfully!");

                nameField.setText("");
                ageField.setText("");
                courseField.setText("");
                yearField.setText("");
                contactField.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        viewButton.addActionListener(_ -> {
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

                StringBuilder records = new StringBuilder();
                while (rs.next()) {
                    records.append("ID: ").append(rs.getInt("id")).append("\n");
                    records.append("Name: ").append(rs.getString("name")).append("\n");
                    records.append("Age: ").append(rs.getInt("age")).append("\n");
                    records.append("Course: ").append(rs.getString("course")).append("\n");
                    records.append("Year: ").append(rs.getString("year")).append("\n");
                    records.append("Contact: ").append(rs.getString("contact")).append("\n\n");
                }

                JOptionPane.showMessageDialog(frame, records.length() > 0 ? records.toString() : "No records found.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error fetching records: " + ex.getMessage());
            }
        });

        editButton.addActionListener(_ -> {
            String idStr = JOptionPane.showInputDialog(frame, "Enter ID to edit:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr.trim());

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE id = ?")) {

                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        nameField.setText(rs.getString("name"));
                        ageField.setText(String.valueOf(rs.getInt("age")));
                        courseField.setText(rs.getString("course"));
                        yearField.setText(rs.getString("year"));
                        contactField.setText(rs.getString("contact"));

                        editingId = id;
                        updateButton.setEnabled(true);
                        submitButton.setEnabled(false);

                    } else {
                        JOptionPane.showMessageDialog(frame, "No record found with ID: " + id);
                    }

                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        updateButton.addActionListener(_ -> {
            if (editingId == -1) {
                JOptionPane.showMessageDialog(frame, "No record selected for editing.");
                return;
            }

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE students SET name=?, age=?, course=?, year=?, contact=? WHERE id=?")) {

                stmt.setString(1, nameField.getText());
                stmt.setInt(2, Integer.parseInt(ageField.getText()));
                stmt.setString(3, courseField.getText());
                stmt.setString(4, yearField.getText());
                stmt.setString(5, contactField.getText());
                stmt.setInt(6, editingId);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Record updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Update failed.");
                }

                editingId = -1;
                nameField.setText("");
                ageField.setText("");
                courseField.setText("");
                yearField.setText("");
                contactField.setText("");
                updateButton.setEnabled(false);
                submitButton.setEnabled(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error updating: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(_ -> {
            String idStr = JOptionPane.showInputDialog(frame, "Enter ID of student to delete:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr.trim());

                try (Connection conn = getConnection();
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {

                    stmt.setInt(1, id);
                    int rows = stmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Record deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "No matching record found.");
                    }

                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/myoop";
        String user = "root";
        String pass = "";
        return DriverManager.getConnection(url, user, pass);
    }
}