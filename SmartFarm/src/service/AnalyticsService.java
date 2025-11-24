package service;

import model.Crop;
import model.Farmer;
import java.util.*;

public class AnalyticsService {

    public Map<String, Object> generateSustainabilitySummary(List<Farmer> farmers) {
        double totalWater = farmers.stream()
                .flatMap(f -> f.getCrops().stream())
                .mapToDouble(c -> estimateDailyWaterPerHectare(c) * c.getAreaHectares())
                .sum();

        double estimatedCarbonKg = farmers.stream()
                .flatMap(f -> f.getCrops().stream())
                .mapToDouble(this::estimateCarbonForCrop)
                .sum();

        Map<String, Object> report = new HashMap<>();
        report.put("totalWaterDailyUnits", totalWater);
        report.put("estimatedCarbonKg", estimatedCarbonKg);
        report.put("farmerCount", farmers.size());
        report.put("organicCrops",
                farmers.stream().flatMap(f -> f.getCrops().stream()).filter(Crop::isOrganicCertified).count());
        return report;
    }

    private double estimateDailyWaterPerHectare(Crop c) {
        return switch (c.getGrowthStage()) {
            case "Seedling" -> 8.0;
            case "Vegetative" -> 6.0;
            case "Flowering" -> 5.0;
            case "Mature" -> 4.0;
            default -> 5.0;
        };
    }

    private double estimateCarbonForCrop(Crop c) {
        double perHectare = c.isOrganicCertified() ? 500 : 800;
        return perHectare * c.getAreaHectares();
    }

    public Map<String, Double> incomeByFarmer(List<Farmer> farmers) {
        Map<String, Double> map = new HashMap<>();
        for (Farmer f : farmers) {
            double income = f.getCrops().stream().mapToDouble(c -> c.getAreaHectares() * 1000).sum();
            map.put(f.getId(), income);
        }
        return map;
    }
}