package com.cp.concurrency.volatiles;

public class NonVolatileExample {
    private Integer number = 0; // No volatile keyword

    public static void main(String[] args) {
        NonVolatileExample nonVolatileObject = new NonVolatileExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 200000; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(nonVolatileObject.number);
                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nonVolatileObject.number = 5;
        System.out.println("number changed");
    }
}
