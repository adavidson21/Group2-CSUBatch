package org.example.scheduler;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.example.common.Job;
import org.example.queueManager.QueueManager;

/**
 * Scheduler selects the next job based on the chosen policy (FCFS, SJF, Priority),
 * and passes it to the Dispatcher (via the scheduledJobQueue).
 */
public class Scheduler {
    private final QueueManager queueManager;
    private final SchedulingPolicy policy;
    private final BlockingQueue<Job> scheduledJobQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = true;
    private Thread schedulerThread;

    public Scheduler(SchedulingPolicy policy, QueueManager queueManager) {
        this.policy = policy;
        this.queueManager = queueManager;
        startSchedulerThread();
    }

    /**
     * Allows Dispatcher to receive jobs selected by Scheduler.
     */
    public BlockingQueue<Job> getScheduledJobQueue() {
        return scheduledJobQueue;
    }

    /**
     * Starts the internal scheduler thread to fetch and forward jobs.
     */
    private void startSchedulerThread() {
        schedulerThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Job jobToExecute = selectNextJob();
                    if (jobToExecute != null) {
                        scheduledJobQueue.put(jobToExecute);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }

    /**
     * Selects the next job from the queue based on the current policy.
     */
    private Job selectNextJob() throws InterruptedException {
        List<Job> buffer = new ArrayList<>();

        Job firstJob = queueManager.dequeueJob();
        buffer.add(firstJob);
        Thread.sleep(50);

        while (queueManager.getQueueSize() > 0) {
            buffer.add(queueManager.dequeueJob());
        }

        if (buffer.isEmpty()) return null;

        Job selected;
        switch (policy) {
            case SJF:
                selected = Collections.min(buffer, Comparator.comparingLong(Job::getExecutionTimeMs));
                break;
            case Priority:
                selected = Collections.max(buffer, Comparator.comparingInt(Job::getExecutionPriority));
                break;
            case FCFS:
            default:
                selected = buffer.get(0);
                break;
        }

        buffer.remove(selected);
        for (Job job : buffer) {
            queueManager.enqueueJob(job);
        }

        return selected;
    }

    /**
     * Stops the scheduler thread.
     */
    public void stopScheduler() {
        isRunning = false;
        schedulerThread.interrupt();
    }

    public SchedulingPolicy getPolicy() {
        return policy;
    }
}
