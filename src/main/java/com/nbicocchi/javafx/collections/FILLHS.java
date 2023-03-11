package com.nbicocchi.javafx.collections;

import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FILLHS extends ExperimentTask {

    @Override
    protected ExperimentResult experiment(int items) {
        Set<Integer> set = new HashSet<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            set.add(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new ExperimentResult(items, (int)NANOSECONDS.toMicros(end - start));
    }
}
