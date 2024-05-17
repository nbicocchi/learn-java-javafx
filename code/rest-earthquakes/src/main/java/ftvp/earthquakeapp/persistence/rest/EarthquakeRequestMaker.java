package ftvp.earthquakeapp.persistence.rest;

import com.fasterxml.jackson.databind.JsonNode;
import ftvp.earthquakeapp.persistence.model.Earthquake;
import ftvp.earthquakeapp.persistence.model.Geometry;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeRequestMaker extends RequestMaker {

    String protocol = "https";
    String host = "earthquake.usgs.gov";
    String path = "fdsnws/event/1/query";

    String place;
    String startDate;
    String endDate;
    double minmag;
    double maxmag;

    private List<Earthquake> earthquakes = new ArrayList<>();

    public EarthquakeRequestMaker() {
        super();
    }

    public String getPlace() {
        return place;
    }

    public String getStartDate() { return startDate; }

    public String getEndDate() {
        return endDate;
    }

    public double getMinmag() {
        return minmag;
    }

    public double getMaxmag() {
        return maxmag;
    }

    public void setMaxmag(double maxmag) {
        this.maxmag = maxmag;
    }

    public void setMinmag(double minmag) {
        this.minmag = minmag;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<Earthquake> getByParams() {

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme(protocol);
        builder.host(host);
        builder.addPathSegments(path);
        builder.addQueryParameter("format", "geojson");

        if(place != null && !place.isEmpty()){
            GeometryRequestMaker geometryRequestMaker = new GeometryRequestMaker();
            Geometry coords = geometryRequestMaker.geocode(place);

            builder.addQueryParameter("latitude", String.valueOf(coords.getLatitude()));
            builder.addQueryParameter("longitude", String.valueOf(coords.getLongitude()));
            builder.addQueryParameter("maxradiuskm", "500");
        }

        if(startDate != null){
            builder.addQueryParameter("starttime", startDate);
        }
        else{
            builder.addQueryParameter("starttime", LocalDate.now().toString());
        }

        if(endDate != null){
            builder.addQueryParameter("endtime", endDate);
        }

        if(minmag != 0.0){
            builder.addQueryParameter("minmagnitude", String.valueOf(minmag));
        }

        if(maxmag != 0.0){
            builder.addQueryParameter("maxmagnitude", String.valueOf(maxmag));
        }

        URL myurl = builder.build().url();

        Request request = new Request.Builder()
                .url(myurl)
                .build();

        Call call = client.newCall(request);

        try (Response response = call.execute()){

            if(!response.isSuccessful()){
                throw new RuntimeException("Unsuccessful response: code = " + response.code());
            }

            ResponseBody responseBody = response.body();
            JsonNode bodyNode = mapper.readTree(responseBody.string());

            for(JsonNode feature : bodyNode.get("features")){

                Geometry geom = new Geometry(
                        feature.get("geometry").get("coordinates").get(0).asDouble(),
                        feature.get("geometry").get("coordinates").get(1).asDouble(),
                        feature.get("geometry").get("coordinates").get(2).asDouble()
                );

                Earthquake tmp = mapper.readValue(feature.get("properties").toString(), Earthquake.class);
                tmp.setId(feature.get("id").asText());
                tmp.setGeometry(geom);
                tmp.setDatetime();

                earthquakes.add(tmp);
            }

            return earthquakes;
        }
        catch (IOException e) {
            //Catching errors and throwing exceptions
            throw new RuntimeException(e.getMessage());
        }
    }
}
