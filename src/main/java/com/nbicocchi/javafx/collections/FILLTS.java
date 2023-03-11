package com.nbicocchi.javafx.collections;

import java.util.Set;
import java.util.TreeSet;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FILLTS extends ExperimentTask {

    @Override
    protected ExperimentResult experiment(int items) {
        Set<Integer> set = new TreeSet<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            set.add(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new ExperimentResult(items, (int)NANOSECONDS.toMicros(end - start));
    }
}
