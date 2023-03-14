package com.nbicocchi.javafx.collections;

import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Pair;

import java.util.List;

public class CollectionsController {
    public static String[] plugins = {
            "com.nbicocchi.javafx.collections.FILLALH",
            "com.nbicocchi.javafx.collections.FILLALT",
            "com.nbicocchi.javafx.collections.FILLLLH",
            "com.nbicocchi.javafx.collections.FILLLLT",
            "com.nbicocchi.javafx.collections.FILLTS",
            "com.nbicocchi.javafx.collections.FILLHS",
            "com.nbicocchi.javafx.collections.RETRAL",
            "com.nbicocchi.javafx.collections.RETRALSORTED",
            "com.nbicocchi.javafx.collections.RETRLL",
            "com.nbicocchi.javafx.collections.RETRLLSORTED",
            "com.nbicocchi.javafx.collections.RETRTS",
            "com.nbicocchi.javafx.collections.RETRHS",
            "com.nbicocchi.javafx.collections.SORTAL",
            "com.nbicocchi.javafx.collections.SORTLL",
    };

    @FXML private ScatterChart<Number, Number> chart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ProgressIndicator progress;
    @FXML private ChoiceBox<String> chCollection;

    @FXML
    void onStart() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        XYChart.Series<Number, Number> data = getSeriesByName(chCollection.getValue());
        ExperimentTask task = (ExperimentTask) Class.forName(chCollection.getValue()).newInstance();
        progress.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            progress.progressProperty().unbind();
            progress.progressProperty().setValue(100);
            List<Pair<Integer, Integer>> results = task.getValue();
            for (Pair<Integer, Integer> result : results) {
                data.getData().add(new XYChart.Data<>(result.getKey(), result.getValue()));
            }
        });
        Thread t = new Thread(task);
        t.start();
    }

    @FXML
    void onClear() {
        chart.getData().clear();
    }

    public void initialize() {
        chCollection.getItems().addAll(plugins);
        chCollection.getSelectionModel().select(0);
        xAxis.setLabel("Collection Size (items)");
        yAxis.setLabel("Time (microseconds)");
        xAxis.setLowerBound(0);
        yAxis.setLowerBound(0);
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
}
