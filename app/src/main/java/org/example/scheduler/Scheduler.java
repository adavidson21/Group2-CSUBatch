package org.example.scheduler;

import org.example.common.Job;
import org.example.queueManager.QueueManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scheduler {
    private final QueueManager queueManager;
    private final SchedulingPolicy policy;
    private volatile boolean isRunning = true;
    private Thread schedulerThread;

    public Scheduler(SchedulingPolicy policy, QueueManager queueManager) {
        this.policy = policy;
        this.queueManager = queueManager;
        startSchedulerThread();
    }

    private void startSchedulerThread() {
        schedulerThread = new Thread(() -> {
            while (isRunning) {
                try {
                    // Wait until at least one job is available
                    Job firstJob = queueManager.dequeueNewJob();

                    List<Job> buffer = new ArrayList<>();
                    buffer.add(firstJob);

                    // Small window to allow more jobs to be submitted
                    Thread.sleep(500);

                    while (!queueManager.isNewJobQueueEmpty()) {
                        Job job = queueManager.dequeueNewJob();
                        buffer.add(job);
                    }

                    // Apply the appropriate scheduling policy
                    switch (policy) {
                        case SJF:
                            buffer.sort(Comparator.comparingLong(Job::getExecutionTimeMs));
                            break;
                        case PRIORITY:
                            buffer.sort(Comparator.comparingInt(Job::getExecutionPriority));
                            break;
                        case FCFS:
                        default:
                            // No sorting needed
                            break;
                    }

                    // Enqueue the sorted jobs into the scheduled queue
                    for (Job job : buffer) {
                        queueManager.enqueueScheduledJob(job);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        schedulerThread.start();
    }

    public void stop() {
        isRunning = false;
        schedulerThread.interrupt();
    }

    public SchedulingPolicy getPolicy() {
        return policy;
    }
}
