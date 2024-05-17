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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("overview-view.fxml"));
        Parent root = loader.load();
        OverviewController overviewController = loader.getController();
        overviewController.initialize();

        Scene scene = new Scene(root);
        stage.setTitle("Earthquake App");
        Image logo = new Image("ftvp/images/app-logo.jpg");
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}