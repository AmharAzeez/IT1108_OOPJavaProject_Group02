package service;

import model.Farmer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FarmerService {
    private final List<Farmer> farmers = new ArrayList<>();

    public void registerFarmer(Farmer farmer) {
        if (findById(farmer.getId()).isEmpty()) {
            farmers.add(farmer);
        }
    }

    public List<Farmer> getAll() { return new ArrayList<>(farmers); }

    public Optional<Farmer> findById(String id) {
        return farmers.stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    public List<Farmer> findByLocation(String location) {
        return farmers.stream().filter(f -> f.getLocation().equalsIgnoreCase(location)).toList();
    }

    public boolean removeFarmer(String id) {
        return findById(id).map(farmers::remove).orElse(false);
    }
}