package com.nbicocchi.javafx.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends AbstractShape implements Computable {
    public Circle(Rectangle2D bounds, Color color) {
        super(bounds, color);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public double getArea() {
        double r = bounds.getWidth() / 2.0;
        return r * r * Math.PI;
    }

    @Override
    public double getPerimeter() {
        double d = bounds.getWidth();
        return d * Math.PI;
    }
}
