package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class ConsumerSynchronizedYieldBetter extends Consumer {
    public ConsumerSynchronizedYieldBetter(Queue<Integer> queue, Integer maxItems) {
        super(queue, maxItems);
    }

    @Override
    public Long call() throws Exception {
        long start = System.nanoTime();
        while (processedItems < maxItems) {
            if (!queue.isEmpty()) {
                synchronized (queue) {
                    queue.remove();
                }
                //System.out.printf("Consumer %s received %d items\n", Thread.currentThread().getName(), count);
                processedItems += 1;
            } else {
                Thread.yield();
            }
        }
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }
}
