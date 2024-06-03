package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * COLLECTION: TreeSet
 * ACTION: Insert an integer in the collection
 */
public class FILLTS extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        Set<Integer> set = new TreeSet<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            set.add(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
