# Java Multithreading Examples - Complete Guide

## 📚 Welcome!

This repository contains comprehensive examples of Java multithreading and concurrency concepts. Each example is designed to be beginner-friendly with detailed explanations of concepts, goals, and code implementation.

## 🎯 What is Multithreading?

**Multithreading** allows your Java program to execute multiple tasks simultaneously. Think of it like a restaurant with multiple waiters - each waiter can serve different customers at the same time, making the service faster and more efficient.

### Why Use Multithreading?
- **Better Performance**: Utilize multiple CPU cores effectively
- **Responsive Applications**: UI stays responsive while background tasks run
- **Efficient Resource Usage**: Don't waste time waiting for I/O operations
- **Parallel Processing**: Handle multiple requests simultaneously

## 📁 Project Structure

```
src/com/cp/concurrency/
├── threads/              # Basic thread creation by extending Thread class
├── runnables/            # Thread creation using Runnable interface
├── anonymous/            # Creating threads using anonymous inner classes
├── executor/             # Using ExecutorService for thread management
├── callablefuture/       # Tasks that return values
├── interruptthread/      # How to interrupt running threads
├── interthread/          # Communication between threads
├── mutex/                # Synchronization to prevent data corruption
├── reentrant/            # Advanced locking mechanism
├── semaphore/            # Controlling access to resources
├── countdownlatch/       # Waiting for multiple threads to complete
├── deadlock/             # Understanding and avoiding deadlocks
└── volatiles/            # Volatile keyword for visibility
```

## 📖 Documentation Files

Each package has its own detailed documentation file explaining:
- **The Concept**: What is this concept about?
- **What We're Trying to Achieve**: What problem does it solve?
- **How We Achieve It**: The approach and strategy
- **Code Explanation**: Line-by-line explanation of the implementation

### Available Documentation:

1. **[Threads Package](docs/01-threads.md)** - Basic thread creation
2. **[Runnables Package](docs/02-runnables.md)** - Using Runnable interface
3. **[Anonymous Package](docs/03-anonymous.md)** - Anonymous inner classes
4. **[Executor Package](docs/04-executor.md)** - ExecutorService and thread pools
5. **[Callable & Future Package](docs/05-callable-future.md)** - Returning values from threads
6. **[Interrupt Thread Package](docs/06-interrupt-thread.md)** - Gracefully stopping threads
7. **[Inter-Thread Communication](docs/07-interthread-communication.md)** - Thread coordination
8. **[Mutex Package](docs/08-mutex-synchronization.md)** - Preventing race conditions
9. **[Reentrant Lock Package](docs/09-reentrant-lock.md)** - Advanced locking
10. **[Semaphore Package](docs/10-semaphore.md)** - Resource access control
11. **[CountDownLatch Package](docs/11-countdownlatch.md)** - Waiting for threads
12. **[Deadlock Package](docs/12-deadlock.md)** - Understanding deadlocks
13. **[Volatile Package](docs/13-volatile.md)** - Variable visibility

## 🚀 Quick Start

### Prerequisites
- Java JDK 8 or higher
- Basic understanding of Java programming

### Running Examples

1. **Compile the code:**
   ```bash
   javac -d out src/com/cp/concurrency/**/*.java
   ```

2. **Run any example:**
   ```bash
   java -cp out com.cp.concurrency.threads.CustomThreadExample
   ```

3. **Or use your IDE:**
   - Right-click on any Java file
   - Select "Run" or "Run as Java Application"

## 🎓 Learning Path

For beginners, we recommend following this order:

1. **Start Here**: [Threads](docs/01-threads.md) → [Runnables](docs/02-runnables.md) → [Anonymous](docs/03-anonymous.md)
2. **Thread Management**: [Executor](docs/04-executor.md) → [Callable & Future](docs/05-callable-future.md)
3. **Synchronization**: [Mutex](docs/08-mutex-synchronization.md) → [Reentrant Lock](docs/09-reentrant-lock.md)
4. **Advanced Topics**: [Inter-Thread Communication](docs/07-interthread-communication.md) → [Semaphore](docs/10-semaphore.md) → [CountDownLatch](docs/11-countdownlatch.md)
5. **Important Concepts**: [Deadlock](docs/12-deadlock.md) → [Volatile](docs/13-volatile.md)

## 📊 Quick Reference Guide

| Concept | When to Use | Documentation |
|---------|-------------|---------------|
| **Thread** | Simple thread creation | [Threads](docs/01-threads.md) |
| **Runnable** | Preferred way to create threads | [Runnables](docs/02-runnables.md) |
| **ExecutorService** | Production code, thread management | [Executor](docs/04-executor.md) |
| **Callable/Future** | Tasks that return values | [Callable & Future](docs/05-callable-future.md) |
| **synchronized** | Prevent race conditions | [Mutex](docs/08-mutex-synchronization.md) |
| **ReentrantLock** | Advanced locking needs | [Reentrant Lock](docs/09-reentrant-lock.md) |
| **Semaphore** | Limit concurrent access | [Semaphore](docs/10-semaphore.md) |
| **CountDownLatch** | Wait for multiple threads | [CountDownLatch](docs/11-countdownlatch.md) |
| **BlockingQueue** | Producer-Consumer pattern | [Inter-Thread Communication](docs/07-interthread-communication.md) |
| **volatile** | Simple visibility guarantees | [Volatile](docs/13-volatile.md) |

## 📝 Best Practices

1. ✅ **Use ExecutorService** instead of creating threads manually
2. ✅ **Prefer Runnable** over extending Thread
3. ✅ **Always unlock** ReentrantLock in finally block
4. ✅ **Use BlockingQueue** instead of wait/notify when possible
5. ✅ **Avoid deadlocks** by acquiring locks in consistent order
6. ✅ **Handle InterruptedException** properly
7. ✅ **Use synchronized** for simple cases, ReentrantLock for complex ones

## ⚠️ Common Mistakes to Avoid

1. ❌ Calling `run()` instead of `start()` - won't create a new thread
2. ❌ Forgetting to unlock ReentrantLock - causes deadlock
3. ❌ Using wait/notify without synchronized - throws IllegalMonitorStateException
4. ❌ Not handling InterruptedException
5. ❌ Assuming volatile makes operations atomic
6. ❌ Creating too many threads (use thread pools instead)

## 📚 Additional Resources

- [Oracle Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java Concurrency in Practice](https://jcip.net/) - Book by Brian Goetz

## 🤝 Contributing

Feel free to add more examples or improve existing ones!

---

**Happy Learning! 🎉**
