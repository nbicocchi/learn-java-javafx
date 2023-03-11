package com.nbicocchi.javafx.collections;

import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class LLFT extends ExperimentTask {

    @Override
    protected ExperimentResult experiment(int items) {
        List<Integer> list = new LinkedList<>();
        long startFill = System.nanoTime();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        long endFill = System.nanoTime();

        long startRetrieve = System.nanoTime();
        for (int i = 0; i < items; i++) {
            list.contains(randomGenerator.nextInt(items));
        }
        long endRetrieve = System.nanoTime();
        return new ExperimentResult(items, (int)NANOSECONDS.toMicros(endFill - startFill),
                (int)NANOSECONDS.toMicros(endRetrieve - startRetrieve));
    }
}
