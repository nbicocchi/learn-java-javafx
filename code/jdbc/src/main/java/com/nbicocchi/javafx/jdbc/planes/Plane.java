package com.nbicocchi.javafx.jdbc.planes;

import java.time.LocalDate;
import java.util.UUID;

public class Plane {
    UUID uuid;
    String name;
    double length;
    double wingspan;
    LocalDate firstFlight;
    String category;

    public Plane() {

    }

    public Plane(String name, double length, double wingspan, LocalDate firstFlight, String category) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.length = length;
        this.wingspan = wingspan;
        this.firstFlight = firstFlight;
        this.category = category;
    }

    public Plane(UUID uuid, String name, double length, double wingspan, LocalDate firstFlight, String category) {
        this.uuid = uuid;
        this.name = name;
        this.length = length;
        this.wingspan = wingspan;
        this.firstFlight = firstFlight;
        this.category = category;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWingspan() {
        return wingspan;
    }

    public void setWingspan(double wingspan) {
        this.wingspan = wingspan;
    }

    public LocalDate getFirstFlight() {
        return firstFlight;
    }

    public void setFirstFlight(LocalDate firstFlight) {
        this.firstFlight = firstFlight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toCSV() {
        return name + ";" + length + ";" + wingspan + ";" + firstFlight + ";" + category;
    }

    @Override
    public String toString() {
        return "Plane{" + "uuid=" + uuid + ", name='" + name + '\'' + ", length=" + length + ", wingspan=" + wingspan + ", firstFlight=" + firstFlight + ", category='" + category + '\'' + '}';
    }
}
