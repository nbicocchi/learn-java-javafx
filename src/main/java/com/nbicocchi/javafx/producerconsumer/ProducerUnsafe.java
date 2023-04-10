package com.nbicocchi.javafx.producerconsumer;

import java.util.Deque;
import java.util.random.RandomGenerator;

public class ProducerUnsafe extends Producer {
    public ProducerUnsafe(Deque<Integer> deque, int dequeMaxItems, int maxItems) {
        super(deque, dequeMaxItems, maxItems);
    }

    @Override
    protected Long call() throws Exception {
        RandomGenerator rnd = RandomGenerator.getDefault();
        while (count < maxItems) {
            if (deque.size() < dequeMaxItems) {
                deque.addFirst(rnd.nextInt());
                //System.out.printf("Producer %s pushed %d items\n", Thread.currentThread().getName(), count);
                count += 1;
            }
        }
        return count;
    }
}
