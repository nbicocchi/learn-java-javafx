package com.nbicocchi.javafx.games.pong;

import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PongController {
    AnimationTimer timer;
    Sprite leftPlayer;
    Sprite rightPlayer;
    Sprite ball;

    @FXML private AnchorPane root;
    @FXML private Text scoreLeft;
    @FXML private Text scoreRight;

    @FXML
    public void handlePlay() {
        initializeGameObjects();
        initializeScores();
        initializeTimer();
    }

    void initializeGameObjects() {
        // remove sprites from eventual former match
        root.getChildren().removeAll(ball, leftPlayer, rightPlayer);

        // ball
        Circle circle = new Circle(8, Color.WHITE);
        circle.setTranslateX(8);
        circle.setTranslateY(8);
        ball = new Sprite("ball", circle,
                new PVector(root.getWidth() / 2, root.getHeight() / 2),
                new PVector(5, 5));

        // left player
        Rectangle lplayer = new Rectangle(20, 90, Color.WHITE);
        lplayer.setArcWidth(10);
        lplayer.setArcHeight(10);
        leftPlayer = new Sprite("left_player", lplayer,
                new PVector(10, root.getHeight() / 2));

        // right player
        Rectangle rplayer = new Rectangle(20, 90, Color.WHITE);
        rplayer.setArcWidth(10);
        rplayer.setArcHeight(10);
        rightPlayer = new Sprite("right_player", rplayer,
                new PVector(root.getWidth() - 30, root.getHeight() / 2));

        // add sprites to panel
        root.getChildren().addAll(leftPlayer, rightPlayer, ball);
    }

    private void initializeScores() {
        scoreRight.setText("0");
        scoreLeft.setText("0");
    }

    void initializeTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                mainLoop();
            }
        };
        timer.start();
    }

    private void mainLoop() {
        rebounds();
        ball.update();
        leftPlayer.update();
        rightPlayer.update();
    }

    private void rebounds() {
        double offset;

        // right wall
        offset = ball.getBoundsInParent().getMaxX() - root.getLayoutBounds().getMaxX();
        if (offset > 0) {
            ball.getLocation().x -= offset;
            ball.getVelocity().x *= -1;
            scoreRight();
        }

        // left wall
        offset = ball.getBoundsInParent().getMinX() - root.getLayoutBounds().getMinX();
        if (offset < 0) {
            ball.getLocation().x -= offset;
            ball.getVelocity().x *= -1;
            scoreLeft();
        }

        // lower wall
        offset = ball.getBoundsInParent().getMaxY() - root.getLayoutBounds().getMaxY();
        if (offset > 0) {
            ball.getLocation().y -= offset;
            ball.getVelocity().y *= -1;
        }

        // upper wall
        offset = ball.getBoundsInParent().getMinY() - root.getLayoutBounds().getMinY();
        if (offset < 0) {
            ball.getLocation().y -= offset;
            ball.getVelocity().y *= -1;
        }

        // right player collision
        if (ball.intersects(rightPlayer)) {
            offset = ball.getBoundsInParent().getMaxX() - rightPlayer.getBoundsInParent().getMinX();
            ball.getVelocity().x *= -1;
            ball.getLocation().x -= offset;
        }

        // left player collision
        if (ball.intersects(leftPlayer)) {
            offset = ball.getBoundsInParent().getMinX() - leftPlayer.getBoundsInParent().getMaxX();
            ball.getVelocity().x *= -1;
            ball.getLocation().x -= offset;
        }
    }

    private void scoreRight() {
        scoreRight.setText(Integer.toString(Integer.parseInt(scoreRight.getText()) + 1));
    }

    private void scoreLeft() {
        scoreLeft.setText(Integer.toString(Integer.parseInt(scoreLeft.getText()) + 1));
    }

    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP)
            rightPlayer.getVelocity().y = -6;
        if (event.getCode() == KeyCode.DOWN)
            rightPlayer.getVelocity().y = 6;
        if (event.getCode() == KeyCode.W)
            leftPlayer.getVelocity().y = -6;
        if (event.getCode() == KeyCode.S)
            leftPlayer.getVelocity().y = 6;
    }

    void keyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN)
            rightPlayer.getVelocity().y = 0;
        if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.S)
            leftPlayer.getVelocity().y = 0;
    }
}
