package com.nbicocchi.javafx.camera.lib;

import java.awt.*;
import java.util.Objects;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.collections.ObservableList;

public interface WebcamUtils {
     Dimension[] NON_STANDARD_RESOLUTIONS = new Dimension[] {
            WebcamResolution.QQVGA.getSize(),
            WebcamResolution.HQVGA.getSize(),
            WebcamResolution.QVGA.getSize(),
            WebcamResolution.WQVGA.getSize(),
            WebcamResolution.HVGA.getSize(),
            WebcamResolution.VGA.getSize(),
            WebcamResolution.WVGA.getSize(),
            WebcamResolution.FWVGA.getSize(),
            WebcamResolution.SVGA.getSize(),
            WebcamResolution.DVGA.getSize(),
            WebcamResolution.WSVGA1.getSize(),
            WebcamResolution.WSVGA2.getSize(),
            WebcamResolution.XGA.getSize(),
            WebcamResolution.XGAP.getSize(),
            WebcamResolution.WXGA1.getSize(),
            WebcamResolution.WXGA2.getSize(),
            WebcamResolution.WXGAP.getSize(),
            WebcamResolution.SXGA.getSize(),
            WebcamResolution.SXGAP.getSize(),
            WebcamResolution.WSXGAP.getSize(),
            WebcamResolution.HD.getSize(),
            WebcamResolution.FHD.getSize(),
            WebcamResolution.UXGA.getSize(),
            WebcamResolution.WUXGA.getSize(),
            WebcamResolution.QHD.getSize(),
            WebcamResolution.UHD4K.getSize()
    };
    Dimension DEFAULT_RESOLUTION = NON_STANDARD_RESOLUTIONS[0];

    private static boolean isValidResolution(Dimension resolution) {
        Objects.requireNonNull(resolution);
        for (Dimension dimension : NON_STANDARD_RESOLUTIONS) {
            if (resolution.equals(dimension)) {
                return true;
            }
        }
        return false;
    }

    static void startUpWebcam(Webcam webcam, Dimension resolution) {
        Objects.requireNonNull(webcam);
        if (webcam.isOpen()) {
            throw new RuntimeException("Webcam has been already initialized.");
        }
        webcam.open();
        webcam.setCustomViewSizes(NON_STANDARD_RESOLUTIONS);
        Dimension[] dimensionsSupported = webcam.getDevice().getResolutions();
        Dimension maxDimension = dimensionsSupported[dimensionsSupported.length - 1];
        if (Objects.isNull(resolution)) {
            if (Objects.isNull(maxDimension)) {
                resolution = DEFAULT_RESOLUTION;
            } else {
                resolution = maxDimension;
            }
        }
        changeResolution(webcam, resolution);
        if (!webcam.isOpen()) {
            throw new IllegalStateException("Failed to open webcam.");
        }
    }

    static void changeResolution(Webcam webcam, Dimension resolution) {
        Objects.requireNonNull(webcam);
        Objects.requireNonNull(resolution);
        if (!isValidResolution(resolution)) {
            throw new IllegalArgumentException("Resolution " + resolution + " is not supported.");
        }
        webcam.close();
        webcam.setViewSize(resolution);
        System.out.println("Resolution is now set on: " + resolution);
        webcam.open();
    }

    static void shutDownWebcams(ObservableList<Webcam> webcams) {
        for (Webcam webcam: webcams) {
            if (webcam.isOpen()) {
                webcam.close();
            }
        }
    }
}