package com.nbicocchi.javafx.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends AbstractShape implements Computable {
    public Rectangle(Rectangle2D bounds, Color color) {
        super(bounds, color);
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public double getArea() {
        return bounds.getWidth() * bounds.getHeight();
    }

    @Override
    public double getPerimeter() {
        return (bounds.getWidth() + bounds.getHeight()) * 2.0;
    }
}
