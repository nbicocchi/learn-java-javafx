package com.nbicocchi.javafx.producerconsumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ProducerConsumerController {
    public static final int MAX_ITEMS = 100000;
    @FXML private ScatterChart<String, Number> chart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ChoiceBox<String> chArchitecture;
    @FXML private ChoiceBox<String> chQueueSize;
    @FXML private Label lbSpeed;

    public void initialize() {
        String[] experiments = {"Synchronized", "Synchronized + W/N", "Thread Safe Deque"};
        chArchitecture.getItems().addAll(experiments);
        chArchitecture.getSelectionModel().select(0);
        chQueueSize.getItems().addAll("1", "4", "16", "64", "256", "1024");
        chQueueSize.getSelectionModel().select(1);
        xAxis.setCategories(FXCollections.observableArrayList(chQueueSize.getItems()));
        yAxis.setLowerBound(0);
    }

    @FXML
    void onStart() {
        int queueSize = Integer.parseInt(chQueueSize.getSelectionModel().getSelectedItem());

        switch (chArchitecture.getValue()) {
            case "Synchronized" -> {
                Deque<Integer> deque = new LinkedList<>();
                Producer producer = new ProducerSafe(deque, queueSize, MAX_ITEMS);
                Consumer consumer = new ConsumerSafe(deque, MAX_ITEMS);
                runExperiment(producer, consumer, deque);
            }
            case "Synchronized + W/N" -> {
                Deque<Integer> deque = new LinkedList<>();
                Producer producer = new ProducerSafeWaitNotify(deque, queueSize, MAX_ITEMS);
                Consumer consumer = new ConsumerSafeWaitNotify(deque, MAX_ITEMS);
                runExperiment(producer, consumer, deque);
            }
            case "Thread Safe Deque" -> {
                Deque<Integer> deque = new ConcurrentLinkedDeque<>();
                Producer producer = new ProducerUnsafe(deque, queueSize, MAX_ITEMS);
                Consumer consumer = new ConsumerUnSafe(deque, MAX_ITEMS);
                runExperiment(producer, consumer, deque);
            }
        }
    }

    @FXML
    void onClear() {
        chart.getData().clear();
    }

    void runExperiment(Producer producer, Consumer consumer, Deque<Integer> deque) {
        consumer.setOnSucceeded(event -> {
            XYChart.Series<String, Number> data = getSeriesByName(chArchitecture.getValue());
            data.getData().add(new XYChart.Data<>(
                    chQueueSize.getSelectionModel().getSelectedItem(),
                    consumer.getValue()));
        });
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    XYChart.Series getSeriesByName(String name) {
        for (XYChart.Series<String, Number> series : chart.getData()) {
            if (series.getName().equals(name)) {
                return series;
            }
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);
        chart.getData().add(series);
        return series;
    }
}
