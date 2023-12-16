package com.nbicocchi.javafx.games.spacelander;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SpaceLanderApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spacelander-view.fxml"));
        loader.load();
        SpaceLanderController controller = loader.getController();
        Scene scene = new Scene(loader.getRoot());
        scene.setOnKeyPressed(controller::keyPressed);
        scene.setOnKeyReleased(controller::keyReleased);
        primaryStage.setTitle("Space Lander");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
