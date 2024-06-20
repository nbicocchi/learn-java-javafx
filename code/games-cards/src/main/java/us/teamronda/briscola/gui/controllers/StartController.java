package us.teamronda.briscola.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import us.teamronda.briscola.GameLogic;
import us.teamronda.briscola.gui.Guis;
import us.teamronda.briscola.gui.SceneSwitcher;
import us.teamronda.briscola.objects.Player;

public class StartController extends SceneSwitcher {

    @FXML
    public TextField usernameField;
    @FXML
    private Button playButton;

    public void initialize() {
        setSceneHolder(playButton);
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) startGame();
    }

    @FXML
    public void onButtonClicked(ActionEvent event) {
        startGame();
    }

    /**
     * This method handle the initial part of the game for the gui
     */
    private void startGame() {
        // Disable the button to prevent the user
        // from pressing enter and clicking the button
        // multiple times
        playButton.setDisable(true);

        // Check if the username is not empty
        if (verification()) {
            // Try to add the player to the game
            if (GameLogic.getInstance().addPlayer(new Player(usernameField.getText()))) {
                // a little easter egg :)
                if (usernameField.getText().equalsIgnoreCase("nbicocchi")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning!");
                    alert.setContentText("YOU HAVE ALREADY WON! 🎉");
                    alert.showAndWait();

                    // We need to switch to the ranking gui first
                    // because the stop method uses the RankingController#getInstance
                    // singleton, which is not initialized until we switch to said gui.
                    switchTo(Guis.RANKING);
                    GameLogic.getInstance().stop();
                } else {
                    // This is the normal behaviour of the button
                    switchTo(Guis.TABLE);
                }
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning!");
                alert.setContentText("Username already taken!");
                alert.showAndWait();
            }
        }

        // If we get here, it means that something went wrong
        playButton.setDisable(false);
    }

    /**
     *
     * this method prevent the case where the player  don't put a name by showing a warning
     */
    public boolean verification() {
        if (usernameField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setContentText("The username must not be empty!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
