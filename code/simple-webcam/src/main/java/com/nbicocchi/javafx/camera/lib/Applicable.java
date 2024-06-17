package com.nbicocchi.javafx.camera.lib;

import javafx.scene.image.ImageView;

public interface Applicable {
    void apply(ImageView imageAffected);
    boolean isApplied();
}
