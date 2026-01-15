package com.multithreading;

/**
 * Basic Threading Example
 * Demonstrates fundamental threading concepts in Java.
 */
public class BasicThreading {

    /**
     * Worker class that extends Thread.
     */
    static class Worker extends Thread {
        private final int workerId;
        private final long duration;

        public Worker(int workerId, long duration) {
            this.workerId = workerId;
            this.duration = duration;
        }

        @Override
        public void run() {
            System.out.println("Worker " + workerId + " starting...");
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Worker " + workerId + " interrupted");
            }
            System.out.println("Worker " + workerId + " finished after " + duration + "ms");
        }
    }

    /**
     * Worker using Runnable interface.
     */
    static class RunnableWorker implements Runnable {
        private final int workerId;
        private final long duration;

        public RunnableWorker(int workerId, long duration) {
            this.workerId = workerId;
            this.duration = duration;
        }

        @Override
        public void run() {
            System.out.println("Runnable Worker " + workerId + " starting...");
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Runnable Worker " + workerId + " interrupted");
            }
            System.out.println("Runnable Worker " + workerId + " finished after " + duration + "ms");
        }
    }

    public static void runExample() throws InterruptedException {
        System.out.println("=== Basic Threading Example ===\n");

        // Method 1: Extending Thread class
        System.out.println("Method 1: Extending Thread class");
        Worker[] workers = new Worker[3];
        for (int i = 0; i < 3; i++) {
            workers[i] = new Worker(i, i * 500L);
            workers[i].start();
        }

        for (Worker worker : workers) {
            worker.join();
        }

        System.out.println("\nMethod 2: Implementing Runnable interface");
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(new RunnableWorker(i, i * 500L));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("\nAll threads completed!\n");
    }

    public static void main(String[] args) throws InterruptedException {
        runExample();
    }
}
