package org.example.dispatcher;

import org.example.common.Job;
import java.util.concurrent.BlockingQueue;

/**
 * Dispatcher receives jobs from the Scheduler's scheduled job queue and executes them.
 */
public class Dispatcher implements Runnable {
    private final BlockingQueue<Job> scheduledJobs;
    private volatile boolean isRunning = true;

    public Dispatcher(BlockingQueue<Job> scheduledJobs) {
        this.scheduledJobs = scheduledJobs;
    }

    void executeJob(Job job) {
        System.out.println("Executing: " + job.getName() +
                " (Priority: " + job.getExecutionPriority() +
                ", Execution Time: " + job.getExecutionTimeMs() + "ms)");
        try {
            Thread.sleep(job.getExecutionTimeMs());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        job.setIsCompleted(true);
        System.out.println("Completed: " + job.getName());
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Job job = scheduledJobs.take(); // Block until a job is available
                executeJob(job);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Dispatcher interrupted.");
            }
        }
    }
}
