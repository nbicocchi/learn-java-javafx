package com.nbicocchi.javafx.pong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PongApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pong-view.fxml"));
        Parent root = loader.load();
        PongController controller = loader.getController();
        Scene scene = new Scene(root, 800, 500);
        scene.setOnKeyPressed(e -> controller.keyPressed(e));
        scene.setOnKeyReleased(e -> controller.keyReleased(e));
        primaryStage.setTitle("Pong");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        controller.initializeGameObjects();
        controller.initializeTimer();
    }
}