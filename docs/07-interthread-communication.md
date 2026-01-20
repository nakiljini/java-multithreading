# Inter-Thread Communication Package

## 📦 Package: `com.cp.concurrency.interthread`

## 🎯 The Concept

**Inter-thread communication** allows threads to coordinate and communicate with each other. This is essential when:
- One thread produces data and another consumes it (Producer-Consumer pattern)
- Threads need to wait for certain conditions
- Threads need to signal each other about state changes

Think of it like a restaurant kitchen:
- **Producer (Chef)**: Makes food and puts it on the counter
- **Consumer (Waiter)**: Takes food from the counter to serve customers
- They need to coordinate: Chef waits if counter is full, Waiter waits if counter is empty

## 🎯 What We're Trying to Achieve

We want to:
- Coordinate threads that depend on each other
- Implement Producer-Consumer pattern safely
- Make threads wait and notify each other
- Prevent race conditions in shared data access
- Use thread-safe data structures

## 🔧 How We Achieve It

We have two approaches:
1. **wait() and notify()** - Basic thread coordination using synchronized blocks
2. **BlockingQueue** - High-level thread-safe queue that handles synchronization automatically

## 📝 Code Explanation: ProducerConsumerWithWaitNotify.java

```java
package com.cp.concurrency.interthread;

import java.util.Scanner;

public class ProducerConsumerWithWaitNotify {
    public static void main(String[] args) throws InterruptedException {
        ProducerConsumerWithWaitNotify pcwn = new ProducerConsumerWithWaitNotify();
```

**Explanation:**
- Creates an instance to use as the lock object for synchronization

```java
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pcwn.producer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
```

**Explanation:**
- Creates producer thread that calls `producer()` method

```java
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pcwn.consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
```

**Explanation:**
- Creates consumer thread that calls `consumer()` method
- Starts both threads
- `join()` waits for threads to complete

```java
    private void producer() throws InterruptedException {
        synchronized (this) {
            System.out.println("Producer Running");
            wait();
            System.out.println("Resumed");
        }
    }
```

**Explanation:**
- `synchronized (this)`: Acquires lock on this object
- **CRITICAL**: wait() must be called inside synchronized block!
- `wait()`: 
  - Releases the lock (other threads can now enter)
  - Puts current thread to sleep
  - Waits until another thread calls `notify()` or `notifyAll()`
- When notified, thread wakes up, re-acquires lock, and continues
- Prints "Resumed" after being woken up

```java
    private void consumer() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Thread.sleep(1000);
        synchronized (this) {
            System.out.println("Waiting for user intervention");
            scanner.nextLine();
            System.out.println("Key Pressed");
            notify();
        }
    }
}
```

**Explanation:**
- Waits 1 second (gives producer time to start and wait)
- `synchronized (this)`: Acquires same lock
- Waits for user to press Enter
- `notify()`: Wakes up ONE waiting thread (the producer)
- **CRITICAL**: notify() must be called inside synchronized block!

## 📝 Code Explanation: ProducerConsumerWithBlockingQueue.java

```java
package com.cp.concurrency.interthread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerWithBlockingQueue {
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
```

**Explanation:**
- `BlockingQueue<Integer>`: Thread-safe queue that blocks when empty/full
- `ArrayBlockingQueue<>(10)`: Queue with capacity of 10
- If queue is full, `put()` blocks
- If queue is empty, `take()` blocks

```java
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                producer();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
```

**Explanation:**
- Creates producer and consumer threads
- Starts both threads concurrently

```java
    public static void producer() {
        Random random = new Random();
        while (true) {
            queue.offer(random.nextInt(100));
        }
    }
```

