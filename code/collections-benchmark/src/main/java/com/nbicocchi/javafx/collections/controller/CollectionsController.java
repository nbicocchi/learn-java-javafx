package com.nbicocchi.javafx.collections.controller;

import com.nbicocchi.javafx.collections.experiment.ExperimentTask;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;

public class CollectionsController {
    public static String[] plugins = {
            "com.nbicocchi.javafx.collections.experiment.FILLALH",
            "com.nbicocchi.javafx.collections.experiment.FILLALT",
            "com.nbicocchi.javafx.collections.experiment.FILLLLH",
            "com.nbicocchi.javafx.collections.experiment.FILLLLT",
            "com.nbicocchi.javafx.collections.experiment.FILLTS",
            "com.nbicocchi.javafx.collections.experiment.FILLHS",
            "com.nbicocchi.javafx.collections.experiment.RETRAL",
            "com.nbicocchi.javafx.collections.experiment.RETRALSORTED",
            "com.nbicocchi.javafx.collections.experiment.RETRLL",
            "com.nbicocchi.javafx.collections.experiment.RETRLLSORTED",
            "com.nbicocchi.javafx.collections.experiment.RETRTS",
            "com.nbicocchi.javafx.collections.experiment.RETRHS",
            "com.nbicocchi.javafx.collections.experiment.SORTAL",
            "com.nbicocchi.javafx.collections.experiment.SORTLL",
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
        task.valueProperty().addListener((observable, oldValue, newValue) -> data.getData().add(new XYChart.Data<>(newValue.getKey(), newValue.getValue())));
        task.setOnSucceeded(event -> {
            progress.progressProperty().unbind();
            progress.progressProperty().setValue(100);
        });
        progress.progressProperty().bind(task.progressProperty());
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
