package com.nbicocchi.javafx.temperature;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ConverterFull extends Application {
    private Button btnCF;
    private Button btnFC;
    private TextField tfF;
    private TextField tfC;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        tfC = new TextField("0");
        tfF = new TextField("32");
        btnCF = new Button("°C->°F");
        btnFC = new Button("°F->°C");
        btnCF.setOnAction(event -> {
            double tempFahrenheit = Double.parseDouble(tfC.getText()) * 1.8 + 32;
            tfF.setText(Double.toString(tempFahrenheit));
            if (tempFahrenheit < 32) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Temperature Warning");
                alert.setContentText("Water freezes here!");
                alert.show();
            }
        });
        btnFC.setOnAction(actionEvent -> {
            double tempCelsius = (Double.parseDouble(tfF.getText()) - 32) * 0.555;
            tfC.setText(Double.toString(tempCelsius));
            if (tempCelsius < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Temperature Warning");
                alert.setContentText("Water freezes here!");
                alert.show();
            }
        });
        GridPane gridPane = new GridPane();
        gridPane.add(tfC, 0, 0);
        gridPane.add(new Label("°C"), 1, 0);
        gridPane.add(tfF, 0, 1);
        gridPane.add(new Label("°F"), 1, 1);
        gridPane.add(btnCF, 0, 2);
        gridPane.add(btnFC, 1, 2);
        primaryStage.setTitle("Temperature Converter");
        primaryStage.setScene(new Scene(gridPane, 250, 100));
        primaryStage.show();
    }
}