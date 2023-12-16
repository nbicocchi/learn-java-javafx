package com.nbicocchi.javafx.regex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegexApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("regex-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Regular Expressions Tester");
        stage.show();
    }
}
