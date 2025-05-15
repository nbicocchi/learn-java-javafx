package com.nbicocchi.javafx.games.debugsprite;

import com.nbicocchi.javafx.games.common.FixedFpsAnimationTimer;
import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.stream.Collectors;

public class DebugController {
    @FXML private AnchorPane root;
    @FXML private TextArea textArea;
    FixedFpsAnimationTimer timer;

    public void initialize() {
        Sprite.DEBUG_ENABLED = true;
    }

    void initializeTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new FixedFpsAnimationTimer(() -> mainLoop());
        timer.start();
    }

    private List<Sprite> balls() {
        return root.getChildren().stream()
                .filter(child -> child instanceof Sprite)
                .map(child -> (Sprite)child)
                .collect(Collectors.toList());
    }

    private void mainLoop() {
        handleCollision();
        for (Sprite sprite : balls()) {
            sprite.update();
        }
    }

    private void handleCollision() {
        for (Sprite sprite : balls()) {
            double offset;

            offset = sprite.getBoundsInParent().getMaxX() - root.getLayoutBounds().getMaxX();
            if (offset > 0) {
                sprite.getLocation().x -= offset;
                sprite.getVelocity().x *= -1;
            }

            offset = sprite.getBoundsInParent().getMinX() - root.getLayoutBounds().getMinX();
            if (offset < 0) {
                sprite.getLocation().x += offset;
                sprite.getVelocity().x *= -1;
            }

            offset = sprite.getBoundsInParent().getMaxY() - root.getLayoutBounds().getMaxY();
            if (offset > 0) {
                sprite.getLocation().y -= offset;
                sprite.getVelocity().y *= -1;
            }

            offset = sprite.getBoundsInParent().getMinY() - root.getLayoutBounds().getMinY();
            if (offset < 0) {
                sprite.getLocation().y += offset;
                sprite.getVelocity().y *= -1;
            }
        }
    }

    @FXML
    public void handleStart() {
        handleAddSprite();
        initializeTimer();
    }

    @FXML
    public void handleAddSprite() {
        Circle circle = new Circle(50, Color.BLACK);
        circle.setTranslateX(50);
        circle.setTranslateY(50);
        Sprite s = new Sprite("circle", circle, new PVector(0, 0), new PVector(5, 10));
        root.getChildren().add(s);
    }
}
