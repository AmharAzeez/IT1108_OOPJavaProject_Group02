package service;

import model.Crop;
import java.util.*;

public class TraceabilityService {
    private final Map<String, List<String>> traces = new HashMap<>();

    public String createTrace(Crop crop) {
        String traceId = UUID.randomUUID().toString();
        List<String> events = new ArrayList<>();
        events.add("Planted: " + crop.getPlantedOn());
        traces.put(traceId, events);
        return traceId;
    }

    public void addEvent(String traceId, String event) {
        traces.computeIfAbsent(traceId, k -> new ArrayList<>()).add(event);
    }

    public List<String> getTrace(String traceId) {
        return traces.getOrDefault(traceId, Collections.emptyList());
    }
}