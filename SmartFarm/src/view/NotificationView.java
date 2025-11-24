package view;

import controller.NotificationController;
import service.NotificationService;
import model.Farmer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationView extends JFrame {
    private final NotificationController controller;
    private final List<Farmer> farmers;
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public NotificationView(List<Farmer> farmers) {
        this.controller = new NotificationController(new NotificationService());
        this.farmers = farmers;

        setTitle("Notification System");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JList<String> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton queueBtn = new JButton("Queue Harvest Reminders");
        JButton sendBtn = new JButton("Dispatch All");
        JPanel bottom = new JPanel();
        bottom.add(queueBtn);
        bottom.add(sendBtn);
        add(bottom, BorderLayout.SOUTH);

        queueBtn.addActionListener(e -> {
            controller.remindHarvest(farmers);
            model.addElement("Queued harvest reminders for all farmers.");
        });

        sendBtn.addActionListener(e -> {
            controller.dispatchAll();
            model.addElement("Sent all notifications (see console output).");
        });
    }
}