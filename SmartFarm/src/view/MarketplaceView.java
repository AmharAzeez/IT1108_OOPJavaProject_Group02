package view;

import controller.MarketplaceController;
import model.Order;
import service.MarketplaceService;

import javax.swing.*;
import java.awt.*;

/**
 * Minimal marketplace view: place orders and list them.
 */
public class MarketplaceView extends JFrame {
    private final MarketplaceController controller;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public MarketplaceView() {
        this.controller = new MarketplaceController(new MarketplaceService());
        setTitle("Marketplace");
        setSize(600, 360);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout());
        JTextField cropField = new JTextField(6);
        JTextField farmerField = new JTextField(6);
        JTextField consumerField = new JTextField(10);
        JTextField qtyField = new JTextField(4);
        JButton placeBtn = new JButton("Place Order");
        top.add(new JLabel("CropId:")); top.add(cropField);
        top.add(new JLabel("FarmerId:")); top.add(farmerField);
        top.add(new JLabel("Consumer:")); top.add(consumerField);
        top.add(new JLabel("Qty:")); top.add(qtyField);
        top.add(placeBtn);

        JList<String> list = new JList<>(listModel);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        placeBtn.addActionListener(e -> {
            try {
                Order o = controller.placeOrder(cropField.getText().trim(), farmerField.getText().trim(),
                        consumerField.getText().trim(), Integer.parseInt(qtyField.getText().trim()));
                listModel.addElement(o.toString());
                cropField.setText(""); farmerField.setText(""); consumerField.setText(""); qtyField.setText("");
                JOptionPane.showMessageDialog(this, "Order placed: " + o.getId());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number.");
            }
        });
    }
}