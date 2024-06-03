package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
/**
 * COLLECTION: LinkedList
 * ACTION: Insert an integer as last element
 */
public class FILLLLT extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        List<Integer> list = new LinkedList<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