**Explanation:**
- `producer()`: Continuously produces random numbers
- `queue.offer(random.nextInt(100))`: 
  - `offer()`: Adds element to queue (non-blocking)
  - If queue is full, returns false (doesn't block)
  - In this example, queue might fill up quickly
- Runs in infinite loop

```java
    public static void consumer() throws InterruptedException {
        Random random = new Random();
        while (true) {
            Thread.sleep(1000);
            if (random.nextInt(10) == 0) {
                Integer value = queue.take();
                System.out.println("Taken value :=" + value + "  ,Size of Queue : =" + queue.size());
            }
        }
    }
}
```

**Explanation:**
- `consumer()`: Consumes numbers from queue
- Waits 1 second between checks
- 10% chance to consume (random.nextInt(10) == 0)
- `queue.take()`: 
  - **Blocks** if queue is empty
  - Returns and removes element when available
  - Thread-safe - no manual synchronization needed!

## 🔄 wait() and notify() Rules

### ⚠️ CRITICAL Rules:

1. **Must be in synchronized block**
   ```java
   synchronized (lock) {
       wait();  // ✅ OK
   }
   wait();  // ❌ IllegalMonitorStateException!
   ```

2. **wait() releases the lock**
   - Other threads can enter synchronized block
   - When notified, thread must re-acquire lock before continuing

3. **notify() wakes ONE thread**
   - `notify()`: Wakes one waiting thread (randomly chosen)
   - `notifyAll()`: Wakes ALL waiting threads

4. **Same lock object required**
   - Producer and consumer must use the same lock object

## 💡 BlockingQueue Advantages

### Why BlockingQueue is Better:

1. **No manual synchronization** - Handles it automatically
2. **Less code** - Simpler and cleaner
3. **Built-in blocking** - `take()` and `put()` handle waiting
4. **Thread-safe** - Designed for concurrent access
5. **Less error-prone** - Can't forget synchronized blocks

### Comparison:

**wait/notify approach:**
```java
synchronized (lock) {
    while (queue.isEmpty()) {
        wait();  // Manual wait
    }
    item = queue.remove();
}
```

**BlockingQueue approach:**
```java
item = queue.take();  // Automatic blocking!
```

## 📊 Execution Flow: wait/notify

```
Time 0s:    Producer starts, acquires lock, calls wait()
            Producer releases lock, goes to sleep

Time 1s:    Consumer starts, waits 1 second
            Consumer acquires lock, waits for user input

User presses Enter:
            Consumer calls notify()
            Producer wakes up, waits to re-acquire lock
            Consumer releases lock
            Producer acquires lock, prints "Resumed"
```

## 📊 Execution Flow: BlockingQueue

```
Time 0s:    Producer starts adding numbers
            Queue fills up quickly (capacity 10)

Time 1s:    Consumer wakes up, 10% chance to consume
            If queue not empty: takes number, prints it
            If queue empty: blocks until number available

Ongoing:    Producer adds numbers (queue.offer - non-blocking)
            Consumer randomly takes numbers (queue.take - blocking)
```

## 🎓 Key Takeaways

1. **wait/notify requires synchronized** - Must be in synchronized block
2. **wait() releases lock** - Allows other threads to proceed
3. **notify() wakes one thread** - notifyAll() wakes all
4. **BlockingQueue is easier** - Handles synchronization automatically
5. **Producer-Consumer pattern** - Common pattern for thread coordination

## ⚠️ Common Mistakes

1. **Calling wait/notify without synchronized** - IllegalMonitorStateException
2. **Using different lock objects** - Threads won't communicate
3. **Forgetting to notify** - Thread waits forever
4. **Race conditions** - Check condition in loop, not if statement

## 🔄 When to Use

**Use wait/notify when:**
- ✅ Learning basic thread coordination
- ✅ Need fine-grained control
- ✅ Simple coordination scenarios

**Use BlockingQueue when:**
- ✅ Producer-Consumer pattern
- ✅ Want simpler code
- ✅ Production code (recommended)
- ✅ Need thread-safe queue

---

**Next:** Learn about [Mutex and Synchronization](08-mutex-synchronization.md) to prevent race conditions!
