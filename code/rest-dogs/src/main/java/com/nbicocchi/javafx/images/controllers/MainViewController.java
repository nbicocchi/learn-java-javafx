package com.nbicocchi.javafx.images.controllers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nbicocchi.javafx.images.models.DogImage;
import com.nbicocchi.javafx.images.models.DogResponse;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewController {
    @FXML
    private ImageView imageDisplayNode;
    @FXML
    private Label imageDetails;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void initialize() {
        imageDisplayNode.setFitWidth(500);
        imageDisplayNode.setFitHeight(500);
    }

    public void loadNewImage() {
        Task<DogImage> t = getNextDogTask();
        t.setOnFailed(workerStateEvent -> System.out.println("Something wrong happened!"));
        t.setOnSucceeded(workerStateEvent -> {
            String URL = t.getValue().URL();
            BufferedImage image = t.getValue().bufferedImage();
            imageDetails.setText(URL + " [" + image.getWidth() + "x" + image.getHeight() + "]");
            imageDisplayNode.setImage(SwingFXUtils.toFXImage(image, null));
        });
        executorService.submit(t);
    }

    public Task<DogImage> getNextDogTask() {
        return new Task<>() {
            @Override
            protected DogImage call() throws UnirestException, IOException, URISyntaxException {
                HttpResponse<JsonNode> apiResponse = Unirest.get("https://dog.ceo/api/breeds/image/random").asJson();
                DogResponse dogResponse = new Gson().fromJson(apiResponse.getBody().toString(), DogResponse.class);
                BufferedImage image = ImageIO.read(new URI(dogResponse.message()).toURL());
                return new DogImage(dogResponse.message(), image);
            }
        };
    }
}

