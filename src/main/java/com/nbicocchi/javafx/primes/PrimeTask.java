package com.nbicocchi.javafx.primes;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class PrimeTask extends Task<List<Integer>> {
    PrimeSearcher engine;
    List<Integer> primes;
    int start;
    int end;
    long startTimestamp;
    long endTimestamp;

    public PrimeTask(PrimeSearcher engine, int start, int end) {
        this.engine = engine;
        this.start = start;
        this.end = end;
    }

    @Override
    protected List<Integer> call() {
        startTimestamp = System.nanoTime();
        primes = new ArrayList<>();
        int step = (end - start) / 100;
        for (int i = start; i < end; i++) {
            if (i % step == 0) {
                updateProgress(i - start, end - start);
            }
            if (isCancelled()) {
                break;
            }
            if (engine.isPrime(i)) {
                primes.add(i);
            }
        }
        endTimestamp = System.nanoTime();
        return primes;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public double getOverallSpeed() {
        double interval = (endTimestamp - startTimestamp) / 1000000000.0;
        return primes.size() / interval;
    }
}