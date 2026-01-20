# Anonymous Package - Anonymous Inner Classes

## 📦 Package: `com.cp.concurrency.anonymous`

## 🎯 The Concept

**Anonymous inner classes** are classes without a name that are defined inline. Instead of creating a separate class file for a simple task, we can create the class definition right where we need it. This is useful for quick, one-time thread creation without the overhead of creating a separate class.

Think of it like ordering food - sometimes you just want a quick snack (anonymous class) rather than going to a restaurant and ordering a full meal (separate class).

## 🎯 What We're Trying to Achieve

We want to create threads quickly without:
- Creating a separate class file
- Writing boilerplate code
- Having to name a class that's only used once

This is perfect for simple, one-time tasks that don't need to be reused.

## 🔧 How We Achieve It

We create an anonymous class inline by:
1. **Using `new Runnable()`** - Create a new instance of Runnable
2. **Defining the class inline** - Write the class body with `{ }` right there
3. **Implementing run() method** - Put the code directly in the anonymous class
4. **Passing to Thread constructor** - All in one statement!

## 📝 Code Explanation: AnonymousThreadExample.java

```java
package com.cp.concurrency.anonymous;

public class AnonymousThreadExample {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello = " + i);
                }
            }
        });
```

**Line-by-Line Explanation:**

1. `Thread t1 = new Thread(...)`: We're creating a Thread object
2. `new Runnable() { ... }`: This is the anonymous class! Let's break it down:
   - `new Runnable()`: We're creating a new instance of something that implements Runnable
   - `{ ... }`: The curly braces contain the class definition
   - Inside the braces, we implement the `run()` method
3. `@Override public void run()`: We're implementing the required method from Runnable interface
4. The `for` loop: The actual work the thread will do

**The entire anonymous class is created inline - no separate class file needed!**

```java
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello = " + i);
                }
            }
        });
        t1.start();
        t2.start();
    }
}
```

**Explanation:**
- We create a second thread `t2` with another anonymous class
- Both threads are created with inline anonymous classes
- `t1.start()` and `t2.start()`: Start both threads to run concurrently

## 🔄 Comparison: Three Approaches

### 1. Separate Class (RunnableTaskExample)
```java
class RunnableTask implements Runnable {
    public void run() { /* code */ }
}
Thread t = new Thread(new RunnableTask());
```

### 2. Anonymous Class (Current approach)
```java
Thread t = new Thread(new Runnable() {
    public void run() { /* code */ }
});
```

### 3. Lambda Expression (Modern Java 8+)
```java
Thread t = new Thread(() -> {
    /* code */
});
```

## 💡 Advantages of Anonymous Classes

1. **Quick and Convenient**: No need to create a separate class file
2. **Inline Definition**: Everything is in one place
3. **Perfect for One-Time Use**: When the task is used only once
4. **Less Code**: More concise than creating a separate class

## ⚠️ Limitations

1. **Not Reusable**: Can't reuse the anonymous class elsewhere
2. **Less Readable for Complex Code**: If the `run()` method is long, it becomes hard to read
3. **Can't Have Constructors**: Anonymous classes can't have explicit constructors
4. **Limited to Simple Tasks**: Best for simple, short tasks

## 📊 Expected Output

```
Hello = 0
Hello = 0
Hello = 1
Hello = 1
...
```

Output from both threads will be interleaved randomly.

## 🎓 Key Takeaways

1. **Anonymous classes have no name** - Defined inline where used
2. **Perfect for one-time tasks** - Quick and convenient
3. **Syntax: `new InterfaceName() { implementation }`** - Create and implement in one go
4. **Use for simple tasks** - Keep the code short and readable

## 🔄 When to Use

**Use anonymous classes when:**
- ✅ Task is simple and short
- ✅ Task is used only once
- ✅ You want quick, inline thread creation
- ✅ Code readability is maintained

**Don't use when:**
- ❌ Task is complex or long
- ❌ Task needs to be reused
- ❌ Code becomes hard to read

## 🚀 Modern Alternative: Lambda Expressions

In Java 8+, you can use lambda expressions which are even more concise:

```java
Thread t = new Thread(() -> {
    for (int i = 0; i < 10; i++) {
        System.out.println("Hello = " + i);
    }
});
```

This is equivalent but more modern and readable!

---

**Next:** Learn about [ExecutorService](04-executor.md) for professional thread management!
