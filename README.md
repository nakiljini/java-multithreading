# Java Multithreading Examples

A comprehensive collection of Java multithreading examples demonstrating core concepts and patterns.

## Overview

This repository contains practical examples of multithreading in Java, including:

- **Basic Threading**: Introduction to creating and managing threads
- **Thread Synchronization**: Using synchronized keyword to prevent race conditions
- **Producer-Consumer Pattern**: Classic pattern for coordinating work between threads
- **Thread Pools**: Efficient thread management using ExecutorService

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Installation

Clone the repository:

```bash
git clone https://github.com/nakiljini/java-multithreading.git
cd java-multithreading
```

Build the project:

```bash
mvn clean install
```

## Usage

### Run All Examples

Execute all examples in sequence:

```bash
mvn exec:java
```

Or compile and run directly:

```bash
mvn compile
java -cp target/classes com.multithreading.Main
```

### Run Individual Examples

Each example can be run independently:

```bash
# Basic threading
java -cp target/classes com.multithreading.BasicThreading

# Thread synchronization
java -cp target/classes com.multithreading.ThreadSynchronization

# Producer-consumer pattern
java -cp target/classes com.multithreading.ProducerConsumer

# Thread pool
java -cp target/classes com.multithreading.ThreadPool
```

### Run Tests

Execute the test suite:

```bash
mvn test
```

## Examples Description

### 1. Basic Threading (`BasicThreading.java`)

Demonstrates fundamental threading concepts:
- Creating threads by extending `Thread` class
- Creating threads by implementing `Runnable` interface
- Starting threads with `.start()`
- Waiting for thread completion with `.join()`
- Running multiple threads concurrently

**Key Concepts:**
- Two ways to create threads in Java
- Threads allow concurrent execution
- Main thread waits for all worker threads to complete

### 2. Thread Synchronization (`ThreadSynchronization.java`)

Shows how to prevent race conditions using synchronization:
- Thread-safe counter with `synchronized` methods
- Comparison with unsafe counter (demonstrates race conditions)
- Proper synchronization ensures data integrity

**Key Concepts:**
- Race conditions occur when multiple threads access shared data
- `synchronized` keyword ensures only one thread accesses critical section at a time
- Proper synchronization is essential for data integrity

### 3. Producer-Consumer Pattern (`ProducerConsumer.java`)

Implements the classic producer-consumer pattern:
- Producers generate work items and add them to a queue
- Consumers process items from the queue
- Uses `BlockingQueue` for thread-safe communication
- Demonstrates coordination between thread groups

**Key Concepts:**
- `BlockingQueue` provides thread-safe communication between threads
- Producer-consumer pattern decouples production from consumption
- Proper shutdown signaling with volatile flags

### 4. Thread Pool (`ThreadPool.java`)

Uses ExecutorService for efficient thread management:
- Creating a pool of reusable threads with `Executors.newFixedThreadPool()`
- Submitting tasks using `Callable` and retrieving results with `Future`
- Handling task completion
- Proper executor shutdown

**Key Concepts:**
- Thread pools avoid overhead of creating/destroying threads
- `ExecutorService` pattern provides clean API
- `Future` objects represent pending results
- Always shutdown executors to prevent resource leaks

## Project Structure

```
java-multithreading/
├── pom.xml                                    # Maven configuration
├── README.md                                  # This file
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── multithreading/
│   │               ├── Main.java              # Run all examples
│   │               ├── BasicThreading.java    # Basic threading example
│   │               ├── ThreadSynchronization.java  # Synchronization
│   │               ├── ProducerConsumer.java  # Producer-consumer pattern
│   │               └── ThreadPool.java        # Thread pool example
│   └── test/
│       └── java/
│           └── com/
│               └── multithreading/
│                   ├── BasicThreadingTest.java
│                   ├── ThreadSynchronizationTest.java
│                   ├── ProducerConsumerTest.java
│                   └── ThreadPoolTest.java
```

## Best Practices

1. **Use Thread Pools**: For many tasks, use `ExecutorService` instead of managing threads manually
2. **Synchronize Shared Data**: Always use `synchronized` when multiple threads access shared mutable data
3. **Use BlockingQueue for Communication**: Thread-safe and ideal for inter-thread communication
4. **Handle InterruptedException**: Always handle interrupts properly
5. **Shutdown Executors**: Always call `shutdown()` and `awaitTermination()` on executors
6. **Prefer Runnable over Thread**: Implementing `Runnable` is more flexible than extending `Thread`
7. **Test Thoroughly**: Race conditions can be intermittent; test multithreaded code extensively

## Common Pitfalls

- **Race Conditions**: Not synchronizing access to shared data
- **Deadlocks**: Circular dependencies in lock acquisition
- **Resource Leaks**: Not properly shutting down executors or joining threads
- **Ignoring InterruptedException**: Always handle thread interruption properly
- **Creating Too Many Threads**: Use thread pools instead

## When to Use Threading

Threading is best for:
- **I/O-bound tasks**: Network requests, file operations, database queries
- **Concurrent operations**: Multiple independent tasks that can run simultaneously
- **Responsive applications**: Keeping UIs responsive during background work
- **Parallel processing**: Taking advantage of multi-core processors

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Resources

- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [java.util.concurrent package](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/package-summary.html)
- [Java Concurrency in Practice](https://jcip.net/)
- [Baeldung Java Concurrency](https://www.baeldung.com/java-concurrency)
