package com.cp.concurrency.interthread;

import java.util.Scanner;

public class ProducerConsumerWithWaitNotify {

    public static void main(String[] args) throws InterruptedException {

        ProducerConsumerWithWaitNotify pcwn = new ProducerConsumerWithWaitNotify();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pcwn.producer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pcwn.consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private void producer() throws InterruptedException {

        synchronized (this) {
            System.out.println("Producer Running");
            wait();
            System.out.println("Resumed");
        }
    }


    private void consumer() throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        Thread.sleep(1000);
        synchronized (this) {
            System.out.println("Waiting for user intervention");
            scanner.nextLine();
            System.out.println("Key Pressed");
            notify();
        }
    }
}
