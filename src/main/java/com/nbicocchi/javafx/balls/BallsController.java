package com.nbicocchi.javafx.balls;

import com.nbicocchi.javafx.common.PVector;
import com.nbicocchi.javafx.common.Sprite;
import com.nbicocchi.javafx.common.UtilsColor;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class BallsController {
    public static int SPRITE_COUNT = 5;
    public static double SPRITE_DRAG_COEFFICIENT = -0.05;
    public static double SPRITE_MAX_SPEED = 20;
    public static double WIND_X = 0.0;
    public static double WIND_Y = 0.0;
    public static double GRAVITY_X = 0.0;
    public static double GRAVITY_Y = 5.0;

    @FXML private Pane root;
    @FXML private CheckBox enableDrag;
    @FXML private CheckBox enableGravity;
    @FXML private CheckBox enableWind;
    @FXML private TextField tfDrag;
    @FXML private TextField tfGravityY;
    @FXML private TextField tfWindX;

    List<Sprite> bouncingSprites = new ArrayList<>();
    AnimationTimer timer;
    Line force;
    Text text;

    PVector gravity = new PVector(GRAVITY_X, GRAVITY_Y);
    PVector wind = new PVector(WIND_X, WIND_Y);
    double dragCoefficient = SPRITE_DRAG_COEFFICIENT;

    public void initialize() {
        tfDrag.textProperty().addListener((observable, oldValue, newValue) -> dragCoefficient = Double.parseDouble(newValue));
        tfGravityY.textProperty().addListener((observable, oldValue, newValue) -> gravity = new PVector(0, Double.parseDouble(newValue)));
        tfWindX.textProperty().addListener((observable, oldValue, newValue) -> wind = new PVector(Double.parseDouble(newValue), 0));
        onReset();
    }

    @FXML
    void onReset() {
        tfDrag.setText(Double.toString(SPRITE_DRAG_COEFFICIENT));
        tfGravityY.setText(Double.toString(GRAVITY_Y));
        tfWindX.setText(Double.toString(WIND_X));
        enableDrag.selectedProperty().set(false);
        enableWind.selectedProperty().set(false);
        enableGravity.selectedProperty().set(false);
        initializeObjects();
        initializeTimer();
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

    private void initializeObjects() {
        bouncingSprites.clear();
        for (int i = 0; i < SPRITE_COUNT; i++) {
            bouncingSprites.add(generateBoncingSprite());
        }
        root.getChildren().clear();
        root.getChildren().addAll(bouncingSprites);
    }

    private Sprite generateBoncingSprite() {
        Rectangle view = new Rectangle(100, 100);
        view.setStroke(Color.ORANGE);
        view.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.3));

        RandomGenerator rnd = RandomGenerator.getDefault();
        PVector location = new PVector(rnd.nextDouble() * root.getWidth(), rnd.nextDouble() * root.getHeight());
        PVector velocity = new PVector(rnd.nextDouble() * SPRITE_MAX_SPEED, rnd.nextDouble() * SPRITE_MAX_SPEED);
        return new Sprite(view, location, velocity);
    }


    private void mainLoop() {
        // physics: apply forces
        if (enableWind.selectedProperty().get()) {
            bouncingSprites.forEach(s -> s.applyImpulseForce(wind));
        }
        if (enableGravity.selectedProperty().get()) {
            bouncingSprites.forEach(s -> s.applyImpulseForce(gravity));
        }
        if (enableDrag.selectedProperty().get()) {
            bouncingSprites.forEach(s -> s.applyImpulseForce(s.getVelocity().multiply(dragCoefficient)));
        }
        // update
        bouncingSprites.forEach(Sprite::update);
        // check boundaries (application specific!)
        bouncingSprites.forEach(this::checkBallBounds);
        // update in fx scene
        bouncingSprites.forEach(Sprite::display);
    }

    private void checkBallBounds(Sprite sprite) {
        double radius = sprite.getWidth();
        // upper horizontal wall collision
        if (sprite.getLocation().y < 0) {
            sprite.getLocation().y = 0;
            sprite.getVelocity().y *= -1;
        }
        // lower horizontal wall collision
        if (sprite.getLocation().y + radius > root.getHeight()) {
            sprite.getLocation().y = root.getHeight() - radius;
            sprite.getVelocity().y *= -1;
        }
        // left vertical wall collision
        if (sprite.getLocation().x < 0) {
            sprite.getLocation().x = 0;
            sprite.getVelocity().x *= -1;
        }
        // right vertical wall collision
        if (sprite.getLocation().x + radius > root.getWidth()) {
            sprite.getLocation().x = root.getWidth() - radius;
            sprite.getVelocity().x *= -1;
        }
    }

    @FXML
    void onMouseDragged(MouseEvent event) {
        if (force != null) {
            force.setEndX(event.getX());
            force.setEndY(event.getY());
            double magnitude = Math.hypot(force.getStartX() - force.getEndX(), force.getStartY() - force.getEndY());
            force.setStroke(UtilsColor.getColorScale(0, 500, Color.GREEN.getHue(), Color.RED.getHue(), magnitude).deriveColor(1, 1, 1, 0.3));
            text.setText(String.format("%.1f", magnitude));
        }
    }

    @FXML
    void onMousePressed(MouseEvent event) {
        force = new Line(event.getX(), event.getY(), event.getX(), event.getY());
        force.setStrokeWidth(5);
        text = new Text("");
        text.setTranslateX(event.getX());
        text.setTranslateY(event.getY());
        text.setFont(new Font(14));
        text.setFill(Color.BLACK);
        root.getChildren().add(force);
        root.getChildren().add(text);
    }

    @FXML
    void onMouseReleased(MouseEvent event) {
        for (Sprite ball : bouncingSprites) {
            if (ball.contains(new Point2D(force.getStartX(), force.getStartY()))) {
                PVector f = new PVector(force.getStartX() - force.getEndX(), force.getStartY() - force.getEndY());
                ball.applyImpulseForce(f);
            }
        }
        root.getChildren().removeAll(force, text);
        force = null;
        text = null;
    }
}
