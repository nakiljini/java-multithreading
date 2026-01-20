package com.cp.concurrency.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ExecutorServiceExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 50; i++) {
            executorService.submit(() -> {
                System.out.println("Doing through executor");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + e.getMessage());
                }
            });
        }
        executorService.shutdown();
        try {
            if (executorService.awaitTermination(5, TimeUnit.DAYS)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Timeout reached before completion.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
    }
}