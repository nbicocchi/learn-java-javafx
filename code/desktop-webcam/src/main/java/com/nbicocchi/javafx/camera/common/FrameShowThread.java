package com.nbicocchi.javafx.camera.common;

import java.time.Instant;
import java.util.Objects;

import com.nbicocchi.javafx.camera.lib.WebcamUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;

public class FrameShowThread extends Thread {
    private final ChoiceBox<Webcam> webcamChoiceBox;
    private final Text FPSTray;
    private final RadioButton stabilityTray;

    private Thread FPSTrayThread;

    private Webcam activeWebcam;
    private final ImageView webcamDisplay;
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    private WebcamMotionDetector motionDetector;

    public FrameShowThread(ChoiceBox<Webcam> webcamChoiceBox, Webcam activeWebcam, ImageView webcamDisplay, Text FPSTray, RadioButton stabilityTray) {
        Objects.requireNonNull(webcamChoiceBox);
        Objects.requireNonNull(activeWebcam);
        Objects.requireNonNull(webcamDisplay);
        this.webcamChoiceBox = webcamChoiceBox;
        this.activeWebcam = activeWebcam;
        this.webcamDisplay = webcamDisplay;
        this.FPSTray = FPSTray;
        this.stabilityTray = stabilityTray;
    }

    public ChoiceBox<Webcam> getWebcamChoiceBox() {
        return webcamChoiceBox;
    }

    public Text getFPSTray() {
        return FPSTray;
    }

    public Webcam getActiveWebcam() {
        return activeWebcam;
    }

    public ImageView getWebcamDisplay() {
        return webcamDisplay;
    }

    public RadioButton getStabilityTray() {
        return stabilityTray;
    }

    public void startShowingFrame() {
        if (this.isAlive()) {
            throw new IllegalStateException(this.getName() + " already started");
        }
        webcamDisplay.imageProperty().bind(imageProperty);
        this.initFrameShowThread();
        if (!this.isAlive()) {
            throw new IllegalThreadStateException("Failed to start " + this.getName() + ".");
        }
    }

    public void stopShowingFrame() throws InterruptedException {
        this.threadStopUtility(FPSTrayThread);
        this.threadStopUtility(this);
    }

    private void threadStopUtility(Thread threadToStop) throws InterruptedException {
        if (threadToStop.isAlive()) {
            threadToStop.interrupt();
            threadToStop.join();
            if (threadToStop.isAlive()) {
                throw new IllegalThreadStateException("Failed to stop " + threadToStop.getName() + ".");
            }
        } else {
            throw new IllegalThreadStateException(threadToStop.getName() + " already stopped.");
        }
    }

    @Override
    public void run() {
        System.out.println(this.getName() + " started.");
        webcamChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldWebcam, newWebcam) -> {
            activeWebcam = newWebcam;
            if (!activeWebcam.isOpen()) {
                WebcamUtils.startUpWebcam(activeWebcam, null);
            }
        });
        this.initFPSTrayThread();
        this.initMotionMonitor();
        stabilityTray.setSelected(false);
        while (!interrupted()) {
            try {
                imageProperty.set(SwingFXUtils.toFXImage(activeWebcam.getImage(), null));
                boolean previousStabilityStatus = stabilityTray.isSelected();
                boolean currentStabilityStatus = !motionDetector.isMotion();
                if (currentStabilityStatus != previousStabilityStatus) {
                    stabilityTray.setSelected(currentStabilityStatus);
                }
            } catch (Exception e) {
                System.out.println("Skipped frame: " + e.getMessage());
                break;
            }
        }
        motionDetector.stop();
        stabilityTray.setSelected(true);
        System.out.println(this.getName() + " terminated.");
    }
    private void initMotionMonitor() {
        stabilityTray.setSelected(true);
        stabilityTray.disarm();

        int interval = 210;
        int threshold = 10;
        int inertia = 10;
        motionDetector = new WebcamMotionDetector(webcamChoiceBox.getSelectionModel().getSelectedItem(), threshold, inertia);
        motionDetector.setInterval(interval);
        motionDetector.start();
    }

    private void runFPSTrayThread() {
        FPSTrayThread = new Thread(() -> {
            System.out.println("FPSTray started.");
            long start = Instant.now().toEpochMilli();
            while (!interrupted()) {
                if (Instant.now().toEpochMilli() - start >= 1000) {
                    FPSTray.setText("FPS: " + (int) activeWebcam.getFPS());
                    start = Instant.now().toEpochMilli();
                }
            }
            FPSTray.setText("FPS: --");
            System.out.println("FPSTray terminated.");
        });
    }

    private void initFrameShowThread() {
        this.setName("Webcam Frame-Showing Thread");
        this.setDaemon(true);
        this.setPriority(MAX_PRIORITY);
        this.start();
    }

    private void initFPSTrayThread() {
        this.runFPSTrayThread();
        FPSTrayThread.setName("FPSTray Thread");
        FPSTrayThread.setDaemon(true);
        FPSTrayThread.start();
    }
}
