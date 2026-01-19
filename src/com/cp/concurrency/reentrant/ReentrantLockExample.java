package com.cp.concurrency.reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    public static Integer integer1 = 0;
    public static Integer integer2 = 0;
    static ReentrantLock lock1 = new ReentrantLock();
    static ReentrantLock lock2 = new ReentrantLock();

    private static void increment() {
        lock1.lock();
        integer1++;
        lock1.unlock();
    }

    private static void decrement() {
        lock2.lock();
        integer2--;
        lock2.unlock();
    }

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                increment();
            }
        }, "increment1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                decrement();
            }
        }, "decrement1");

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                increment();
            }
        }, "increment2");

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                decrement();
            }
        }, "decrement2");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();


        System.out.println("Final Values is  : integer1 " + integer1);
        System.out.println("Final Values is  : integer2 " + integer2);
    }

}
