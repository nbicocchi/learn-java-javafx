package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;

public abstract class Producer<T> implements Runnable {
    final Queue<T> q;
    boolean running;
    int count;
    int maxitems;
    T item;

    public Producer(int maxitems, T item, Queue<T> q) {
        super();
        this.running = true;
        this.count = 0;
        this.maxitems = maxitems;
        this.item = item;
        this.q = q;
    }
}
