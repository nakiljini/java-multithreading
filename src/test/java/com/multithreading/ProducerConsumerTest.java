package com.multithreading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for ProducerConsumer pattern.
 */
class ProducerConsumerTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testProducerAddsItems() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        ProducerConsumer.Producer producer = new ProducerConsumer.Producer(queue, 0, 5);
        
        producer.start();
        producer.join();
        
        assertEquals(5, queue.size());
        
        // Verify items are in correct format
        for (int i = 0; i < 5; i++) {
            String item = queue.poll();
            assertNotNull(item);
            assertTrue(item.startsWith("Item-0-"));
        }
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testConsumerProcessesItems() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        
        // Add items to queue
        for (int i = 0; i < 5; i++) {
            queue.put("Item-0-" + i);
        }
        
        ProducerConsumer.Consumer consumer = new ProducerConsumer.Consumer(queue, 0);
        consumer.start();
        
        // Wait a bit for consumption
        Thread.sleep(2000);
        
        // Stop consumer
        consumer.stopConsuming();
        consumer.join();
        
        // Queue should be empty
        assertTrue(queue.isEmpty());
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testProducerConsumerIntegration() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        
        ProducerConsumer.Producer producer = new ProducerConsumer.Producer(queue, 0, 3);
        ProducerConsumer.Consumer consumer = new ProducerConsumer.Consumer(queue, 0);
        
        producer.start();
        consumer.start();
        
        producer.join();
        
        // Wait for queue to be processed
        Thread.sleep(2000);
        
        consumer.stopConsuming();
        consumer.join();
        
        assertTrue(queue.isEmpty());
    }
}
