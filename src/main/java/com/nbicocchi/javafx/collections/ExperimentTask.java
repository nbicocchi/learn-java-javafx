package com.nbicocchi.javafx.collections;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.random.RandomGenerator;

public abstract class ExperimentTask extends Task<ExperimentResult> {
    public static final int MICROSECONDS_IN_ONE_SECOND = 1000000;
    public static final int MAX_RUNS = 100;
    public static final int MAX_FILL_ITEMS = 200000;
    public static final int MAX_RETRIEVE_ITEMS = 512;
    public static final int MAX_TIME_PER_EXPERIMENT =  MICROSECONDS_IN_ONE_SECOND / MAX_RUNS;
    RandomGenerator randomGenerator = RandomGenerator.getDefault();
    int fillItems;
    int timeThreshold;

    @Override
    protected ExperimentResult call() {
        setup();
        for (int i = 0; i < MAX_RUNS; i++) {
            ExperimentResult result = experiment(randomGenerator.nextInt(fillItems));
            if (result.getTime() < timeThreshold) {
                updateValue(result);
            }
        }
        return new ExperimentResult(0,0);
    }

    /**
     * Defines automagically fillItems and timeThreshold for the experiment
     */
    protected void setup() {
        for (fillItems = 1024; timeThreshold < MAX_TIME_PER_EXPERIMENT && fillItems < MAX_FILL_ITEMS; fillItems *= 2) {
            timeThreshold = medianRun(fillItems, 3).getTime();
        }
    }

    /**
     * Returns the median result of a set of experiments
     * @param items the size of the collection to be tested
     * @param runs the number of runs
     * @return the median run
     */
    protected ExperimentResult medianRun(int items, int runs) {
        List<ExperimentResult> results = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            results.add(experiment(items));
        }
        results.sort(Comparator.comparingInt(ExperimentResult::getTime));
        return results.get(results.size() / 2);
    }

    /**
     * The experiment
     * @param items the size of the collection to be tested
     * @return experimental results
     */
    protected abstract ExperimentResult experiment(int items);
}