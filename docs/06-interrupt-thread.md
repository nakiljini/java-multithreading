# Interrupt Thread Package - Gracefully Stopping Threads

## 📦 Package: `com.cp.concurrency.interruptthread`

## 🎯 The Concept

**Thread interruption** is a cooperative mechanism to signal a thread that it should stop what it's doing. Unlike the deprecated `stop()` method which was unsafe, interruption allows threads to:
- Check if they've been interrupted
- Clean up resources before stopping
- Handle the interruption gracefully

Think of it like a polite request to stop rather than forcing someone to stop immediately. The thread can check the request and decide how to respond.

## 🎯 What We're Trying to Achieve

We want to:
- Stop long-running threads gracefully
- Allow threads to clean up before stopping
- Avoid unsafe thread termination
- Handle cancellation of tasks properly
- Use the recommended way to stop threads in Java

## 🔧 How We Achieve It

1. **Check interruption status** - Threads periodically check `isInterrupted()`
2. **Stop when interrupted** - Thread exits its loop when interrupted
3. **Interrupt threads** - Use `shutdownNow()` or `thread.interrupt()`
4. **Handle InterruptedException** - Properly restore interrupt flag

## 📝 Code Explanation: ExecutorServiceInterruptExample.java

```java
package com.cp.concurrency.interruptthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorServiceInterruptExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        AtomicInteger atomicInteger = new AtomicInteger();
```

**Explanation:**
- Creates a thread pool with 5 threads
- `AtomicInteger`: Thread-safe counter (we'll learn about this in advanced topics)

```java
        for (int i = 0; i < 50; i++) {
            executorService.submit(() -> {
                int i1 = 0;
                while (i1 < 100 && !Thread.currentThread().isInterrupted()) {
                    System.out.println("Task Number: " + atomicInteger.get());
                    System.out.println("Execution Number: " + ++i1);
                }
                atomicInteger.getAndIncrement();
            });
        }
```

**Explanation:**
- Submits 50 tasks to the executor
- Each task has a loop that runs up to 100 iterations
- **Key Line**: `!Thread.currentThread().isInterrupted()`
  - `Thread.currentThread()`: Gets the current thread
  - `isInterrupted()`: Checks if this thread has been interrupted
  - Loop continues only if NOT interrupted
  - This is the cooperative check - thread checks and stops itself
- When interrupted, thread exits loop and increments the counter

```java
        executorService.shutdown();
        executorService.shutdownNow();
    }
}
```

**Explanation:**
- `shutdown()`: Stops accepting new tasks
- `shutdownNow()`: Attempts to stop all running tasks by interrupting them
  - Sends interrupt signal to all running threads
  - Threads check `isInterrupted()` and stop gracefully

## 🔄 How Interruption Works

### Step-by-Step Process:

1. **Task is running** - Thread executes the loop
2. **Interrupt signal sent** - `shutdownNow()` sends interrupt
3. **Thread checks status** - `isInterrupted()` returns true
4. **Thread stops** - Loop condition becomes false, thread exits
5. **Cleanup happens** - Thread can clean up before terminating

## 💡 Key Concepts

### 1. Interrupt Flag
- Each thread has an interrupt flag (boolean)
- `interrupt()` sets the flag to true
- `isInterrupted()` checks the flag
- `interrupted()` checks and clears the flag

### 2. Cooperative Cancellation
- Thread must check interruption status
- Thread decides when to stop
- Allows cleanup before stopping

### 3. InterruptedException
- Some methods throw InterruptedException (like `Thread.sleep()`)
- This means the thread was interrupted while waiting
- Always handle it properly!

## 📊 Execution Flow

```
Time 0s:    50 tasks submitted, start executing
            Tasks running loops, checking isInterrupted()

Time 1s:    shutdown() called - stops accepting new tasks
            shutdownNow() called - sends interrupt to all threads

Time 1.001s: All threads check isInterrupted() = true
            Loops exit immediately
            Tasks complete and increment counter
```

## ⚠️ Important: Handling InterruptedException

When a method throws `InterruptedException`, you should:

```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Option 1: Restore interrupt flag and exit
    Thread.currentThread().interrupt();
    return;
    
    // Option 2: Handle and continue (if appropriate)
    // Log the interruption, clean up, etc.
}
```

**Why restore the flag?**
- `InterruptedException` clears the interrupt flag
- Restoring it allows calling code to know thread was interrupted
- This is a best practice!

## 🔄 Old vs New Approach

### ❌ Old (Deprecated - DON'T USE)
```java
thread.stop();  // UNSAFE! Can leave data in inconsistent state
```

### ✅ New (Recommended)
```java
thread.interrupt();  // Safe - thread checks and stops gracefully
// In thread:
while (!Thread.currentThread().isInterrupted()) {
    // Do work
}
```

## 🎓 Key Takeaways

1. **Interruption is cooperative** - Thread must check and stop itself
2. **Always check isInterrupted()** - In loops and long-running operations
3. **Handle InterruptedException** - Restore interrupt flag
4. **shutdownNow() interrupts threads** - Sends interrupt signal to all
5. **Safer than stop()** - Allows cleanup before termination

## ⚠️ Common Mistakes

1. **Not checking interruption** - Thread never stops
2. **Ignoring InterruptedException** - Don't just catch and continue
3. **Calling stop()** - Deprecated and unsafe
4. **Not restoring interrupt flag** - Breaks the interruption chain

## 🔄 When to Use

**Use interruption when:**
- ✅ Need to cancel long-running tasks
- ✅ Want graceful thread termination
- ✅ Need cleanup before stopping
- ✅ Using ExecutorService (shutdownNow() uses interruption)

**Don't use stop()** - It's deprecated and unsafe!

---

**Next:** Learn about [Inter-Thread Communication](07-interthread-communication.md) for thread coordination!
