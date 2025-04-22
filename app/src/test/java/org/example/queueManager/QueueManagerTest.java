package org.example.queueManager;

import org.example.common.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Queue Manager class.
 */
public class QueueManagerTest {
    private QueueManager queueManager;
    private Job job1;
    private Job job2;

    @BeforeEach
    public void setUp() {
        queueManager = new QueueManager();
        job1 = new Job("Job1", 1, 1000, LocalDateTime.now());
        job2 = new Job("Job2", 2, 2000, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should enqueue and then dequeue a job successfully")
    public void QueueManager_EnqueueAndDequeueJob_ShouldBeSuccessful() throws InterruptedException {
        // Arrange / Act
        queueManager.enqueueJob(job1);
        Job dequeued = queueManager.dequeueJob();

        // Assert
        assertEquals(job1, dequeued);
        assertEquals(0, queueManager.getQueueSize());
    }

    @Test
    @DisplayName("Should block on dequeue until a job is enqueued")
    public void QueueManager_EmptyQueue_ShouldBlockDequeue() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        Future<Job> future = executor.submit(() -> {
            latch.countDown(); // let main thread know we're waiting
            return queueManager.dequeueJob(); // should block
        });

        latch.await(); // wait until thread starts waiting
        Thread.sleep(100); // give it a moment to block

        // Act
        queueManager.enqueueJob(job1);

        // Assert
        Job result = future.get(1, TimeUnit.SECONDS);
        assertEquals(job1, result);

        executor.shutdown();
    }

    @Test
    @DisplayName("Should block new jobs from being added when queue size limit is reached.")
    public void QueueManager_QueueFull_ShouldBlockEnqueue() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Fill the queue to capacity
        for (int i = 0; i < 10; i++) {
            Job job = new Job("Job" + i, 1, 1000, LocalDateTime.now());
            queueManager.enqueueJob(job);
        }

        // Act
        Future<?> future = executor.submit(() -> {
            try {
                queueManager.enqueueJob(job1); // should block until a dequeue occurs
            } catch (InterruptedException ignored) {}
        });

        Thread.sleep(200); // give it time to attempt to enqueue (and get blocked)

        // Assert
        assertFalse(future.isDone(), "Enqueue should be blocked since queue is full");
        assertEquals(10, queueManager.getQueueSize());
        executor.shutdown();
    }

    @Test
    @DisplayName("Should clear all jobs from queue")
    public void QueueManager_Empty_ShouldClearJobs() throws InterruptedException {
        // Arrange / Act
        queueManager.enqueueJob(job1);
        queueManager.enqueueJob(job2);

        // Assert
        assertEquals(2, queueManager.getQueueSize());
        queueManager.empty();
        assertEquals(0, queueManager.getQueueSize());
    }

    @Test
    @DisplayName("Should check if a job is present in queue")
    public void QueueManager_CheckForJob_ShouldReturnCorrectly() throws InterruptedException {
        // Arrange / Act
        queueManager.enqueueJob(job1);

        // Assert
        assertTrue(queueManager.checkForJob(job1));
        assertFalse(queueManager.checkForJob(job2));
    }

    @Test
    @DisplayName("Should return the current queue size")
    public void QueueManager_GetQueueSize_ShouldReturnSize() throws InterruptedException {
        // Arrange / Act
        assertEquals(0, queueManager.getQueueSize());
        queueManager.enqueueJob(job1);

        // Assert
        assertEquals(1, queueManager.getQueueSize());
    }
}
