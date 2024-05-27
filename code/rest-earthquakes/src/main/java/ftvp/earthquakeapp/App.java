package ftvp.earthquakeapp;

import ftvp.earthquakeapp.controller.OverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("overview-view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Earthquake App");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("app-logo.jpg")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}