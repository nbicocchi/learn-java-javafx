package com.nbicocchi.javafx.collections.experiment;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FILLALT extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        List<Integer> list = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
