# Multithreading Examples

A comprehensive collection of Python multithreading examples demonstrating core concepts and patterns.

## Overview

This repository contains practical examples of multithreading in Python, including:

- **Basic Threading**: Introduction to creating and managing threads
- **Thread Synchronization**: Using locks to prevent race conditions
- **Producer-Consumer Pattern**: Classic pattern for coordinating work between threads
- **Thread Pools**: Efficient thread management using ThreadPoolExecutor

## Requirements

- Python 3.6 or higher
- No external dependencies (uses only Python standard library)

## Installation

Clone the repository:

```bash
git clone https://github.com/nakiljini/multithreading.git
cd multithreading
```

## Usage

### Run All Examples

Execute all examples in sequence:

```bash
python main.py
```

### Run Individual Examples

Each example can be run independently:

```bash
# Basic threading
python basic_threading.py

# Thread synchronization
python thread_synchronization.py

# Producer-consumer pattern
python producer_consumer.py

# Thread pool
python thread_pool.py
```

### Run Tests

Execute the test suite:

```bash
python -m unittest test_multithreading.py -v
```

Or run with pytest if available:

```bash
pytest test_multithreading.py -v
```

## Examples Description

### 1. Basic Threading (`basic_threading.py`)

Demonstrates fundamental threading concepts:
- Creating threads with `threading.Thread`
- Starting threads with `.start()`
- Waiting for thread completion with `.join()`
- Running multiple threads concurrently

**Key Concepts:**
- Threads allow concurrent execution
- Multiple threads can run simultaneously
- Main thread waits for all worker threads to complete

### 2. Thread Synchronization (`thread_synchronization.py`)

Shows how to prevent race conditions using locks:
- Thread-safe counter with `threading.Lock`
- Comparison with unsafe counter (demonstrates race conditions)
- Context manager (`with` statement) for automatic lock management

**Key Concepts:**
- Race conditions occur when multiple threads access shared data
- Locks ensure only one thread accesses critical section at a time
- Proper synchronization is essential for data integrity

### 3. Producer-Consumer Pattern (`producer_consumer.py`)

Implements the classic producer-consumer pattern:
- Producers generate work items and add them to a queue
- Consumers process items from the queue
- Uses `queue.Queue` for thread-safe communication
- Demonstrates coordination between thread groups

**Key Concepts:**
- Queues provide thread-safe communication between threads
- Producer-consumer pattern decouples production from consumption
- Event objects signal when to stop processing

### 4. Thread Pool (`thread_pool.py`)

Uses ThreadPoolExecutor for efficient thread management:
- Creating a pool of reusable threads
- Submitting tasks and retrieving results
- Using `map()` for batch processing
- Handling task completion with `as_completed()`

**Key Concepts:**
- Thread pools avoid overhead of creating/destroying threads
- ExecutorService pattern provides clean API
- Futures represent pending results
- Efficient for many short-lived tasks

## Project Structure

```
multithreading/
├── README.md                    # This file
├── main.py                      # Run all examples
├── basic_threading.py           # Basic threading example
├── thread_synchronization.py    # Lock-based synchronization
├── producer_consumer.py         # Producer-consumer pattern
├── thread_pool.py              # Thread pool example
└── test_multithreading.py      # Unit tests
```

## Best Practices

1. **Use Thread Pools**: For many tasks, use `ThreadPoolExecutor` instead of managing threads manually
2. **Synchronize Shared Data**: Always use locks when multiple threads access shared mutable data
3. **Use Queues for Communication**: `queue.Queue` is thread-safe and ideal for inter-thread communication
4. **Prefer Context Managers**: Use `with` statements for locks and executors to ensure proper cleanup
5. **Handle Exceptions**: Wrap thread code in try-except blocks to catch and handle errors
6. **Consider GIL**: Python's Global Interpreter Lock means CPU-bound tasks may not benefit from threading; use multiprocessing instead
7. **Test Thoroughly**: Race conditions can be intermittent; test multithreaded code extensively

## Common Pitfalls

- **Race Conditions**: Not synchronizing access to shared data
- **Deadlocks**: Circular dependencies in lock acquisition
- **Resource Leaks**: Not properly joining threads or closing executors
- **GIL Limitations**: Using threads for CPU-bound tasks (use multiprocessing instead)

## When to Use Threading

Threading is best for:
- **I/O-bound tasks**: Network requests, file operations, database queries
- **Concurrent operations**: Multiple independent tasks that spend time waiting
- **Responsive UIs**: Keeping interfaces responsive during background work

For CPU-bound tasks, consider `multiprocessing` instead.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Resources

- [Python threading documentation](https://docs.python.org/3/library/threading.html)
- [concurrent.futures documentation](https://docs.python.org/3/library/concurrent.futures.html)
- [Python threading tutorial](https://realpython.com/intro-to-python-threading/)
- [Threading best practices](https://superfastpython.com/threading-best-practices/)
