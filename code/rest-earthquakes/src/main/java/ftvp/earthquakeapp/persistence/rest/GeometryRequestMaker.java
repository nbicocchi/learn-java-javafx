package ftvp.earthquakeapp.persistence.rest;

import com.fasterxml.jackson.databind.JsonNode;
import ftvp.earthquakeapp.persistence.model.Geometry;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;

public class GeometryRequestMaker extends RequestMaker {

    String protocol = "https";
    String host = "geocode.search.hereapi.com";
    String path = "v1/geocode";

    Dotenv dotenv = Dotenv.configure()
            .directory("code/rest-earthquakes/.env")
            .load();
    String APIKEY = dotenv.get("API_KEY");

    public GeometryRequestMaker() {
    }

    public Geometry geocode(String place) {

        URL myurl = new HttpUrl.Builder()
                .scheme(protocol)
                .host(host)
                .addPathSegments(path)
                .addQueryParameter("q", place.replace(' ', '+'))
                .addQueryParameter("apiKey", APIKEY)
                .build().url();

        Request request = new Request.Builder()
                .url(myurl)
                .build();

        Call call = client.newCall(request);

        try (Response response = call.execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Unsuccessful response: code = " + response.code());
            }

            ResponseBody responseBody = response.body();
            JsonNode bodyNode = mapper.readTree(responseBody.string());

            Geometry coordinates = new Geometry();

            if (bodyNode.get("items").isEmpty()) {
                coordinates.setLatitude(0.0);
                coordinates.setLongitude(0.0);
            } else {
                coordinates.setLatitude(bodyNode.get("items").get(0).get("position").get("lat").asDouble());
                coordinates.setLongitude(bodyNode.get("items").get(0).get("position").get("lng").asDouble());
                coordinates.setAltitude(0.0);
            }

            return coordinates;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
