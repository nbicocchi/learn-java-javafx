package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.Set;
import java.util.TreeSet;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class RETRTS extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < items; i++) {
            set.add(randomGenerator.nextInt(items));
        }
        long start = System.nanoTime();
        for (int i = 0; i < MAX_RETRIEVE_ITEMS; i++) {
            set.contains(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
