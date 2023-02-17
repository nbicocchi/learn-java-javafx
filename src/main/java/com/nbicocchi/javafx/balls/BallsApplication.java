package com.nbicocchi.javafx.balls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BallsApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("balls-view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Balls");
        stage.setScene(scene);
        stage.show();
    }
}
