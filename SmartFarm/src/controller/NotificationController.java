package controller;

import service.NotificationService;
import model.Farmer;
import java.util.List;

public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    public void remindHarvest(List<Farmer> farmers) {
        service.notifyFarmersForHarvest(farmers);
    }

    public void dispatchAll() {
        service.sendAll();
    }
}