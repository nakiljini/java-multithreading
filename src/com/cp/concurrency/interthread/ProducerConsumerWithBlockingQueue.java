package com.cp.concurrency.interthread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerWithBlockingQueue {

    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                producer();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void producer() {
        Random random = new Random();
        while (true) {
            queue.offer(random.nextInt(100));
        }
    }

    public static void consumer() throws InterruptedException {
        Random random = new Random();
        while (true) {
            Thread.sleep(1000);
            if (random.nextInt(10) == 0) {
                Integer value = queue.take();

                System.out.println("Taken value :=" + value + "  ,Size of Queue : =" + queue.size());
            }
        }
    }
}
