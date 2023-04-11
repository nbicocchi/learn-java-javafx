package com.nbicocchi.javafx.producerconsumer;

import javafx.concurrent.Task;

import java.util.Queue;

public abstract class Producer extends Task<Long> {
    final Queue<Integer> queue;
    final int dequeMaxItems;
    int maxItems;
    long count;

    public Producer(Queue<Integer> queue, int dequeMaxItems, int maxItems) {
        this.queue = queue;
        this.dequeMaxItems = dequeMaxItems;
        this.maxItems = maxItems;
        this.count = 0;
    }
}
