package com.nbicocchi.javafx.collections;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class FILLLLH extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        List<Integer> list = new LinkedList<>();
        long start = System.nanoTime();
        for (int i = 0; i < items; i++) {
            list.add(0, randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
