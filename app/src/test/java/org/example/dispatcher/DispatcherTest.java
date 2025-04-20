package org.example.dispatcher;

import org.example.common.Job;
import org.example.queueManager.QueueManager;
import org.example.perfEvaluator.PerfEvaluator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class DispatcherTest {
    private QueueManager queueManager;
    private Dispatcher dispatcher;
    private PerfEvaluator perfEvaluator = mock(PerfEvaluator.class);

    @BeforeEach
    public void setUp() {
        //initialize what the dispatcher needs to run
        queueManager = mock(QueueManager.class);
        dispatcher = new Dispatcher(queueManager, perfEvaluator);
    }

    @AfterAll
    public static void tearDown() {
        // clean up after all tests

    }

    @Test
    @DisplayName("It should execute jobs and mark them as completed.")
    public void testExecuteJob() {
        Job job = new Job("Job1", 1, 1000, null);
        dispatcher.executeJob(job);
        //assert that the job has completed
        assertTrue(job.getIsCompleted());
    }

    @Test
    @DisplayName("It should run the dispatcher thread and process multiple jobs.")
    public void testRun() throws InterruptedException {
        Job job1 = new Job("Job1", 1, 500, null);
        Job job2 = new Job("Job2", 1, 500, null);

        queueManager.enqueueJob(job1);
        queueManager.enqueueJob(job2);

        // mock queue manager to return jobs on dequeue
        when(queueManager.dequeueJob()).thenReturn(job1);
        when(queueManager.dequeueJob()).thenReturn(job2);

        // run the thread
        Thread dispatcherThread = new Thread(dispatcher);
        dispatcherThread.start();

        // give enough time for the dispatcher to process the jobs
        Thread.sleep(1500);

        verify(queueManager, times(2)).dequeueJob();
    }
}
