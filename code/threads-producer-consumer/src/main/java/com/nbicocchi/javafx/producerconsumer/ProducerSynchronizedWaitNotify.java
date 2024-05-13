package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.random.RandomGenerator;

public class ProducerSynchronizedWaitNotify extends Producer {
    public ProducerSynchronizedWaitNotify(Queue<Integer> queue, Integer dequeMaxItems, Integer maxItems) {
        super(queue, dequeMaxItems, maxItems);
    }

    @Override
    public Long call() throws Exception {
        RandomGenerator rnd = RandomGenerator.getDefault();
        while (processedItems < maxItems) {
            synchronized (queue) {
                if (queue.size() < dequeMaxItems) {
                    queue.add(rnd.nextInt());
                    //System.out.printf("Producer %s pushed %d items\n", Thread.currentThread().getName(), count);
                    processedItems += 1;
                    queue.notifyAll();
                } else {
                    queue.wait();
                }
            }
        }
        return processedItems;
    }
}
