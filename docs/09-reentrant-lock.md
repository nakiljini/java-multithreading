# Reentrant Lock Package - Advanced Locking

## 📦 Package: `com.cp.concurrency.reentrant`

## 🎯 The Concept

**ReentrantLock** is an advanced locking mechanism that provides more flexibility than `synchronized`. It's called "reentrant" because the same thread can acquire the lock multiple times (it keeps track of how many times).

Think of it like a key that can open the same lock multiple times:
- First time: Lock the door
- Second time: Still works (reentrant)
- Must unlock the same number of times to fully unlock

**Key Advantages over synchronized:**
- Can have multiple locks
- Can check if lock is available
- Can interrupt while waiting for lock
- Can have timeout when acquiring lock
- More control and flexibility

## 🎯 What We're Trying to Achieve

We want to:
- Have more control over locking than `synchronized` provides
- Use multiple locks for different operations
- Handle lock acquisition with timeouts
- Interrupt threads waiting for locks
- Use conditions for thread coordination

## 🔧 How We Achieve It

1. **Create ReentrantLock objects** - One or more locks
2. **Acquire lock** - Call `lock()` before critical section
3. **Release lock** - Call `unlock()` in finally block (CRITICAL!)
4. **Use conditions** - For thread coordination (like wait/notify)

## 📝 Code Explanation: ReentrantLockExample.java

```java
package com.cp.concurrency.reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    public static Integer integer1 = 0;
    public static Integer integer2 = 0;
    static ReentrantLock lock1 = new ReentrantLock();
    static ReentrantLock lock2 = new ReentrantLock();
```

**Explanation:**
- `integer1` and `integer2`: Two separate counters
- `lock1` and `lock2`: Two separate locks
- **Key Point**: Different operations can use different locks!
- This allows parallel execution of independent operations

```java
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
```

**Explanation:**
- `increment()`: Uses `lock1` to protect `integer1`
- `decrement()`: Uses `lock2` to protect `integer2`
- **Important**: These can run in parallel because they use different locks!
- `lock()`: Acquires the lock (blocks if another thread has it)
- `unlock()`: Releases the lock

**⚠️ CRITICAL**: Always unlock in finally block in real code!

```java
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
```

**Explanation:**
- Creates 4 threads: 2 incrementing, 2 decrementing
- Thread names: "increment1", "decrement1", etc.
- All threads can run in parallel because:
  - Increment threads use `lock1`
  - Decrement threads use `lock2`
  - Different locks = no contention!

```java
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
```

**Explanation:**
- Starts all threads
- Waits for all to complete
- Prints final values

## 📝 Code Explanation: ReentrantLockWithWaitAndNotifyExample.java

```java
package com.cp.concurrency.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockWithWaitAndNotifyExample {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
```

**Explanation:**
- `ReentrantLock lock`: The lock object
- `Condition condition`: Similar to wait/notify but for ReentrantLock
- `lock.newCondition()`: Creates a condition associated with this lock
- Can have multiple conditions per lock!

```java
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
```

**Explanation:**
- Thread 1:
  - `lock.lock()`: Acquires the lock
  - Sleeps for 5 seconds
  - `condition.await()`: 
    - Releases the lock (like wait())
    - Waits until signaled
    - When signaled, re-acquires lock and continues
  - **CRITICAL**: `unlock()` in finally block ensures lock is always released!

```java
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
```

**Explanation:**
- Thread 2:
  - Waits 500ms (gives Thread 1 time to acquire lock and wait)
  - Acquires the lock
  - `condition.signal()`: Wakes up Thread 1 (like notify())
  - Releases lock in finally block

## 🔄 ReentrantLock vs synchronized

### synchronized:
```java
synchronized (lock) {
    // Code here
    // Can't check if lock is available
    // Can't interrupt while waiting
    // Can't have timeout
}
```

### ReentrantLock:
```java
lock.lock();
try {
    // Code here
} finally {
    lock.unlock();
}

// Can check if available:
if (lock.tryLock()) { ... }

// Can interrupt:
lock.lockInterruptibly();

// Can timeout:
lock.tryLock(5, TimeUnit.SECONDS);
```

## 💡 Key Concepts

### 1. Reentrant
- Same thread can acquire lock multiple times
- Must unlock same number of times
- Prevents deadlock in recursive calls

### 2. Multiple Locks
- Can have different locks for different operations
- Allows parallel execution of independent operations
- Better performance than single lock

### 3. Condition
- Like wait/notify but for ReentrantLock
- Can have multiple conditions per lock
- More flexible than wait/notify

### 4. Fairness
- `new ReentrantLock(true)`: Fair lock (FIFO ordering)
- `new ReentrantLock(false)`: Unfair (default, better performance)

## 📊 Execution Flow

```
Time 0s:    Thread 1: lock.lock() - acquires lock
            Thread 1: Sleeps 5 seconds
            Thread 2: Tries to acquire lock - waits

Time 0.5s:  Thread 1: condition.await() - releases lock, waits
            Thread 2: lock.lock() - acquires lock (Thread 1 released it)
            Thread 2: condition.signal() - wakes Thread 1
            Thread 2: unlock() - releases lock
            Thread 1: Re-acquires lock, continues execution
```

## 🎓 Key Takeaways

1. **ReentrantLock is more flexible** - More features than synchronized
2. **Multiple locks** - Different locks for different operations
3. **Always unlock in finally** - Prevents deadlock
4. **Condition for coordination** - Like wait/notify but better
5. **Can check/interrupt/timeout** - More control than synchronized

## ⚠️ Critical Rules

1. **ALWAYS unlock in finally block**
   ```java
   lock.lock();
   try {
       // Code
   } finally {
       lock.unlock();  // Always executes!
   }
   ```

2. **Don't forget to unlock** - Causes deadlock!

3. **Use tryLock() for non-blocking**
   ```java
   if (lock.tryLock()) {
       try {
           // Code
       } finally {
           lock.unlock();
       }
   }
   ```

## 🔄 When to Use

**Use ReentrantLock when:**
- ✅ Need multiple locks
- ✅ Need to check if lock is available
- ✅ Need timeout when acquiring lock
- ✅ Need to interrupt while waiting
- ✅ Need multiple conditions
- ✅ Need fairness control

**Use synchronized when:**
- ✅ Simple cases
- ✅ Don't need advanced features
- ✅ Want simpler code

---

**Next:** Learn about [Semaphore](10-semaphore.md) for controlling resource access!
