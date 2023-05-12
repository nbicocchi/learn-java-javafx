package com.nbicocchi.javafx.games.balls;

import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import com.nbicocchi.javafx.games.common.UtilsColor;
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
import java.util.Optional;
import java.util.random.RandomGenerator;

public class BallsController {
    public static int SPRITE_COUNT = 5;
    public static double SPRITE_DRAG_COEFFICIENT = -0.02;
    public static double SPRITE_MAX_SPEED = 20;
    public static double WIND_X = 1.0;
    public static double WIND_Y = 0.0;
    public static double GRAVITY_X = 0.0;
    public static double GRAVITY_Y = 2.0;

    @FXML private Pane root;
    @FXML private CheckBox enableDrag;
    @FXML private CheckBox enableGravity;
    @FXML private CheckBox enableWind;
    @FXML private TextField tfDrag;
    @FXML private TextField tfGravityX;
    @FXML private TextField tfGravityY;
    @FXML private TextField tfWindX;
    @FXML private TextField tfWindY;

    List<Sprite> bouncingSprites = new ArrayList<>();
    AnimationTimer timer;
    Line force;
    Text text;

    public void initialize() {
        onReset();
    }

    @FXML
    void onReset() {
        initializeGUI();
        initializeObjects();
        initializeTimer();
    }

    private void initializeGUI() {
        enableDrag.selectedProperty().set(false);
        enableWind.selectedProperty().set(false);
        enableGravity.selectedProperty().set(false);
        tfDrag.setText(Double.toString(SPRITE_DRAG_COEFFICIENT));
        tfGravityX.setText(Double.toString(GRAVITY_X));
        tfGravityY.setText(Double.toString(GRAVITY_Y));
        tfWindX.setText(Double.toString(WIND_X));
        tfWindY.setText(Double.toString(WIND_Y));
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
        // physics: apply forces
        if (enableWind.selectedProperty().get()) {
            PVector wind = new PVector(
                    Double.parseDouble(tfWindX.getText()),
                    Double.parseDouble(tfWindY.getText()));
            bouncingSprites.forEach(s -> s.applyImpulseForce(wind));
        }

        if (enableGravity.selectedProperty().get()) {
            PVector gravity = new PVector(
                    Double.parseDouble(tfGravityX.getText()),
                    Double.parseDouble(tfGravityY.getText()));
            bouncingSprites.forEach(s -> s.applyImpulseForce(gravity));
        }

        if (enableDrag.selectedProperty().get()) {
            double drag = Double.parseDouble(tfDrag.getText());
            bouncingSprites.forEach(s -> s.applyImpulseForce(s.getVelocity().multiply(drag)));
        }

        // check boundaries (application specific!)
        bouncingSprites.forEach(this::checkBallBounds);
        // update sprite model
        bouncingSprites.forEach(Sprite::update);
        // update sprite view
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
        force.setEndX(event.getX());
        force.setEndY(event.getY());
        double magnitude = Math.hypot(force.getStartX() - force.getEndX(), force.getStartY() - force.getEndY());
        force.setStroke(UtilsColor.getColorScale(0, 300, Color.GREEN.getHue(), Color.RED.getHue(), magnitude).deriveColor(1, 1, 1, 0.3));
        text.setText(String.format("%.1f", magnitude));
    }

    @FXML
    void onMousePressed(MouseEvent event) {
        force = new Line(event.getX(), event.getY(), event.getX(), event.getY());
        force.setStrokeWidth(5);
        text = new Text("");
        text.setTranslateX(event.getX());
        text.setTranslateY(event.getY());
        text.setFont(new Font(16));
        text.setFill(Color.BLACK);
        root.getChildren().add(force);
        root.getChildren().add(text);
    }

    @FXML
    void onMouseReleased(MouseEvent event) {
        Optional<Sprite> sprite = bouncingSprites.stream()
                .filter(bs -> bs.contains(new Point2D(force.getStartX(), force.getStartY())))
                .findFirst();
        if (sprite.isPresent()) {
            PVector impulse = new PVector(
                    force.getStartX() - force.getEndX(),
                    force.getStartY() - force.getEndY());
            sprite.get().applyImpulseForce(impulse.multiply(0.2));
        }
        root.getChildren().removeAll(force, text);
    }
}
