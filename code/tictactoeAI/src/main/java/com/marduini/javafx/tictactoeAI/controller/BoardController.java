package com.marduini.javafx.tictactoeAI.controller;

import com.marduini.javafx.tictactoeAI.model.Game;
import com.marduini.javafx.tictactoeAI.model.State;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    @FXML private Label Info;
    @FXML private StackPane p1;
    @FXML private StackPane p2;
    @FXML private StackPane p3;
    @FXML private StackPane p4;
    @FXML private StackPane p5;
    @FXML private StackPane p6;
    @FXML private StackPane p7;
    @FXML private StackPane p8;
    @FXML private StackPane p9;
    @FXML private AnchorPane board;

    /**
     * List containing the panes p1,p2,p3...p9, associated with the FXML file, so associated to the current scene
     */
    ArrayList<StackPane> panes;
    /**
     * Instance of the class game, used to perform the minimax methods by the O-player
     */
    Game game;


    /**
     *  establishes if there's a game in progress or not
     */
    private boolean inGame;

    /**
     *  true if anyone has win or it is a tie, so the current game is finished
     */
    private boolean isWin;

    public BoardController() {
        this.inGame = false;
        this.isWin = false;
        this.game = new Game();
    }

    /**
     *  Shows a simple warning dialog to protect the game in progress
     */
    void showAlertInGame() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Game in progress");
        alert.setHeaderText("A game is already in progress");
        alert.setContentText("Before starting a new game you must finish this one!");
        alert.showAndWait();
    }

    @FXML
    void handleNewGame() {
        if(inGame)
            showAlertInGame();
        else{
            inGame = true;
            isWin = false;
            Info.setText("Game's starting...");
            startGame();
            Info.setText("User, it's your turn!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panes = new ArrayList<>(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8,p9));
        panes.forEach(this::resetLabel);
        panes.forEach(this::setPane);
    }



    public void resetLabel(StackPane p) {
        getLabel(p).setText("");
    }


    public void startGame() {
        board.setVisible(true);
        panes.forEach(this::resetLabel);
        panes.forEach(pane -> pane.setDisable(false));
        panes.forEach(this::setPane);
    }

    /**
     *
     * @param p stackpane clicked by the player where he wants to insert his symbol
     */
    public void setPane(StackPane p) {
        p.setOnMouseClicked(mouseEvent -> {
            if(checkValidMove(getLabel(p))) {    // Checking whether user's play is valid or not
                getLabel(p).setText("X");       // The user is assigned to symbol X

                // Every time the user makes a play, we need to check if he won or not
                checkWin();


                if(!isWin){
                    // The turn of the user is finished but anyone won yet, so it's AI's turn
                    Info.setText("It's AI's turn!");

                    // Defining a pause time in order to let the AI computing her best move
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e-> insertAImove(game.minMaxDecision(getBoardCurrentState())));
                    pause.play();
                }
            }
        });
    }

    /***
     * Returns the Label inserted in the stackpane passed
     * @param p pane that contains the label we need
     * @return Label
     */
    public Label getLabel(StackPane p) {
        return (Label) p.getChildren().getFirst();
    }

    /***
     * Returns true if the content of the label is empty
     * @param l, Label
     * @return boolean
     */
    public boolean checkValidMove(Label l) {
        return l.getText().isEmpty();
    }

    /***
     * Insert the best move obtained from the AI in the board, and then check for the win
     * @param move the best move for current state of the board
     */
    public void insertAImove(int move) {
        getLabel(panes.get(move)).setText("O");
        checkWin();
        if(!isWin)
            Info.setText("User, It's your turn!");
    }

    /***
     *
     * @return a State object representing the current state of the board
     */
    public State getBoardCurrentState() {
        String[] board = new String[9];
        for(int i = 0; i < 9; i++)
            board[i] = getLabel(panes.get(i)).getText();
        return new State(0, board);
    }

    /**
     *  check for the finish of the match, if the user or the AI win, or if it is a tie so every pane is filled
     */
    public void checkWin() {
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> getLabel(p1).getText() + getLabel(p2).getText() + getLabel(p3).getText();
                case 1 -> getLabel(p4).getText() + getLabel(p5).getText() + getLabel(p6).getText();
                case 2 -> getLabel(p7).getText() + getLabel(p8).getText() + getLabel(p9).getText();
                case 3 -> getLabel(p1).getText() + getLabel(p5).getText() + getLabel(p9).getText();
                case 4 -> getLabel(p3).getText() + getLabel(p5).getText() + getLabel(p7).getText();
                case 5 -> getLabel(p1).getText() + getLabel(p4).getText() + getLabel(p7).getText();
                case 6 -> getLabel(p2).getText() + getLabel(p5).getText() + getLabel(p8).getText();
                case 7 -> getLabel(p3).getText() + getLabel(p6).getText() + getLabel(p9).getText();
                default -> null;
            };

            //X winner
            if (line.equals("XXX")) {
                Info.setText("YOU WON!");
                isWin = true;
                panes.forEach(pane -> pane.setDisable(true));
                inGame = false;
            }

            //O winner
            else if (line.equals("OOO")) {
                Info.setText("AI WON!");
                isWin = true;
                panes.forEach(pane -> pane.setDisable(true));
                inGame = false;
            }
        }

        // check for the draw
        boolean check = true;
        for(StackPane pane : panes) {
            if(getLabel(pane).getText().isEmpty())
                check = false;
        }

        if(check){
            Info.setText("IT'S A TIE!");
            panes.forEach(pane -> pane.setDisable(true));
            inGame = false;
            isWin = true;
        }
    }
}