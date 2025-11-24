package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.Farmer;

public class MainDashboard extends JFrame {

    private final List<Farmer> farmers = new ArrayList<>();

    public MainDashboard() {
        setTitle("EcoFarm Connect - Main Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 350);
        setLayout(new GridLayout(3, 3, 10, 10));
        setLocationRelativeTo(null);

        JButton cropsBtn = new JButton("Crop Management");
        JButton farmersBtn = new JButton("Farmer Management");
        JButton inventoryBtn = new JButton("Inventory Management");
        JButton marketBtn = new JButton("Marketplace");

        JButton analyticsBtn = new JButton("Analytics Dashboard");
        JButton notifyBtn = new JButton("Notification System");
        JButton traceBtn = new JButton("Traceability System");

        cropsBtn.addActionListener(e -> new CropManagementView().setVisible(true));
        farmersBtn.addActionListener(e -> new FarmerManagementView().setVisible(true));
        inventoryBtn.addActionListener(e -> new InventoryManagementView().setVisible(true));
        marketBtn.addActionListener(e -> new MarketplaceView().setVisible(true));

        analyticsBtn.addActionListener(e -> new AnalyticsDashboardView(farmers).setVisible(true));
        notifyBtn.addActionListener(e -> new NotificationView(farmers).setVisible(true));
        traceBtn.addActionListener(e -> new TraceabilityView().setVisible(true));

        add(cropsBtn);
        add(farmersBtn);
        add(inventoryBtn);
        add(marketBtn);
        add(analyticsBtn);
        add(notifyBtn);
        add(traceBtn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard().setVisible(true));
    }
}