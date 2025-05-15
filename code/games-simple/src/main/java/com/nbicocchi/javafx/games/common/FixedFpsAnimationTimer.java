package com.nbicocchi.javafx.games.common;

import javafx.animation.AnimationTimer;

/**
 * A animation timer firing at most TARGET_FPS times per second.
 */
public class FixedFpsAnimationTimer extends AnimationTimer {
    private static final long TARGET_FPS = 120;
    private static final long INTERVAL_NS = 1_000_000_000 / TARGET_FPS;
    private long lastUpdate = 0;
    private Runnable runnable;

    public FixedFpsAnimationTimer(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void handle(long now) {
        if (now - lastUpdate >= INTERVAL_NS) {
            lastUpdate = now;
            runnable.run();
        }
    }
}
