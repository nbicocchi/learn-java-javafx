package com.nbicocchi.javafx.geometry;

import javafx.scene.canvas.GraphicsContext;

public interface DrawableShape extends Shape {
    void paint(GraphicsContext gc);
}
