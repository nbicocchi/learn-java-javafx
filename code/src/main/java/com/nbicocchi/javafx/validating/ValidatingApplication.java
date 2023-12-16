package com.nbicocchi.javafx.validating;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ValidatingApplication extends Application {
    Label label;
    long count = 0;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ValidatingTextField tfInterval = new ValidatingTextField(input -> input.matches("\\d+"));
        tfInterval.setText("100000");
        label = new Label("Processing: n/a");
        HBox box1 = new HBox();
        box1.getChildren().addAll(tfInterval, label);
        box1.setAlignment(Pos.CENTER);
        box1.setSpacing(10);
        box1.setPadding(new Insets(5));
        box1.setPrefWidth(400);
        Button btCount = new Button("Start w/o Thread");
        Button btCountWithThread = new Button("Start w/ Thread");
        btCount.setOnAction(x -> onStart(Integer.parseInt(tfInterval.getText())));
        btCountWithThread.setOnAction(x -> onStartWithTask(Integer.parseInt(tfInterval.getText())));
        btCount.disableProperty().bind(tfInterval.isValidPropertyProperty().not());
        btCountWithThread.disableProperty().bind(tfInterval.isValidPropertyProperty().not());
        HBox box2 = new HBox();
        box2.getChildren().addAll(btCount, btCountWithThread);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(10);
        box2.setPadding(new Insets(15));
        box2.setPrefWidth(400);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(box1, box2);
        Scene scene = new Scene(vbox);
        primaryStage.setTitle("Counter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void onStart(int interval) {
        for (int i = 0; i < interval; i++) {
            count++;
            if (interval % 100 == 0) {
                updateLabel();
            }
            updateLabel();
        }
    }

    void onStartWithTask(int interval) {
        Task<Long> task = new Task<>() {

            @Override
            protected Long call() {
                for (int i = 0; i < interval; i++) {
                    count++;
                    if (interval % 100 == 0) {
                        updateProgress(i, interval);
                    }
                }
                return null;
            }
        };
        task.progressProperty().addListener((observable, oldValue, newValue) -> {
            updateLabel();
        });
        task.setOnSucceeded(x -> {
            updateLabel();
        });
        new Thread(task).start();
    }

    void updateLabel() {
        label.textProperty().set(String.format("Processing: %d", count));
    }
}
