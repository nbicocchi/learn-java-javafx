package com.nbicocchi.javafx.threads.managerworkers;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class PrimesController {
    int startBlock;
    int endBlock;
    int currentBlock;
    int blockSize;
    PrimeTask[] tasks;
    List<Integer> primes;
    List<Double> speeds;
    ExecutorService executorService;

    @FXML private Label lbCurrent;
    @FXML private Label lbPrimes;
    @FXML private Label lbSpeed;
    @FXML private Label lbSpeedAvg;
    @FXML private ProgressBar pbProgress;
    @FXML private TextField tfBlockSize;
    @FXML private TextField tfEndBlock;
    @FXML private TextField tfStartBlock;
    @FXML private ChoiceBox<Integer> chWorkers;

    public void initialize() {
        chWorkers.getItems().addAll(1,2,4,8,16,32);
        chWorkers.getSelectionModel().select(2);
        tfStartBlock.textProperty().set("0");
        tfEndBlock.textProperty().set("100");
        tfBlockSize.textProperty().set("250000");
    }

    void initSearch() {
        currentBlock = 0;
        startBlock = Integer.parseInt(tfStartBlock.getText());
        endBlock = Integer.parseInt(tfEndBlock.getText());
        blockSize = Integer.parseInt(tfBlockSize.getText());
        tasks = new PrimeTask[chWorkers.getValue()];
        primes = new ArrayList<>();
        speeds = new ArrayList<>();
    }

    void fillUnusedTasks() {
        for (int i = 0; i < chWorkers.getValue(); i++) {
            if (currentBlock >= endBlock) {
                return;
            }
            if (tasks[i] == null || (tasks[i].getState() == Worker.State.SUCCEEDED || tasks[i].getState() == Worker.State.FAILED || tasks[i].getState() == Worker.State.CANCELLED)) {
                tasks[i] = new PrimeTask(new PrimeSearcherFast(), startBlock + currentBlock * blockSize, startBlock + (currentBlock + 1) * (blockSize) - 1);
                currentBlock += 1;
                PrimeTask task = tasks[i];
                task.setOnSucceeded(event -> {
                    updateSearchData(task);
                    fillUnusedTasks();
                });
                Thread t = new Thread(tasks[i]);
                t.start();
            }
        }
    }

    void updateSearchData(PrimeTask task) {
        primes.addAll(task.getValue());
        speeds.add(chWorkers.getValue() * task.getProcessingSpeed() / 1000.0);
        lbCurrent.textProperty().set(String.format("%d/%d", currentBlock, endBlock));
        lbPrimes.textProperty().set(String.format("%d", primes.size()));
        lbSpeed.textProperty().set(String.format("%.1fK / s", speeds.get(speeds.size() - 1)));
        lbSpeedAvg.textProperty().set(String.format("%.1fK / s", speeds.stream().mapToDouble(a -> a).average().orElse(0.0)));
        pbProgress.progressProperty().set(currentBlock / (double)endBlock);
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

    @FXML
    void onStart() {
        initSearch();
        fillUnusedTasks();
    }

    @FXML
    void onStop() {
        cancelTasks();
    }

    public void cancelTasks() {
        for (int i = 0; i < chWorkers.getValue(); i++) {
            if (tasks[i] != null) {
                tasks[i].cancel();
            }
        }
    }
}
