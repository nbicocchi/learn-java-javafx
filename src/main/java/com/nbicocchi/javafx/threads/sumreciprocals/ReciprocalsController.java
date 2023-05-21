package com.nbicocchi.javafx.threads.sumreciprocals;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReciprocalsController {
    @FXML private BorderPane borderPane;
    @FXML private ProgressBar progressBar;
    @FXML private TextField numberField;
    @FXML private Label sumField;
    @FXML private LineChart<Number, Number> lineChart;
    XYChart.Series<Number, Number> data;
    ExecutorService executorService;

    @FXML
    public void initialize() {
        progressBar.prefWidthProperty().bind(borderPane.widthProperty());
        data = new XYChart.Series<>();
        lineChart.getData().add(data);
        lineChart.setLegendVisible(false);
        executorService = Executors.newSingleThreadExecutor();
    }

    @FXML
    void handleStop() {
        executorService.shutdownNow();
        executorService = Executors.newSingleThreadExecutor();
        sumField.setText("n/a");
        progressBar.setProgress(0.0);
    }

    @FXML
    void handleExecute() {
        int n = Integer.parseInt(numberField.getText());
        Task<Pair<Integer, Double>> task = new Task<>() {
            @Override
            protected Pair<Integer, Double> call() throws InterruptedException {
                double sum = 0.0;
                for (int i = 1; i <= n; i++) {
                    sum += 1.0 / (double) i;
                    updateProgress(i, n);
                    updateValue(new Pair<>(i, sum));
                    Thread.sleep(10);
                }
                return new Pair<>(n, sum);
            }
        };
        task.progressProperty().addListener((observable, oldValue, newValue) -> progressBar.setProgress(newValue.doubleValue()));
        task.valueProperty().addListener((observable, oldValue, newValue) -> updatePartialData(task.getValue()));
        data.getData().clear();
        executorService.submit(task);
    }

    void updatePartialData(Pair<Integer, Double> pair) {
        sumField.setText(String.format("%d -> %f", pair.getKey(), pair.getValue()));
        data.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
    }
}