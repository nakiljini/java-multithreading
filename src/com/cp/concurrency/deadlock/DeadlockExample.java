package com.cp.concurrency.deadlock;

public class DeadlockExample {

    public static void main(String[] args) throws InterruptedException {

        Account account1 = new Account("12345", 100000);
        Account account2 = new Account("54321", 200000);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 500; i++) {
                    transfer(account1, account2, 100);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 500; i++) {
                    transfer(account2, account1, 100);
                }
            }
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }


    private static void transfer(Account fromAccount, Account toAccount, Integer amount) {

        synchronized (fromAccount.getLock()) {
            synchronized (toAccount.getLock()) {
                System.out.println(" from Account Amount " + fromAccount.getAmount() + " to Account Amount " + toAccount.getAmount() + " Amount  " + amount);
                toAccount.setAmount(toAccount.getAmount() + amount);
                fromAccount.setAmount(fromAccount.getAmount() - amount);
            }
        }
    }
}

class Account {

    private String accountNumber;

    private Integer amount;

    private Object lock;

    public Account(String accountNumber, Integer amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.lock = new Object();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Object getLock() {
        return lock;
    }

    public void setLock(Object lock) {
        this.lock = lock;
    }
}
