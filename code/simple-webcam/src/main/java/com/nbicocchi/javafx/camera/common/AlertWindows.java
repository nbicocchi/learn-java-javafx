package com.nbicocchi.javafx.camera.common;

import javafx.scene.control.Alert;

public interface AlertWindows {
    static void showFailedToTakePictureAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getDialogPane().setMinWidth(675);
        alert.getDialogPane().setMaxWidth(675);
        alert.setTitle("Warning!");
        alert.setHeaderText("Unable to take picture");
        alert.setContentText("""
                The application is unable to take the picture or to open the editor.
                Quick fixes:
                 ~ Retry to take the photo;
                 ~ Check if your webcam works properly;
                 ~ Try to restart the application;
                 ~ Try to restart the computer.""");
        alert.showAndWait();
    }

    static void showFatalError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinWidth(400);
        alert.getDialogPane().setMaxWidth(300);
        alert.setTitle("Fatal Error");
        alert.setHeaderText("An error has occurred.");
        alert.setContentText("""
                The application ran into a fatal error.
                Try to restart it or reboot the computer.""");
        alert.showAndWait();
    }

    static void showDialogAlert(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinWidth(400);
        alert.getDialogPane().setMaxWidth(300);
        alert.setTitle("Attention");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
