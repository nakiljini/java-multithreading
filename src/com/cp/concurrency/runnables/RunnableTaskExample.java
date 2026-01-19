package com.cp.concurrency.runnables;

class RunnableTask implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello from RunnableTask: " + i);
        }
    }
}


public class RunnableTaskExample {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnableTask());
        thread1.start();

        Thread thread2 = new Thread(new RunnableTask());
        thread2.start();
    }
}
