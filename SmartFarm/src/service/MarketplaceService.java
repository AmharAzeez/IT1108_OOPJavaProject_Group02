package service;

import model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MarketplaceService {
    private final List<Order> orders = new ArrayList<>();

    public Order placeOrder(String cropId, String farmerId, String consumer, int qty) {
        Order o = new Order(cropId, farmerId, consumer, qty);
        orders.add(o);
        // simple notification stub could be added
        return o;
    }

    public List<Order> getAllOrders() { return new ArrayList<>(orders); }

    public Optional<Order> findById(String id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    public boolean markDelivered(String orderId) {
        Optional<Order> opt = findById(orderId);
        opt.ifPresent(o -> o.setDelivered(true));
        return opt.isPresent();
    }
}