package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class ConsumerSynchronizedYield extends Consumer {
    public ConsumerSynchronizedYield(Queue<Integer> queue, Integer maxItems) {
        super(queue, maxItems);
    }

    @Override
    public Long call() throws Exception {
        long start = System.nanoTime();
        while (processedItems < maxItems) {
            synchronized (queue) {
                if (!queue.isEmpty()) {
                    queue.remove();
                    //System.out.printf("Consumer %s received %d items\n", Thread.currentThread().getName(), count);
                    processedItems += 1;
                } else {
                    Thread.yield();
                }
            }
        }
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }
}
