package view;

import controller.CropController;
import model.Crop;
import service.SmartCropService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple crop management: add crop, list crops, predict harvest, water estimate.
 */
public class CropManagementView extends JFrame {
    private final CropController controller;
    private final List<Crop> crops = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public CropManagementView() {
        this.controller = new CropController(new SmartCropService());
        setTitle("Crop Management");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(10);
        JTextField varietyField = new JTextField(8);
        JButton addBtn = new JButton("Add Crop");
        top.add(new JLabel("Name:")); top.add(nameField);
        top.add(new JLabel("Variety:")); top.add(varietyField);
        top.add(addBtn);

        JList<String> cropList = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(cropList);

        JPanel bottom = new JPanel(new FlowLayout());
        JButton predictBtn = new JButton("Predict Harvest");
        JButton waterBtn = new JButton("Estimate Daily Water");
        bottom.add(predictBtn); bottom.add(waterBtn);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String id = "C" + (crops.size() + 1);
            Crop c = new Crop(id, nameField.getText(), varietyField.getText(), 0.5, LocalDate.now());
            crops.add(c);
            listModel.addElement(c.toString());
            nameField.setText(""); varietyField.setText("");
            JOptionPane.showMessageDialog(this, "Added: " + c);
        });

        predictBtn.addActionListener(e -> {
            int idx = cropList.getSelectedIndex();
            if (idx >= 0) {
                Crop selected = crops.get(idx);
                String pred = controller.getHarvestPrediction(selected);
                JOptionPane.showMessageDialog(this, pred);
            } else {
                JOptionPane.showMessageDialog(this, "Select a crop first.");
            }
        });

        waterBtn.addActionListener(e -> {
            double total = controller.calculateTotalDailyWater(crops);
            JOptionPane.showMessageDialog(this, "Estimated total daily water (units): " + total);
        });
    }
}