# Mutex Package - Synchronization and Race Conditions

## 📦 Package: `com.cp.concurrency.mutex`

## 🎯 The Concept

**Mutex** (Mutual Exclusion) ensures that only one thread can access a shared resource at a time. This prevents **race conditions** where multiple threads modify shared data simultaneously, leading to incorrect results.

Think of it like a bathroom with a lock:
- Only one person can use it at a time
- Others must wait until it's unlocked
- Prevents conflicts and ensures privacy

In programming:
- Multiple threads trying to increment a counter
- Without synchronization: Some increments are lost
- With synchronization: All increments are counted correctly

## 🎯 What We're Trying to Achieve

We want to:
- Prevent race conditions (data corruption)
- Ensure thread-safe access to shared variables
- Make operations atomic (all-or-nothing)
- Maintain data consistency across threads

## 🔧 How We Achieve It

We use the `synchronized` keyword in three ways:
1. **Synchronized Method** - Entire method is synchronized
2. **Synchronized Block** - Only specific code block is synchronized
3. **Static Synchronization** - Synchronizes at class level

## 📝 Code Explanation: SynchronizedMethodExample.java

```java
package com.cp.concurrency.mutex;

public class SynchronizedMethodExample {
    private int count = 0;
```

**Explanation:**
- `count`: Shared variable that will be accessed by multiple threads
- Without synchronization, this would be unsafe!

```java
    public static void main(String[] args) throws InterruptedException {
        SynchronizedMethodExample example = new SynchronizedMethodExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.increment();
            }
        });
```

**Explanation:**
- Creates two threads
- Each thread calls `increment()` 1000 times
- **Expected result**: count should be 2000
- **Without synchronization**: Might be less than 2000 (lost increments!)

```java
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Count: " + example.count);
    }

    public synchronized void increment() {
        count++;
    }
}
```

**Explanation:**
- `synchronized void increment()`: 
  - Only ONE thread can execute this method at a time
  - Other threads wait until the lock is released
  - Ensures `count++` is atomic (can't be interrupted)
- `count++` is actually 3 operations:
  1. Read count
  2. Increment value
  3. Write count back
  - Without synchronization, these can be interleaved between threads!

## 📝 Code Explanation: SynchronizedBlockExample.java

```java
package com.cp.concurrency.mutex;

public class SynchronizedBlockExample {
    private final Object lock = new Object();
    private int count = 0;
```

**Explanation:**
- `lock`: Object used for synchronization
- `count`: Shared variable

```java
    public void increment() {
        synchronized (lock) {
            count++;
        }
    }
```

**Explanation:**
- `synchronized (lock)`: Only this block is synchronized
- Uses explicit lock object
- More flexible than synchronized method
- Other code in the method (if any) can run concurrently

**Why use a separate lock object?**
- More control over what's synchronized
- Can use different locks for different operations
- Better performance (less code is locked)

## 📝 Code Explanation: StaticSynchronizationExample.java

```java
package com.cp.concurrency.mutex;

public class StaticSynchronizationExample {
    private static int count = 0;

    public synchronized static void increment() {
        count++;
    }
```

**Explanation:**
- `static int count`: Class-level variable (shared across all instances)
- `synchronized static void increment()`: 
  - Locks on the **class object**, not instance
  - All instances share the same lock
  - Prevents multiple threads from executing ANY static synchronized method

**Key Difference:**
- Instance method: Locks on `this` (each instance has its own lock)
- Static method: Locks on `Class` object (all instances share one lock)

## 🔄 The Race Condition Problem

### Without Synchronization:
```
Thread 1: Read count (value = 5)
Thread 2: Read count (value = 5)  // Both read same value!
Thread 1: Increment to 6
Thread 2: Increment to 6          // Lost one increment!
Thread 1: Write 6
Thread 2: Write 6                 // Final value is 6, should be 7!
```

### With Synchronization:
```
Thread 1: Acquire lock
Thread 1: Read count (value = 5)
Thread 1: Increment to 6
Thread 1: Write 6
Thread 1: Release lock
Thread 2: Acquire lock (waits until Thread 1 releases)
Thread 2: Read count (value = 6)  // Now reads correct value
Thread 2: Increment to 7
Thread 2: Write 7
Thread 2: Release lock
```

## 💡 Key Concepts

### 1. Intrinsic Lock (Monitor)
- Every Java object has an intrinsic lock
- `synchronized` uses this lock
- Only one thread can hold the lock at a time

### 2. Atomic Operations
- Operations that can't be interrupted
- `synchronized` makes operations atomic
- Prevents partial updates

### 3. Lock Scope
- **Synchronized method**: Entire method is locked
- **Synchronized block**: Only specific code is locked
- Smaller scope = better performance

## 📊 Execution Comparison

### Synchronized Method:
```java
public synchronized void method() {
    // All code here is synchronized
    // Other threads wait for entire method
}
```

### Synchronized Block:
```java
public void method() {
    // This code can run concurrently
    synchronized (lock) {
        // Only this block is synchronized
    }
    // This code can also run concurrently
}
```

## 🎓 Key Takeaways

1. **synchronized prevents race conditions** - Only one thread executes at a time
2. **Synchronized method** - Entire method is locked
3. **Synchronized block** - Only specific code is locked (more flexible)
4. **Static synchronization** - Locks on class, not instance
5. **Every object has a lock** - Used automatically by synchronized

## ⚠️ Important Points

1. **Synchronization has overhead** - Only use when necessary
2. **Deadlock risk** - Multiple locks can cause deadlock
3. **Performance impact** - Locked code runs sequentially
4. **Lock granularity** - Smaller locks = better performance

## 🔄 When to Use

**Use synchronized method when:**
- ✅ Entire method needs to be thread-safe
- ✅ Simple cases
- ✅ Method is short

**Use synchronized block when:**
- ✅ Only part of method needs synchronization
- ✅ Want better performance
- ✅ Need different locks for different operations

**Use static synchronization when:**
- ✅ Working with static variables
- ✅ Need class-level locking

## 📈 Performance Tips

1. **Minimize lock scope** - Use synchronized blocks instead of methods
2. **Avoid locking in loops** - Lock outside the loop if possible
3. **Use separate locks** - Different locks for independent operations
4. **Don't over-synchronize** - Only protect shared data

---

**Next:** Learn about [ReentrantLock](09-reentrant-lock.md) for advanced locking features!
