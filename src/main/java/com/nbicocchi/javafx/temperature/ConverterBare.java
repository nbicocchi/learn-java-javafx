package com.nbicocchi.javafx.temperature;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ConverterBare extends Application {
    private Button btnCF;
    private Button btnFC;
    private TextField tfF;
    private TextField tfC;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        tfC = new TextField("0");
        tfF = new TextField("32");
        btnCF = new Button("°C->°F");
        btnFC = new Button("°F->°C");
        FlowPane flowPane = new FlowPane(tfC, new Label("°C"), tfF, new Label("°F"), btnCF, btnFC);
        flowPane.setAlignment(Pos.TOP_CENTER);
        primaryStage.setScene(new Scene(flowPane, 550, 50));
        primaryStage.setTitle("Temperature Converter");
        primaryStage.show();
    }
}
