package org.example.dispatcher;

import org.example.common.Job;
import org.example.queueManager.QueueManager;

/**
 * Dispatcher receives jobs from the Scheduler's scheduled job queue and executes them.
 */
public class Dispatcher implements Runnable {
    private final QueueManager queueManager;
    private volatile boolean isRunning = true;

    public Dispatcher(QueueManager queueManager) {
        this.queueManager = queueManager;
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
                Job job = queueManager.dequeueScheduledJob();
                if (job != null) {
                    executeJob(job);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}
