"""
Main entry point for multithreading examples.
Runs all examples in sequence.
"""
import sys
from basic_threading import run_basic_example
from thread_synchronization import run_synchronization_example
from producer_consumer import run_producer_consumer_example
from thread_pool import run_thread_pool_example, map_example


def main():
    """Run all multithreading examples."""
    print("=" * 60)
    print("MULTITHREADING EXAMPLES")
    print("=" * 60)
    print()
    
    examples = [
        ("Basic Threading", run_basic_example),
        ("Thread Synchronization", run_synchronization_example),
        ("Producer-Consumer Pattern", run_producer_consumer_example),
        ("Thread Pool", run_thread_pool_example),
        ("Thread Pool Map", map_example),
    ]
    
    for name, func in examples:
        try:
            func()
        except Exception as e:
            print(f"Error running {name}: {e}")
            sys.exit(1)
    
    print("=" * 60)
    print("ALL EXAMPLES COMPLETED SUCCESSFULLY")
    print("=" * 60)


if __name__ == "__main__":
    main()
