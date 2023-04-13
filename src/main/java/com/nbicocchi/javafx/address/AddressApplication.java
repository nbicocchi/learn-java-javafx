package com.nbicocchi.javafx.address;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AddressApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("person-overview-view.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Address Application");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("address_book_512.png")));
        stage.setScene(scene);
        stage.show();
    }
}
