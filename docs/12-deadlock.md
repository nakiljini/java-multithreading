# Deadlock Package - Understanding and Avoiding Deadlocks

## 📦 Package: `com.cp.concurrency.deadlock`

## 🎯 The Concept

**Deadlock** is a situation where two or more threads are blocked forever, waiting for each other to release locks. It's like two people trying to pass through a narrow doorway:
- Person A needs Person B to move, but Person B needs Person A to move
- Both wait forever - DEADLOCK!

In programming:
- Thread 1 holds Lock A and waits for Lock B
- Thread 2 holds Lock B and waits for Lock A
- Both threads wait forever - program hangs!

## 🎯 What We're Trying to Achieve

We want to:
- Understand how deadlocks occur
- See a real example of deadlock scenario
- Learn how to prevent deadlocks
- Recognize deadlock conditions

## 🔧 How Deadlock Happens

Deadlock requires four conditions (all must be true):
1. **Mutual Exclusion** - Resources can't be shared
2. **Hold and Wait** - Thread holds one lock and waits for another
3. **No Preemption** - Can't force thread to release lock
4. **Circular Wait** - Threads wait in a circle

## 📝 Code Explanation: DeadlockExample.java

```java
package com.cp.concurrency.deadlock;

public class DeadlockExample {
    public static void main(String[] args) throws InterruptedException {
        Account account1 = new Account("12345", 100000);
        Account account2 = new Account("54321", 200000);
```

**Explanation:**
- Creates two bank accounts
- Each account has its own lock (for thread safety)

```java
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 500; i++) {
                    transfer(account1, account2, 100);
                }
            }
        });
```

**Explanation:**
- Thread 1: Transfers money from account1 to account2 (500 times)
- Each transfer needs to lock both accounts

```java
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 500; i++) {
                    transfer(account2, account1, 100);
                }
            }
        });
```

**Explanation:**
- Thread 2: Transfers money from account2 to account1 (500 times)
- **Opposite direction** - This creates the potential for deadlock!

```java
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
```

**Explanation:**
- Starts both threads
- Waits for both to complete
- **Potential Problem**: If deadlock occurs, threads never finish, join() waits forever!

```java
    private static void transfer(Account fromAccount, Account toAccount, Integer amount) {
        synchronized (fromAccount.getLock()) {
            synchronized (toAccount.getLock()) {
                System.out.println(" from Account Amount " + fromAccount.getAmount() 
                    + " to Account Amount " + toAccount.getAmount() + " Amount  " + amount);
                toAccount.setAmount(toAccount.getAmount() + amount);
                fromAccount.setAmount(fromAccount.getAmount() - amount);
            }
        }
    }
}
```

**Explanation:**
- `synchronized (fromAccount.getLock())`: Acquires lock on source account
- `synchronized (toAccount.getLock())`: Acquires lock on destination account
- **Nested locks** - This is where deadlock can occur!

### How Deadlock Happens:

```
Time 0s:    Thread 1: Locks account1 (fromAccount)
            Thread 2: Locks account2 (fromAccount)

Time 0.001s: Thread 1: Tries to lock account2 (toAccount) - WAITS (Thread 2 has it)
            Thread 2: Tries to lock account1 (toAccount) - WAITS (Thread 1 has it)

Result:     Both threads wait forever - DEADLOCK!
```

## 📝 Code Explanation: Account Class

```java
class Account {
    private String accountNumber;
    private Integer amount;
    private Object lock;

    public Account(String accountNumber, Integer amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.lock = new Object();
    }
```

**Explanation:**
- Each account has its own `lock` object
- Used for synchronization
- Allows independent locking of different accounts

```java
    public Object getLock() {
        return lock;
    }
    // ... other getters and setters
}
```

**Explanation:**
- Provides access to the lock object
- Used in synchronized blocks

## 🔄 Deadlock Scenario Visualization

```
Thread 1:                    Thread 2:
┌─────────────┐              ┌─────────────┐
│ Lock A      │              │ Lock B      │
│ (account1)  │              │ (account2)  │
└──────┬──────┘              └──────┬──────┘
       │                            │
       │ Waits for Lock B           │ Waits for Lock A
       │ (held by Thread 2)         │ (held by Thread 1)
       │                            │
       ▼                            ▼
   [BLOCKED]                    [BLOCKED]
   
   DEADLOCK! Both threads wait forever!
```

## 💡 How to Prevent Deadlocks

### 1. Lock Ordering (Most Common Solution)
Always acquire locks in the same order:

```java
private static void transfer(Account fromAccount, Account toAccount, Integer amount) {
    // Determine lock order based on account number
    Account first = fromAccount.getAccountNumber().compareTo(toAccount.getAccountNumber()) < 0 
        ? fromAccount : toAccount;
    Account second = first == fromAccount ? toAccount : fromAccount;
    
    synchronized (first.getLock()) {
        synchronized (second.getLock()) {
            // Transfer logic
        }
    }
}
```

### 2. Timeout on Lock Acquisition
Use `tryLock()` with timeout:

```java
if (lock1.tryLock(5, TimeUnit.SECONDS)) {
    try {
        if (lock2.tryLock(5, TimeUnit.SECONDS)) {
            try {
                // Transfer logic
            } finally {
                lock2.unlock();
            }
        }
    } finally {
        lock1.unlock();
    }
}
```

### 3. Avoid Nested Locks
Restructure code to minimize lock nesting.

### 4. Use Single Lock
If possible, use one lock for all accounts (reduces parallelism but prevents deadlock).

## 🎓 Key Takeaways

1. **Deadlock = threads wait forever** - Program hangs
2. **Requires 4 conditions** - All must be true for deadlock
3. **Nested locks are risky** - Can cause deadlock
4. **Lock ordering prevents deadlock** - Always acquire in same order
5. **Use timeout** - Don't wait forever for locks

## ⚠️ Warning Signs

Watch out for:
- Multiple locks acquired in different orders
- Nested synchronized blocks
- Circular dependencies between threads
- Program hangs with no error message

## 🔄 When Deadlock Occurs

**In this example:**
- Thread 1: account1 → account2
- Thread 2: account2 → account1
- Different lock order = potential deadlock!

**Solution:**
- Always lock accounts in same order (e.g., by account number)
- Or use timeout when acquiring locks
- Or restructure to avoid nested locks

## 📊 Deadlock Detection

Java doesn't automatically detect deadlocks, but you can:
1. Use thread dumps: `jstack <pid>` or `kill -3 <pid>`
2. Look for threads in "BLOCKED" state
3. Check if threads are waiting for locks held by each other

---

**Next:** Learn about [Volatile](13-volatile.md) for variable visibility across threads!
