package com.nbicocchi.javafx.games.sierpinski;

import com.nbicocchi.javafx.games.common.FixedFpsAnimationTimer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SierpinskiController {
    @FXML Canvas canvas;

    private double width;
    private double height;
    private double rootHeight;
    private List<Triangle> renderList;

    private record Triangle(double topX, double topY, double height) {


    }

    private void mainLoop() {
        calcTriangles();
        clearBackground();
        drawTriangles();
    }

    public void initialize() {
        width = canvas.getWidth();
        height = canvas.getHeight();
        rootHeight = canvas.getHeight();
        renderList = new ArrayList<>();

        FixedFpsAnimationTimer timer = new FixedFpsAnimationTimer(() -> mainLoop());
        timer.start();
    }

    private void calcTriangles() {
        renderList.clear();

        rootHeight += rootHeight * 0.0002;
        if (rootHeight >= 2.0 * height) {
            rootHeight = height;
        }

        shrink(new Triangle(width / 2, 0, rootHeight));
    }

    private void shrink(Triangle triangle) {
        double topX = triangle.topX();
        double topY = triangle.topY();
        double triangleHeight = triangle.height();

        if (topY >= height) {
            return;
        }

        double smallest = 32;
        if (triangleHeight < smallest) {
            renderList.add(triangle);
            return;
        }

        Triangle top = new Triangle(topX, topY, triangleHeight / 2);
        Triangle left = new Triangle(topX - triangleHeight / 4, topY + triangleHeight / 2, triangleHeight / 2);
        Triangle right = new Triangle(topX + triangleHeight / 4, topY + triangleHeight / 2, triangleHeight / 2);
        shrink(top);
        shrink(left);
        shrink(right);
    }

    private void clearBackground() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    private void drawTriangles() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);

        for (Triangle triangle : renderList) {
            if (triangle.topY() < height) {
                drawTriangle(triangle);
            }
        }
    }

    private void drawTriangle(Triangle triangle) {
        double topX = triangle.topX();
        double topY = triangle.topY();
        double h = triangle.height();

        double[] pointsX = new double[3];
        double[] pointsY = new double[3];

        pointsX[0] = topX;
        pointsY[0] = topY;

        pointsX[1] = topX + h / 2;
        pointsY[1] = topY + h;

        pointsX[2] = topX - h / 2;
        pointsY[2] = topY + h;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillPolygon(pointsX, pointsY, 3);
    }

}