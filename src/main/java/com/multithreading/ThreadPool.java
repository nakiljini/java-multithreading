package com.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Thread Pool Example
 * Demonstrates using ExecutorService for efficient thread management.
 */
public class ThreadPool {

    static class Task implements Callable<TaskResult> {
        private final int taskId;

        public Task(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public TaskResult call() throws Exception {
            Random random = new Random();
            double duration = 0.5 + random.nextDouble() * 1.5;
            long durationMs = (long) (duration * 1000);

            System.out.println("Task " + taskId + " starting (will take " + 
                             String.format("%.2f", duration) + "s)");
            Thread.sleep(durationMs);
            
            int result = taskId * taskId;
            System.out.println("Task " + taskId + " completed");
            
            return new TaskResult(taskId, result, durationMs);
        }
    }

    static class TaskResult {
        final int taskId;
        final int result;
        final long durationMs;

        public TaskResult(int taskId, int result, long durationMs) {
            this.taskId = taskId;
            this.result = result;
            this.durationMs = durationMs;
        }

        @Override
        public String toString() {
            return "Task " + taskId + " = " + result;
        }
    }

    public static void runExample() throws InterruptedException, ExecutionException {
        System.out.println("=== Thread Pool Example ===\n");

        int numTasks = 10;
        int maxWorkers = 3;

        System.out.println("Executing " + numTasks + " tasks with " + maxWorkers + " workers\n");

        ExecutorService executor = Executors.newFixedThreadPool(maxWorkers);
        List<Future<TaskResult>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // Submit all tasks
        for (int i = 0; i < numTasks; i++) {
            Future<TaskResult> future = executor.submit(new Task(i));
            futures.add(future);
        }

        // Retrieve results as they complete
        List<TaskResult> results = new ArrayList<>();
        for (Future<TaskResult> future : futures) {
            TaskResult result = future.get();
            results.add(result);
            System.out.println("Retrieved result: " + result);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        double totalTime = (endTime - startTime) / 1000.0;

        System.out.println("\nAll tasks completed in " + String.format("%.2f", totalTime) + " seconds");
        System.out.print("Results: [");
        for (int i = 0; i < results.size(); i++) {
            System.out.print("(" + results.get(i).taskId + ", " + results.get(i).result + ")");
            if (i < results.size() - 1) System.out.print(", ");
        }
        System.out.println("]\n");
    }

    public static void runFixedThreadPoolExample() throws InterruptedException {
        System.out.println("=== Fixed Thread Pool Map Example ===\n");

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Integer> numbers = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        System.out.println("Input:  " + numbers);

        List<Future<Integer>> futures = new ArrayList<>();
        for (Integer num : numbers) {
            futures.add(executor.submit(() -> {
                Thread.sleep(200);
                return num * num;
            }));
        }

        List<Integer> results = new ArrayList<>();
        for (Future<Integer> future : futures) {
            try {
                results.add(future.get());
            } catch (ExecutionException e) {
                System.err.println("Task failed: " + e.getMessage());
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Output: " + results + "\n");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        runExample();
        runFixedThreadPoolExample();
    }
}
