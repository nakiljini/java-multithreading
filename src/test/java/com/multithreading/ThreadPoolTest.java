package com.multithreading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for ThreadPool examples.
 */
class ThreadPoolTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testTaskReturnsCorrectResult() throws Exception {
        ThreadPool.Task task = new ThreadPool.Task(5);
        ThreadPool.TaskResult result = task.call();
        
        assertEquals(5, result.taskId);
        assertEquals(25, result.result); // 5 * 5
        assertTrue(result.durationMs > 0);
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testTaskResultToString() throws Exception {
        ThreadPool.Task task = new ThreadPool.Task(3);
        ThreadPool.TaskResult result = task.call();
        
        assertEquals("Task 3 = 9", result.toString());
    }
}
