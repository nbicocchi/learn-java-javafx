package com.nbicocchi.javafx.collections;

import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public abstract class ExperimentTask extends Task<List<Pair<Integer, Integer>>> {
    public static final int MICROSECONDS_IN_TWO_SECONDS = 2000000;
    public static final int MAX_RUNS = 100;
    public static final int MAX_FILL_ITEMS = 100000;
    public static final int MAX_RETRIEVE_ITEMS = 1024;
    public static final int MAX_TIME_PER_EXPERIMENT = MICROSECONDS_IN_TWO_SECONDS / MAX_RUNS;
    RandomGenerator randomGenerator = RandomGenerator.getDefault();
    int fillItems;

    @Override
    protected List<Pair<Integer, Integer>> call() {
        setup();
        List<Pair<Integer, Integer>> results = new ArrayList<>();
        for (int i = 0; i < MAX_RUNS; i++) {
            results.add(experiment(randomGenerator.nextInt(fillItems)));
            updateProgress(i, MAX_RUNS);
        }
        return results;
    }

    /**
     * Defines automagically fillItems for the experiment
     */
    protected void setup() {
        for (fillItems = 1024; fillItems < MAX_FILL_ITEMS; fillItems *= 2) {
            if (experiment(fillItems).getValue() > MAX_TIME_PER_EXPERIMENT) {
                break;
            }
        }
    }

    /**
     * The experiment
     * @param items the size of the collection to be tested
     * @return experimental results
     */
    protected abstract Pair<Integer, Integer> experiment(int items);
}