package com.nbicocchi.javafx.games.spaceinvaders;

import com.nbicocchi.javafx.games.common.FixedFpsAnimationTimer;
import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class SpaceInvadersController {
    @FXML AnchorPane root;
    FixedFpsAnimationTimer timer;
    HashMap<String, Image> availableImages;
    SpriteDeadAlive ship;

    @FXML
    public void handlePlay() {
        loadResources();
        initializeGameObjects();
        initializeTimer();
    }

    private void loadResources() {
        availableImages = new HashMap<>();
        availableImages.put("ship_missile", new Image(Objects.requireNonNull(getClass().getResourceAsStream("missile.png")), 20, 30, false, true));
        availableImages.put("ship", new Image(Objects.requireNonNull(getClass().getResourceAsStream("ship.png")), 60, 60, false, true));
        availableImages.put("alien", new Image(Objects.requireNonNull(getClass().getResourceAsStream("alien.png")), 50, 50, false, true));
    }

    private void initializeGameObjects() {
        // remove all sprites
        root.getChildren().clear();
        // ship
        ship = new SpriteDeadAlive(
                "ship",
                new ImageView(availableImages.get("ship")),
                new PVector(400, 520));
        root.getChildren().add(ship);

        // aliens
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 7; column++) {
                SpriteDeadAlive alien = new SpriteDeadAlive(
                        "alien",
                        new ImageView(availableImages.get("alien")),
                        new PVector(80 + 60 * column, 20 + 60 * row),
                        new PVector(0.15, 0.05));
                root.getChildren().add(alien);
            }
        }
    }

    private void initializeTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new FixedFpsAnimationTimer(() -> mainLoop());
        timer.start();
    }

    private void mainLoop() {
        RandomGenerator rnd = RandomGenerator.getDefault();
        List<SpriteDeadAlive> sprites = sprites();
        List<SpriteDeadAlive> aliens = sprites("alien");
        List<SpriteDeadAlive> alienMissiles = sprites("alien_missile");
        List<SpriteDeadAlive> shipMissiles = sprites("ship_missile");
        // one random alien shoots
        if (rnd.nextDouble() < 0.02) {
            shootAlien(aliens.get(rnd.nextInt(aliens.size())));
        }
        // change aliens direction on the x-axis
        if (rnd.nextDouble() < 0.002) {
            aliens.forEach(x -> x.getVelocity().x *= -1);
        }
        // alien missiles kill stuff
        alienMissiles.stream().filter(missile -> missile.intersects(ship)).forEach(missile -> {
            missile.setAlive(false);
            ship.setAlive(false);
        });
        // ship missiles kill stuff
        shipMissiles.forEach(missile -> aliens.stream().filter(a -> a.intersects(missile)).forEach(a -> {
            missile.setAlive(false);
            a.setAlive(false);
        }));
        // updates sprites
        sprites.forEach(Sprite::update);
        // remove all references of out-of-screen nodes and dead entities
        root.getChildren().removeIf(s -> (s.getTranslateX() > root.getHeight()) || (s.getTranslateY() < 0));
        root.getChildren().removeIf(x -> (x instanceof SpriteDeadAlive) && (!((SpriteDeadAlive) x).isAlive()));
        // new game?
        if (!ship.isAlive() || aliens.size() == 0) {
            initializeGameObjects();
        }
    }

    private List<SpriteDeadAlive> sprites() {
        return root.getChildren().stream().filter(x -> x instanceof SpriteDeadAlive).map(x -> (SpriteDeadAlive) x).collect(Collectors.toList());
    }

    private List<SpriteDeadAlive> sprites(String selector) {
        return root.getChildren().stream().filter(x -> x instanceof SpriteDeadAlive).map(x -> (SpriteDeadAlive) x).filter(x -> x.getId().equals(selector)).collect(Collectors.toList());
    }

    private void shootShip() {
        PVector location = new PVector(ship.getTranslateX() + 15, ship.getTranslateY() - 20);
        PVector velocity = new PVector(0, -10);
        root.getChildren().add(new SpriteDeadAlive(
                "ship_missile",
                new ImageView(availableImages.get("ship_missile")),
                location,
                velocity));
    }

    private void shootAlien(SpriteDeadAlive alien) {
        PVector location = new PVector(alien.getTranslateX() + 15, alien.getTranslateY() + 50);
        PVector velocity = new PVector(0, 5);
        root.getChildren().add(new SpriteDeadAlive(
                "alien_missile",
                new Circle(5, Color.WHITE),
                location,
                velocity));
    }

    void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE -> shootShip();
            case LEFT -> ship.getVelocity().x = -5;
            case RIGHT -> ship.getVelocity().x = 5;
            case Q -> Platform.exit();
        }
    }

    void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT, RIGHT -> ship.getVelocity().x = 0;
        }
    }
}
