package com.nbicocchi.javafx.games.spaceinvaders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SpaceInvadersApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spaceinvaders-view.fxml"));
        loader.load();
        SpaceInvadersController controller = loader.getController();
        Scene scene = new Scene(loader.getRoot());
        scene.setOnKeyPressed(controller::keyPressed);
        scene.setOnKeyReleased(controller::keyReleased);
        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
