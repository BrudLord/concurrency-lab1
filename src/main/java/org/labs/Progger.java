package org.labs;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Progger implements Callable<Integer> {
    private Integer eaten = 0;
    private ReentrantLock firstSpoon;
    private ReentrantLock secondSpoon;
    PriorityBlockingQueue<Request> requests;

    public Progger(Integer number, List<ReentrantLock> spoons, PriorityBlockingQueue<Request> requests) {
        if (number.equals(0)) {
            this.firstSpoon = spoons.get(0);
            this.secondSpoon = spoons.get(spoons.size() - 1);
        } else {
            this.firstSpoon = spoons.get(number - 1);
            this.secondSpoon = spoons.get(number);
        }
        this.requests = requests;
    }

    @Override
    public Integer call() throws Exception {
        while (true) {
            Request request = new Request(eaten);
            requests.offer(request);
            Integer food = request.food().get();
            if (food.equals(0)) {
                return eaten;
            }
            firstSpoon.lock();
            secondSpoon.lock();
            Thread.sleep(ThreadLocalRandom.current().nextInt(5, 10));
            eaten += 1;
            secondSpoon.unlock();
            firstSpoon.unlock();
        }
    }
}
