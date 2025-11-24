package view;

import controller.AnalyticsController;
import model.Farmer;
import service.AnalyticsService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AnalyticsDashboardView extends JFrame {
    private final AnalyticsController controller;
    private final List<Farmer> farmers;

    public AnalyticsDashboardView(List<Farmer> farmers) {
        this.controller = new AnalyticsController(new AnalyticsService());
        this.farmers = farmers;
        setTitle("Analytics Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Generate Report");
        refreshBtn.addActionListener(e -> {
            Map<String, Object> r = controller.getSustainabilityReport(farmers);
            Map<String, Double> incomes = controller.getIncomeSummary(farmers);

            StringBuilder sb = new StringBuilder("SUSTAINABILITY REPORT\n-------------------\n");
            r.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
            sb.append("\nINCOME SUMMARY\n--------------\n");
            incomes.forEach((k, v) -> sb.append(k).append(": LKR ").append(v).append("\n"));
            reportArea.setText(sb.toString());
        });

        add(refreshBtn, BorderLayout.SOUTH);
    }
}