package com.nbicocchi.javafx.producerconsumer;

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;

import java.lang.reflect.InvocationTargetException;
import java.util.Queue;

public class ProducerConsumerController {
    public static final int MAX_ITEMS = 25000;
    public static final int MIN_QUEUE = 1;
    public static final int MAX_QUEUE = 128;
    public static final String[] producers = {
            "com.nbicocchi.javafx.producerconsumer.ProducerSynchronized",
            "com.nbicocchi.javafx.producerconsumer.ProducerSynchronizedWaitNotify",
            "com.nbicocchi.javafx.producerconsumer.ProducerUnsafe",
    };
    public static final String[] consumers = {
            "com.nbicocchi.javafx.producerconsumer.ConsumerSynchronized",
            "com.nbicocchi.javafx.producerconsumer.ConsumerSynchronizedWaitNotify",
            "com.nbicocchi.javafx.producerconsumer.ConsumerUnsafe",
    };
    public static final String[] sharedObjects = {
            "java.util.LinkedList",
            "java.util.ArrayDeque",
            "java.util.concurrent.ConcurrentLinkedDeque",
            "java.util.concurrent.LinkedBlockingQueue",
    };

    @FXML private ScatterChart<Number, Number> chart;
    @FXML private ChoiceBox<String> chProducer;
    @FXML private ChoiceBox<String> chConsumer;
    @FXML private ChoiceBox<String> chSharedObject;

    int experimentID = 0;

    public void initialize() {
        chProducer.getItems().addAll(producers);
        chProducer.getSelectionModel().select(0);
        chConsumer.getItems().addAll(consumers);
        chConsumer.getSelectionModel().select(0);
        chSharedObject.getItems().addAll(sharedObjects);
        chSharedObject.getSelectionModel().select(0);
    }

    @FXML
    void onClear() {
        chart.getData().clear();
    }

    @FXML
    void onStart() throws InterruptedException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        for (int i = MIN_QUEUE; i < MAX_QUEUE; i++) {
            final int items = i;
            Queue<Integer> queue =
                    (Queue) Class.forName(chSharedObject.getValue()).newInstance();
            Producer producer =
                    (Producer) Class.forName(chProducer.getValue()).getDeclaredConstructor(
                            Queue.class, Integer.class, Integer.class).newInstance(queue, items, MAX_ITEMS);
            Consumer consumer =
                    (Consumer) Class.forName(chConsumer.getValue()).getDeclaredConstructor(
                            Queue.class, Integer.class).newInstance(queue, MAX_ITEMS);
            consumer.setOnSucceeded(event -> {
                String experimentName = String.format("Experiment-%d", experimentID);
                XYChart.Series<Number, Number> data = getSeriesByName(experimentName);
                data.getData().add(new XYChart.Data<>(items, consumer.getValue()));
            });
            runExperiment(producer, consumer);
        }
        experimentID++;
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
