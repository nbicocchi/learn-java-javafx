package com.nbicocchi.javafx.fractals.model;

import javafx.geometry.Point2D;

public class FractalBean {
    double xMin;
    double xMax;
    double yMin;
    double yMax;

    public FractalBean(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public double getWidth() {
        return xMax - xMin;
    }

    public double getHeight() {
        return yMax - yMin;
    }

    public void moveLeft() {
        xMin -= getWidth() / 10;
        xMax -= getWidth() / 10;
    }

    public void moveRight() {
        xMin += getWidth() / 10;
        xMax += getWidth() / 10;
    }

    public void moveUp() {
        yMin -= getHeight() / 10;
        yMax -= getHeight() / 10;
    }

    public void moveDown() {
        yMin += getHeight() / 10;
        yMax += getHeight() / 10;
    }

    public Point2D mapToBean(FractalBean other, double x, double y) {
        double targetX = other.getxMin() + other.getWidth() * (x / (xMax - xMin));
        double targetY = other.getyMin() + other.getHeight() * (y / (yMax - yMin));
        return new Point2D(targetX, targetY);
    }

    public double areaRatio(FractalBean other) {
        double area = getWidth() * getHeight();
        double otherArea = other.getWidth() * other.getHeight();
        return area / otherArea;
    }
}
