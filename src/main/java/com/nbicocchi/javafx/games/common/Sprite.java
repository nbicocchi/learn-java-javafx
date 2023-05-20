package com.nbicocchi.javafx.games.common;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Sprite extends Region {
    public static boolean DEBUG_ENABLED = false;
    PVector location;
    PVector velocity;
    PVector acceleration;
    double mass = 1.0;

    public Sprite(String name, Node view) {
        this.location = new PVector(0, 0);
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        setId(name);
        getChildren().add(view);
    }

    public Sprite(String name, Node view, PVector location) {
        this.location = location;
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        setId(name);
        getChildren().add(view);
    }

    public Sprite(String name, Node view, PVector location, PVector velocity) {
        this.location = location;
        this.velocity = velocity;
        this.acceleration = new PVector(0, 0);
        setId(name);
        getChildren().add(view);
    }

    public Sprite(String name, Node view, PVector location, PVector velocity, PVector acceleration) {
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        setId(name);
        getChildren().add(view);
    }

    public Sprite(String name, Node view, PVector location, PVector velocity, PVector acceleration, double mass) {
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
        setId(name);
        getChildren().add(view);
    }

    public PVector getLocation() {
        return location;
    }

    public void setLocation(PVector location) {
        this.location = location;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public PVector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public ObservableList<Node> getViews() {
        return getChildren();
    }

    public void addView(Node view) {
        getChildren().add(view);
    }

    public void addView(String selector, Node view) {
        view.setId(selector);
        getChildren().add(view);
    }

    public void removeView(String selector) {
        getChildren().remove(lookup(selector));
    }

    public Node getView(String selector) {
        return lookup(selector);
    }

    public void applyImpulseForce(PVector force) {
        velocity = velocity.add(force.multiply(1 / mass));
    }

    public void update() {
        // update velocity and location
        velocity = velocity.add(acceleration);
        location = location.add(velocity);
        // update position on parent component
        setTranslateX(location.x );
        setTranslateY(location.y);
        // eventually draw borders
        if (DEBUG_ENABLED) {
            setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    public boolean intersects(Sprite other) {
        return getBoundsInParent().intersects(other.getBoundsInParent());
    }

    public boolean contains(double x, double y) {
        return getBoundsInParent().contains(x, y);
    }
}
