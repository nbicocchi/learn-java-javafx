package com.nbicocchi.javafx;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
        System.out.println("Screen resolution: " + screenWidth + " x " + screenHeight);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("home.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);


        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/CameraIcon.png"))));
        stage.setTitle("Camera");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

        int minWidth = (int) stage.getWidth();
        int minHeight = (int) stage.getHeight();
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
    }

    public static void main(String[] args) {
        launch();
    }
}