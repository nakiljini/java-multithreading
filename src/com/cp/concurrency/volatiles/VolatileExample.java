package com.cp.concurrency.volatiles;

public class VolatileExample {
    private volatile Integer number = 0; // Using volatile keyword

    public static void main(String[] args) {
        VolatileExample volatileObject = new VolatileExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 200000; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(volatileObject.number);
                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileObject.number = 5;
        System.out.println("number changed");
    }
}