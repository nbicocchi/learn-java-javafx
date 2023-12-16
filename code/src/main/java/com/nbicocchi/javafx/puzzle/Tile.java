package com.nbicocchi.javafx.puzzle;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Tile extends Button {
    Point2D correct;
    Point2D current;

    public Tile(Image image, Point2D position) {
        correct = new Point2D(position.getX(), position.getY());
        current = new Point2D(position.getX(), position.getY());
        setPrefSize(image.getWidth(), image.getHeight());
        setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        setOnMouseEntered(event -> {
            setBorder(new Border(new BorderStroke(Color.MEDIUMVIOLETRED, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
        });
        setOnMouseExited(event -> {
            setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        });
    }

    public void setEmpty() {
        setBackground(null);
    }

    public boolean isEmpty() {
        return getBackground() == null;
    }

    public Point2D getCorrect() {
        return correct;
    }

    public void setCorrect(Point2D correct) {
        this.correct = correct;
    }

    public Point2D getCurrent() {
        return current;
    }

    public void setCurrent(Point2D current) {
        this.current = current;
    }

    public boolean isOK() {
        return current.equals(correct);
    }
}