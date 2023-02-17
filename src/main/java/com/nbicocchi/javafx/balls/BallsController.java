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
    List<Sprite> allSprites = new ArrayList<>();
    AnimationTimer timer;
    Line force;
    Text text;
    PVector gravity = BallsSettings.FORCE_GRAVITY;
    PVector wind = BallsSettings.FORCE_WIND;
    double dragCoefficient = BallsSettings.SPRITE_DRAG_COEFFICIENT;
    @FXML
    private Pane root;
    @FXML
    private CheckBox enableDrag;
    @FXML
    private CheckBox enableGravity;
    @FXML
    private CheckBox enableWind;
    @FXML
    private TextField tfDrag;
    @FXML
    private TextField tfGravityY;
    @FXML
    private TextField tfWindX;

    public void initialize() {
        tfDrag.textProperty().addListener((observable, oldValue, newValue) -> dragCoefficient = Double.parseDouble(newValue));
        tfGravityY.textProperty().addListener((observable, oldValue, newValue) -> gravity = new PVector(0, Double.parseDouble(newValue)));
        tfWindX.textProperty().addListener((observable, oldValue, newValue) -> wind = new PVector(Double.parseDouble(newValue), 0));
        onReset();
    }

    @FXML
    void onReset() {
        tfDrag.setText(Double.toString(BallsSettings.SPRITE_DRAG_COEFFICIENT));
        tfGravityY.setText(Double.toString(BallsSettings.FORCE_GRAVITY.y));
        tfWindX.setText(Double.toString(BallsSettings.FORCE_WIND.x));
        enableDrag.selectedProperty().set(false);
        enableWind.selectedProperty().set(false);
        enableGravity.selectedProperty().set(false);
        initializeObjects();
        initializeTimer();
    }

    private void initializeObjects() {
        allSprites.clear();
        root.getChildren().clear();
        for (int i = 0; i < BallsSettings.SPRITE_COUNT; i++) {
            addSprite();
        }
    }

    private void initializeTimer() {
        if (timer != null)
            timer.stop();
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                mainLoop();
            }
        };
        timer.start();
    }

    private void addSprite() {
        RandomGenerator rnd = RandomGenerator.of("Random");
        Rectangle r = new Rectangle(100, 100);
        r.setStroke(Color.ORANGE);
        r.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.3));
        PVector location = new PVector(rnd.nextDouble() * root.getWidth(), rnd.nextDouble() * root.getHeight());
        PVector velocity = new PVector(rnd.nextDouble() * BallsSettings.SPRITE_MAX_SPEED, rnd.nextDouble() * BallsSettings.SPRITE_MAX_SPEED);
        Sprite sprite = new Sprite(r, location, velocity);
        allSprites.add(sprite);
        root.getChildren().add(sprite);
    }

    private void mainLoop() {
        // physics: apply forces
        if (enableWind.selectedProperty().get()) {
            allSprites.forEach(s -> s.applyImpulseForce(wind));
        }
        if (enableGravity.selectedProperty().get()) {
            allSprites.forEach(s -> s.applyImpulseForce(gravity));
        }
        if (enableDrag.selectedProperty().get()) {
            allSprites.forEach(s -> s.applyImpulseForce(s.getVelocity().multiply(dragCoefficient)));
        }
        // update
        allSprites.forEach(Sprite::update);
        // check boundaries (application specific!)
        allSprites.forEach(this::checkBallBounds);
        // update in fx scene
        allSprites.forEach(Sprite::display);
    }

    private void checkBallBounds(Sprite s) {
        double radius = s.getWidth();
        // upper horizontal wall collision
        if (s.getLocation().y < 0) {
            s.getLocation().y = 0;
            s.getVelocity().y *= -1;
        }
        // lower horizontal wall collision
        if (s.getLocation().y + radius > root.getHeight()) {
            s.getLocation().y = root.getHeight() - radius;
            s.getVelocity().y *= -1;
        }
        // left vertical wall collision
        if (s.getLocation().x < 0) {
            s.getLocation().x = 0;
            s.getVelocity().x *= -1;
        }
        // right vertical wall collision
        if (s.getLocation().x + radius > root.getWidth()) {
            s.getLocation().x = root.getWidth() - radius;
            s.getVelocity().x *= -1;
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
        for (Sprite ball : allSprites) {
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
