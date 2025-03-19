package org.example.dispatcher;

import org.example.common.Job;
import org.example.fileLogger.FileLogger;
import org.example.queueManager.QueueManager;

import java.util.logging.Logger;

/**
 * The Dispatcher governs a thread that executes submitted jobs.
 */
public class Dispatcher implements Runnable {
    private final QueueManager queueManager;
    private static final Logger fileLogger = FileLogger.getLogger();
    private volatile boolean isRunning = true;
    private boolean isBatchMode = false;

    public Dispatcher(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public void setIsBatchMode(boolean isBatchMode) {
        this.isBatchMode = isBatchMode;
    }

    public boolean getIsBatchMode() {
        return this.isBatchMode;
    }

    void simulateJobDuration(Job job) {
        try {
            Thread.sleep(job.getExecutionTimeMs());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // mark job as completed
        job.setIsCompleted(true);
    }

    void executeBatchJob(Job job) {
        fileLogger.info("Job: " + job.getName() + " | Status: Started");
        this.simulateJobDuration(job);
        // write job completion to a file
        fileLogger.info("Job: " + job.getName() + " | Status: Completed | Execution Duration: " + job.getExecutionTimeMs() + "ms");
    }

    void executeJob(Job job) {
        System.out.println("Dispatcher: executing job: " + job.getName());
        this.simulateJobDuration(job);
        System.out.println("Dispatcher: Job: " + job.getName() + " has completed in " + job.getExecutionTimeMs() + "ms");

    }

    @Override
    public void run() {
        while(isRunning){
            try {
                if (!isBatchMode) {
                    System.out.println("Dispatcher: current queue size: " + this.queueManager.getQueueSize());
                }
                // pull jobs from the queue and execute them
                Job jobFromQueue = this.queueManager.dequeueJob();
                if (jobFromQueue.getIsCompleted()) {
                    isRunning = false;
                    break;
                }
                if (isBatchMode) {
                    this.executeBatchJob(jobFromQueue);
                } else {
                    this.executeJob(jobFromQueue);
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Dispatcher: thread interrupted. Exiting process.");
                e.printStackTrace();
            }
        }
    }
}
