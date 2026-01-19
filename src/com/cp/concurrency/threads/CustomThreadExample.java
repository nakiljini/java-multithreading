package com.cp.concurrency.threads;

class CustomThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello from run method: " + i);
        }
    }
}

public class CustomThreadExample {

    public static void main(String[] args) {
        CustomThread thread1 = new CustomThread();
        thread1.start();
        CustomThread thread2 = new CustomThread();
        thread2.start();
    }
}
