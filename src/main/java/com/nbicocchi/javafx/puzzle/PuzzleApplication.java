package com.nbicocchi.javafx.puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PuzzleApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("puzzle-view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Puzzle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
