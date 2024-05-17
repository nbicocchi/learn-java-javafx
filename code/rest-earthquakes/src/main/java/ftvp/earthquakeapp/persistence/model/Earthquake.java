package ftvp.earthquakeapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Earthquake {

    String id;
    String title;
    double mag;
    String place;
    long time;
    String detail;
    String alert;
    int tsunami;
    Date datetime;
    Geometry geometry;

    public Earthquake(){}

    public Earthquake(String id, String title, double mag, String place, long time, String detail, String alert, int tsunami, Geometry geometry) {
        this.id = id;
        this.title = title;
        this.mag = Math.round(mag*100.0)/100.0;
        this.place = place;
        this.time = time;
        this.detail = detail;
        this.alert = alert;
        this.tsunami = tsunami;
        this.datetime = new Date(time);
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }

    public String getDetail() {
        return detail;
    }

    public String getAlert() {
        return alert;
    }

    public int getTsunami() {
        return tsunami;
    }

    public Date getDatetime() {
        return datetime;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMag(double mag) {
        this.mag = Math.round(mag*100.0)/100.0;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void setTsunami(int tsunami) {
        this.tsunami = tsunami;
    }

    public void setDatetime() {
        this.datetime = new Date(time);
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Earthquake that = (Earthquake) o;
        return Objects.equals(id, that.id);
        }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getMag(), getPlace(), getTime(),
                getDetail(), getAlert(), getTsunami(), getDatetime(), getGeometry());
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", mag=" + mag +
                ", place='" + place + '\'' +
                ", time=" + time +
                ", detail='" + detail + '\'' +
                ", alert='" + alert + '\'' +
                ", tsunami=" + tsunami +
                ", datetime=" + datetime +
                ", geometry=" + geometry +
                '}';
    }
}
