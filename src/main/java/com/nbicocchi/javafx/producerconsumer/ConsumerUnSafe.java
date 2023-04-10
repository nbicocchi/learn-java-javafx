package com.nbicocchi.javafx.producerconsumer;

import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class ConsumerUnSafe extends Consumer {
    public ConsumerUnSafe(Deque<Integer> deque, int maxItems) {
        super(deque, maxItems);
    }

    @Override
    public Long call() throws Exception {
        long start = System.nanoTime();
        while (count < maxItems - 10) {
            if (!deque.isEmpty()) {
                deque.removeLast();
                //System.out.printf("Consumer %s received %d items\n", Thread.currentThread().getName(), count);
                count += 1;
            }
        }
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }
}
