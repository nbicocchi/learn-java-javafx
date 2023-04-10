package com.nbicocchi.javafx.producerconsumer;

import java.util.Deque;
import java.util.random.RandomGenerator;

public class ProducerSafe extends Producer {
    public ProducerSafe(Deque<Integer> deque, int dequeMaxItems, int maxItems) {
        super(deque, dequeMaxItems, maxItems);
    }

    @Override
    protected Integer call() throws Exception {
        RandomGenerator rnd = RandomGenerator.getDefault();
        while (count < maxItems) {
            synchronized (deque) {
                if (deque.size() < dequeMaxItems) {
                    deque.addFirst(rnd.nextInt());
                    //System.out.printf("Producer %s pushed %d items\n", Thread.currentThread().getName(), count);
                    count += 1;
                }
            }
        }
        return count;
    }
}
