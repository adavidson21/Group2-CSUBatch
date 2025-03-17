package org.example.scheduler;

import org.example.common.Job;
import org.example.dispatcher.Dispatcher;
import org.example.queueManager.QueueManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {
    private Scheduler fcfsScheduler;
    private Scheduler sjfScheduler;
    private Scheduler priorityScheduler;

    private Dispatcher fcfsDispatcher;
    private Dispatcher sjfDispatcher;
    private Dispatcher priorityDispatcher;

    private QueueManager fcfsQueue;
    private QueueManager sjfQueue;
    private QueueManager priorityQueue;

    private List<Job> jobs;

    @BeforeEach
    void setUp() {
        jobs = new ArrayList<>();
        jobs.add(new Job("Job-1", 3, 2000));
        jobs.add(new Job("Job-2", 1, 1000));
        jobs.add(new Job("Job-3", 2, 1500));

        fcfsQueue = new QueueManager();
        fcfsScheduler = new Scheduler(SchedulingPolicy.FCFS, fcfsQueue);
        fcfsDispatcher = new Dispatcher(fcfsScheduler.getScheduledJobQueue());
        new Thread(fcfsDispatcher).start();

        sjfQueue = new QueueManager();
        sjfScheduler = new Scheduler(SchedulingPolicy.SJF, sjfQueue);
        sjfDispatcher = new Dispatcher(sjfScheduler.getScheduledJobQueue());
        new Thread(sjfDispatcher).start();

        priorityQueue = new QueueManager();
        priorityScheduler = new Scheduler(SchedulingPolicy.Priority, priorityQueue);
        priorityDispatcher = new Dispatcher(priorityScheduler.getScheduledJobQueue());
        new Thread(priorityDispatcher).start();
    }

    @Test
    void testFCFSScheduling() throws InterruptedException {
        for (Job job : jobs) {
            fcfsQueue.enqueueJob(job);
        }

        Thread.sleep(6000); // Allow time for execution

        assertTrue(jobs.get(0).getIsCompleted());
        assertTrue(jobs.get(1).getIsCompleted());
        assertTrue(jobs.get(2).getIsCompleted());
    }

    @Test
    void testSJFScheduling() throws InterruptedException {
        for (Job job : jobs) {
            sjfQueue.enqueueJob(job);
        }

        Thread.sleep(6000);

        assertTrue(jobs.get(1).getIsCompleted());
        assertTrue(jobs.get(2).getIsCompleted());
        assertTrue(jobs.get(0).getIsCompleted());
    }

    @Test
    void testPriorityScheduling() throws InterruptedException {
        for (Job job : jobs) {
            priorityQueue.enqueueJob(job);
        }

        Thread.sleep(6000);

        assertTrue(jobs.get(0).getIsCompleted());
        assertTrue(jobs.get(2).getIsCompleted());
        assertTrue(jobs.get(1).getIsCompleted());
    }
}
