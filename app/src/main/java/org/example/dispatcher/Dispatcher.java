package org.example.dispatcher;

import java.util.logging.Logger;

import org.example.common.Job;
import org.example.fileLogger.FileLogger;
import org.example.queueManager.QueueManager;

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
            Thread.sleep(job.getExecutionTime()); // user input is in seconds, convert to milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // mark job as completed
        job.setIsCompleted(true);
    }

    void executeBatchJob(Job job) {
        fileLogger.info(String.format("Job %s | Status: Started %n", job.getName()));
        this.simulateJobDuration(job);
        // write job completion to a file
        fileLogger.info(String.format("Job %s | Status: Completed | Execution Duration: %d seconds. %n", job.getName(), job.getExecutionTime() / 1000));
    }

    void executeJob(Job job) {
        System.out.printf("Dispatcher: executing job: %s.%n", job.getName());
        this.simulateJobDuration(job);
        System.out.printf("Dispatcher: Job: %s has completed in %d seconds. %n", job.getName(), job.getExecutionTime() / 1000);
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                if (!isBatchMode) {
                    System.out.printf("Dispatcher: current queue size: %s. %n", this.queueManager.getQueueSize());
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
                isRunning = false;
            }
        }
    }
}
