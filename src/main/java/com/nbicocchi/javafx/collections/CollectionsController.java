package com.nbicocchi.javafx.collections;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.random.RandomGenerator;

public class CollectionsController {
    private final XYChart.Series<String, Number> seriesSYN = new XYChart.Series<>();
    private final XYChart.Series<String, Number> seriesSYNWN = new XYChart.Series<>();
    private final XYChart.Series<String, Number> seriesTSAFE = new XYChart.Series<>();
    @FXML
    private ScatterChart<String, Number> chartScatter;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ChoiceBox<String> chArchitecture;
    @FXML
    private ChoiceBox<String> chQueueSize;
    @FXML
    private Label lbSpeed;

    @FXML
    void onStart() {
        RandomGenerator rnd = RandomGenerator.getDefault();
        int queueSize = Integer.parseInt(chQueueSize.getSelectionModel().getSelectedItem());
        int item = rnd.nextInt();
        switch (chArchitecture.getValue()) {
            case "Synchronized" -> {
                int result = experimentSynchronized(queueSize, item);
                lbSpeed.setText(String.format("%dK / s", result / 1000));
                seriesSYN.getData().add(new XYChart.Data<>(Integer.toString(queueSize), result));
            }
            case "Synchronized + W/N" -> {
                int result = experimentSynchronizedWN(queueSize, item);
                lbSpeed.setText(String.format("%dK / s", result / 1000));
                seriesSYNWN.getData().add(new XYChart.Data<>(Integer.toString(queueSize), result));
            }
            case "Thread Safe Queue" -> {
                int result = experimentThreadSafeQueue(queueSize, item);
                lbSpeed.setText(String.format("%dK / s", result / 1000));
                seriesTSAFE.getData().add(new XYChart.Data<>(Integer.toString(queueSize), result));
            }
        }
    }

    int experimentSynchronized(int queueSize, int item) {
        return 1000;
    }

    int experimentSynchronizedWN(int queueSize, int item) {
        return 1000;
    }

    int experimentThreadSafeQueue(int queueSize, int item) {
        return 1000;
    }

    public void initialize() {
        chArchitecture.getItems().addAll("Synchronized", "Synchronized + W/N", "Thread Safe Queue");
        chArchitecture.getSelectionModel().select(0);
        chQueueSize.getItems().addAll("1", "4", "16", "64", "256", "1024");
        chQueueSize.getSelectionModel().select(1);
        xAxis.setCategories(FXCollections.observableArrayList(chQueueSize.getItems()));
        seriesSYN.setName("Synchronized");
        seriesSYNWN.setName("Synchronized + W/N");
        seriesTSAFE.setName("Thread Safe Queue");
        chartScatter.getData().add(seriesSYN);
        chartScatter.getData().add(seriesSYNWN);
        chartScatter.getData().add(seriesTSAFE);
        yAxis.setLowerBound(0);
    }
}
