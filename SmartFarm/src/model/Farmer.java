package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Farmer {
    private final String id;
    private String name;
    private String location;
    private List<Crop> crops = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();

    public Farmer(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<Crop> getCrops() { return crops; }
    public List<String> getCertifications() { return certifications; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }

    public void addCrop(Crop crop) { crops.add(crop); }
    public boolean removeCrop(Crop crop) { return crops.remove(crop); }
    public void addCertification(String cert) { certifications.add(cert); }

    @Override
    public String toString() {
        return id + " - " + name + " (" + location + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Farmer)) return false;
        Farmer farmer = (Farmer) o;
        return Objects.equals(id, farmer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}