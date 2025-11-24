package service;

import model.Farmer;
import java.util.*;

public class NotificationService {
    private final List<String> messageQueue = new ArrayList<>();

    public void queueNotification(String recipientId, String message) {
        messageQueue.add("To: " + recipientId + " | " + message);
    }

    public List<String> getPending() { return new ArrayList<>(messageQueue); }

    public void sendAll() {
        for (String m : messageQueue)
            System.out.println("[NOTIFY] " + m);
        messageQueue.clear();
    }

    public void notifyFarmersForHarvest(List<Farmer> farmers) {
        for (Farmer f : farmers)
            queueNotification(f.getId(), "Reminder: Check crops for harvest window & quality checks.");
    }
}