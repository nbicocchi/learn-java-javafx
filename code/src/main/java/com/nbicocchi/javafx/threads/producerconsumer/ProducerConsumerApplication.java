package com.nbicocchi.javafx.threads.producerconsumer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProducerConsumerApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("producerconsumer-view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Producer Consumer Benchmark");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
