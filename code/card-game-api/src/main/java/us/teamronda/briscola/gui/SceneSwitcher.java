package us.teamronda.briscola.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Setter;

import java.io.IOException;

/**
 * Enables a controller to switch between {@link Scene scenes}
 * by giving a generic node as a starting point.
 */

@Setter(AccessLevel.PROTECTED)
public class SceneSwitcher {

    private Node sceneHolder;

    /**
     * This method allow to switch between guis
     * @param gui The enum corresponding the gui to switch to
     * @throws IllegalStateException if the {@code sceneHolder} object is null
     */
    public void switchTo(Guis gui) {
        if (sceneHolder == null) {
            throw new IllegalStateException("Scene holder not set");
        }

        // Get the current stage from the node
        // (Unchecked cast moment: do not do this at home)
        Stage stage = (Stage) sceneHolder.getScene().getWindow();
        // Try to load the new scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(gui.getPath()));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal Error");
            alert.setContentText("Could not load the fxml file: " + gui.getPath());
            alert.showAndWait();

            Platform.exit();
        }
    }
}
