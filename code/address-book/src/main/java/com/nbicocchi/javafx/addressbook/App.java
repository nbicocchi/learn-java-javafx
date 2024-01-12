package com.nbicocchi.javafx.addressbook;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Objects;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("person-overview-view.fxml")));
        Scene scene = new Scene(view);
        stage.setTitle("Address Application");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/address_book_512.png"))));
        stage.setScene(scene);
        stage.show();
    }
}
