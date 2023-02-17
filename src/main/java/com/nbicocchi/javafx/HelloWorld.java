package com.nbicocchi.javafx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloWorld extends Application {
    ImageView imageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Text text = new Text();
        text.setText("Hello World!!");
        text.setX(50);
        text.setY(50);
        text.setFont(Font.font("Verdana", 50));
        text.setFill(Color.LIMEGREEN);
        Line line = new Line();
        line.setStartX(200);
        line.setStartY(200);
        line.setEndX(500);
        line.setEndY(200);
        line.setStrokeWidth(5);
        line.setStroke(Color.RED);
        line.setOpacity(0.5);
        line.setRotate(45);
        Rectangle rectangle = new Rectangle();
        rectangle.setX(100);
        rectangle.setY(100);
        rectangle.setWidth(100);
        rectangle.setHeight(100);
        rectangle.setFill(Color.BLUE);
        rectangle.setStrokeWidth(5);
        rectangle.setStroke(Color.BLACK);
        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(150.0, 250.0, 300.0, 300.0, 100.0, 400.0);
        triangle.setFill(Color.YELLOW);
        Circle circle = new Circle();
        circle.setCenterX(350);
        circle.setCenterY(350);
        circle.setRadius(50);
        circle.setFill(Color.ORANGE);
        TextField tf = new TextField();
        tf.setFocusTraversable(false);
        tf.setText("Hello!");
        tf.setLayoutX(200);
        tf.setLayoutY(500);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("alien.png")));
        imageView = new ImageView(image);
        imageView.setX(400);
        imageView.setY(400);
        Group root = new Group();
        root.getChildren().addAll(text, line, rectangle, triangle, circle, imageView, tf);
        Scene scene = new Scene(root, 600, 600, Color.LIGHTSKYBLUE);
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnMouseDragged(this::mouseDragged);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            imageView.setX(imageView.getX() - 10);
        } else if (event.getCode() == KeyCode.RIGHT) {
            imageView.setX(imageView.getX() + 10);
        } else if (event.getCode() == KeyCode.UP) {
            imageView.setY(imageView.getY() - 10);
        } else if (event.getCode() == KeyCode.DOWN) {
            imageView.setY(imageView.getY() + 10);
        }
    }

    public void mouseDragged(MouseEvent event) {
        imageView.setX(event.getX());
        imageView.setY(event.getY());
    }
}