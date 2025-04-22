package org.example.dispatcher;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.example.common.Job;
import org.example.fileLogger.FileLogger;
import org.example.perfEvaluator.PerfEvaluator;
import org.example.queueManager.QueueManager;


/**
 * The Dispatcher governs a thread that executes submitted jobs.
 */
public class Dispatcher implements Runnable {
    private final QueueManager queueManager;
    private final PerfEvaluator perfEvaluator;
    private static final Logger fileLogger = FileLogger.getLogger();
    private CountDownLatch jobCompletionLatch;

    private volatile boolean isRunning = true;
    private boolean isBatchMode = false;
    private boolean isPerfMode = false;

    /**
     * Dispatcher constructor.
     * @param queueManager The queue manager instance.
     * @param perfEvaluator The performance evaluator instance.
     */
    public Dispatcher(QueueManager queueManager, PerfEvaluator perfEvaluator) {
        this.queueManager = queueManager;
        this.perfEvaluator = perfEvaluator;
    }

    /**
     * Simulates the duration of the job based on its execution time.
     * @param job The Job.
     */
    void simulateJobDuration(Job job) {
        try {
            job.setActualProcessingStartTime(LocalDateTime.now());
            Thread.sleep(job.getExecutionTime()); // user input is in seconds, convert to milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (jobCompletionLatch != null) {
                // this latch keeps track of the number of jobs that have completed
                jobCompletionLatch.countDown();
            }
            // mark job itself as completed
            job.setIsCompleted(true);
            job.setActualCompletionTime(LocalDateTime.now());
            // add to perfEvaluator for tracking
            this.perfEvaluator.addCompletedJob(job);
        }
    }

    /**
     * Executes the batch job.
     * @param job The batch Job to execute.
     */
    void executeBatchJob(Job job) {
        fileLogger.info(String.format("Job %s | Status: Started %n", job.getName()));
        this.simulateJobDuration(job);
        // write job completion to a file
        fileLogger.info(String.format("Job %s | Status: Completed | Execution Duration: %d seconds. %n", job.getName(), job.getExecutionTime() / 1000));
    }

    /**
     * Executes a job.
     * @param job The Job to execute.
     */
    void executeJob(Job job) {
        System.out.printf("Dispatcher: executing job: %s.%n", job.getName());
        this.simulateJobDuration(job);
        if (!this.getIsPerfMode()) {
            System.out.printf("Dispatcher: Job: %s has completed in %d seconds. %n", job.getName(), job.getExecutionTime() / 1000);
        }
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                if (!isBatchMode && !isPerfMode) {
                    // only display under normal run circumstances
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
                isRunning = false;
            }
        }
    }

    //region Getters and Setters
    public void setCountdownLatch(CountDownLatch latch) {
        this.jobCompletionLatch = latch;
    }

    public void setIsBatchMode(boolean isBatchMode) {
        this.isBatchMode = isBatchMode;
    }

    public boolean getIsBatchMode() {
        return this.isBatchMode;
    }

    public void setIsPerfMode(boolean isPerfMode) {
        this.isPerfMode = isPerfMode;
    }

    public boolean getIsPerfMode() {
        return this.isPerfMode;
    }
    //endregion
}
