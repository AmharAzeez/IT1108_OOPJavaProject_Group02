package controller;

import model.Order;
import service.MarketplaceService;

import java.util.List;
import java.util.Optional;

public class MarketplaceController {
    private final MarketplaceService service;

    public MarketplaceController(MarketplaceService service) {
        this.service = service;
    }

    public Order placeOrder(String cropId, String farmerId, String consumer, int qty) {
        return service.placeOrder(cropId, farmerId, consumer, qty);
    }

    public List<Order> allOrders() { return service.getAllOrders(); }

    public boolean deliver(String orderId) { return service.markDelivered(orderId); }

    public Optional<Order> findOrder(String id) { return service.findById(id); }
}