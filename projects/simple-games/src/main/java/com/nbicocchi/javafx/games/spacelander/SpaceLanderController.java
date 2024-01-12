package com.nbicocchi.javafx.games.spacelander;

import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import com.nbicocchi.javafx.games.common.UtilsColor;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class SpaceLanderController {
    public static final double SPEED_THRESHOLD = 0.4;
    @FXML AnchorPane root;
    @FXML Text textLose;
    @FXML Text textWin;
    @FXML Text textSpeed;
    Sprite ship, pad;
    AnimationTimer timer;
    HashMap<String, Node> availableNodes;
    HashMap<String, PVector> forces;
    HashMap<String, Boolean> activeForces;

    @FXML
    public void initialize() {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResourceAsStream("moon.jpg")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));
    }

    @FXML
    public void handlePlay() {
        loadResources();
        initializeGameObjects();
        initializeForces();
        initializeTimer();
    }

    private void loadResources() {
        Node ship = new ImageView(new Image(getClass().getResourceAsStream("ship.png")));
        Node pad = new ImageView(new Image(getClass().getResourceAsStream("pad.png")));
        Node downThrust = new ImageView(new Image(getClass().getResourceAsStream("thrust.png")));
        Node leftThrust = new ImageView(new Image(getClass().getResourceAsStream("thrust.png")));
        Node rightThrust = new ImageView(new Image(getClass().getResourceAsStream("thrust.png")));
        Node explosion = new ImageView(new Image(getClass().getResourceAsStream("explosion.png")));

        downThrust.setTranslateX(-25);
        leftThrust.rotateProperty().set(90);
        leftThrust.setTranslateX(-30);
        leftThrust.setTranslateY(-10);
        rightThrust.rotateProperty().set(-90);
        rightThrust.setTranslateX(-20);
        rightThrust.setTranslateY(-10);

        availableNodes = new HashMap<>();
        availableNodes.put("ship", ship);
        availableNodes.put("pad", pad);
        availableNodes.put("downThrust", downThrust);
        availableNodes.put("leftThrust", leftThrust);
        availableNodes.put("rightThrust", rightThrust);
        availableNodes.put("explosion", explosion);
    }

    private void initializeGameObjects() {
        // pad and ship
        pad = new Sprite("pad", availableNodes.get("pad"), new PVector(520, 360));
        ship = new Sprite("ship", availableNodes.get("ship"), new PVector(360, 0));

        // remove all sprites and add what's needed
        root.getChildren().clear();
        root.getChildren().add(ship);
        root.getChildren().add(pad);
        root.getChildren().add(textSpeed);
        root.getChildren().add(textLose);
        root.getChildren().add(textWin);

        // invisible stuff
        textLose.setVisible(false);
        textWin.setVisible(false);
    }

    private void initializeForces() {
        // set all possible forces
        forces = new HashMap<>(Map.of(
                "gravity", new PVector(0, 0.01),
                "downThrust", new PVector(0, -0.02),
                "leftThrust", new PVector(0.01, 0),
                "rightThrust", new PVector(-0.01, 0)
        ));

        // set active forces when the game begins
        activeForces = new HashMap<>(Map.of(
                "gravity", true
        ));
    }

    private void initializeTimer() {
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
        // apply forces
        for (Map.Entry<String, Boolean> force : activeForces.entrySet()) {
            if (force.getValue()) {
                ship.applyImpulseForce(forces.get(force.getKey()));
            }
        }

        // update ship and pad
        ship.update();
        pad.update();

        // update speed text
        textSpeed.setText(String.format("speed = %.2f", ship.getVelocity().magnitude()));
        textSpeed.setFill(UtilsColor.getColorScale(0.0, 0.4, Color.GREEN.getHue(), Color.RED.getHue(), ship.getVelocity().y)
                .deriveColor(1, 1, 1, 1.0));

        // check explosion
        endGame();
    }


    private void endGame() {
        // left or right
        if (ship.getBoundsInParent().getMinX() < root.getLayoutBounds().getMinX() ||
                ship.getBoundsInParent().getMaxX() > root.getLayoutBounds().getMaxX()) {
            explode();
        }

        // ground outside pad
        if (ship.getBoundsInParent().getMaxY() > pad.getBoundsInParent().getMaxY() &&
                !ship.intersects(pad)) {
            explode();
        }

        // ground inside pad with excess velocity
        if (ship.getBoundsInParent().getMaxY() > pad.getBoundsInParent().getMaxY() &&
                ship.intersects(pad) &&
                ship.getVelocity().magnitude() > SPEED_THRESHOLD) {
            explode();
        }

        // ground inside pad with good velocity
        if (ship.getBoundsInParent().getMaxY() > pad.getBoundsInParent().getMaxY() &&
                ship.intersects(pad) &&
                ship.getVelocity().magnitude() < SPEED_THRESHOLD) {
            win();
        }
    }

    private void explode() {
        ship.getViews().clear();
        ship.addView("explosion", availableNodes.get("explosion"));
        textLose.setVisible(true);
        timer.stop();
    }

    private void win() {
        textWin.setVisible(true);
        timer.stop();
    }

    void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> {
                activeForces.put("leftThrust", true);
                ship.addView("leftThrust", availableNodes.get("leftThrust"));
            }
            case RIGHT -> {
                activeForces.put("rightThrust", true);
                ship.addView("rightThrust", availableNodes.get("rightThrust"));
            }
            case DOWN -> {
                activeForces.put("downThrust", true);
                ship.addView("downThrust", availableNodes.get("downThrust"));
            }
        }
    }

    void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> {
                activeForces.put("leftThrust", false);
                ship.removeView("#leftThrust");
            }
            case RIGHT -> {
                activeForces.put("rightThrust", false);
                ship.removeView("#rightThrust");
            }
            case DOWN -> {
                activeForces.put("downThrust", false);
                ship.removeView("#downThrust");
            }
        }
    }
}
