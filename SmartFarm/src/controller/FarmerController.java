package controller;

import model.Farmer;
import service.FarmerService;

import java.util.List;
import java.util.Optional;

public class FarmerController {
    private final FarmerService service;

    public FarmerController(FarmerService service) {
        this.service = service;
    }

    public void register(Farmer f) { service.registerFarmer(f); }

    public List<Farmer> listAll() { return service.getAll(); }

    public Optional<Farmer> find(String id) { return service.findById(id); }
}