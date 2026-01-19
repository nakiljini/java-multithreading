package com.cp.concurrency.mutex;

public class SynchronizedMethodExample {
    private int count = 0;

    public static void main(String[] args) throws InterruptedException {
        SynchronizedMethodExample example = new SynchronizedMethodExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Count: " + example.count);
    }

    public synchronized void increment() {
        count++;
    }
}
