package org.example.uiController;

import org.example.common.Job;
import org.example.dispatcher.Dispatcher;
import org.example.queueManager.QueueManager;
import org.example.scheduler.Scheduler;
import org.example.scheduler.SchedulingPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UIControllerTest {
    private UIController UI;
    private QueueManager queueManager;
    private Scheduler scheduler;

    @BeforeEach
    public void setUp() {
        queueManager = new QueueManager();
        scheduler = new Scheduler(SchedulingPolicy.FCFS, queueManager);
        UI = new UIController(scheduler, queueManager);
        UI.startDispatcher();
    }

    @Test
    public void testSubmitJobMarksJobComplete() throws InterruptedException {
        Job job = new Job("TestJob", 2, 500);
        UI.submitJob(job);

        Thread.sleep(800); // Give time for scheduler and dispatcher to run

        assertTrue(job.getIsCompleted());
    }
}
