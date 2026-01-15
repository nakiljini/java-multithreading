package com.multithreading;

/**
 * Main entry point for Java multithreading examples.
 * Runs all examples in sequence.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("JAVA MULTITHREADING EXAMPLES");
        System.out.println("============================================================");
        System.out.println();

        try {
            BasicThreading.runExample();
            ThreadSynchronization.runExample();
            ProducerConsumer.runExample();
            ThreadPool.runExample();
            ThreadPool.runFixedThreadPoolExample();

            System.out.println("============================================================");
            System.out.println("ALL EXAMPLES COMPLETED SUCCESSFULLY");
            System.out.println("============================================================");
        } catch (Exception e) {
            System.err.println("Error running examples: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
