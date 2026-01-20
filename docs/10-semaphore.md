# Semaphore Package - Controlling Resource Access

## 📦 Package: `com.cp.concurrency.semaphore`

## 🎯 The Concept

**Semaphore** is a synchronization mechanism that controls access to a resource by limiting the number of threads that can access it simultaneously. Think of it like a parking lot with limited spaces:
- Parking lot has 10 spaces (permits)
- 50 cars (threads) want to park
- Only 10 cars can park at once
- When a car leaves, another can enter
- Others must wait until a space is available

In programming:
- Database connection pool (limit concurrent connections)
- API rate limiting (limit requests per second)
- Resource access control (limit concurrent access)

## 🎯 What We're Trying to Achieve

We want to:
- Limit how many threads can access a resource at the same time
- Control concurrent access to shared resources
- Prevent resource exhaustion
- Manage limited resources efficiently

## 🔧 How We Achieve It

1. **Create Semaphore with permits** - Specify how many threads can access simultaneously
2. **Acquire permit** - Thread calls `acquire()` to get a permit (blocks if none available)
3. **Use the resource** - Thread does its work
4. **Release permit** - Thread calls `release()` to return the permit

## 📝 Code Explanation: SemaphoreExample.java

```java
package com.cp.concurrency.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    Semaphore semaphore = new Semaphore(10);
```

**Explanation:**
- `Semaphore semaphore = new Semaphore(10)`: 
  - Creates a semaphore with 10 permits
  - Only 10 threads can acquire permits simultaneously
  - Other threads must wait

```java
    public static void main(String[] args) {
        SemaphoreExample se = new SemaphoreExample();
        for (int i = 0; i < 50; i++) {
```

**Explanation:**
- Creates 50 threads
- But semaphore only allows 10 at a time!

```java
            Thread t1 = new Thread(() -> {
                try {
                    se.semaphore.acquire();
                    System.out.println("Acquired semaphore");
                    System.out.println("size of semaphore" + se.semaphore.availablePermits());
```

**Explanation:**
- `semaphore.acquire()`: 
  - Tries to get a permit
  - If permit available: Gets it and continues
  - If no permit available: **Blocks** until one becomes available
  - Thread waits here if all 10 permits are taken
- `availablePermits()`: Returns how many permits are currently available
  - Initially: 10
  - After first thread acquires: 9
  - After 10 threads acquire: 0 (others must wait)

```java
                    Thread.sleep(5000);
                    se.semaphore.release();
                    System.out.println("Released semaphore");
                    System.out.println("size of semaphore after Released" + se.semaphore.availablePermits());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            t1.start();
        }
    }
}
```

**Explanation:**
- `Thread.sleep(5000)`: Simulates work (5 seconds)
- `semaphore.release()`: 
  - Returns the permit
  - Makes it available for waiting threads
  - One waiting thread can now acquire it
- After release, `availablePermits()` increases by 1

## 🔄 How Semaphore Works

### Step-by-Step Execution:

```
Time 0s:    50 threads try to acquire permits
            First 10 threads: Successfully acquire (permits: 0)
            Remaining 40 threads: Block and wait

Time 5s:    First 10 threads finish, release permits
            Permits available: 10
            Next 10 waiting threads: Acquire permits
            Remaining 30 threads: Still waiting

Time 10s:   Next 10 threads finish, release permits
            Next 10 waiting threads: Acquire permits
            ... and so on
```

## 💡 Key Concepts

### 1. Permits
- Semaphore has a fixed number of permits
- Thread needs a permit to proceed
- Permits are returned when thread is done

### 2. Blocking Behavior
- `acquire()`: Blocks if no permit available
- `release()`: Makes permit available for waiting threads
- Threads wait in FIFO order (fair semaphore)

### 3. Available Permits
- `availablePermits()`: Current number of available permits
- Useful for monitoring and debugging

### 4. Fair vs Unfair
- `new Semaphore(10, true)`: Fair (FIFO ordering)
- `new Semaphore(10, false)`: Unfair (default, better performance)

## 📊 Visual Representation

```
Semaphore (10 permits)

Thread 1  ──┐
Thread 2  ──┤
Thread 3  ──┤
...        ├──> [Permits: 10] ──> Resource
Thread 10 ──┘
            │
Thread 11 ──┴──> [WAITING] (no permit available)
Thread 12 ──┴──> [WAITING]
...
Thread 50 ──┴──> [WAITING]

When Thread 1 releases:
Thread 11 ──> [Gets permit] ──> Resource
```

## 🎓 Key Takeaways

1. **Semaphore limits concurrent access** - Controls how many threads at once
2. **acquire() blocks** - Waits if no permit available
3. **release() frees permit** - Makes it available for others
4. **Useful for resource pools** - Database connections, API limits, etc.
5. **Always release** - In finally block to ensure release

## ⚠️ Important Points

1. **Always release in finally block**
   ```java
   try {
       semaphore.acquire();
       // Use resource
   } finally {
       semaphore.release();  // Always release!
   }
   ```

2. **Don't forget to release** - Permits won't be available for others

3. **Can acquire multiple permits**
   ```java
   semaphore.acquire(3);  // Acquires 3 permits
   semaphore.release(3);  // Releases 3 permits
   ```

## 🔄 Real-World Examples

### 1. Database Connection Pool
```java
Semaphore dbConnections = new Semaphore(10);
// Only 10 database connections at a time
```

### 2. API Rate Limiting
```java
Semaphore apiCalls = new Semaphore(100);
// Maximum 100 API calls per minute
```

### 3. File Access Control
```java
Semaphore fileAccess = new Semaphore(5);
// Only 5 threads can access files simultaneously
```

## 🔄 When to Use

**Use Semaphore when:**
- ✅ Need to limit concurrent access to a resource
- ✅ Have a resource pool (connections, files, etc.)
- ✅ Need to control rate limiting
- ✅ Want to prevent resource exhaustion

**Don't use when:**
- ❌ Need mutual exclusion (use synchronized or ReentrantLock)
- ❌ Only one thread should access (use lock instead)

---

**Next:** Learn about [CountDownLatch](11-countdownlatch.md) for waiting for multiple threads!
