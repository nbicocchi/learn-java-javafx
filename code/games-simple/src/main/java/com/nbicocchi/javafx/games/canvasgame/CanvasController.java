package com.nbicocchi.javafx.games.canvasgame;

import com.nbicocchi.javafx.games.common.FixedFpsAnimationTimer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class CanvasController {
    @FXML private Canvas canvas;
    FixedFpsAnimationTimer timer;
    List<Ball> balls;

    public void initialize() {
        balls = new ArrayList<>();
    }

    void initializeTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new FixedFpsAnimationTimer(() -> mainLoop());
        timer.start();
    }

    private void mainLoop() {
        drawBackground();
        drawBalls();
        removeDeadBalls();
    }

    private void drawBackground() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawBalls() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Ball ball : balls) {
            ball.update();
            ball.paint(gc);
        }
    }

    private void removeDeadBalls() {
        balls.removeIf(ball -> !canvas.getLayoutBounds().contains(ball.x, ball.y));
    }

    @FXML
    public void handleStart() {
        initializeTimer();
    }

    @FXML
    public void handleEnd() {
        Platform.exit();
    }

    @FXML
    public void handleMouseMoved() {
        RandomGenerator rnd = RandomGenerator.getDefault();
        Ball ball = new Ball(
                canvas.getWidth() / 2,
                canvas.getHeight() / 2,
                rnd.nextInt(20) - 10,
                rnd.nextInt(20) - 10,
                rnd.nextInt(10, 70),
                Color.MEDIUMPURPLE.deriveColor(0, 1, 1, rnd.nextDouble()));
        if (ball.vx == 0 || ball.vy == 0) {
            ball.vx += 1;
            ball.vy += 1;
        }
        balls.add(ball);
    }
}
