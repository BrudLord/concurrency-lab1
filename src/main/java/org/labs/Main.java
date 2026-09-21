package org.labs;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Playground playground = new Playground(10, 10, 1000);
        List<Integer> res = playground.start();
        for (Integer val : res) {
            System.out.println(val);
        }
    }
}