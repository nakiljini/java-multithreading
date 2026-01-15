"""
Basic Threading Example
Demonstrates fundamental threading concepts in Python.
"""
import threading
import time


def worker(worker_id, duration):
    """
    Simple worker function that simulates work.
    
    Args:
        worker_id: Unique identifier for the worker
        duration: Time in seconds to simulate work
    """
    print(f"Worker {worker_id} starting...")
    time.sleep(duration)
    print(f"Worker {worker_id} finished after {duration} seconds")


def run_basic_example():
    """Run basic threading example with multiple workers."""
    print("=== Basic Threading Example ===")
    threads = []
    
    # Create and start 5 threads
    for i in range(5):
        thread = threading.Thread(target=worker, args=(i, i * 0.5))
        threads.append(thread)
        thread.start()
    
    # Wait for all threads to complete
    for thread in threads:
        thread.join()
    
    print("All threads completed!\n")


if __name__ == "__main__":
    run_basic_example()
