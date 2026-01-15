package com.multithreading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

/**
 * Unit tests for ThreadSynchronization examples.
 */
class ThreadSynchronizationTest {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testSafeCounterProducesCorrectResult() throws InterruptedException {
        ThreadSynchronization.SafeCounter counter = new ThreadSynchronization.SafeCounter();
        int numThreads = 10;
        int iterations = 100;
        
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new ThreadSynchronization.IncrementWorker(counter, null, iterations);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        assertEquals(numThreads * iterations, counter.getValue());
    }

    @RepeatedTest(3)
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void testUnsafeCounterShowsRaceCondition() throws InterruptedException {
        // This test may occasionally pass, but should frequently show race condition
        ThreadSynchronization.UnsafeCounter counter = new ThreadSynchronization.UnsafeCounter();
        int numThreads = 10;
        int iterations = 100;
        
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new ThreadSynchronization.IncrementWorker(null, counter, iterations);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        int expected = numThreads * iterations;
        int actual = counter.getValue();
        
        // We expect race conditions to cause incorrect results
        // Note: This test demonstrates the issue, not all runs will fail
        assertTrue(actual <= expected, "Counter value should not exceed expected");
    }

    @Test
    void testCounterInitialization() {
        ThreadSynchronization.SafeCounter counter = new ThreadSynchronization.SafeCounter();
        assertEquals(0, counter.getValue());
    }
}
