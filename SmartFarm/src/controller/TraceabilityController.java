package controller;

import service.TraceabilityService;
import model.Crop;
import java.util.List;

public class TraceabilityController {
    private final TraceabilityService service;

    public TraceabilityController(TraceabilityService service) {
        this.service = service;
    }

    public String startTrace(Crop crop) {
        return service.createTrace(crop);
    }

    public void addTraceEvent(String traceId, String event) {
        service.addEvent(traceId, event);
    }

    public List<String> getTrace(String traceId) {
        return service.getTrace(traceId);
    }
}