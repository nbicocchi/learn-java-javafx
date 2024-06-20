package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * COLLECTION: ArrayList
 * ACTION: Check if the collection contains a certain integer
 * METHOD: Collections.sort -> binarySearch
 */
public class RETRALSORTED extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        Collections.sort(list);
        long start = System.nanoTime();
        for (int i = 0; i < MAX_RETRIEVE_ITEMS; i++) {
            Collections.binarySearch(list, randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
