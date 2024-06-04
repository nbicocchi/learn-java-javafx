package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * COLLECTION: HashSet
 * ACTION: Check if the collection contains a certain integer
 */
public class RETRHS extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        Set<Integer> set = new HashSet<>();
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
