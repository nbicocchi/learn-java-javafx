package com.nbicocchi.javafx.games.gameoflife;

import com.nbicocchi.javafx.games.common.FixedFpsAnimationTimer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.random.RandomGenerator;

public class LifeController {
    public static final int ROWS = 200;
    public static final int COLS = 200;

    @FXML private CheckBox cbAnimation;
    @FXML private TilePane tilePane;
    GameOfLifeBean gameOfLifeBean;
    FixedFpsAnimationTimer animationTimer;

    public void initialize() {
        gameOfLifeBean = new GameOfLifeBean(ROWS, COLS);
        initPanel();
        updatePanel();
    }

    void initPanel() {
        tilePane.getChildren().clear();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Rectangle rect = new Rectangle(3, 3, Color.PINK);
                rect.setArcHeight(2);
                rect.setArcWidth(2);
                tilePane.getChildren().add(rect);
            }
        }
    }

    void updatePanel() {
        int[][] grid = gameOfLifeBean.getGrid();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Rectangle tile = (Rectangle) tilePane.getChildren().get(i * COLS + j);
                Color color = grid[i][j] == 0 ? Color.PINK : Color.PURPLE;
                tile.setFill(color);
            }
        }
    }

    @FXML
    void onInit() {
        gameOfLifeBean.init(RandomGenerator.getDefault());
        updatePanel();
    }

    @FXML
    void onStep() {
        gameOfLifeBean.evolve();
        updatePanel();
    }

    @FXML
    void onAnimation() {
        if (cbAnimation.isSelected()) {
            animationTimer = new FixedFpsAnimationTimer(() -> mainLoop());
            animationTimer.start();
        } else {
            animationTimer.stop();
        }
    }

    private void mainLoop() {
        gameOfLifeBean.evolve();
        updatePanel();
    }
}
