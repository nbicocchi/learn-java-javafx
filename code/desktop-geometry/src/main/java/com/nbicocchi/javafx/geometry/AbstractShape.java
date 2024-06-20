package com.nbicocchi.javafx.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class AbstractShape implements DrawableShape {
    Rectangle2D bounds;
    Color color;

    public AbstractShape(Rectangle2D bounds, Color color) {
        this.bounds = bounds;
        this.color = color;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle2D bounds) {
        this.bounds = bounds;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void paint(GraphicsContext gc);
}
