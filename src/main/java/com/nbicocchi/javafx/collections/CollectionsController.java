package com.nbicocchi.javafx.collections;

import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;

public class CollectionsController {
    private final XYChart.Series<Number, Number> arraylist = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> linkedList = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> hashset = new XYChart.Series<>();
    @FXML
    private ScatterChart<Number, Number> chart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ChoiceBox<String> chCollection;

    @FXML
    void onStart() {
        switch (chCollection.getValue()) {
            case "ArrayList(tail)" -> {
                XYChart.Series<Number, Number> fill = getSeriesByName("ArrayList(tail)-fill");
                XYChart.Series<Number, Number> retrieve = getSeriesByName("ArrayList(tail)-retrieve");
                ExperimentTask experimentTask = new ALFT();
                experimentTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                    fill.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getFillTime()));
                    retrieve.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getRetrieveTime()));
                });
                Thread th = new Thread(experimentTask);
                th.start();
            }
            case "LinkedList(tail)" -> {
                XYChart.Series<Number, Number> fill = getSeriesByName("LinkedList(tail)-fill");
                XYChart.Series<Number, Number> retrieve = getSeriesByName("LinkedList(tail)-retrieve");
                ExperimentTask experimentTask = new LLFT();
                experimentTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                    fill.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getFillTime()));
                    retrieve.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getRetrieveTime()));
                });
                Thread th = new Thread(experimentTask);
                th.start();
            }
            case "ArrayList(head)" -> {
                XYChart.Series<Number, Number> fill = getSeriesByName("ArrayList(head)-fill");
                XYChart.Series<Number, Number> retrieve = getSeriesByName("ArrayList(head)-retrieve");
                ExperimentTask experimentTask = new ALFH();
                experimentTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                    fill.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getFillTime()));
                    retrieve.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getRetrieveTime()));
                });
                Thread th = new Thread(experimentTask);
                th.start();
            }
            case "LinkedList(head)" -> {
                XYChart.Series<Number, Number> series = getSeriesByName("LinkedList(head)");
                ExperimentTask experimentTask = new LLFH();
                experimentTask.valueProperty().addListener((observable, oldValue, newValue) -> series.getData().add(new XYChart.Data<>(newValue.getItems(), newValue.getFillTime())));
                Thread th = new Thread(experimentTask);
                th.start();
            }
            case "TreeSet" -> {
                XYChart.Series<Number, Number> series = getSeriesByName("TreeSet");
            }
            case "HashSet" -> {
                XYChart.Series<Number, Number> series = getSeriesByName("HashSet");

            }
        }
    }

    XYChart.Series getSeriesByName(String name) {
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

    public void initialize() {
        chCollection.getItems().addAll(
                "ArrayList(tail)", "LinkedList(tail)",
                "ArrayList(head)", "LinkedList(head)",
                "TreeSet", "HashSet");
        chCollection.getSelectionModel().select(0);
        xAxis.setLabel("Items");
        yAxis.setLabel("Microseconds");
        xAxis.setLowerBound(0);
        yAxis.setLowerBound(0);
    }
}
