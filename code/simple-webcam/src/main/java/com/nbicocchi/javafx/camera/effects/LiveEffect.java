package com.nbicocchi.javafx.camera.effects;

import com.nbicocchi.javafx.camera.lib.Applicable;
import com.nbicocchi.javafx.camera.lib.Enableable;
import com.nbicocchi.javafx.camera.lib.Resettable;
import javafx.scene.control.MenuItem;

public abstract class LiveEffect extends MenuItem implements Enableable, Applicable, Resettable {
    private boolean enabled;
    private boolean applied;
    String status;

    public LiveEffect() {
        this.enabled = false;
        this.applied = false;
        status = "Apply";
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
        if (isApplied()) {
            status = "Applied";
        } else {
            status = "Apply";
        }
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isDisabled() {
        return !enabled;
    }

    @Override
    public boolean isApplied() {
        return applied;
    }

    @Override
    public void reset(){
        applied = false;
        enabled = true;
    }
}
