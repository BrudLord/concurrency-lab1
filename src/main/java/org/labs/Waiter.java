package org.labs;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Waiter implements Callable<Integer> {
    public AtomicInteger food;

    public Waiter(AtomicInteger food) {
        this.food = food;
    }

    @Override
    public Integer call() throws Exception {
        while (true) {
            int food = this.food.get();
            if (food <= 0) {
                return 0;
            }
            boolean isCorrect = this.food.compareAndSet(food, food - 1);
            if (isCorrect) {
                return 1;
            }
        }
    }
}
