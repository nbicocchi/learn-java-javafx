package com.nbicocchi.javafx.camera.effects;

import javafx.scene.image.ImageView;

public interface LiveEffectsInterface {
    void enable();
    void disable();
    boolean isDisabled();
    boolean isEnabled();
    void toggle(ImageView imageAffected);
    boolean isApplied();
    void resetStatus();
}
