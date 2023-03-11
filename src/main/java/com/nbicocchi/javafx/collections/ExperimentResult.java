package com.nbicocchi.javafx.collections;

public class ExperimentResult {
    int items;
    int fillTime;
    int retrieveTime;

    public ExperimentResult(int items, int fillTime) {
        this.items = items;
        this.fillTime = fillTime;
    }

    public ExperimentResult(int items, int fillTime, int retrieveTime) {
        this.items = items;
        this.fillTime = fillTime;
        this.retrieveTime = retrieveTime;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getFillTime() {
        return fillTime;
    }

    public void setFillTime(int fillTime) {
        this.fillTime = fillTime;
    }

    public int getRetrieveTime() {
        return retrieveTime;
    }

    public void setRetrieveTime(int retrieveTime) {
        this.retrieveTime = retrieveTime;
    }
}
