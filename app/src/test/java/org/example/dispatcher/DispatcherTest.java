package org.example.dispatcher;

import org.example.common.Job;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DispatcherTest {
    private Dispatcher dispatcher;
    private BlockingQueue<Job> dispatcherQueue;

    @BeforeEach
    public void setUp() {
        dispatcherQueue = new LinkedBlockingQueue<>();
        dispatcher = new Dispatcher(dispatcherQueue);
    }

    @AfterAll
    public static void tearDown() {
        // clean up after all tests
    }

    @Test
    @DisplayName("It should execute jobs and mark them as completed.")
    public void testExecuteJob() throws InterruptedException {
        Job job = new Job("Job1", 1, 500);
        dispatcherQueue.put(job);

        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();

        Thread.sleep(1000); // Allow time to execute

        assertTrue(job.getIsCompleted(), "Job should be marked as completed after execution.");
    }

    @Test
    @DisplayName("It should run the dispatcher thread and process multiple jobs.")
    public void testRun() throws InterruptedException {
        Job job1 = new Job("Job1", 1, 300);
        Job job2 = new Job("Job2", 1, 400);

        dispatcherQueue.put(job1);
        dispatcherQueue.put(job2);

        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();

        Thread.sleep(1500); // Allow time for both to finish

        assertTrue(job1.getIsCompleted(), "Job1 should be completed.");
        assertTrue(job2.getIsCompleted(), "Job2 should be completed.");
    }
}
