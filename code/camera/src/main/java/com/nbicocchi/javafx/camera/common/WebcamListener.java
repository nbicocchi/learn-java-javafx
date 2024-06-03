package com.nbicocchi.javafx.camera.common;

import javafx.collections.ObservableList;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;

public class WebcamListener implements WebcamDiscoveryListener {
    ObservableList<Webcam> webcams;

    public WebcamListener(ObservableList<Webcam> webcams) {
        this.webcams = webcams;
        initWebcamsDetection();
    }

    private void initWebcamsDetection() {
        for (Webcam webcam : Webcam.getWebcams()) {
            System.out.println("Webcam detected: " + webcam.getName());
            webcams.addLast(webcam);
        }
        Webcam.addDiscoveryListener(this);
        System.out.println("Webcam added. Listening for events...");
    }

    @Override
    public void webcamFound(WebcamDiscoveryEvent webcamDiscoveryEvent) {
        System.out.println("Webcam connected: " + webcamDiscoveryEvent.getWebcam().getName());
        webcams.addLast(webcamDiscoveryEvent.getWebcam());
    }

    @Override
    public void webcamGone(WebcamDiscoveryEvent webcamDiscoveryEvent) {
        System.out.println("Webcam disconnected: " + webcamDiscoveryEvent.getWebcam().getName());
        webcams.remove(webcamDiscoveryEvent.getWebcam());
    }
}
