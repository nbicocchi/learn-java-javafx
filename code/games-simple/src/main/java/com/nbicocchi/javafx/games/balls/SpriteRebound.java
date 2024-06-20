package com.nbicocchi.javafx.games.balls;

import com.nbicocchi.javafx.games.common.PVector;
import com.nbicocchi.javafx.games.common.Sprite;
import javafx.scene.Node;

public class SpriteRebound extends Sprite {
    public SpriteRebound(String name, Node view) {
        super(name, view);
    }

    public SpriteRebound(String name, Node view, PVector location) {
        super(name, view, location);
    }

    public SpriteRebound(String name, Node view, PVector location, PVector velocity) {
        super(name, view, location, velocity);
    }
    public SpriteRebound(String name, Node view, PVector location, PVector velocity, PVector acceleration) {
        super(name, view, location, velocity, acceleration);
    }

    public SpriteRebound(String name, Node view, PVector location, PVector velocity, PVector acceleration, double mass) {
        super(name, view, location, velocity, acceleration, mass);
    }

    public void update() {
        super.update();
        reboundWalls();
    }

    public void reboundWalls() {
        double offset;

        // right wall
        offset = getBoundsInParent().getMaxX() - getParent().getLayoutBounds().getMaxX();
        if (offset > 0) {
            getLocation().x -= offset;
            getVelocity().x *= -1;
        }

        // left wall
        offset = getBoundsInParent().getMinX() - getParent().getLayoutBounds().getMinX();
        if (offset < 0) {
            getLocation().x -= offset;
            getVelocity().x *= -1;
        }

        // lower wall
        offset = getBoundsInParent().getMaxY() - getParent().getLayoutBounds().getMaxY();
        if (offset > 0) {
            getLocation().y -= offset;
            getVelocity().y *= -1;
        }

        // upper wall
        offset = getBoundsInParent().getMinY() - getParent().getLayoutBounds().getMinY();
        if (offset < 0) {
            getLocation().y -= offset;
            getVelocity().y *= -1;
        }
    }
}
