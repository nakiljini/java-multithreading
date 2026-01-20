# Runnables Package - Using Runnable Interface

## 📦 Package: `com.cp.concurrency.runnables`

## 🎯 The Concept

The **Runnable interface** is a functional interface in Java that represents a task that can be executed by a thread. It's a better alternative to extending the Thread class because:
- Java supports only single inheritance - if you extend Thread, you can't extend another class
- Runnable is more flexible and follows better design principles
- Runnable separates the task (what to do) from the thread (how to execute)

## 🎯 What We're Trying to Achieve

We want to create threads in a more flexible way that:
- Allows our class to extend other classes if needed
- Separates the task definition from thread creation
- Follows better object-oriented design principles
- Is the recommended approach for most multithreading scenarios

## 🔧 How We Achieve It

Instead of extending Thread, we:
1. **Implement Runnable interface** - Create a class that implements `Runnable`
2. **Implement the run() method** - Put the code you want to execute
3. **Pass Runnable to Thread constructor** - Create a Thread object and pass our Runnable
4. **Call start() method** - Start the thread execution

## 📝 Code Explanation: RunnableTaskExample.java

```java
package com.cp.concurrency.runnables;

class RunnableTask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello from RunnableTask: " + i);
        }
    }
}
```

**Explanation:**
- `class RunnableTask implements Runnable`: We create a class that implements the `Runnable` interface. This is just a regular class - it's NOT a thread itself.
- `implements Runnable`: This means our class must provide an implementation for the `run()` method.
- `public void run()`: This method contains the code that will execute when the thread runs.
- The `for` loop: The actual work - printing numbers 0 to 9.

```java
public class RunnableTaskExample {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnableTask());
        thread1.start();
        
        Thread thread2 = new Thread(new RunnableTask());
        thread2.start();
    }
}
```

**Explanation:**
- `new RunnableTask()`: We create an instance of our Runnable class. This is just a regular object, not a thread.
- `new Thread(new RunnableTask())`: We create a Thread object and pass our RunnableTask to it. The Thread will execute the `run()` method of RunnableTask.
- `thread1.start()`: This starts the thread, which will call the `run()` method of RunnableTask in a new thread.
- We create two threads, each with its own RunnableTask instance, so both run concurrently.

## 🔄 Comparison: Thread vs Runnable

### Extending Thread (Previous approach)
```java
class CustomThread extends Thread {
    public void run() { /* code */ }
}
CustomThread t = new CustomThread();
t.start();
```

**Limitation:** Can't extend another class!

### Implementing Runnable (Current approach)
```java
class RunnableTask implements Runnable {
    public void run() { /* code */ }
}
Thread t = new Thread(new RunnableTask());
t.start();
```

**Advantage:** Can still extend another class!

## 💡 Why Runnable is Better

1. **Flexibility**: Your class can extend another class
   ```java
   class MyTask extends SomeClass implements Runnable {
       // Can extend AND implement!
   }
   ```

2. **Separation of Concerns**: 
   - Runnable = What to do (the task)
   - Thread = How to execute (the mechanism)

3. **Reusability**: Same Runnable can be used with different thread types
   ```java
   RunnableTask task = new RunnableTask();
   Thread t1 = new Thread(task);
   ExecutorService executor = Executors.newFixedThreadPool(5);
   executor.submit(task);  // Can reuse with ExecutorService too!
   ```

4. **Recommended by Java**: This is the preferred approach in Java documentation

## 📊 Expected Output

Similar to the Thread example, output will be interleaved:
```
Hello from RunnableTask: 0
Hello from RunnableTask: 0
Hello from RunnableTask: 1
Hello from RunnableTask: 1
...
```

## 🎓 Key Takeaways

1. **Runnable is an interface** - It defines what to do, not how to execute
2. **Thread is the executor** - It takes a Runnable and runs it in a new thread
3. **Always prefer Runnable** - More flexible and follows better design
4. **Same Runnable can be reused** - Can be passed to different Thread objects or ExecutorService

## 🔄 When to Use

**Always use Runnable when:**
- ✅ Production code
- ✅ You might need to extend another class
- ✅ You want better code organization
- ✅ You plan to use ExecutorService (it works with Runnable)

**Use Thread extension only when:**
- Learning basic concepts
- Quick prototyping
- You're sure you won't need to extend another class

---

**Next:** Learn about [Anonymous inner classes](03-anonymous.md) for quick thread creation!
