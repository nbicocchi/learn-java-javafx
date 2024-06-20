package com.nbicocchi.javafx.sumreciprocals;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
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
    @FXML private ScatterChart<Number, Number> lineChart;
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
        int maxPointsOnTheChart = 100;
        long max = Long.parseLong(numberField.getText());
        long step = Math.max(max / maxPointsOnTheChart, 1);
        Task<Pair<Long, Double>> task = getTask(max, step);
        task.progressProperty().addListener((observable, oldValue, newValue) -> progressBar.setProgress(newValue.doubleValue()));
        task.valueProperty().addListener((observable, oldValue, newValue) -> updatePartialData(task.getValue()));
        data.getData().clear();
        executorService.submit(task);
    }

    private Task<Pair<Long, Double>> getTask(long max, long step) {
        return new Task<>() {
            @Override
            protected Pair<Long, Double> call() throws InterruptedException {
                double sum = 0.0;
                for (long i = 1; i <= max; i++) {
                    sum += 1.0 / (double) i;

                    if (i % step == 0) {
                        updateProgress(i, max);
                        updateValue(new Pair<>(i, sum));
                        Thread.sleep(2);
                    }
                }
                return new Pair<>(max, sum);
            }
        };
    }

    void updatePartialData(Pair<Long, Double> pair) {
        sumField.setText(String.format("n=%d sum=%.6f", pair.getKey(), pair.getValue()));
        data.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
    }
}