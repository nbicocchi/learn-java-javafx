package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.random.RandomGenerator;

public class ProducerSynchronizedYield extends Producer {
    public ProducerSynchronizedYield(Queue<Integer> queue, Integer dequeMaxItems, Integer maxItems) {
        super(queue, dequeMaxItems, maxItems);
    }

    @Override
    protected Long call() throws Exception {
        RandomGenerator rnd = RandomGenerator.getDefault();
        while (processedItems < maxItems) {
            synchronized (queue) {
                if (queue.size() < dequeMaxItems) {
                    queue.add(rnd.nextInt());
                    //System.out.printf("Producer %s pushed %d items\n", Thread.currentThread().getName(), count);
                    processedItems += 1;
                } else {
                    Thread.yield();
                }
            }
        }
        return processedItems;
    }
}
