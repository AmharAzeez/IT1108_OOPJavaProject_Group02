package service;

import model.Crop;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lightweight domain logic for crops:
 * - Simple harvest prediction
 * - Water usage estimate
 * - Basic filters
 */
public class SmartCropService {

    public String predictHarvestWindow(Crop crop) {
        // naive estimate based on growth stage
        return switch (crop.getGrowthStage()) {
            case "Seedling" -> "Estimated harvest: ~90 days";
            case "Vegetative" -> "Estimated harvest: ~60 days";
            case "Flowering" -> "Estimated harvest: ~30 days";
            case "Mature" -> "Ready for harvest";
            default -> "Unknown stage — insufficient data";
        };
    }

    public double estimateDailyWaterUsagePerHectare(Crop crop) {
        // simple rules: seedlings need more water
        return switch (crop.getGrowthStage()) {
            case "Seedling" -> 8.0;
            case "Vegetative" -> 6.0;
            case "Flowering" -> 5.0;
            case "Mature" -> 4.0;
            default -> 5.0;
        };
    }

    public double estimateTotalWater(List<Crop> crops) {
        return crops.stream()
                .mapToDouble(c -> estimateDailyWaterUsagePerHectare(c) * c.getAreaHectares())
                .sum();
    }

    public int daysSincePlanting(Crop crop) {
        if (crop.getPlantedOn() == null) return -1;
        return Period.between(crop.getPlantedOn(), LocalDate.now()).getDays();
    }

    public List<Crop> filterOrganic(List<Crop> crops) {
        return crops.stream().filter(Crop::isOrganicCertified).collect(Collectors.toList());
    }
}