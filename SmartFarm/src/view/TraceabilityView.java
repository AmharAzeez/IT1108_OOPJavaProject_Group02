package view;

import controller.TraceabilityController;
import service.TraceabilityService;
import model.Crop;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TraceabilityView extends JFrame {
    private final TraceabilityController controller;
    private String currentTraceId;

    public TraceabilityView() {
        this.controller = new TraceabilityController(new TraceabilityService());
        setTitle("Traceability");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        add(new JScrollPane(area), BorderLayout.CENTER);

        JTextField cropId = new JTextField(6);
        JTextField date = new JTextField(10);
        JButton start = new JButton("Start Trace");
        JPanel top = new JPanel();
        top.add(new JLabel("Crop ID:")); top.add(cropId);
        top.add(new JLabel("Planted (YYYY-MM-DD):")); top.add(date);
        top.add(start);
        add(top, BorderLayout.NORTH);

        JTextField event = new JTextField(20);
        JButton addEv = new JButton("Add Event");
        JButton show = new JButton("Show Trace");
        JPanel bottom = new JPanel();
        bottom.add(event); bottom.add(addEv); bottom.add(show);
        add(bottom, BorderLayout.SOUTH);

        start.addActionListener(e -> {
            try {
                Crop c = new Crop(cropId.getText().trim(), "Demo", "Var", 0.5,
                        java.time.LocalDate.parse(date.getText().trim()));
                currentTraceId = controller.startTrace(c);
                area.setText("Trace started: " + currentTraceId + "\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date.");
            }
        });

        addEv.addActionListener(e -> {
            if (currentTraceId == null) {
                JOptionPane.showMessageDialog(this, "Start a trace first.");
                return;
            }
            controller.addTraceEvent(currentTraceId, event.getText().trim());
            area.append("Added: " + event.getText().trim() + "\n");
            event.setText("");
        });

        show.addActionListener(e -> {
            if (currentTraceId == null) {
                JOptionPane.showMessageDialog(this, "Start a trace first.");
                return;
            }
            List<String> ev = controller.getTrace(currentTraceId);
            area.setText("Trace ID: " + currentTraceId + "\n------------------\n");
            ev.forEach(x -> area.append(x + "\n"));
        });
    }
}