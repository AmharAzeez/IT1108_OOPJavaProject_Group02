package view;

import controller.FarmerController;
import model.Farmer;
import service.FarmerService;

import javax.swing.*;

public class FarmerManagementView extends JFrame {
    // These are injected by the Designer based on your bindings
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField locField;
    private JButton registerButton;
    private JList<String> farmerList;

    // Our own fields
    private final FarmerController controller = new FarmerController(new FarmerService());
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public FarmerManagementView() {
        // connect list to model
        farmerList.setModel(listModel);

        // button click
        registerButton.addActionListener(e -> registerFarmer());
    }

    private void registerFarmer() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String loc = locField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "ID and Name are required.");
            return;
        }

        // check duplicate ID
        if (controller.find(id).isPresent()) {
            JOptionPane.showMessageDialog(mainPanel, "Farmer ID already exists!");
            return;
        }

        Farmer f = new Farmer(id, name, loc);
        controller.register(f);
        listModel.addElement(f.toString());

        // clear fields
        idField.setText("");
        nameField.setText("");
        locField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Farmer Management");
            frame.setContentPane(new FarmerManagementView().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setVisible(true);
        });
    }
}
