"""
Thread Pool Example
Demonstrates using ThreadPoolExecutor for efficient thread management.
"""
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import random


def task(task_id):
    """
    Simulated task that takes variable time to complete.
    
    Args:
        task_id: Unique identifier for the task
        
    Returns:
        Tuple of (task_id, result, duration)
    """
    duration = random.uniform(0.5, 2.0)
    print(f"Task {task_id} starting (will take {duration:.2f}s)")
    time.sleep(duration)
    result = task_id * task_id
    print(f"Task {task_id} completed")
    return task_id, result, duration


def run_thread_pool_example():
    """Run thread pool example with multiple tasks."""
    print("=== Thread Pool Example ===\n")
    
    num_tasks = 10
    max_workers = 3
    
    print(f"Executing {num_tasks} tasks with {max_workers} workers\n")
    
    start_time = time.time()
    
    # Using ThreadPoolExecutor
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        # Submit all tasks
        futures = [executor.submit(task, i) for i in range(num_tasks)]
        
        # Process completed tasks as they finish
        results = []
        for future in as_completed(futures):
            task_id, result, duration = future.result()
            results.append((task_id, result))
            print(f"Retrieved result: Task {task_id} = {result}")
    
    end_time = time.time()
    total_time = end_time - start_time
    
    print(f"\nAll tasks completed in {total_time:.2f} seconds")
    print(f"Results: {sorted(results)}\n")


def map_example():
    """Demonstrate ThreadPoolExecutor.map() for batch processing."""
    print("=== Thread Pool Map Example ===\n")
    
    def square(n):
        """Simple function to square a number."""
        time.sleep(0.2)
        return n * n
    
    numbers = list(range(10))
    
    with ThreadPoolExecutor(max_workers=4) as executor:
        results = list(executor.map(square, numbers))
    
    print(f"Input:  {numbers}")
    print(f"Output: {results}\n")


if __name__ == "__main__":
    run_thread_pool_example()
    map_example()
