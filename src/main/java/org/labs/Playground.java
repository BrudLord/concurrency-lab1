package org.labs;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Playground {
    private final AtomicInteger food;
    private final ExecutorService waitersThreadPool;
    private final ExecutorService proggersThreadPool;
    private final Integer proggersCnt;
    private final Integer waitersCnt;
    private final List<ReentrantLock> spoons;
    private final PriorityBlockingQueue<Request> requests;

    public Playground(Integer waitersCnt, Integer proggersCnt, Integer foodCnt) {
        this.food = new AtomicInteger(foodCnt);
        this.waitersThreadPool = Executors.newVirtualThreadPerTaskExecutor();
        this.proggersThreadPool = Executors.newVirtualThreadPerTaskExecutor();
        this.proggersCnt = proggersCnt;
        this.waitersCnt = waitersCnt;
        this.spoons = new ArrayList<>();
        for (int i = 0; i < proggersCnt; i++) {
            this.spoons.add(new ReentrantLock());
        }
        this.requests = new PriorityBlockingQueue<>(
                proggersCnt,
                Comparator.comparingInt(Request::eaten)
        );
    }

    public List<Integer> start() throws InterruptedException, ExecutionException {
        List<Progger> proggers = new ArrayList<>();
        for (int i = 0; i < proggersCnt; i++) {
            proggers.add(new Progger(i, this.spoons, requests));
        }
        for (int i = 0; i < waitersCnt; i++) {
            waitersThreadPool.submit(new Waiter(food, requests));
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
