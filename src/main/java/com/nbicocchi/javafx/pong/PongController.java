package com.nbicocchi.javafx.pong;

import com.nbicocchi.javafx.common.PVector;
import com.nbicocchi.javafx.common.Sprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PongController {
    AnimationTimer timer;
    Sprite leftPlayer;
    Sprite rightPlayer;
    Sprite ball;
    @FXML private Pane root;
    @FXML private Text scoreLeft;
    @FXML private Text scoreRight;

    void initializeGameObjects() {
        double h = root.getHeight();
        double w = root.getWidth();
        // ball
        ball = new Sprite(new Circle(7.0, Color.WHITE));
        ball.setLocation(new PVector(w / 2, h / 2));
        ball.setVelocity(new PVector(6, 6));
        Rectangle r;
        // left player
        r = new Rectangle(10, 90, Color.WHITE);
        r.setArcWidth(10);
        r.setArcHeight(10);
        leftPlayer = new Sprite(r);
        leftPlayer.setLocation(new PVector(20, h / 2));
        // right player
        r = new Rectangle(10, 90, Color.WHITE);
        r.setArcWidth(10);
        r.setArcHeight(10);
        rightPlayer = new Sprite(r);
        rightPlayer.setLocation(new PVector(w - 30, h / 2));
        root.getChildren().addAll(leftPlayer, rightPlayer, ball);
    }

    void initializeTimer() {
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                mainLoop();
            }
        };
        timer.start();
    }

    private void mainLoop() {
        ball.update();
        leftPlayer.update();
        rightPlayer.update();
        // check boundaries (application specific!)
        checkBallBounds(ball);
        ball.display();
        leftPlayer.display();
        rightPlayer.display();
    }

    private void checkBallBounds(Sprite s) {
        double radius = ((Circle) (s.getView())).getRadius();
        // upper horizontal wall collision
        if (s.getLocation().y < radius) {
            s.getLocation().y = radius;
            s.getVelocity().y *= -1;
        }
        // lower horizontal wall collision
        if (s.getLocation().y + radius > root.getHeight()) {
            s.getLocation().y = root.getHeight() - radius;
            s.getVelocity().y *= -1;
        }
        // left vertical wall collision
        if (s.getLocation().x < radius) {
            s.getLocation().x = radius;
            s.getVelocity().x *= -1;
            scoreRight();
        }
        // right vertical wall collision
        if (s.getLocation().x + radius > root.getWidth()) {
            s.getLocation().x = root.getWidth() - radius;
            s.getVelocity().x *= -1;
            scoreLeft();
        }
        // right player and ball collision
        if (ball.intersects(rightPlayer) && ball.getVelocity().x > 0) {
            ball.getVelocity().x *= -1;
            ball.getLocation().x = rightPlayer.getLocation().x - radius;
            if (ball.getVelocity().y * rightPlayer.getVelocity().y > 0) {
                ball.getVelocity().y += 1;
            } else {
                ball.getVelocity().y -= 1;
            }
        }
        // left player and ball collision
        if (ball.intersects(leftPlayer) && ball.getVelocity().x < 0) {
            ball.getVelocity().x *= -1;
            ball.getLocation().x += leftPlayer.getLocation().x + leftPlayer.getWidth();
            if (ball.getVelocity().y * leftPlayer.getVelocity().y > 0) {
                ball.getVelocity().y += 1;
            } else {
                ball.getVelocity().y -= 1;
            }
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
