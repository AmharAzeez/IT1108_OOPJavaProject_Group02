package controller;

import model.Crop;
import service.SmartCropService;

import java.util.List;

public class CropController {
    private final SmartCropService service;

    public CropController(SmartCropService service) {
        this.service = service;
    }

    public String getHarvestPrediction(Crop crop) {
        return service.predictHarvestWindow(crop);
    }

    public double calculateTotalDailyWater(List<Crop> crops) {
        return service.estimateTotalWater(crops);
    }

    public int getDaysSincePlanting(Crop crop) {
        return service.daysSincePlanting(crop);
    }
}