"""
Producer-Consumer Pattern
Demonstrates the classic producer-consumer pattern using threading and queues.
"""
import threading
import queue
import time
import random


def producer(queue_obj, producer_id, num_items):
    """
    Producer function that generates items and puts them in the queue.
    
    Args:
        queue_obj: Queue to put items into
        producer_id: Unique identifier for the producer
        num_items: Number of items to produce
    """
    for i in range(num_items):
        item = f"Item-{producer_id}-{i}"
        queue_obj.put(item)
        print(f"Producer {producer_id} produced: {item}")
        time.sleep(random.uniform(0.1, 0.5))
    print(f"Producer {producer_id} finished")


def consumer(queue_obj, consumer_id, stop_event):
    """
    Consumer function that processes items from the queue.
    
    Args:
        queue_obj: Queue to get items from
        consumer_id: Unique identifier for the consumer
        stop_event: Event to signal when to stop
    """
    while not stop_event.is_set() or not queue_obj.empty():
        try:
            item = queue_obj.get(timeout=0.5)
            print(f"Consumer {consumer_id} consumed: {item}")
            time.sleep(random.uniform(0.1, 0.3))
            queue_obj.task_done()
        except queue.Empty:
            continue
    print(f"Consumer {consumer_id} finished")


def run_producer_consumer_example():
    """Run producer-consumer pattern example."""
    print("=== Producer-Consumer Pattern Example ===\n")
    
    # Create a queue with max size
    work_queue = queue.Queue(maxsize=10)
    stop_event = threading.Event()
    
    # Create producer threads
    num_producers = 2
    num_items_per_producer = 5
    producers = []
    for i in range(num_producers):
        thread = threading.Thread(target=producer, args=(work_queue, i, num_items_per_producer))
        producers.append(thread)
        thread.start()
    
    # Create consumer threads
    num_consumers = 3
    consumers = []
    for i in range(num_consumers):
        thread = threading.Thread(target=consumer, args=(work_queue, i, stop_event))
        consumers.append(thread)
        thread.start()
    
    # Wait for all producers to finish
    for thread in producers:
        thread.join()
    
    # Wait for queue to be empty
    work_queue.join()
    
    # Signal consumers to stop
    stop_event.set()
    
    # Wait for all consumers to finish
    for thread in consumers:
        thread.join()
    
    print("\nAll producers and consumers finished!\n")


if __name__ == "__main__":
    run_producer_consumer_example()
