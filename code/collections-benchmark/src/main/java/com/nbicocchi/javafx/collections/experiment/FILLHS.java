package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * COLLECTION: HashSet
 * ACTION: Insert an integer in the collection
 */
public class FILLHS extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        Set<Integer> set = new HashSet<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            set.add(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
