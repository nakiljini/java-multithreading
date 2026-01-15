package com.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;

/**
 * Producer-Consumer Pattern
 * Demonstrates the classic producer-consumer pattern using BlockingQueue.
 */
public class ProducerConsumer {

    static class Producer extends Thread {
        private final BlockingQueue<String> queue;
        private final int producerId;
        private final int numItems;

        public Producer(BlockingQueue<String> queue, int producerId, int numItems) {
            this.queue = queue;
            this.producerId = producerId;
            this.numItems = numItems;
        }

        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < numItems; i++) {
                try {
                    String item = "Item-" + producerId + "-" + i;
                    queue.put(item);
                    System.out.println("Producer " + producerId + " produced: " + item);
                    Thread.sleep(random.nextInt(500) + 100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Producer " + producerId + " finished");
        }
    }

    static class Consumer extends Thread {
        private final BlockingQueue<String> queue;
        private final int consumerId;
        private volatile boolean running = true;

        public Consumer(BlockingQueue<String> queue, int consumerId) {
            this.queue = queue;
            this.consumerId = consumerId;
        }

        public void stopConsuming() {
            running = false;
        }

        @Override
        public void run() {
            Random random = new Random();
            while (running || !queue.isEmpty()) {
                try {
                    String item = queue.poll(500, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (item != null) {
                        System.out.println("Consumer " + consumerId + " consumed: " + item);
                        Thread.sleep(random.nextInt(300) + 100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Consumer " + consumerId + " finished");
        }
    }

    public static void runExample() throws InterruptedException {
        System.out.println("=== Producer-Consumer Pattern Example ===\n");

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

        // Create and start producers
        int numProducers = 2;
        int numItemsPerProducer = 5;
        Producer[] producers = new Producer[numProducers];
        for (int i = 0; i < numProducers; i++) {
            producers[i] = new Producer(queue, i, numItemsPerProducer);
            producers[i].start();
        }

        // Create and start consumers
        int numConsumers = 3;
        Consumer[] consumers = new Consumer[numConsumers];
        for (int i = 0; i < numConsumers; i++) {
            consumers[i] = new Consumer(queue, i);
            consumers[i].start();
        }

        // Wait for all producers to finish
        for (Producer producer : producers) {
            producer.join();
        }

        // Give consumers time to finish remaining items
        Thread.sleep(1000);

        // Stop all consumers
        for (Consumer consumer : consumers) {
            consumer.stopConsuming();
        }

        // Wait for all consumers to finish
        for (Consumer consumer : consumers) {
            consumer.join();
        }

        System.out.println("\nAll producers and consumers finished!\n");
    }

    public static void main(String[] args) throws InterruptedException {
        runExample();
    }
}
