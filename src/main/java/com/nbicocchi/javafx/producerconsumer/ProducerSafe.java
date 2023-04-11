package com.nbicocchi.javafx.producerconsumer;

import java.util.Queue;
import java.util.random.RandomGenerator;

public class ProducerSafe extends Producer {
    public ProducerSafe(Queue<Integer> queue, int dequeMaxItems, int maxItems) {
        super(queue, dequeMaxItems, maxItems);
    }

    @Override
    protected Long call() throws Exception {
        RandomGenerator rnd = RandomGenerator.getDefault();
        while (count < maxItems) {
            synchronized (queue) {
                if (queue.size() < dequeMaxItems) {
                    queue.add(rnd.nextInt());
                    //System.out.printf("Producer %s pushed %d items\n", Thread.currentThread().getName(), count);
                    count += 1;
                }
            }
        }
        return count;
    }
}
