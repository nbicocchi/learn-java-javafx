package com.nbicocchi.javafx.managerworkers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PrimesController {
    public static final String[] engines = {
            "com.nbicocchi.javafx.managerworkers.PrimeSearcherFast",
            "com.nbicocchi.javafx.managerworkers.PrimeSearcherSlow",
    };
    int blockSize;
    int blockStart;
    int blockEnd;
    int blockProcessed;
    List<Integer> primes;
    List<Double> speeds;
    ExecutorService executorService;

    @FXML private Label lbCurrent;
    @FXML private Label lbPrimes;
    @FXML private Label lbSpeed;
    @FXML private Label lbSpeedAvg;
    @FXML private ProgressBar pbProgress;
    @FXML private TextField tfBlockSize;
    @FXML private TextField tfBlockStart;
    @FXML private TextField tfBlockEnd;
    @FXML private ChoiceBox<Integer> chWorkers;
    @FXML private ChoiceBox<String> chEngines;

    public void initialize() {
        chWorkers.getItems().addAll(1,2,4,8,16,32,64);
        chWorkers.getSelectionModel().select(0);
        chEngines.getItems().addAll(engines);
        chEngines.getSelectionModel().select(0);
        tfBlockStart.textProperty().set("0");
        tfBlockEnd.textProperty().set("100");
        tfBlockSize.textProperty().set("25000");
    }

    void initSearch() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        blockStart = Integer.parseInt(tfBlockStart.getText());
        blockEnd = Integer.parseInt(tfBlockEnd.getText());
        blockSize = Integer.parseInt(tfBlockSize.getText());
        blockProcessed = 0;
        primes = new ArrayList<>();
        speeds = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(chWorkers.getValue());
        for (int blockID = blockStart; blockID <= blockEnd; blockID++) {
            PrimeTask task = new PrimeTask(
                    (PrimeSearcher) Class.forName(chEngines.getValue()).getDeclaredConstructor().newInstance(),
                    blockID,
                    blockSize);
            task.setOnSucceeded(event -> updateSearchData(task));
            executorService.submit(task);
        }
    }

    void updateSearchData(PrimeTask task) {
        primes.addAll(task.getValue());
        speeds.add(chWorkers.getValue() * task.getProcessingSpeed() / 1000.0);
        lbCurrent.textProperty().set(String.format("Block# %d", task.getBlockID()));
        lbPrimes.textProperty().set(String.format("%d", primes.size()));
        lbSpeed.textProperty().set(String.format("%.1fK / s", speeds.get(speeds.size() - 1)));
        lbSpeedAvg.textProperty().set(String.format("%.1fK / s", speeds.stream().mapToDouble(a -> a).average().orElse(0.0)));
        pbProgress.progressProperty().set((++blockProcessed) / (double)(blockEnd - blockStart));
    }

    @FXML
    void onStart() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        initSearch();
    }

    @FXML
    void onStop() {
        executorService.shutdownNow();
    }

    @FXML
    void onSave() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            String str = primes.stream().sorted().map(x -> Integer.toString(x)).collect(Collectors.joining(","));
            Files.writeString(file.toPath(), str, StandardOpenOption.CREATE);
        }
    }
}
