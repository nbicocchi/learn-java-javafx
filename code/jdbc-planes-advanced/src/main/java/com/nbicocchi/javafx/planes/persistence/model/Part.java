package com.nbicocchi.javafx.planes.persistence.model;

import java.time.LocalDate;
import java.util.Objects;

public class Part {
    Long Id;
    Plane plane;
    String partCode;
    String description;
    Double duration;

    public Part() {
    }

    public Part(Plane plane, String partCode, String description, Double duration) {
        this.Id = null;
        this.plane = plane;
        this.partCode = partCode;
        this.description = description;
        this.duration = duration;
    }

    public Part(Long id, Plane plane, String partCode, String description, Double duration) {
        this.Id = id;
        this.plane = plane;
        this.partCode = partCode;
        this.description = description;
        this.duration = duration;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
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
        return Objects.equals(Id, part.Id) && Objects.equals(plane, part.plane) && Objects.equals(partCode, part.partCode) && Objects.equals(description, part.description) && Objects.equals(duration, part.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, plane, partCode, description, duration);
    }

    @Override
    public String toString() {
        return "Part{" + "Id=" + Id + ", plane=" + plane + ", partCode='" + partCode + '\'' + ", description='" + description + '\'' + ", duration=" + duration + '}';
    }
}
