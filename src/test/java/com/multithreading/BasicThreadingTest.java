package com.multithreading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

/**
 * Unit tests for BasicThreading examples.
 */
class BasicThreadingTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testWorkerThreadCompletes() throws InterruptedException {
        BasicThreading.Worker worker = new BasicThreading.Worker(1, 100);
        worker.start();
        worker.join();
        assertFalse(worker.isAlive());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testRunnableWorkerCompletes() throws InterruptedException {
        Thread thread = new Thread(new BasicThreading.RunnableWorker(1, 100));
        thread.start();
        thread.join();
        assertFalse(thread.isAlive());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMultipleThreadsConcurrent() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(new BasicThreading.RunnableWorker(i, 100));
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long elapsed = System.currentTimeMillis() - startTime;
        
        // Should complete in ~100ms (concurrent) not 300ms (sequential)
        assertTrue(elapsed < 250, "Threads should run concurrently");
    }
}
