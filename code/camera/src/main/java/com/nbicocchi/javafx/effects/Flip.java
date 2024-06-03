package com.nbicocchi.javafx.effects;

import java.util.Objects;

import javafx.geometry.Point3D;
import javafx.scene.image.ImageView;

public class Flip extends LiveEffect {
    private static double rotationValue;

    public Flip() {
        setApplied(true);
        rotationValue = 0.0;
    }

    @Override
    public void toggle(ImageView imageAffected) {
        Objects.requireNonNull(imageAffected);
        setApplied(!isApplied());
        flip(imageAffected);
    }

    public void flip(ImageView picture) {
        Objects.requireNonNull(picture);
        if (rotationValue == 180) {
            rotationValue = 0;
        } else {
            rotationValue = 180;
        }
        viewportFlipper(picture);
        System.out.println("flip: " + this.isApplied());
    }

    public static void viewportFlipper(ImageView picture) {
        Objects.requireNonNull(picture);
        picture.setRotationAxis(new Point3D(0, 1, 0));
        picture.setRotate(rotationValue);
    }
}
