package com.nbicocchi.javafx.producerconsumer;

import javafx.concurrent.Task;

import java.util.Deque;

public abstract class Consumer extends Task<Long> {
    final Deque<Integer> deque;
    int maxItems;
    int count;

    public Consumer(Deque<Integer> deque, int maxItems) {
        this.deque = deque;
        this.maxItems = maxItems;
        this.count = 0;
    }
}
