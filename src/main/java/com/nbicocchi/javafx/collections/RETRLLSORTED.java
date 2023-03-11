package com.nbicocchi.javafx.collections;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class RETRLLSORTED extends  ExperimentTask {

    @Override
    protected ExperimentResult experiment(int items) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        Collections.sort(list);
        long start = System.nanoTime();
        for (int i = 0; i < MAX_RETRIEVE_ITEMS; i++) {
            Collections.binarySearch(list, randomGenerator.nextInt(items));
        }
        long end = System.nanoTime();
        return new ExperimentResult(items, (int)NANOSECONDS.toMicros(end - start));
    }
}
