package com.nbicocchi.javafx.datavis;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WordFrequencyController {
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    void onOpen(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            List<Map.Entry<String, Long>> frequencies = WordExtractor.extract(file.getAbsolutePath());
            frequencies.stream().sorted((e1, e2) -> (int) (e2.getValue() - e1.getValue())).limit(50).forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));
            barChart.getData().clear();
            barChart.getData().add(series);
        }
    }

    @FXML
    void onClose(ActionEvent event) {
        Platform.exit();
    }
}
