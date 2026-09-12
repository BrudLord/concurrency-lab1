package org.labs;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTests {
    @Test
    void allFoodIsEaten() throws Exception {
        int waitersCount = 2;
        int programmersCount = 5;
        int foodCount = 10000;
        Playground playground = new Playground(waitersCount, programmersCount, foodCount);

        List<Integer> eaten = playground.start();

        assertEquals(programmersCount, eaten.size());
        assertEquals(foodCount, eaten.stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    void nobodyEatsWhenThereIsNoFood() throws Exception {
        int waitersCount = 2;
        int programmersCount = 5;
        int foodCount = 0;
        Playground playground = new Playground(waitersCount, programmersCount, foodCount);

        List<Integer> eaten = playground.start();

        assertEquals(programmersCount, eaten.size());
        assertEquals(foodCount, eaten.stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    void everyoneEatsApproximatelyTheSameAmount() throws Exception {
        int waitersCount = 10;
        int programmersCount = 10;
        int foodCount = 1000000;
        Playground playground = new Playground(waitersCount, programmersCount, foodCount);

        List<Integer> eaten = playground.start();

        assertFoodDistribution(foodCount, programmersCount, eaten);
    }

    private void assertFoodDistribution(int foodCount, int programmersCount, List<Integer> eaten) {
        double expectedPerProgrammer = (double) foodCount / programmersCount;
        double tolerance = expectedPerProgrammer * 0.05;

        for (Integer amount : eaten) {
            assertTrue(
                    Math.abs(amount - expectedPerProgrammer) <= tolerance,
                    () -> "Expected " + expectedPerProgrammer + " +/- 5%, but got " + amount
            );
        }
    }
}
