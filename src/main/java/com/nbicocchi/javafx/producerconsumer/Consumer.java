package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;

public abstract class Consumer<T> implements Runnable {
    final Queue<T> q;
    public boolean running;
    int count;

    public Consumer(Queue<T> q) {
        super();
        this.running = true;
        this.count = 0;
        this.q = q;
    }
}
