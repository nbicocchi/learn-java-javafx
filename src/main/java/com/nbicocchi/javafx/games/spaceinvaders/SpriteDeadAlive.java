package com.nbicocchi.javafx.games.spaceinvaders;

import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import javafx.scene.Node;

public class SpriteDeadAlive extends Sprite {
    boolean isAlive = true;

    public SpriteDeadAlive(String name, Node view) {
        super(name, view);
    }

    public SpriteDeadAlive(String name, Node view, PVector location) {
        super(name, view, location);
    }

    public SpriteDeadAlive(String name, Node view, PVector location, PVector velocity) {
        super(name, view, location, velocity);
    }

    public SpriteDeadAlive(String name, Node view, PVector location, PVector velocity, PVector acceleration) {
        super(name, view, location, velocity, acceleration);
    }

    public SpriteDeadAlive(String name, Node view, PVector location, PVector velocity, PVector acceleration, double mass) {
        super(name, view, location, velocity, acceleration, mass);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
