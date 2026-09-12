package org.labs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Playground {
    private final AtomicInteger food;
    private final ExecutorService waitersThreadPool;
    private final ExecutorService proggersThreadPool;
    private final Integer proggersCnt;
    private final List<ReentrantLock> spoons;

    public Playground(Integer waitersCnt, Integer proggersCnt, Integer foodCnt) {
        this.food = new AtomicInteger(foodCnt);
        this.waitersThreadPool = Executors.newFixedThreadPool(waitersCnt);
        this.proggersThreadPool = Executors.newFixedThreadPool(proggersCnt);
        this.proggersCnt = proggersCnt;
        this.spoons = new ArrayList<>();
        for (int i = 0; i <= proggersCnt; i++) {
            this.spoons.add(new ReentrantLock());
        }
    }

    public List<Integer> start() throws InterruptedException, ExecutionException {
        List<Progger> proggers = new ArrayList<>();
        for (int i = 0; i < proggersCnt; i++) {
            proggers.add(new Progger(waitersThreadPool, new Waiter(food), i, this.spoons));
        }
        List<Future<Integer>> results = proggersThreadPool.invokeAll(proggers);
        List<Integer> result = new ArrayList<>();
        for (Future<Integer> future : results) {
            result.add(future.get());
        }
        proggersThreadPool.shutdown();
        waitersThreadPool.shutdown();
        return result;
    }
}
