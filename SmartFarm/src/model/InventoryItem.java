package model;

public class InventoryItem {
    private final String id;
    private String name;
    private String category; // Seed, Fertilizer, Equipment
    private int quantity;
    private String unit; // kg, liters, pcs

    public InventoryItem(String id, String name, String category, int quantity, String unit) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }

    public void adjustQuantity(int delta) { this.quantity += delta; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + quantity + " " + unit + ")";
    }
}