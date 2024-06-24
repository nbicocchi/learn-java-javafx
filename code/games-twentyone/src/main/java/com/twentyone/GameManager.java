package com.twentyone;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * handles the communication between the physic and the gui 
 */
@SuppressWarnings("unused")
public class GameManager extends Thread {

    private static final double FRAME_SLEEP = 0.01;//wait between two frames
    private Game game;
    private GameController controller;


    public GameManager(int points) {
        super();
        //waiting for the controller to give a value to these fields    
        Ball ball = new Ball(new Coord(0, 0), 0);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(player1, player2, ball, points);
        this.game = game;
    }

    

    public Game getGame() {
        return game;
    }

    /**
     * life cycle of the app
     */
    
    @Override
    public void run() {
        while(!game.isTerminated()) {

            game.runLifeCycle(FRAME_SLEEP, GameController.getGamesize());


            Platform.runLater(()->{//actions to be executed in the next frame of the gui
                controller.getBall().setLayoutX(game.getBall().getPos().x);
                controller.getBall().setLayoutY(game.getBall().getPos().y);
                controller.getPlayer1Score().setText(Integer.toString(game.getPlayer1().getScore()));
                controller.getPlayer2Score().setText(Integer.toString(game.getPlayer2().getScore()));

                boolean player1Playing = game.getPlayingPlayer()==game.getPlayer1();
                controller.getPlayer1Selection().setVisible(player1Playing);
                controller.getPlayer2Selection().setVisible(!player1Playing);
            });

            if(game.isTerminated()){
                Platform.runLater(()->{
                    controller.openWin(game.getPlayingPlayer(), (game.getPlayingPlayer()==game.getPlayer1()?game.getPlayer2():game.getPlayer1()));
                });
            }


            try {
                sleep((long)(FRAME_SLEEP * 1000));
            } catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    public void throwBall(double x, double y) {
        game.getBall().setThrowing(true);
        game.getBall().setDirection((new Coord(x, y)).multiply(2));
        
    }

    /**
     * initialize the fields dependent from the gui
     */
    public void initialize( Coord basketCollider, GameController controller) {
        this.controller = controller;
        ImageView ball = controller.getBall();
        game.setFreeThrowPos(new Coord(ball.getLayoutX(), ball.getLayoutY()));
        game.getBall().setPos(game.getFreeThrowPos());
        game.getBall().setRadius(ball.getFitWidth() / 2);
        game.setBasketCollider(basketCollider);
        start();
    }


    public void terminateGame(){
        game.terminate();
    }
  
}
