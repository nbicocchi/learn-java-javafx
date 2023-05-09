package com.nbicocchi.javafx.threads.sumreciprocals;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReciprocalsController {
    @FXML private ProgressBar progressBar;
    @FXML private TextField numberField;
    @FXML private Label sumField;
    ExecutorService executorService;

    @FXML
    public void initialize() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @FXML
    void handleStop() {
        executorService.shutdownNow();
        new Alert(Alert.AlertType.INFORMATION, "Sum of reciprocals failed!").showAndWait();
        executorService = Executors.newSingleThreadExecutor();
        sumField.setText("n/a");
        progressBar.setProgress(0.0);
    }

    @FXML
    void handleExecute() {
        Integer n = Integer.parseInt(numberField.getText());
        Task<Double> task = new Task<>() {
            @Override
            protected Double call() throws InterruptedException {
                double sum = 0.0;
                for (int i = 1; i <= n; i++) {
                    sum += 1.0 / (double) i;
                    updateProgress(i, n);
                    updateValue(sum);
                    Thread.sleep(10);
                }
                return sum;
            }
        };
        task.progressProperty().addListener((observable, oldValue, newValue) -> progressBar.setProgress(newValue.doubleValue()));
        task.valueProperty().addListener((observable, oldValue, newValue) -> sumField.setText(newValue.toString()));
        task.setOnSucceeded(event -> sumField.setText(task.getValue().toString()));
        executorService.submit(task);
    }
}