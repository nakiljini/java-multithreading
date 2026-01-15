package com.multithreading;

/**
 * Thread Synchronization Example
 * Demonstrates thread synchronization using synchronized keyword to prevent race conditions.
 */
public class ThreadSynchronization {

    /**
     * Thread-safe counter using synchronized methods.
     */
    static class SafeCounter {
        private int value = 0;

        public synchronized void increment() {
            int current = value;
            try {
                Thread.sleep(1); // Simulate some processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            value = current + 1;
        }

        public synchronized int getValue() {
            return value;
        }
    }

    /**
     * Unsafe counter without synchronization (demonstrates race conditions).
     */
    static class UnsafeCounter {
        private int value = 0;

        public void increment() {
            int current = value;
            try {
                Thread.sleep(1); // Simulate some processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            value = current + 1;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Worker that increments a counter.
     */
    static class IncrementWorker extends Thread {
        private final SafeCounter safeCounter;
        private final UnsafeCounter unsafeCounter;
        private final int iterations;

        public IncrementWorker(SafeCounter safeCounter, UnsafeCounter unsafeCounter, int iterations) {
            this.safeCounter = safeCounter;
            this.unsafeCounter = unsafeCounter;
            this.iterations = iterations;
        }

        @Override
        public void run() {
            for (int i = 0; i < iterations; i++) {
                if (safeCounter != null) {
                    safeCounter.increment();
                }
                if (unsafeCounter != null) {
                    unsafeCounter.increment();
                }
            }
        }
    }

    public static void runExample() throws InterruptedException {
        System.out.println("=== Thread Synchronization Example ===\n");

        int numThreads = 10;
        int iterations = 100;

        // Test with unsafe counter
        System.out.println("1. Unsafe Counter (Race Condition):");
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        Thread[] unsafeThreads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            unsafeThreads[i] = new IncrementWorker(null, unsafeCounter, iterations);
            unsafeThreads[i].start();
        }

        for (Thread thread : unsafeThreads) {
            thread.join();
        }

        System.out.println("Expected: " + (numThreads * iterations) + ", Got: " + unsafeCounter.getValue());

        // Test with safe counter
        System.out.println("\n2. Safe Counter (With Synchronization):");
        SafeCounter safeCounter = new SafeCounter();
        Thread[] safeThreads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            safeThreads[i] = new IncrementWorker(safeCounter, null, iterations);
            safeThreads[i].start();
        }

        for (Thread thread : safeThreads) {
            thread.join();
        }

        System.out.println("Expected: " + (numThreads * iterations) + ", Got: " + safeCounter.getValue());
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        runExample();
    }
}
