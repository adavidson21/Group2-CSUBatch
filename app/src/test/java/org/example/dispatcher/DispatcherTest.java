package org.example.dispatcher;

import org.example.common.Job;
import org.example.queueManager.QueueManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DispatcherTest {
    private Dispatcher dispatcher;
    private QueueManager queueManager;

    @BeforeEach
    public void setUp() {
        queueManager = new QueueManager();
        dispatcher = new Dispatcher(queueManager);
    }

    @Test
    public void testExecuteSingleJob() throws InterruptedException {
        Job job = new Job("Test Job", 1, 200);
        queueManager.enqueueScheduledJob(job);

        Thread thread = new Thread(dispatcher);
        thread.start();

        Thread.sleep(300); // Allow time to process job
        dispatcher.stop();

        assertTrue(job.getIsCompleted(), "Job should be marked as completed.");
    }
}
