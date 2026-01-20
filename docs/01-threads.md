# Threads Package - Basic Thread Creation

## 📦 Package: `com.cp.concurrency.threads`

## 🎯 The Concept

**Threads** are the smallest unit of execution in Java. A thread is like a separate path of execution that can run concurrently with other threads. Think of threads as workers in a factory - each worker can do their job independently while others work simultaneously.

## 🎯 What We're Trying to Achieve

We want to create multiple threads that can execute code simultaneously. This allows our program to:
- Perform multiple tasks at the same time
- Make better use of CPU resources
- Create responsive applications

## 🔧 How We Achieve It

The simplest way to create a thread in Java is by:
1. **Extending the Thread class** - Create a class that inherits from `Thread`
2. **Override the run() method** - Put the code you want to execute in this method
3. **Create thread objects** - Instantiate your custom thread class
4. **Call start() method** - This starts the thread execution (NOT `run()`!)

## 📝 Code Explanation: CustomThreadExample.java

```java
package com.cp.concurrency.threads;

class CustomThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello from run method: " + i);
        }
    }
}
```

**Explanation:**
- `class CustomThread extends Thread`: We create a class that extends the `Thread` class. This gives us thread capabilities.
- `@Override public void run()`: We override the `run()` method. This is where we put the code that will execute when the thread runs.
- The `for` loop: This is the actual work the thread will do - printing numbers 0 to 9.

```java
public class CustomThreadExample {
    public static void main(String[] args) {
        CustomThread thread1 = new CustomThread();
        thread1.start();
        
        CustomThread thread2 = new CustomThread();
        thread2.start();
    }
}
```

**Explanation:**
- `CustomThread thread1 = new CustomThread()`: We create a new thread object. At this point, the thread is created but NOT running yet.
- `thread1.start()`: This is crucial! We call `start()` which:
  - Creates a new thread of execution
  - Calls the `run()` method in that new thread
  - Returns immediately (doesn't wait for run() to finish)
- We create two threads, so both will run concurrently.

## ⚠️ Important Points

1. **Never call `run()` directly!** 
   - `thread1.run()` - This runs in the MAIN thread (no new thread created)
   - `thread1.start()` - This creates a NEW thread and runs `run()` in it

2. **Thread execution is non-deterministic**
   - You can't predict which thread prints first
   - Output from thread1 and thread2 will be interleaved randomly

3. **Each thread has its own execution path**
   - Thread1 counts 0-9 independently
   - Thread2 counts 0-9 independently
   - They don't interfere with each other

## 📊 Expected Output

The output will be mixed/interleaved, something like:
```
Hello from run method: 0
Hello from run method: 0
Hello from run method: 1
Hello from run method: 1
Hello from run method: 2
...
```

Notice how both threads start at 0 and run simultaneously!

## 🔄 When to Use This Approach

**Use when:**
- Learning basic threading concepts
- Simple scenarios where you just need a basic thread
- Quick prototyping

**Don't use when:**
- You need to extend another class (Java doesn't support multiple inheritance)
- Production code (prefer Runnable interface - see next section)
- You need more flexibility

## 🎓 Key Takeaways

1. Extending `Thread` is the simplest way to create threads
2. Always override `run()` method with your code
3. Always call `start()`, never `run()` directly
4. Multiple threads run concurrently and independently

---

**Next:** Learn about the [Runnable interface approach](02-runnables.md) which is preferred in most cases!
