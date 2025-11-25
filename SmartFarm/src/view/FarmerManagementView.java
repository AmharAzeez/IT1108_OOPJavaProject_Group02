package view;

import controller.FarmerController;
import model.Farmer;
import service.FarmerService;

import javax.swing.*;
import java.awt.*;

/**
 * Manage farmers: register and list.
 */
public class FarmerManagementView extends JFrame {
    private final FarmerController controller;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public FarmerManagementView() {
        this.controller = new FarmerController(new FarmerService());
        setTitle("Farmer Management");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout());
        JTextField idField = new JTextField(6);
        JTextField nameField = new JTextField(10);
        JTextField locField = new JTextField(8);
        JButton regBtn = new JButton("Register Farmer");
        top.add(new JLabel("ID:")); top.add(idField);
        top.add(new JLabel("Name:")); top.add(nameField);
        top.add(new JLabel("Location:")); top.add(locField);
        top.add(regBtn);

        JList<String> list = new JList<>(listModel);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        regBtn.addActionListener(e -> {
            if (idField.getText().isBlank() || nameField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "ID and Name are required.");
                return;
            }
            Farmer f = new Farmer(idField.getText().trim(), nameField.getText().trim(), locField.getText().trim());
            controller.register(f);
            listModel.addElement(f.toString());
            idField.setText(""); nameField.setText(""); locField.setText("");
        });


    }
    public static void main(String[] args) {
        // Make sure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            FarmerManagementView view = new FarmerManagementView();
            view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            view.setVisible(true);
        });
    }

}