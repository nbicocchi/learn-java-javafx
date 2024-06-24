package com.twentyone;

/**
 * rapresent a point or a vector
 */
public class Coord {
    
    public double x;
    public double y;

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coord() {
        this.x = 0;
        this.y = 0;
    }

    public Coord sum(Coord c) {
        return new Coord(x + c.x, y + c.y);
    }

    public Coord multiply(double value) {
        return new Coord(x * value, y * value);
    }

    public Coord multiply(Coord c) {
        return new Coord(x * c.x, y * c.y);
    }

    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Coord getOpposite() {
        return new Coord(-x, -y);
    }

    public void invertX() {
        this.x = -x;
    }

    public void invertY() {
        this.y = -y;
    }
    
}
