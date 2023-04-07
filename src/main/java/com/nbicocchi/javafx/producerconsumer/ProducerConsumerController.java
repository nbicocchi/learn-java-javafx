package com.nbicocchi.javafx.producerconsumer;

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

public class ProducerConsumerController {
    private final XYChart.Series<String, Number> seriesSYN = new XYChart.Series<>();
    private final XYChart.Series<String, Number> seriesSYNWN = new XYChart.Series<>();
    private final XYChart.Series<String, Number> seriesTSAFE = new XYChart.Series<>();
    @FXML private ScatterChart<String, Number> chartScatter;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ChoiceBox<String> chArchitecture;
    @FXML private ChoiceBox<String> chQueueSize;
    @FXML private Label lbSpeed;

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
        Queue<Integer> queue = new LinkedList<>();
        Producer<Integer> producer = new ProducerSynchronized<>(queueSize, item, queue);
        Consumer<Integer> consumer = new ConsumerSynchcronized<>(queue);
        return runTest(producer, consumer);
    }

    int experimentSynchronizedWN(int queueSize, int item) {
        Queue<Integer> queue = new LinkedList<>();
        Producer<Integer> producer = new ProducerSynchronizedWaitNotify<>(queueSize, item, queue);
        Consumer<Integer> consumer = new ConsumerSynchcronizedWaitNotify<>(queue);
        return runTest(producer, consumer);
    }

    int experimentThreadSafeQueue(int queueSize, int item) {
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        Producer<Integer> producer = new ProducerLinkedQueue<>(queueSize, item, queue);
        Consumer<Integer> consumer = new ConsumerLinkedQueue<>(queue);
        return runTest(producer, consumer);
    }

    int runTest(Producer<Integer> p, Consumer<Integer> c) {
        Thread t0 = new Thread(p);
        Thread t1 = new Thread(c);
        t0.start();
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        p.running = false;
        c.running = false;
        try {
            t0.join(2000);
            t1.join(2000);
        } catch (InterruptedException ignored) {
        }
        return c.count;
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
