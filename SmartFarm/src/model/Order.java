package model;

import java.time.LocalDate;
import java.util.UUID;

public class Order {
    private final String id;
    private final String cropId;
    private final String farmerId;
    private final String consumerName;
    private final int quantity; // units or kg
    private final LocalDate orderDate;
    private boolean delivered;

    public Order(String cropId, String farmerId, String consumerName, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.cropId = cropId;
        this.farmerId = farmerId;
        this.consumerName = consumerName;
        this.quantity = quantity;
        this.orderDate = LocalDate.now();
        this.delivered = false;
    }

    public String getId() { return id; }
    public String getCropId() { return cropId; }
    public String getFarmerId() { return farmerId; }
    public String getConsumerName() { return consumerName; }
    public int getQuantity() { return quantity; }
    public LocalDate getOrderDate() { return orderDate; }
    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    @Override
    public String toString() {
        return id + " : " + consumerName + " ordered " + quantity + " of crop " + cropId;
    }
}