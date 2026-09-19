package org.labs;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Progger implements Callable<Integer> {
    private ExecutorService waitersThreadPool;
    private Waiter waiter;
    private Integer eaten = 0;
    private ReentrantLock firstSpoon;
    private ReentrantLock secondSpoon;

    public Progger(ExecutorService waitersThreadPool, Waiter waiter, Integer number, List<ReentrantLock> spoons) {
        this.waitersThreadPool = waitersThreadPool;
        this.waiter = waiter;
        if (number.equals(0)) {
            this.firstSpoon = spoons.get(0);
            this.secondSpoon = spoons.get(spoons.size() - 1);
        } else {
            this.firstSpoon = spoons.get(number - 1);
            this.secondSpoon = spoons.get(number);
        }
    }

    @Override
    public Integer call() throws Exception {
        while (true) {
            Future<Integer> task = waitersThreadPool.submit(waiter);
            Integer food = task.get();
            if (food.equals(0)) {
                return eaten;
            }
            firstSpoon.lock();
            secondSpoon.lock();
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 100));
            eaten += 1;
            secondSpoon.unlock();
            firstSpoon.unlock();
        }
    }
}
