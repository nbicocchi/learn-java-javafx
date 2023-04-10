package com.nbicocchi.javafx.producerconsumer;

import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ProducerConsumerController {
    public static final int MAX_ITEMS = 25000;
    public static final int MIN_DEQUE = 1;
    public static final int MAX_DEQUE = 64;
    @FXML private ScatterChart<Number, Number> chart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ChoiceBox<String> chArchitecture;

    public void initialize() {
        String[] experiments = {"Synchronized", "Synchronized + W/N", "Thread Safe Deque"};
        chArchitecture.getItems().addAll(experiments);
        chArchitecture.getSelectionModel().select(0);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(64);
        yAxis.setLowerBound(0);
        xAxis.setLabel("Shared Deque Size (items)");
        yAxis.setLabel("Time (milliseconds)");
    }

    @FXML
    void onStart() throws InterruptedException {
        switch (chArchitecture.getValue()) {
            case "Synchronized" -> runExperimentsSafe();
            case "Synchronized + W/N" -> runExperimentsSafeWaitNotify();
            case "Thread Safe Deque" -> runExperimentsUnsafe();
        }
    }

    @FXML
    void onClear() {
        chart.getData().clear();
    }

    void runExperimentsSafe() throws InterruptedException {
        for (int i = MIN_DEQUE; i < MAX_DEQUE; i++) {
            final int items = i;
            Deque<Integer> deque = new LinkedList<>();
            Producer producer = new ProducerSafe(deque, items, MAX_ITEMS);
            Consumer consumer = new ConsumerSafe(deque, MAX_ITEMS);
            consumer.setOnSucceeded(event -> {
                XYChart.Series<Number, Number> data = getSeriesByName(chArchitecture.getValue());
                data.getData().add(new XYChart.Data<>(items, consumer.getValue()));
            });
            runExperiment(producer, consumer);
        }
    }

    void runExperimentsSafeWaitNotify() throws InterruptedException {
        for (int i = MIN_DEQUE; i < MAX_DEQUE; i++) {
            final int items = i;
            Deque<Integer> deque = new LinkedList<>();
            Producer producer = new ProducerSafeWaitNotify(deque, items, MAX_ITEMS);
            Consumer consumer = new ConsumerSafeWaitNotify(deque, MAX_ITEMS);
            consumer.setOnSucceeded(event -> {
                XYChart.Series<Number, Number> data = getSeriesByName(chArchitecture.getValue());
                data.getData().add(new XYChart.Data<>(items, consumer.getValue()));
            });
            runExperiment(producer, consumer);
        }
    }

    void runExperimentsUnsafe() throws InterruptedException {
        for (int i = MIN_DEQUE; i < MAX_DEQUE; i++) {
            final int items = i;
            Deque<Integer> deque = new ConcurrentLinkedDeque<>();
            Producer producer = new ProducerUnsafe(deque, items, MAX_ITEMS);
            Consumer consumer = new ConsumerUnSafe(deque, MAX_ITEMS);
            consumer.setOnSucceeded(event -> {
                XYChart.Series<Number, Number> data = getSeriesByName(chArchitecture.getValue());
                data.getData().add(new XYChart.Data<>(items, consumer.getValue()));
            });
            runExperiment(producer, consumer);
        }
    }

    void runExperiment(Producer producer, Consumer consumer) throws InterruptedException {
        Thread t0 = new Thread(producer);
        Thread t1 = new Thread(consumer);
        t0.start();
        t1.start();
        t0.join();
        t1.join();
    }

    XYChart.Series<Number, Number> getSeriesByName(String name) {
        for (XYChart.Series<Number, Number> series : chart.getData()) {
            if (series.getName().equals(name)) {
                return series;
            }
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        chart.getData().add(series);
        return series;
    }
}
