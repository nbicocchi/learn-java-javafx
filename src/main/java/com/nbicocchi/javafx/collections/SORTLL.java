package com.nbicocchi.javafx.collections;

import javafx.util.Pair;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class SORTLL extends ExperimentTask {

    @Override
    protected Pair<Integer, Integer> experiment(int items) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        long start = System.nanoTime();
        Collections.sort(list);
        long end = System.nanoTime();
        return new Pair<>(items, (int) NANOSECONDS.toMicros(end - start));
    }
}
