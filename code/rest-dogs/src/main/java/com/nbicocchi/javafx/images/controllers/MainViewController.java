package com.nbicocchi.javafx.images.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbicocchi.javafx.images.models.DogImage;
import com.nbicocchi.javafx.images.models.DogResponse;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewController {
    @FXML
    private ImageView imageDisplayNode;
    @FXML
    private Label imageDetails;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

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
            protected DogImage call() throws IOException, URISyntaxException {
                Request request = new Request.Builder()
                        .url("https://dog.ceo/api/breeds/image/random")
                        .build();

                Call call = client.newCall(request);
                try (Response response = call.execute()) {
                    String json = response.body().string();
                    DogResponse dogResponse = mapper.readValue(json, DogResponse.class);
                    BufferedImage image = ImageIO.read(new URI(dogResponse.message()).toURL());
                    return new DogImage(dogResponse.message(), image);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        };
    }
}

