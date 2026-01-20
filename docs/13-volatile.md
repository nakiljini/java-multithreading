# Volatile Package - Variable Visibility

## 📦 Package: `com.cp.concurrency.volatiles`

## 🎯 The Concept

**Volatile** is a keyword in Java that ensures changes to a variable are immediately visible to all threads. Without volatile, threads might cache variable values in their local memory, causing one thread's changes to be invisible to other threads.

Think of it like a shared whiteboard:
- **Without volatile**: Each person has their own copy - changes aren't seen by others
- **With volatile**: Everyone sees the same whiteboard - changes are immediately visible

**Important**: Volatile ensures **visibility**, not **atomicity**. For atomic operations, use `synchronized` or `AtomicInteger`.

## 🎯 What We're Trying to Achieve

We want to:
- Ensure variable changes are visible to all threads immediately
- Prevent threads from caching stale values
- Understand when volatile is needed
- See the difference between volatile and non-volatile variables

## 🔧 How We Achieve It

1. **Declare variable as volatile** - Add `volatile` keyword
2. **Thread reads variable** - Gets latest value from main memory
3. **Thread writes variable** - Immediately writes to main memory
4. **All threads see changes** - No caching, always fresh value

## 📝 Code Explanation: VolatileExample.java

```java
package com.cp.concurrency.volatiles;

public class VolatileExample {
    private volatile Integer number = 0; // Using volatile keyword
```

**Explanation:**
- `volatile Integer number`: 
  - `volatile` keyword ensures visibility
  - Changes are immediately visible to all threads
  - Threads don't cache this variable locally

```java
    public static void main(String[] args) {
        VolatileExample volatileObject = new VolatileExample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 200000; i++) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(volatileObject.number);
                }
            }
        }).start();
```

**Explanation:**
- Creates a thread that continuously reads `number`
- Prints the value 200,000 times
- With `volatile`: Thread will see changes immediately

```java
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        volatileObject.number = 5;
        System.out.println("number changed");
    }
}
```

**Explanation:**
- Main thread waits 1 second
- Then changes `number` to 5
- **With volatile**: Reading thread should see this change immediately
- **Without volatile**: Reading thread might not see the change (cached value)

## 📝 Code Explanation: NonVolatileExample.java

```java
package com.cp.concurrency.volatiles;

public class NonVolatileExample {
    private Integer number = 0; // No volatile keyword
```

**Explanation:**
- Same code but **without** `volatile` keyword
- Threads might cache the value locally
- Changes might not be visible immediately

```java
    // ... same main method structure ...
```

**Explanation:**
- Same structure as VolatileExample
- But reading thread might not see the change to 5
- Might keep printing 0 (cached value)

## 🔄 Volatile vs Non-Volatile

### Without Volatile (NonVolatileExample):
```
Thread 1 (Reading):           Main Thread:
┌─────────────────┐          ┌─────────────────┐
│ Cached: 0        │          │ number = 0      │
│ (local cache)    │          │                 │
│                  │          │ number = 5      │
│ Still sees: 0    │◄─────────┤ (change)        │
│ (stale value!)   │          │                 │
└─────────────────┘          └─────────────────┘
```

### With Volatile (VolatileExample):
```
Thread 1 (Reading):           Main Thread:
┌─────────────────┐          ┌─────────────────┐
│ Reads from      │          │ number = 0      │
│ main memory     │          │                 │
│                 │          │ number = 5      │
│ Sees: 5         │◄─────────┤ (change)        │
│ (fresh value!)  │          │                 │
└─────────────────┘          └─────────────────┘
```

## 💡 Key Concepts

### 1. Visibility
- **Volatile ensures visibility**: All threads see the latest value
- **Without volatile**: Threads might see stale/cached values
- Changes are written directly to main memory

### 2. Not Atomicity
- **Volatile does NOT make operations atomic**
- `count++` is still NOT thread-safe with volatile!
- Use `synchronized` or `AtomicInteger` for atomicity

### 3. When to Use Volatile
- Simple flags (boolean done = false)
- Status variables
- Variables written by one thread, read by others
- **NOT for**: Counters, accumulators (need atomicity)

### 4. Performance
- Volatile has minimal performance impact
- Forces reads/writes to main memory (slightly slower than cache)
- But much faster than synchronized

## 📊 Example: Volatile for Flag

```java
private volatile boolean running = true;

// Thread 1
while (running) {
    // Do work
}

// Thread 2
running = false;  // Thread 1 will see this immediately!
```

## ⚠️ Common Misconceptions

### ❌ Wrong: Volatile makes operations atomic
```java
private volatile int count = 0;
count++;  // Still NOT thread-safe! Need synchronized or AtomicInteger
```

### ✅ Correct: Volatile ensures visibility
```java
private volatile boolean flag = false;
flag = true;  // All threads see this immediately
```

## 🎓 Key Takeaways

1. **Volatile = Visibility** - Ensures all threads see latest value
2. **NOT Atomicity** - Doesn't make operations atomic
3. **Use for flags** - Simple boolean/status variables
4. **Don't use for counters** - Need synchronized or AtomicInteger
5. **Minimal performance cost** - Faster than synchronized

## 🔄 Volatile vs Synchronized

### Volatile:
- Ensures visibility only
- No locking overhead
- Can't make operations atomic
- Use for simple flags

### Synchronized:
- Ensures visibility AND atomicity
- Has locking overhead
- Makes operations atomic
- Use for critical sections

## 🔄 When to Use

**Use volatile when:**
- ✅ Simple flag (boolean done)
- ✅ Status variable
- ✅ One writer, multiple readers
- ✅ Don't need atomicity

**Don't use volatile when:**
- ❌ Need atomic operations (use synchronized)
- ❌ Multiple writers (use synchronized)
- ❌ Complex operations (use synchronized)

## 📝 Best Practice Example

```java
// ✅ Good use of volatile
private volatile boolean shutdown = false;

public void run() {
    while (!shutdown) {
        // Do work
    }
}

public void stop() {
    shutdown = true;  // All threads see this immediately
}

// ❌ Bad use of volatile (needs atomicity)
private volatile int count = 0;
public void increment() {
    count++;  // NOT thread-safe! Use AtomicInteger instead
}
```

---

**Congratulations!** You've completed all the multithreading concepts! 🎉

**Review:** Go back to [README](README.md) to see the complete overview and continue learning!
