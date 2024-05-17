package ftvp.earthquakeapp.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

    String generated;
    String url;
    String title;
    int status;
    String api;
    long count;

    public Metadata() {}

    public Metadata(String generated, String url, String title, int status, String api, long count) {
        this.generated = generated;
        this.url = url;
        this.title = title;
        this.status = status;
        this.api = api;
        this.count = count;
    }

    public String getGenerated() {
        return generated;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getApi() {
        return api;
    }

    public long getCount() {
        return count;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return getStatus() == metadata.getStatus() && getCount() == metadata.getCount() && Objects.equals(getGenerated(), metadata.getGenerated()) && Objects.equals(getUrl(), metadata.getUrl()) && Objects.equals(getTitle(), metadata.getTitle()) && Objects.equals(getApi(), metadata.getApi());
    }

    @Override
    public int hashCode() {
        return Objects.hash(generated, url, title, status, api, count);
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "generated='" + generated + '\'' +
                ", url=" + url +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", api='" + api + '\'' +
                ", count=" + count +
                '}';
    }
}
