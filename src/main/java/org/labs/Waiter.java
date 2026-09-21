package org.labs;

import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Waiter implements Callable<Integer> {
    public AtomicInteger food;
    private PriorityBlockingQueue<Request> requests;

    public Waiter(AtomicInteger food, PriorityBlockingQueue<Request> requests) {
        this.food = food;
        this.requests = requests;
    }

    @Override
    public Integer call() throws Exception {
        while (true) {
            Request request = requests.take();
            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
            while (true) {
                int food = this.food.get();
                if (food <= 0) {
                    request.food().complete(0);
                    break;
                }
                boolean isCorrect = this.food.compareAndSet(food, food - 1);
                if (isCorrect) {
                    request.food().complete(1);
                    break;
                }
            }
        }
    }
}
