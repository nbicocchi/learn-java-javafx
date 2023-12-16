package com.nbicocchi.javafx.threads.unresponsiveui;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;


public class CounterController {
    @FXML private ProgressBar progressBar;

    @FXML void handleExecute() throws InterruptedException {
        for (int i = 0; i <= 100; i++) {
            progressBar.setProgress(i / 100.0);
            Thread.sleep(50);
        }
    }

    @FXML void handleExecuteThread() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    progressBar.setProgress(i / 100.0);
                    Thread.sleep(50);
                }
                return null;
            }
        };
        Thread t = new Thread(task);
        t.start();
    }
}