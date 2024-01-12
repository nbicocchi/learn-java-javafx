package com.nbicocchi.javafx.managerworkers;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrimeTask extends Task<List<Integer>> {
    final PrimeSearcher engine;
    final int blockID;
    final int start;
    final int end;
    List<Integer> primes;
    long startTimestamp;
    long endTimestamp;

    public PrimeTask(PrimeSearcher engine, int blockID, int blockSize) {
        this.engine = engine;
        this.blockID = blockID;
        this.start = blockID * blockSize;
        this.end = (blockID + 1) * blockSize - 1;
        this.primes = new ArrayList<>();
    }

    @Override
    protected List<Integer> call() {
        startTimestamp = System.nanoTime();
        for (int i = start; i < end; i++) {
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

    public int getBlockID() {
        return blockID;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public double getProcessingSpeed() {
        double seconds = ((endTimestamp - startTimestamp) / (double)TimeUnit.SECONDS.toNanos(1));
        return (end - start) / seconds;
    }
}