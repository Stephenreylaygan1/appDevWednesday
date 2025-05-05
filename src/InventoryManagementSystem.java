import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InventoryManagementSystem extends JFrame {
    private Map<String, Integer> inventory = new HashMap<>();

    private JTextField itemNameTextField = new JTextField(15);
    private JTextField quantityTextField = new JTextField(5);
    private JTextArea inventoryListTextArea = new JTextArea(15, 35);

    public InventoryManagementSystem() {
        setTitle("Inventory Management System");
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameTextField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityTextField);

        JButton addItemButton = new JButton("Add Item");
        JButton updateButton = new JButton("Update Quantity");

        inputPanel.add(addItemButton);
        inputPanel.add(updateButton);
        add(inputPanel, BorderLayout.NORTH);

        // Inventory display
        inventoryListTextArea.setEditable(false);
        add(new JScrollPane(inventoryListTextArea), BorderLayout.CENTER);

        // Add Item Logic
        addItemButton.addActionListener(_ -> {
            String item = itemNameTextField.getText().trim();
            String qtyStr = quantityTextField.getText().trim();

            if (item.isEmpty() || qtyStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both item name and quantity.");
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyStr);

                // If item exists, add to quantity; else, create new entry
                inventory.put(item, inventory.getOrDefault(item, 0) + quantity);

                updateInventoryList();
                itemNameTextField.setText("");
                quantityTextField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
            }
        });

        // Update Quantity Logic
        updateButton.addActionListener(_ -> {
            String item = itemNameTextField.getText().trim();
            String qtyStr = quantityTextField.getText().trim();

            if (item.isEmpty() || qtyStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both item name and quantity.");
                return;
            }

            if (!inventory.containsKey(item)) {
                JOptionPane.showMessageDialog(this, "Item not found.");
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyStr);
                inventory.put(item, quantity); // Replace quantity with new one
                updateInventoryList();
                itemNameTextField.setText("");
                quantityTextField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateInventoryList() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            sb.append(entry.getKey()).append(" - Quantity: ").append(entry.getValue()).append("\n");
        }
        inventoryListTextArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagementSystem::new);
    }
}