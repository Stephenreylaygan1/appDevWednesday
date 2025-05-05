import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LibraryManagementSystem extends JFrame {
    private Map<String, Boolean> books;
    private Map<String, String> titleLookup;

    private JTextField bookTitleTextField;
    private JButton addBookButton;
    private JButton checkoutBookButton;
    private JTextArea bookListTextArea;

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setLayout(new BorderLayout());
        books = new LinkedHashMap<>();
        titleLookup = new HashMap<>();

        JPanel inputPanel = new JPanel(new FlowLayout());
        bookTitleTextField = new JTextField(20);
        inputPanel.add(new JLabel("Book Title:"));
        inputPanel.add(bookTitleTextField);

        // Add Book Button
        addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = bookTitleTextField.getText().trim();
                String lowerTitle = title.toLowerCase();
                if (!title.isEmpty()) {
                    if (!titleLookup.containsKey(lowerTitle)) {
                        books.put(title, true);
                        titleLookup.put(lowerTitle, title);
                        updateBookList();
                    } else {
                        JOptionPane.showMessageDialog(null, "Book already exists.");
                    }
                    bookTitleTextField.setText("");
                }
            }
        });
        inputPanel.add(addBookButton);

        // Checkout Book Button
        checkoutBookButton = new JButton("Checkout Book");
        checkoutBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = bookTitleTextField.getText().trim();
                String lowerInput = input.toLowerCase();

                if (titleLookup.containsKey(lowerInput)) {
                    String originalTitle = titleLookup.get(lowerInput);
                    if (books.get(originalTitle)) {
                        books.put(originalTitle, false);
                        updateBookList();
                        JOptionPane.showMessageDialog(null, "Book checked out: " + originalTitle);
                    } else {
                        JOptionPane.showMessageDialog(null, "Book is already checked out.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found.");
                }
                bookTitleTextField.setText("");
            }
        });
        inputPanel.add(checkoutBookButton);
        add(inputPanel, BorderLayout.NORTH);

        bookListTextArea = new JTextArea(20, 40);
        bookListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookListTextArea);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void updateBookList() {
        bookListTextArea.setText("");
        for (Map.Entry<String, Boolean> entry : books.entrySet()) {
            String status = entry.getValue() ? "Available" : "Checked Out";
            bookListTextArea.append(entry.getKey() + " - " + status + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSystem::new);
    }
}