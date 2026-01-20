# Callable & Future Package - Returning Values from Threads

## 📦 Package: `com.cp.concurrency.callablefuture`

## 🎯 The Concept

**Callable** is like Runnable, but it can return a value and throw checked exceptions. **Future** is a placeholder that represents the result of an asynchronous computation - you submit a task and get a Future object that will eventually contain the result.

Think of it like ordering food online:
- You place an order (submit Callable task)
- You get an order number (Future object)
- You can continue doing other things (main thread continues)
- Later, you check if your order is ready (future.isDone())
- Finally, you get your food (future.get() - the result)

## 🎯 What We're Trying to Achieve

We want to:
- Execute tasks that return values (Runnable can't return values)
- Get results from asynchronous operations
- Continue working while the task executes
- Handle exceptions from the task
- Wait for results when needed

## 🔧 How We Achieve It

1. **Create a Callable** - Implement the `call()` method that returns a value
2. **Submit to ExecutorService** - Get a Future object back
3. **Continue working** - Main thread doesn't block
4. **Get the result** - Call `future.get()` when you need the value (blocks until ready)

## 📝 Code Explanation: CallableExample.java

```java
package com.cp.concurrency.callablefuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
```

**Explanation:**
- Standard ExecutorService setup with 1 thread

```java
        executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Calling Method with callable anonymous class call");
                Thread.sleep(5000);
                return 5;
            }
        });
```

**Explanation:**
- `executorService.submit(new Callable<Integer>() { ... })`: 
  - `Callable<Integer>`: A task that returns an Integer
  - `call()`: Method that returns a value (unlike `run()` which returns void)
  - `throws Exception`: Can throw checked exceptions (Runnable can't)
  - `return 5`: Returns the value 5
- This is using anonymous class syntax

```java
        executorService.submit(() -> {
            System.out.println("Calling Method with lambda call");
            Thread.sleep(5000);
            return 5;
        });
```

**Explanation:**
- Same thing but using lambda expression (more concise)
- Lambda automatically infers it's a Callable because it returns a value

```java
        executorService.shutdown();
    }
}
```

## 📝 Code Explanation: FutureExample.java

```java
package com.cp.concurrency.callablefuture;

import java.util.concurrent.*;

public class FutureExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(() -> {
            Thread.sleep(5000);
            return 5;
        });
```

**Explanation:**
- `Future<Integer> future`: This is the key! Future is a placeholder for the result
- `executorService.submit(...)`: Returns a Future object immediately
- The task runs in background (sleeps 5 seconds, then returns 5)
- Main thread continues immediately (doesn't wait)

```java
        System.out.println("Main Thread Running");
        executorService.shutdown();
```

**Explanation:**
- Main thread prints and continues - it doesn't wait for the task!
- This demonstrates asynchronous execution

```java
        while (!future.isDone()) {
            System.out.println("Main Thread waiting for the response");
            Thread.sleep(1000);
        }
        System.out.println("Main Thread got the response");
```

**Explanation:**
- `future.isDone()`: Checks if the task has completed
- Main thread polls (checks repeatedly) until task is done
- This is one way to wait for the result

```java
        try {
            System.out.println(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
```

**Explanation:**
- `future.get()`: **Blocks** until the result is available, then returns it
- If the task threw an exception, it's wrapped in ExecutionException
- This is the most common way to get the result

```java
        // Get with timeout
        try {
            future.get(5, TimeUnit.MINUTES);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
```

**Explanation:**
- `future.get(5, TimeUnit.MINUTES)`: Get result with timeout
- If result isn't ready in 5 minutes, throws TimeoutException
- Prevents waiting forever

## 🔄 Runnable vs Callable

### Runnable
```java
Runnable task = () -> {
    // Do work
    // Can't return value
    // Can't throw checked exceptions
};
Future<?> future = executor.submit(task);
// future.get() returns null
```

### Callable
```java
Callable<Integer> task = () -> {
    // Do work
    return 42;  // Can return value!
    // Can throw checked exceptions
};
Future<Integer> future = executor.submit(task);
Integer result = future.get();  // Gets the value!
```

## 💡 Key Concepts

### 1. Future Object
- A placeholder for a result that will be available later
- Returned immediately when you submit a Callable
- Represents the "future" result of the computation

### 2. Asynchronous Execution
- Task runs in background
- Main thread continues immediately
- Get result later when needed

### 3. Blocking vs Non-Blocking
- `future.isDone()`: Non-blocking (returns immediately)
- `future.get()`: Blocking (waits until result ready)

### 4. Exception Handling
- Exceptions from Callable are wrapped in ExecutionException
- Always handle ExecutionException when calling get()

## 📊 Execution Timeline

```
Time 0s:    Submit task, get Future immediately
            Main thread: "Main Thread Running"
            Task thread: Starts sleeping (5 seconds)

Time 1s:    Main thread: "Main Thread waiting..."
            Task thread: Still sleeping

Time 2s:    Main thread: "Main Thread waiting..."
            Task thread: Still sleeping

...

Time 5s:    Task thread: Wakes up, returns 5
            Main thread: future.isDone() = true
            Main thread: "Main Thread got the response"
            Main thread: future.get() returns 5
            Main thread: Prints "5"
```

## 🎓 Key Takeaways

1. **Callable returns values** - Runnable can't return values
2. **Future is a placeholder** - Get it immediately, value comes later
3. **get() blocks** - Waits until result is ready
4. **isDone() checks status** - Non-blocking way to check completion
5. **Handle ExecutionException** - Exceptions from Callable are wrapped

## ⚠️ Important Points

1. **future.get() blocks** - Main thread waits until result is ready
2. **Use timeout** - `get(timeout)` prevents waiting forever
3. **Handle exceptions** - Always catch ExecutionException
4. **Don't call get() multiple times** - Result is cached after first call

## 🔄 When to Use

**Use Callable/Future when:**
- ✅ Task needs to return a value
- ✅ You need results from asynchronous operations
- ✅ You want to continue working while task executes
- ✅ You need to handle exceptions from the task

**Use Runnable when:**
- Task doesn't return a value
- Simple fire-and-forget tasks

---

**Next:** Learn about [Interrupting Threads](06-interrupt-thread.md) for graceful cancellation!
