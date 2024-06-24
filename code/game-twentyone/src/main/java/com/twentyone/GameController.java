package com.twentyone;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * handles the game graphic interface
 */
public class GameController {

    private static final Coord gameSize = new Coord(820, 520);//window size

    @FXML
    private ImageView ball;

    @FXML
    private Line trajectory;

    @FXML
    private Circle basketCollider;

    @FXML
    private Label player1Score;

    @FXML
    private Label player2Score;  
    
    @FXML
    private Pane player1Selection;

    @FXML
    private Pane player2Selection;

    @FXML
    private AnchorPane optionsMenu;

    @FXML
    private AnchorPane rulesMenu;

    @FXML
    private AnchorPane winMenu;

    @FXML
    private Label playerLose;

    @FXML
    private Label playerWin;



    /**
     * called from javafx when the gui is ready
     */
    @FXML
    private void initialize() {
        Coord basketColliderPos = new Coord(basketCollider.getLayoutX(), basketCollider.getLayoutY());
        App.getGameManager().initialize(basketColliderPos, this);
    }

    /**
     * draw the trajectory
     */
    @FXML
    public void ballDrag(MouseEvent e) {

        if (!App.getGameManager().getGame().getBall().getThrowing()) {
            double ballX = ball.getLayoutX() + ball.getFitWidth() / 2;
            double ballY = ball.getLayoutY() + ball.getFitHeight() / 2;
            
            trajectory.startXProperty().set(ballX);
            trajectory.startYProperty().set(ballY);  
            trajectory.endXProperty().set(ballX - e.getX());
            trajectory.endYProperty().set(ballY - e.getY());
            
            trajectory.setVisible(true);
        }
    }

    /**
     * hide the trajectory and set the throw to the ball
     */
    @FXML
    public void ballDragReleased(MouseEvent e) {
        if (!App.getGameManager().getGame().getBall().getThrowing()) {
            trajectory.setVisible(false);
            App.getGameManager().throwBall(-e.getX(), -e.getY());

        }
    }

    @FXML
    void closeOptions(MouseEvent event) {
        optionsMenu.setVisible(false);
    }

    @FXML
    void closeRules(MouseEvent event) {
        rulesMenu.setVisible(false);
    }
    

    @FXML
    void goToMenu(MouseEvent event) throws IOException {
        App.getGameManager().terminateGame();
        App.setRoot("menu");
    }

    @FXML
    void openRules(MouseEvent event) {
        rulesMenu.setVisible(true);
    }

    @FXML
    void openOptions(MouseEvent event) {
        optionsMenu.setVisible(true);
    }

    void openWin(Player winner, Player loser) {
        playerWin.setText(winner.getName());
        playerLose.setText(loser.getName());
        winMenu.setVisible(true);
    }

    @FXML
    void playAgain(MouseEvent event) throws IOException {
        int points = App.getGameManager().getGame().getPointsNumber();
        App.getGameManager().terminateGame();
        GameManager newGameManager = new GameManager(points);
        App.setGameManager(newGameManager);
        App.setRoot("game");
    }


    @SuppressWarnings("exports")
    public Label getPlayer1Score() {
        return player1Score;
    }


    @SuppressWarnings("exports")
    public Label getPlayer2Score() {
        return player2Score;
    }


    public Pane getPlayer1Selection() {
        return player1Selection;
    }


    public Pane getPlayer2Selection() {
        return player2Selection;
    }


    public static Coord getGamesize() {
        return gameSize;
    }


    public ImageView getBall() {
        return ball;
    }

    
}