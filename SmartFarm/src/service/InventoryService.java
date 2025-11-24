package service;

import model.InventoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryService {
    private final List<InventoryItem> items = new ArrayList<>();

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public Optional<InventoryItem> findById(String id) {
        return items.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public List<InventoryItem> getAll() { return new ArrayList<>(items); }

    public boolean adjustQuantity(String id, int delta) {
        Optional<InventoryItem> opt = findById(id);
        if (opt.isPresent()) {
            InventoryItem it = opt.get();
            it.adjustQuantity(delta);
            return true;
        }
        return false;
    }

    public List<InventoryItem> lowStock(int threshold) {
        return items.stream().filter(i -> i.getQuantity() <= threshold).toList();
    }
}