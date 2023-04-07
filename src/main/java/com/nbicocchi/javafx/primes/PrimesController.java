package com.nbicocchi.javafx.primes;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
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
import java.util.stream.Collectors;

public class PrimesController {
    public static final int MAX_WORKERS = 4;
    PrimeTask[] tasks = new PrimeTask[MAX_WORKERS];
    List<Integer> primes = new ArrayList<>();
    int start = 0;
    int currentBlock = 0;
    int endBlock = 25;
    int blockSize = 500000;
    @FXML private Label lbCurrent;
    @FXML private Label lbPrimes;
    @FXML private Label lbSpeed;
    @FXML private ProgressBar pbThread0;
    @FXML private ProgressBar pbThread1;
    @FXML private ProgressBar pbThread2;
    @FXML private ProgressBar pbThread3;
    @FXML private TextField tfBlockSize;
    @FXML private TextField tfBlocks;
    @FXML private TextField tfStart;

    public void initialize() {
        tfStart.textProperty().set(Integer.toString(start));
        tfBlockSize.textProperty().set(Integer.toString(blockSize));
        tfBlocks.textProperty().set(Integer.toString(endBlock));
    }

    void initSearch() {
        primes.clear();
        currentBlock = 0;
        start = Integer.parseInt(tfStart.getText());
        endBlock = Integer.parseInt(tfBlocks.getText());
        blockSize = Integer.parseInt(tfBlockSize.getText());
    }

    void fillUnusedTasks() {
        for (int i = 0; i < MAX_WORKERS; i++) {
            if (currentBlock >= endBlock) {
                return;
            }
            if (tasks[i] == null || (tasks[i].getState() == Worker.State.SUCCEEDED || tasks[i].getState() == Worker.State.FAILED || tasks[i].getState() == Worker.State.CANCELLED)) {
                tasks[i] = new PrimeTask(new PrimeSearcherFast(), start + currentBlock * blockSize, start + (currentBlock + 1) * (blockSize) - 1);
                currentBlock += 1;
                PrimeTask task = tasks[i];
                task.setOnSucceeded(event -> {
                    updateSearchData(task);
                    fillUnusedTasks();
                });
                Thread t = new Thread(tasks[i]);
                t.start();
                bindProgressBar(i);
            }
        }
    }

    void updateSearchData(PrimeTask task) {
        primes.addAll(task.getValue());
        lbCurrent.textProperty().set(String.format("%d/%d", currentBlock, endBlock));
        lbPrimes.textProperty().set(String.format("%d", primes.size()));
        lbSpeed.textProperty().set(String.format("%.0fK / s", task.getOverallSpeed() * 0.001));
    }

    void bindProgressBar(int task) {
        switch (task) {
            case 0 -> pbThread0.progressProperty().bind(tasks[0].progressProperty());
            case 1 -> pbThread1.progressProperty().bind(tasks[1].progressProperty());
            case 2 -> pbThread2.progressProperty().bind(tasks[2].progressProperty());
            case 3 -> pbThread3.progressProperty().bind(tasks[3].progressProperty());
        }
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
    void onStop() {
        unbindProgressBars();
        cancelTasks();
    }

    void unbindProgressBars() {
        pbThread0.progressProperty().unbind();
        pbThread1.progressProperty().unbind();
        pbThread2.progressProperty().unbind();
        pbThread3.progressProperty().unbind();
        pbThread0.progressProperty().setValue(0);
        pbThread1.progressProperty().setValue(0);
        pbThread2.progressProperty().setValue(0);
        pbThread3.progressProperty().setValue(0);
    }

    public void cancelTasks() {
        for (int i = 0; i < MAX_WORKERS; i++) {
            if (tasks[i] != null) {
                tasks[i].cancel();
            }
        }
    }

    @FXML
    void onStart() {
        unbindProgressBars();
        initSearch();
        fillUnusedTasks();
    }
}
