package com.nbicocchi.javafx.games.canvasgame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    double x;
    double y;
    double vx;
    double vy;
    double r;
    Color c;

    public Ball(double x, double y, double vx, double vy, double r, Color c) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.c = c;
    }

    public void update() {
        x += vx;
        y += vy;
    }

    public void paint(GraphicsContext gc) {
        gc.setFill(c);
        gc.fillOval(x, y,2 * r, 2 * r);
    }

    @Override
    public String toString() {
        return "Ball{" + "x=" + x + ", y=" + y + ", vx=" + vx + ", vy=" + vy + ", r=" + r + '}';
    }
}
