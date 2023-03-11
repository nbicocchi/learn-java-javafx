package com.nbicocchi.javafx.collections;

public class ExperimentResult {
    final int items;
    final int time;

    public ExperimentResult(int items, int time) {
        this.items = items;
        this.time = time;
    }

    public int getItems() {
        return items;
    }

    public int getTime() {
        return time;
    }
}
