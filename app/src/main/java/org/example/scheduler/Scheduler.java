package org.example.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.example.common.Job;

/**
 * The Scheduler governs a thread that enforces scheduling policies for submitted jobs.
 * It is responsible for managing the 3 scheduling policies: FCFS, SJF, and Priority.
 */
public class Scheduler {
    private final List<Job> jobQueue;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final SchedulingPolicy policy;
    private boolean isRunning = true;

    public Scheduler(SchedulingPolicy policy) {
        this.policy = policy;
        this.jobQueue = new ArrayList<>();
        startSchedulerThread();
    }

    /**
     * Adds a new job to the scheduler in a thread-safe manner.
     */
    public void addJob(Job job) {
        lock.lock();
        try {
            jobQueue.add(job);
            condition.signal(); // Notify scheduler that a job is available
        } finally {
            lock.unlock();
        }
    }

    /**
     * Starts a separate thread that continuously schedules and executes jobs.
     */
    private void startSchedulerThread() {
        Thread schedulerThread = new Thread(() -> {
            while (isRunning) {
                Job jobToExecute = null;
                lock.lock();
                try {
                    while (jobQueue.isEmpty()) {
                        condition.await(); // Wait until jobs are available
                    }
                    jobToExecute = getNextJob();
                    jobQueue.remove(jobToExecute);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }

                // Execute the job outside the lock
                if (jobToExecute != null) {
                    executeJob(jobToExecute);
                }
            }
        });
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }

    /**
     * Determines the next job to execute based on the active scheduling policy.
     */
    private Job getNextJob() {
        switch (policy) {
            case SJF:
                return Collections.min(jobQueue, Comparator.comparingLong(Job::getExecutionTime));
            case Priority:
                return Collections.max(jobQueue, Comparator.comparingInt(Job::getExecutionPriority));
            case FCFS:
            default:
                return jobQueue.get(0); // First job in queue
        }
    }

    /**
     * Simulates job execution.
     */
    private void executeJob(Job job) {
        System.out.println("Executing: " + job.getName() + " (Priority: " + job.getExecutionPriority() +
                ", Execution Time: " + job.getExecutionTime() + "ms)");

        try {
            Thread.sleep(job.getExecutionTime()); // Simulate execution
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        job.setIsCompleted(true);
        System.out.println("Completed: " + job.getName());
    }

    /**
     * Stops the scheduler gracefully.
     */
    public void stopScheduler() {
        isRunning = false;
    }
    public SchedulingPolicy getPolicy(){
        return policy;
    }
}
