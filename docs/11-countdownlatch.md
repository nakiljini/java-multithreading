# CountDownLatch Package - Waiting for Threads

## 📦 Package: `com.cp.concurrency.countdownlatch`

## 🎯 The Concept

**CountDownLatch** is a synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes. Think of it like waiting for all students to finish an exam:
- Teacher waits until all students finish
- Each student finishes and "counts down"
- When count reaches zero, teacher collects papers
- Teacher can proceed only after all students are done

In programming:
- Wait for multiple threads to complete initialization
- Wait for all workers to finish their tasks
- Coordinate startup of multiple services
- Wait for all data to be loaded before processing

## 🎯 What We're Trying to Achieve

We want to:
- Make one thread wait for multiple other threads to complete
- Coordinate the start of a task after prerequisites are done
- Ensure all threads finish before proceeding
- Synchronize multiple threads at a specific point

## 🔧 How We Achieve It

1. **Create CountDownLatch with count** - Specify how many threads must complete
2. **Threads do their work** - Each thread performs its task
3. **Count down** - Each thread calls `countDown()` when done
4. **Wait for completion** - Main thread calls `await()` and waits until count reaches zero

## 📝 Code Explanation: CountDownLatchExample.java

```java
package com.cp.concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(50);
```

**Explanation:**
- `new CountDownLatch(50)`: 
  - Creates a latch with initial count of 50
  - Main thread will wait until count reaches 0
  - 50 threads must call `countDown()` before main thread proceeds

```java
        CountDownLatchExample countDownLatchExample = new CountDownLatchExample();
        for (int i = 0; i < 50; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("CountDownLatch Count" + countDownLatch.getCount());
```

**Explanation:**
- Creates 50 threads
- Each thread will do some work and count down
- `getCount()`: Returns current count (how many threads still need to finish)
  - Initially: 50
  - As threads finish: Decreases
  - When all done: 0

```java
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
```

**Explanation:**
- `Thread.sleep(5000)`: Simulates work (5 seconds)
- `countDownLatch.countDown()`: 
  - Decrements the count by 1
  - When count reaches 0, waiting threads are released
  - Each thread calls this once when done

```java
        try {
            countDownLatch.await();
            System.out.println("50 Threads com ");
        } catch (Exception e) {

        }
    }
}
```

**Explanation:**
- `countDownLatch.await()`: 
  - **Blocks** the main thread
  - Waits until count reaches 0
  - All 50 threads must call `countDown()` before this returns
- After all threads finish, main thread continues and prints message

## 🔄 How CountDownLatch Works

### Step-by-Step Execution:

```
Time 0s:    Main thread: Creates latch (count = 50)
            Main thread: Creates 50 threads
            Main thread: Starts all threads
            Main thread: Calls await() - BLOCKS and waits

Time 0s:    50 threads: Start executing
            Each thread: Does work (sleeps 5 seconds)

Time 5s:    Thread 1: Finishes, calls countDown() (count = 49)
            Thread 2: Finishes, calls countDown() (count = 48)
            ...
            Thread 50: Finishes, calls countDown() (count = 0)

Time 5s:    Main thread: await() returns (count reached 0)
            Main thread: Prints "50 Threads com"
            Main thread: Program continues
```

## 💡 Key Concepts

### 1. Count
- Initial count: Number of threads that must complete
- Decrements: Each `countDown()` reduces count by 1
- Zero: When count reaches 0, waiting threads are released

### 2. One-Time Use
- CountDownLatch cannot be reset
- Once count reaches 0, it stays at 0
- Need a new latch for another round

### 3. await() Behavior
- Blocks until count reaches 0
- Can be called by multiple threads (all wait)
- All waiting threads are released when count = 0

### 4. getCount()
- Returns current count
- Useful for monitoring progress
- Decreases as threads call countDown()

## 📊 Visual Representation

```
CountDownLatch (count = 50)

Main Thread:
  await() ──> [WAITING] ──> (count = 0) ──> Continues

Worker Threads:
  Thread 1: Work ──> countDown() ──> (count = 49)
  Thread 2: Work ──> countDown() ──> (count = 48)
  ...
  Thread 50: Work ──> countDown() ──> (count = 0) ──> Main thread released!
```

## 🎓 Key Takeaways

1. **CountDownLatch coordinates threads** - One waits for many
2. **countDown() decrements** - Each thread calls once when done
3. **await() blocks** - Waits until count reaches 0
4. **One-time use** - Cannot be reset, create new one if needed
5. **Useful for initialization** - Wait for all services to start

## ⚠️ Important Points

1. **Count cannot be reset** - Create new latch if needed again
2. **Each thread calls countDown() once** - Don't call multiple times
3. **await() blocks** - Thread waits until count = 0
4. **Can have timeout** - `await(timeout, TimeUnit)` to avoid waiting forever

## 🔄 Real-World Examples

### 1. Service Initialization
```java
CountDownLatch servicesReady = new CountDownLatch(3);
// Wait for database, cache, and API to initialize
```

### 2. Parallel Processing
```java
CountDownLatch allTasksDone = new CountDownLatch(100);
// Wait for 100 parallel tasks to complete
```

### 3. Game Start
```java
CountDownLatch playersReady = new CountDownLatch(4);
// Wait for all 4 players to join before starting game
```

## 🔄 CountDownLatch vs Other Mechanisms

### vs join()
- `join()`: Wait for ONE specific thread
- `CountDownLatch`: Wait for MANY threads (any order)

### vs CyclicBarrier
- `CountDownLatch`: One-time use, one waits for many
- `CyclicBarrier`: Reusable, all threads wait for each other

## 🔄 When to Use

**Use CountDownLatch when:**
- ✅ One thread needs to wait for multiple threads
- ✅ Need to coordinate startup
- ✅ Wait for initialization to complete
- ✅ One-time synchronization point

**Don't use when:**
- ❌ Need reusable synchronization (use CyclicBarrier)
- ❌ Need mutual exclusion (use lock)
- ❌ Need to reset count (create new latch)

---

**Next:** Learn about [Deadlock](12-deadlock.md) to understand and avoid this critical problem!
