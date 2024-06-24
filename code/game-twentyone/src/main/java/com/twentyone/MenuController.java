package com.twentyone;

import java.io.IOException;
import javafx.fxml.FXML;

public class MenuController {


    private void startGame(int points) throws IOException {

        GameManager gameManager = new GameManager(points);
        App.setGameManager(gameManager);
        App.setRoot("game");

    }

    @FXML
    private void startSeven() throws IOException {
        startGame(7);
    }

    @FXML
    private void startTwentyOne() throws IOException{
        startGame(21);
    }

    @FXML
    private void initialize() {

    }
}
