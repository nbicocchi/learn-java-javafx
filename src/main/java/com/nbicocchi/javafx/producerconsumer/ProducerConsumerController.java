package com.nbicocchi.javafx.producerconsumer;

import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ProducerConsumerController {
    public static final int MAX_ITEMS = 25000;
    public static final int MIN_DEQUE = 1;
    public static final int MAX_DEQUE = 128;
    @FXML private ScatterChart<Number, Number> chart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ChoiceBox<String> chArchitecture;

    public void initialize() {
        String[] experiments = {"LinkedList + Synchronized", "LinkedList + Synchronized + W/N",
                "ConcurrentLinkedDeque"};
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
            case "LinkedList + Synchronized" -> runExperimentsSafe();
            case "LinkedList + Synchronized + W/N" -> runExperimentsSafeWaitNotify();
            case "ConcurrentLinkedDeque" -> runExperimentsLinkedDeque();
        }
    }

    @FXML
    void onClear() {
        chart.getData().clear();
    }

    void runExperimentsSafe() throws InterruptedException {
        for (int i = MIN_DEQUE; i < MAX_DEQUE; i++) {
            final int items = i;
            Queue<Integer> queue = new LinkedList<>();
            Producer producer = new ProducerSafe(queue, items, MAX_ITEMS);
            Consumer consumer = new ConsumerSafe(queue, MAX_ITEMS);
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
            Queue<Integer> queue = new LinkedList<>();
            Producer producer = new ProducerSafeWaitNotify(queue, items, MAX_ITEMS);
            Consumer consumer = new ConsumerSafeWaitNotify(queue, MAX_ITEMS);
            consumer.setOnSucceeded(event -> {
                XYChart.Series<Number, Number> data = getSeriesByName(chArchitecture.getValue());
                data.getData().add(new XYChart.Data<>(items, consumer.getValue()));
            });
            runExperiment(producer, consumer);
        }
    }

    void runExperimentsLinkedDeque() throws InterruptedException {
        for (int i = MIN_DEQUE; i < MAX_DEQUE; i++) {
            final int items = i;
            Queue<Integer> queue = new ConcurrentLinkedDeque<>();
            Producer producer = new ProducerUnsafe(queue, items, MAX_ITEMS);
            Consumer consumer = new ConsumerUnSafe(queue, MAX_ITEMS);
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
