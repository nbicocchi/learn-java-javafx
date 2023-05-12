package com.nbicocchi.javafx.games.spacelander;

import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpaceLanderController {
    @FXML AnchorPane root;
    @FXML Text textLose;
    @FXML Text textWin;
    @FXML Text textSpeed;
    AnimationTimer timer;
    Sprite ship, pad;
    HashMap<String, Image> resources;
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

        forces = new HashMap<>(Map.of(
                "gravity", new PVector(0, 0.01),
                "down", new PVector(0, -0.02),
                "left", new PVector(0.01, 0),
                "right", new PVector(-0.01, 0)
        ));
    }

    @FXML
    public void handlePlay() {
        loadResources();
        initializeGameObjects();
        initializeTimer();
    }

    private void loadResources() {
        resources = new HashMap<>();
        resources.put("explosion", new Image(Objects.requireNonNull(getClass().getResourceAsStream("explosion.png"))));
        resources.put("ship", new Image(Objects.requireNonNull(getClass().getResourceAsStream("ship.png"))));
        resources.put("thrust", new Image(Objects.requireNonNull(getClass().getResourceAsStream("thrust.png"))));
        resources.put("pad", new Image(Objects.requireNonNull(getClass().getResourceAsStream("pad.png"))));
    }

    private void initializeGameObjects() {
        // ship and pad
        ship = new Sprite(new ImageView(resources.get("ship")), new PVector(360, 0));
        pad = new Sprite(new ImageView(resources.get("pad")), new PVector(520, 360));

        // remove all sprites and add the ship
        root.getChildren().clear();
        root.getChildren().add(ship);
        root.getChildren().add(pad);
        root.getChildren().add(textSpeed);

        // the only force is gravity
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

        // update sprites
        ship.update();
        ship.display();
        pad.update();
        pad.display();

        // update speed text
        textSpeed.setText(String.format("speed = %.2f", ship.getVelocity().y));

        // check explosion
        checkExplosion();
    }


    private void checkExplosion() {
        // ground
        if (ship.getLocation().y > 330) {
            explode();
        }
        // left or right
        if (ship.getLocation().x < 0 || ship.getLocation().x > 800) {
            explode();
        }
        // pad
        if (Math.abs(ship.getLocation().x - 560) < 30 &&
                Math.abs(ship.getLocation().y - 315) < 10) {
            if (ship.getVelocity().y < 0.30) {
                win();
            } else {
                explode();
            }
        }
    }

    private void explode() {
        ship.setView(new ImageView(resources.get("explosion")));
        root.getChildren().add(textLose);
        textLose.setVisible(true);
        timer.stop();
    }

    private void win() {
        root.getChildren().add(textWin);
        textWin.setVisible(true);
        timer.stop();
    }

    void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> activeForces.put("left", true);
            case RIGHT -> activeForces.put("right", true);
            case DOWN -> activeForces.put("down", true);
        }
    }

    void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> activeForces.put("left", false);
            case RIGHT -> activeForces.put("right", false);
            case DOWN -> activeForces.put("down", false);
        }
    }
}
