package com.nbicocchi.javafx.collections;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.random.RandomGenerator;

public abstract class ExperimentTask extends Task<ExperimentResult> {
    RandomGenerator randomGenerator = RandomGenerator.getDefault();
    int runs = 100;
    int maxItems = 1024;
    int timeThreshold = 0;

    @Override
    protected ExperimentResult call() {
        setup();
        for (int i = 0; i < runs; i++) {
            ExperimentResult result = experiment(randomGenerator.nextInt(maxItems));
            if (result.getFillTime() < timeThreshold) {
                updateValue(result);
            }
        }
        return new ExperimentResult(0,0);
    }

    protected void setup() {
        for (maxItems = 512; timeThreshold < 10000; maxItems *= 1.5) {
            ExperimentResult result = medianRun(maxItems, 5);
            timeThreshold = result.getFillTime() + result.getRetrieveTime();
        }
    }

    protected ExperimentResult medianRun(int items, int runs) {
        List<ExperimentResult> results = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            results.add(experiment(items));
        }
        results.sort(Comparator.comparingInt(ExperimentResult::getFillTime));
        return results.get(results.size() / 2);
    }

    protected abstract ExperimentResult experiment(int items);
}