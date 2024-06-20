package us.teamronda.briscola.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.Getter;
import us.teamronda.briscola.api.player.IPlayer;
import us.teamronda.briscola.gui.Guis;
import us.teamronda.briscola.gui.SceneSwitcher;

import java.util.List;

public class RankingController extends SceneSwitcher {

    @Getter
    private static RankingController instance;
    @FXML
    @Getter
    private AnchorPane pane;
    @FXML
    private Button nextGameBtt;
    @FXML
    private VBox leadersBox;

    public void initialize() {
        instance = this;
        setSceneHolder(nextGameBtt);
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) switchTo(Guis.START);
    }

    @FXML
    public void onButtonClicked(ActionEvent event) {
        switchTo(Guis.START);
    }

    /**
     * This method is used to create the ranking  to put in the gui
     */
    public void putRanking(List<IPlayer> players) {
        Font font = new Font(20);
        for (int i = 0; i < players.size(); i++) {
            Label l = new Label((i + 1) + ") " + players.get(i).getUsername());
            l.getStyleClass().add("Label");
            l.setFont(font);

            // Add the label to the pane
            leadersBox.getChildren().add(l);
        }
    }
}
