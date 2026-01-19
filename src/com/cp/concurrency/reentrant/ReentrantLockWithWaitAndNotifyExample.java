package com.cp.concurrency.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockWithWaitAndNotifyExample {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public static void main(String[] args) {
        ReentrantLockWithWaitAndNotifyExample example = new ReentrantLockWithWaitAndNotifyExample();

        new Thread(() -> {
            example.lock.lock();
            try {
                System.out.println("Acquired Lock Thread 1");
                Thread.sleep(5000);
                example.condition.await();
                System.out.println("Thread running back");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread 1 interrupted: " + e.getMessage());
            } finally {
                example.lock.unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread 2 interrupted: " + e.getMessage());
            }
            example.lock.lock();
            try {
                System.out.println("Acquired Lock Thread 2");
                example.condition.signal();
                System.out.println("Thread finishing work");
            } finally {
                example.lock.unlock();
            }
        }).start();
    }
}
