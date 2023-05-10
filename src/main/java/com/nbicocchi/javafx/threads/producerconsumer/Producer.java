package com.nbicocchi.javafx.threads.producerconsumer;

import javafx.concurrent.Task;

import java.util.Queue;

public abstract class Producer extends Task<Long> {
    final Queue<Integer> queue;
    final int dequeMaxItems;
    long maxItems;
    long processedItems;

    public Producer(Queue<Integer> queue, Integer dequeMaxItems, Integer maxItems) {
        this.queue = queue;
        this.dequeMaxItems = dequeMaxItems;
        this.maxItems = maxItems;
        this.processedItems = 0;
    }
}
