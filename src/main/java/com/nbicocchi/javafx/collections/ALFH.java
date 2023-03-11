package com.nbicocchi.javafx.collections;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class ALFH extends  ExperimentTask {

    @Override
    protected ExperimentResult experiment(int items) {
        List<Integer> list = new ArrayList<>();
        long startFill = System.nanoTime();
        for (int i = 0; i < items; i++) {
            list.add(0, randomGenerator.nextInt(items));
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
