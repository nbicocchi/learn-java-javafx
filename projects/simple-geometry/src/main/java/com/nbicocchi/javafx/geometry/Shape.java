package com.nbicocchi.javafx.geometry;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public interface Shape {
    Rectangle2D getBounds();
    void setBounds(Rectangle2D bounds);
    Color getColor();
    void setColor(Color color);
}
