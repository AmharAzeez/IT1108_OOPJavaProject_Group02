package model;

import java.time.LocalDate;
import java.util.Objects;

public class Crop {
    private final String id;
    private String name;
    private String variety;
    private String growthStage; // Seedling, Vegetative, Flowering, Mature
    private double areaHectares;
    private LocalDate plantedOn;
    private boolean organicCertified;

    public Crop(String id, String name, String variety, double areaHectares, LocalDate plantedOn) {
        this.id = id;
        this.name = name;
        this.variety = variety;
        this.areaHectares = areaHectares;
        this.plantedOn = plantedOn;
        this.growthStage = "Seedling";
        this.organicCertified = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getVariety() { return variety; }
    public String getGrowthStage() { return growthStage; }
    public double getAreaHectares() { return areaHectares; }
    public LocalDate getPlantedOn() { return plantedOn; }
    public boolean isOrganicCertified() { return organicCertified; }

    public void setName(String name) { this.name = name; }
    public void setVariety(String variety) { this.variety = variety; }
    public void setGrowthStage(String growthStage) { this.growthStage = growthStage; }
    public void setAreaHectares(double areaHectares) { this.areaHectares = areaHectares; }
    public void setPlantedOn(LocalDate plantedOn) { this.plantedOn = plantedOn; }
    public void setOrganicCertified(boolean organicCertified) { this.organicCertified = organicCertified; }

    @Override
    public String toString() {
        return id + " - " + name + " [" + variety + "] (" + growthStage + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crop crop = (Crop) o;
        return Objects.equals(id, crop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}