package com.nbicocchi.javafx.datavis;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;

import java.io.File;

public class WordFrequencyController {
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    void onOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            DataExtractor extractor = new WordExtractor(file.toPath());
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            extractor.extract().stream().sorted((e1, e2) -> e2.getFrequency() - e1.getFrequency()).limit(10).forEach(entry -> {
                series.getData().add(new XYChart.Data<>(entry.getWord(), entry.getFrequency()));
            });
            barChart.getData().clear();
            barChart.getData().add(series);
        }
    }

    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();
    }
}
