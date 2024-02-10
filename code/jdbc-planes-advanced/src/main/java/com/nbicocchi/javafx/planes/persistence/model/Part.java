package com.nbicocchi.javafx.planes.persistence.model;

import java.time.LocalDate;
import java.util.Objects;

public class Part {
    Long Id;
    Long planeID;
    String partCode;
    String description;
    Double duration;

    public Part() {
    }

    public Part(Long planeID, String partCode, String description, Double duration) {
        this.Id = null;
        this.planeID = planeID;
        this.partCode = partCode;
        this.description = description;
        this.duration = duration;
    }

    public Part(Long id, Long planeID, String partCode, String description, Double duration) {
        this.Id = id;
        this.planeID = planeID;
        this.partCode = partCode;
        this.description = description;
        this.duration = duration;
    }

    public Part(Part part) {
        this.Id = part.Id;
        this.planeID = part.planeID;
        this.partCode = part.partCode;
        this.description = part.description;
        this.duration = part.duration;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getPlaneID() {
        return planeID;
    }

    public void setPlaneID(Long planeID) {
        this.planeID = planeID;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Part part = (Part) o;
        return Objects.equals(Id, part.Id) && Objects.equals(planeID, part.planeID) && Objects.equals(partCode, part.partCode) && Objects.equals(description, part.description) && Objects.equals(duration, part.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, planeID, partCode, description, duration);
    }

    @Override
    public String toString() {
        return "Part{" + "Id=" + Id + ", planeID=" + planeID + ", partCode='" + partCode + '\'' + ", description='" + description + '\'' + ", duration=" + duration + '}';
    }
}
