package com.nbicocchi.javafx.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class SORTAL extends ExperimentTask {

    @Override
    protected ExperimentResult experiment(int items) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < items; i++) {
            list.add(randomGenerator.nextInt(items));
        }
        long start = System.nanoTime();
        Collections.sort(list);
        long end = System.nanoTime();
        return new ExperimentResult(items, (int)NANOSECONDS.toMicros(end - start));
    }
}
