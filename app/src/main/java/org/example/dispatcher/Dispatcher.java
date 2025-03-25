package org.example.dispatcher;

import org.example.common.Job;
import org.example.queueManager.QueueManager;

/**
 * The Dispatcher governs a thread that executes submitted jobs.
 */
public class Dispatcher implements Runnable {
    private final QueueManager queueManager;

    private volatile boolean isRunning = true;

    public Dispatcher(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    void executeJob(Job job) {
        System.out.println("Dispatcher: executing job: " + job.getName());
        try {
            Thread.sleep(job.getExecutionTimeMs());
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // mark job as completed
        job.setIsCompleted(true);
        System.out.println("Dispatcher: Job: " + job.getName() + " has completed in " + job.getExecutionTimeMs() + "ms");

    }

    @Override
    public void run() {
        while(isRunning){
            try {
                System.out.println("Dispatcher: current queue size: " + this.queueManager.getQueueSize());
                // pull jobs from the queue and execute them
                Job jobFromQueue = this.queueManager.dequeueJob();
                if (jobFromQueue.getIsCompleted()) {
                    isRunning = false;
                    break;
                }
                this.executeJob(jobFromQueue);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Dispatcher: thread interrupted. Exiting process.");
                isRunning = false;
                e.printStackTrace();
            }
        }
    }
}
