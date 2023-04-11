package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class ConsumerSafeWaitNotify extends Consumer {
    public ConsumerSafeWaitNotify(Queue<Integer> queue, int maxItems) {
        super(queue, maxItems);
    }

    @Override
    public Long call() throws Exception {
        long start = System.nanoTime();
        while (count < maxItems) {
            synchronized (queue) {
                if (!queue.isEmpty()) {
                    queue.remove();
                    //System.out.printf("Consumer %s received %d items\n", Thread.currentThread().getName(), count);
                    count += 1;
                    queue.notifyAll();
                } else {
                    queue.wait();
                }
            }
        }
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }
}
