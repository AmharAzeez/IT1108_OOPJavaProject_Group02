package controller;

import service.AnalyticsService;
import model.Farmer;
import java.util.*;

public class AnalyticsController {
    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    public Map<String, Object> getSustainabilityReport(List<Farmer> farmers) {
        return service.generateSustainabilitySummary(farmers);
    }

    public Map<String, Double> getIncomeSummary(List<Farmer> farmers) {
        return service.incomeByFarmer(farmers);
    }
}