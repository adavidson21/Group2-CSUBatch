package org.example.scheduler;

import org.example.common.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

class SchedulerTest {
    private Scheduler fcfsScheduler;
    private Scheduler sjfScheduler;
    private Scheduler priorityScheduler;
    private List<Job> jobs;

    @BeforeEach
    void setUp() {
        jobs = new ArrayList<>();
        jobs.add(new Job("Job-1", 3, 2000, null));  // Execution time 2000ms, Priority 3
        jobs.add(new Job("Job-2", 1, 1000,null));  // Execution time 1000ms, Priority 1
        jobs.add(new Job("Job-3", 2, 1500,null));  // Execution time 1500ms, Priority 2

        fcfsScheduler = new Scheduler(SchedulingPolicy.FCFS);
        sjfScheduler = new Scheduler(SchedulingPolicy.SJF);
        priorityScheduler = new Scheduler(SchedulingPolicy.Priority);
    }

    @Test
    void testFCFSScheduling() throws InterruptedException {
        for (Job job : jobs) {
            fcfsScheduler.addJob(job);
        }

        Thread.sleep(6000); // Allow time for execution

        assertTrue(jobs.get(0).getIsCompleted(), "First job should be completed");
        assertTrue(jobs.get(1).getIsCompleted(), "Second job should be completed");
        assertTrue(jobs.get(2).getIsCompleted(), "Third job should be completed");

        fcfsScheduler.stopScheduler();
    }

    @Test
    void testSJFScheduling() throws InterruptedException {
        for (Job job : jobs) {
            sjfScheduler.addJob(job);
        }

        Thread.sleep(6000); // Allow time for execution

        // Jobs should complete in order of shortest execution time first
        assertTrue(jobs.get(1).getIsCompleted(), "Shortest job should be completed first");
        assertTrue(jobs.get(2).getIsCompleted(), "Second shortest job should be completed next");
        assertTrue(jobs.get(0).getIsCompleted(), "Longest job should be completed last");

        sjfScheduler.stopScheduler();
    }

    @Test
    void testPriorityScheduling() throws InterruptedException {
        for (Job job : jobs) {
            priorityScheduler.addJob(job);
        }

        Thread.sleep(6000); // Allow time for execution

        // Jobs should complete in order of highest priority first
        assertTrue(jobs.get(0).getIsCompleted(), "Highest priority job should be completed first");
        assertTrue(jobs.get(2).getIsCompleted(), "Second highest priority job should be completed next");
        assertTrue(jobs.get(1).getIsCompleted(), "Lowest priority job should be completed last");

        priorityScheduler.stopScheduler();
    }

    @Test
    void testThreadSafeJobAddition() {
        assertDoesNotThrow(() -> {
            Thread thread1 = new Thread(() -> fcfsScheduler.addJob(new Job("Job-4", 1, 500,null)));
            Thread thread2 = new Thread(() -> fcfsScheduler.addJob(new Job("Job-5", 2, 700,null)));

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();
        }, "Job addition should be thread-safe");
    }
}
