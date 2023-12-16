package com.nbicocchi.javafx.collections;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CollectionsApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("collections-view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Collections Benchmark Suite");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
