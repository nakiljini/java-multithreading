"""
Thread Synchronization Example
Demonstrates thread synchronization using locks to prevent race conditions.
"""
import threading
import time


class Counter:
    """A thread-safe counter using locks."""
    
    def __init__(self):
        self.value = 0
        self.lock = threading.Lock()
    
    def increment(self):
        """Increment counter in a thread-safe manner."""
        with self.lock:
            current = self.value
            time.sleep(0.001)  # Simulate some processing
            self.value = current + 1
    
    def get_value(self):
        """Get current counter value."""
        with self.lock:
            return self.value


class UnsafeCounter:
    """A counter without thread synchronization (demonstrates race conditions)."""
    
    def __init__(self):
        self.value = 0
    
    def increment(self):
        """Increment counter without thread safety."""
        current = self.value
        time.sleep(0.001)  # Simulate some processing
        self.value = current + 1
    
    def get_value(self):
        """Get current counter value."""
        return self.value


def increment_worker(counter, iterations):
    """Worker that increments a counter multiple times."""
    for _ in range(iterations):
        counter.increment()


def run_synchronization_example():
    """Run thread synchronization example comparing safe and unsafe counters."""
    print("=== Thread Synchronization Example ===")
    
    # Test with unsafe counter
    print("\n1. Unsafe Counter (Race Condition):")
    unsafe_counter = UnsafeCounter()
    threads = []
    iterations = 100
    
    for i in range(10):
        thread = threading.Thread(target=increment_worker, args=(unsafe_counter, iterations))
        threads.append(thread)
        thread.start()
    
    for thread in threads:
        thread.join()
    
    print(f"Expected: {10 * iterations}, Got: {unsafe_counter.get_value()}")
    
    # Test with safe counter
    print("\n2. Safe Counter (With Lock):")
    safe_counter = Counter()
    threads = []
    
    for i in range(10):
        thread = threading.Thread(target=increment_worker, args=(safe_counter, iterations))
        threads.append(thread)
        thread.start()
    
    for thread in threads:
        thread.join()
    
    print(f"Expected: {10 * iterations}, Got: {safe_counter.get_value()}")
    print()


if __name__ == "__main__":
    run_synchronization_example()
