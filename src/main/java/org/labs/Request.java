package org.labs;

import java.util.concurrent.CompletableFuture;

public class Request {
    private int eaten;
    private CompletableFuture<Integer> food = new CompletableFuture<>();

    public Request(int eaten) {
        this.eaten = eaten;
    }

    public int eaten() {
        return eaten;
    }

    public CompletableFuture<Integer> food() {
        return food;
    }
}
