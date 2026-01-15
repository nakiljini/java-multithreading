"""
Unit tests for multithreading examples.
"""
import unittest
import threading
import time
from basic_threading import worker
from thread_synchronization import Counter, UnsafeCounter, increment_worker
from producer_consumer import producer, consumer
from thread_pool import task
import queue


class TestBasicThreading(unittest.TestCase):
    """Test basic threading functionality."""
    
    def test_worker_function(self):
        """Test that worker function completes successfully."""
        result = []
        
        def wrapper(worker_id, duration):
            worker(worker_id, duration)
            result.append(worker_id)
        
        thread = threading.Thread(target=wrapper, args=(1, 0.1))
        thread.start()
        thread.join(timeout=1.0)
        
        self.assertFalse(thread.is_alive())
        self.assertEqual(result, [1])
    
    def test_multiple_threads(self):
        """Test multiple threads execute concurrently."""
        start_time = time.time()
        threads = []
        
        for i in range(3):
            thread = threading.Thread(target=worker, args=(i, 0.1))
            threads.append(thread)
            thread.start()
        
        for thread in threads:
            thread.join()
        
        elapsed = time.time() - start_time
        # Should complete in ~0.1s (concurrent) not 0.3s (sequential)
        self.assertLess(elapsed, 0.25)


class TestThreadSynchronization(unittest.TestCase):
    """Test thread synchronization with locks."""
    
    def test_safe_counter(self):
        """Test that safe counter with lock produces correct results."""
        counter = Counter()
        threads = []
        iterations = 100
        num_threads = 10
        
        for _ in range(num_threads):
            thread = threading.Thread(target=increment_worker, args=(counter, iterations))
            threads.append(thread)
            thread.start()
        
        for thread in threads:
            thread.join()
        
        expected = num_threads * iterations
        self.assertEqual(counter.get_value(), expected)
    
    def test_unsafe_counter_race_condition(self):
        """Test that unsafe counter demonstrates race condition."""
        # Run multiple times to increase chance of seeing race condition
        race_detected = False
        
        for _ in range(5):
            counter = UnsafeCounter()
            threads = []
            iterations = 100
            num_threads = 10
            
            for _ in range(num_threads):
                thread = threading.Thread(target=increment_worker, args=(counter, iterations))
                threads.append(thread)
                thread.start()
            
            for thread in threads:
                thread.join()
            
            expected = num_threads * iterations
            if counter.get_value() != expected:
                race_detected = True
                break
        
        # We expect to see at least one race condition
        self.assertTrue(race_detected, "Race condition not detected in unsafe counter")
    
    def test_counter_initialization(self):
        """Test counter initializes to zero."""
        counter = Counter()
        self.assertEqual(counter.get_value(), 0)


class TestProducerConsumer(unittest.TestCase):
    """Test producer-consumer pattern."""
    
    def test_producer(self):
        """Test producer adds items to queue."""
        test_queue = queue.Queue()
        producer(test_queue, 0, 5)
        
        self.assertEqual(test_queue.qsize(), 5)
        
        # Verify items are in correct format
        for i in range(5):
            item = test_queue.get()
            self.assertEqual(item, f"Item-0-{i}")
    
    def test_consumer(self):
        """Test consumer processes items from queue."""
        test_queue = queue.Queue()
        stop_event = threading.Event()
        
        # Add items to queue
        for i in range(5):
            test_queue.put(f"Item-0-{i}")
        
        # Signal stop immediately
        stop_event.set()
        
        # Start consumer
        consumer_thread = threading.Thread(target=consumer, args=(test_queue, 0, stop_event))
        consumer_thread.start()
        consumer_thread.join(timeout=5.0)
        
        # Queue should be empty
        self.assertTrue(test_queue.empty())
        self.assertFalse(consumer_thread.is_alive())
    
    def test_producer_consumer_integration(self):
        """Test full producer-consumer integration."""
        work_queue = queue.Queue()
        stop_event = threading.Event()
        
        # Start producer
        producer_thread = threading.Thread(target=producer, args=(work_queue, 0, 3))
        producer_thread.start()
        
        # Start consumer
        consumer_thread = threading.Thread(target=consumer, args=(work_queue, 0, stop_event))
        consumer_thread.start()
        
        # Wait for producer
        producer_thread.join()
        
        # Wait for queue to be processed
        work_queue.join()
        
        # Stop consumer
        stop_event.set()
        consumer_thread.join(timeout=2.0)
        
        # Verify everything completed
        self.assertTrue(work_queue.empty())
        self.assertFalse(consumer_thread.is_alive())


class TestThreadPool(unittest.TestCase):
    """Test thread pool functionality."""
    
    def test_task_returns_correct_result(self):
        """Test that task function returns correct result."""
        task_id, result, duration = task(5)
        
        self.assertEqual(task_id, 5)
        self.assertEqual(result, 25)  # 5 * 5
        self.assertGreater(duration, 0)
    
    def test_concurrent_tasks(self):
        """Test that tasks execute concurrently in thread pool."""
        from concurrent.futures import ThreadPoolExecutor
        
        start_time = time.time()
        
        with ThreadPoolExecutor(max_workers=3) as executor:
            futures = [executor.submit(task, i) for i in range(3)]
            results = [f.result() for f in futures]
        
        elapsed = time.time() - start_time
        
        # With 3 workers, 3 tasks should complete in ~max_task_time not 3*max_task_time
        # Tasks take 0.5-2.0s, so 3 tasks should take less than 3s if concurrent
        self.assertLess(elapsed, 3.5)
        self.assertEqual(len(results), 3)


if __name__ == "__main__":
    unittest.main()
