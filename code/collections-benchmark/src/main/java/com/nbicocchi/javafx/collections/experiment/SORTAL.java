package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * COLLECTION: ArrayList
 * ACTION: Sort the collection
 */
public class SORTAL extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        long start = System.nanoTime();
        Collections.sort(list);
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
