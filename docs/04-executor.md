# Executor Package - ExecutorService and Thread Pools

## 📦 Package: `com.cp.concurrency.executor`

## 🎯 The Concept

**ExecutorService** is Java's high-level API for managing threads. Instead of manually creating and managing threads, ExecutorService provides a thread pool that:
- Reuses threads (more efficient than creating new ones)
- Manages thread lifecycle automatically
- Controls how many threads run simultaneously
- Handles task queuing and execution

Think of it like a restaurant with a fixed number of waiters (thread pool). When customers (tasks) arrive, they're assigned to available waiters. If all waiters are busy, customers wait in line. This is much more efficient than hiring a new waiter for each customer!

## 🎯 What We're Trying to Achieve

We want to:
- Execute multiple tasks efficiently
- Avoid the overhead of creating/destroying threads repeatedly
- Control resource usage (limit concurrent threads)
- Write cleaner, production-ready code
- Properly manage thread lifecycle (start, execute, shutdown)

## 🔧 How We Achieve It

We use ExecutorService which:
1. **Creates a thread pool** - A fixed number of reusable threads
2. **Submits tasks** - Tasks are added to a queue
3. **Automatically assigns tasks** - Threads pick up tasks from the queue
4. **Manages execution** - Handles all the thread management for us
5. **Properly shuts down** - Ensures all tasks complete before termination

## 📝 Code Explanation: ExecutorServiceExample.java

```java
package com.cp.concurrency.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
```

**Explanation:**
- `ExecutorService`: Interface for managing thread execution
- `Executors.newFixedThreadPool(5)`: Creates a thread pool with exactly 5 threads
  - These 5 threads will be reused for all tasks
  - If more than 5 tasks are submitted, they wait in a queue
  - Much more efficient than creating 50 separate threads!

```java
        for (int i = 0; i < 50; i++) {
            executorService.submit(() -> {
                System.out.println("Doing through executor");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + e.getMessage());
                }
            });
        }
```

**Explanation:**
- `for (int i = 0; i < 50; i++)`: We're submitting 50 tasks
- `executorService.submit(() -> { ... })`: 
  - `submit()`: Adds a task to the thread pool
  - `() -> { ... }`: Lambda expression (Runnable) - the task to execute
  - The task prints a message and sleeps for 500ms
- **Key Point**: We submit 50 tasks, but only 5 threads will execute them!
  - First 5 tasks start immediately
  - Remaining 45 tasks wait in queue
  - As threads finish, they pick up the next task from queue
- `Thread.currentThread().interrupt()`: Properly handles interruption by restoring the interrupt flag

```java
        executorService.shutdown();
```

**Explanation:**
- `shutdown()`: Tells the executor to stop accepting new tasks
- Tasks already submitted will continue to execute
- This is important - without shutdown, the program won't terminate!

```java
        try {
            if (executorService.awaitTermination(5, TimeUnit.DAYS)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Timeout reached before completion.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
    }
}
```

**Explanation:**
- `awaitTermination(5, TimeUnit.DAYS)`: Waits for all tasks to complete
  - Maximum wait time: 5 days (very generous timeout)
  - Returns `true` if all tasks completed, `false` if timeout reached
- This ensures the main thread waits for all tasks before the program ends
- Proper exception handling for interruption

## 🔄 Thread Pool vs Manual Thread Creation

### Manual Thread Creation (Inefficient)
```java
for (int i = 0; i < 50; i++) {
    Thread t = new Thread(() -> { /* task */ });
    t.start();  // Creates 50 threads!
}
```
**Problems:**
- Creates 50 threads (resource intensive)
- No control over concurrent execution
- Threads are destroyed after use (wasteful)

### Thread Pool (Efficient)
```java
ExecutorService executor = Executors.newFixedThreadPool(5);
for (int i = 0; i < 50; i++) {
    executor.submit(() -> { /* task */ });
}
```
**Benefits:**
- Only 5 threads (reused for all 50 tasks)
- Controlled concurrent execution
- Threads are reused (efficient)

## 💡 Key Concepts

### 1. Thread Pool
A collection of reusable threads. Instead of creating a new thread for each task, threads are reused.

### 2. Task Queue
When all threads are busy, new tasks wait in a queue until a thread becomes available.

### 3. Fixed Thread Pool
- Exactly N threads (5 in our example)
- Threads are created once and reused
- Best for CPU-intensive tasks

### 4. Other Thread Pool Types
- `newCachedThreadPool()`: Creates threads as needed, reuses them
- `newSingleThreadExecutor()`: Only one thread (sequential execution)
- `newScheduledThreadPool()`: For scheduled/repeated tasks

## 📊 Execution Flow

```
Time 0: Submit 50 tasks
├─ Tasks 1-5: Start immediately (5 threads available)
└─ Tasks 6-50: Wait in queue

Time 500ms: Tasks 1-5 complete
├─ Tasks 6-10: Start (picked up by available threads)
└─ Tasks 11-50: Still waiting

Time 1000ms: Tasks 6-10 complete
├─ Tasks 11-15: Start
└─ ... and so on
```

## 🎓 Key Takeaways

1. **ExecutorService manages threads for you** - No manual thread creation needed
2. **Thread pools are efficient** - Reuse threads instead of creating new ones
3. **Always shutdown ExecutorService** - Use `shutdown()` and `awaitTermination()`
4. **Control concurrency** - Fixed thread pool limits simultaneous execution
5. **Production-ready approach** - This is the recommended way in real applications

## ⚠️ Important Points

1. **Always call shutdown()** - Otherwise threads keep running
2. **Use awaitTermination()** - Wait for tasks to complete
3. **Handle InterruptedException** - Properly restore interrupt flag
4. **Choose pool size wisely** - Too many threads waste resources, too few cause delays

## 🔄 When to Use

**Use ExecutorService when:**
- ✅ Production code
- ✅ Need to execute multiple tasks
- ✅ Want efficient resource usage
- ✅ Need control over concurrent execution
- ✅ Professional application development

**Don't use manual threads when:**
- ❌ You need fine-grained control (rare cases)
- ❌ Learning basic concepts (use Thread/Runnable first)

---

**Next:** Learn about [Callable and Future](05-callable-future.md) for tasks that return values!
