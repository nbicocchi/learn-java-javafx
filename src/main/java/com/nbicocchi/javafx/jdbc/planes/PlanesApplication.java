package com.nbicocchi.javafx.jdbc.planes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class PlanesApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxml = new FXMLLoader();
        fxml.setController(new PlanesDBForwardController());
        System.out.println(System.getProperty("user.dir"));
        Parent root = fxml.load(new FileInputStream(
                "src/main/resources/com/nbicocchi/javafx/jdbc/planes/planes-view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Plane Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
