package ftvp.earthquakeapp.persistence.model;

import java.util.Objects;

public class Geometry {    private String type;

    double latitude;
    double longitude;
    double altitude;

    public Geometry() {}

    public Geometry(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geometry geometry = (Geometry) o;
        return latitude == geometry.getLatitude() && longitude == geometry.getLongitude() && altitude == geometry.getAltitude();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude(), getAltitude());
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }
}

