package com.cp.concurrency.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

    Semaphore semaphore = new Semaphore(10);

    public static void main(String[] args) {
        SemaphoreExample se = new SemaphoreExample();
        for (int i = 0; i < 50; i++) {

            Thread t1 = new Thread(() -> {
                try {
                    se.semaphore.acquire();
                    System.out.println("Acquired semaphore");
                    System.out.println("size of semaphore" + se.semaphore.availablePermits());
                    Thread.sleep(5000);
                    se.semaphore.release();
                    System.out.println("Released semaphore");
                    System.out.println("size of semaphore after Released" + se.semaphore.availablePermits());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t1.start();
        }
    }
}