package com.nbicocchi.javafx.threads.producerconsumer;

import javafx.concurrent.Task;

import java.util.Queue;

public abstract class Consumer extends Task<Long> {
    final Queue<Integer> queue;
    int maxItems;
    long count;

    public Consumer(Queue<Integer> queue, Integer maxItems) {
        this.queue = queue;
        this.maxItems = maxItems;
        this.count = 0;
    }
}
