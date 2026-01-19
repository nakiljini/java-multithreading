package com.cp.concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(50);

        CountDownLatchExample countDownLatchExample = new CountDownLatchExample();
        for (int i = 0; i < 50; i++) {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("CountDownLatch Count" + countDownLatch.getCount());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    countDownLatch.countDown();
                }
            });
            t.start();
        }
        try {
            countDownLatch.await();
            System.out.println("50 Threads com ");
        } catch (Exception e) {

        }
    }

}
