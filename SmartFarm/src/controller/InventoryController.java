package controller;

import model.InventoryItem;
import service.InventoryService;

import java.util.List;
import java.util.Optional;

public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    public void add(InventoryItem item) { service.addItem(item); }

    public List<InventoryItem> listAll() { return service.getAll(); }

    public boolean adjustQuantity(String id, int delta) { return service.adjustQuantity(id, delta); }

    public List<InventoryItem> lowStock(int threshold) { return service.lowStock(threshold); }
}