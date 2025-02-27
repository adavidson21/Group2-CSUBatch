package org.example;

import org.example.scheduler.Scheduler;
import org.example.scheduler.SchedulingPolicy;
import org.example.common.Job;

public class Main {
    public static void main(String[] args) {
        // Choose scheduling policy (FCFS, SJF, or Priority)
        Scheduler scheduler = new Scheduler(SchedulingPolicy.SJF); // Change to FCFS or Priority

        // Add jobs to the scheduler
        scheduler.addJob(new Job("Job-1", 3, 2000));  // Priority 3, Execution time 2000ms
        scheduler.addJob(new Job("Job-2", 1, 1000));  // Priority 1, Execution time 1000ms
        scheduler.addJob(new Job("Job-3", 2, 1500));  // Priority 2, Execution time 1500ms

        try {
            Thread.sleep(10_000); // Allow jobs to execute
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scheduler.stopScheduler();
    }
}
