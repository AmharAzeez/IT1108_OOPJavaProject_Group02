package view;

import controller.InventoryController;
import model.InventoryItem;
import service.InventoryService;

import javax.swing.*;
import java.awt.*;

/**
 * Basic inventory UI demonstrating add, list and low-stock check.
 */
public class InventoryManagementView extends JFrame {
    private final InventoryController controller;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public InventoryManagementView() {
        this.controller = new InventoryController(new InventoryService());
        setTitle("Inventory Management");
        setSize(600, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout());
        JTextField idField = new JTextField(6);
        JTextField nameField = new JTextField(10);
        JTextField qtyField = new JTextField(4);
        JTextField unitField = new JTextField(4);
        JButton addBtn = new JButton("Add Item");
        top.add(new JLabel("ID:")); top.add(idField);
        top.add(new JLabel("Name:")); top.add(nameField);
        top.add(new JLabel("Qty:")); top.add(qtyField);
        top.add(new JLabel("Unit:")); top.add(unitField);
        top.add(addBtn);

        JList<String> list = new JList<>(listModel);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                InventoryItem it = new InventoryItem(idField.getText().trim(), nameField.getText().trim(),
                        "General", Integer.parseInt(qtyField.getText().trim()), unitField.getText().trim());
                controller.add(it);
                listModel.addElement(it.toString());
                idField.setText(""); nameField.setText(""); qtyField.setText(""); unitField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity");
            }
        });
    }
}