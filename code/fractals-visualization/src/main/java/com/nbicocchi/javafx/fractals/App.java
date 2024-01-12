package com.nbicocchi.javafx.fractals;

import com.nbicocchi.javafx.fractals.controller.FractalsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class App extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fractals-view.fxml"));
        Parent root = loader.load();
        FractalsController controller = loader.getController();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> controller.keyPressed(e));
        primaryStage.setTitle("Fractal Viewer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}