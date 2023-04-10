package com.nbicocchi.javafx.producerconsumer;

import javafx.concurrent.Task;

import java.util.Deque;

public abstract class Producer extends Task<Long> {
    final Deque<Integer> deque;
    final int dequeMaxItems;
    int maxItems;
    long count;

    public Producer(Deque<Integer> deque, int dequeMaxItems, int maxItems) {
        this.deque = deque;
        this.dequeMaxItems = dequeMaxItems;
        this.maxItems = maxItems;
        this.count = 0;
    }
}
