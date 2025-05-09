package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.random.RandomGenerator;

public class ProducerSynchronizedYieldBetter extends Producer {
    public ProducerSynchronizedYieldBetter(Queue<Integer> queue, Integer dequeMaxItems, Integer maxItems) {
        super(queue, dequeMaxItems, maxItems);
    }

    @Override
    protected Long call() throws Exception {
        RandomGenerator rnd = RandomGenerator.getDefault();
        while (processedItems < maxItems) {
            if (queue.size() < dequeMaxItems) {
                synchronized (queue) {
                    queue.add(rnd.nextInt());
                }
                //System.out.printf("Producer %s pushed %d items\n", Thread.currentThread().getName(), count);
                processedItems += 1;
            } else {
                Thread.yield();
            }
        }

        return processedItems;
    }
}
