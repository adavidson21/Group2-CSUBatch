package org.example.queueManager;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.example.common.Job;

/**
 * The QueueManager manages job queues: one for new jobs and one for jobs scheduled for execution.
 */
public class QueueManager {
    private final Queue<Job> newJobQueue = new LinkedList<>();
    private final Queue<Job> scheduledJobQueue = new LinkedList<>();
    private final int MAX_QUEUE_CAPACITY = 10;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    // Enqueue a new job submitted by the user/UI
    public void enqueueJob(Job job) throws InterruptedException {
        lock.lock();
        try {
            while (newJobQueue.size() == MAX_QUEUE_CAPACITY) {
                notFull.await();
            }
            newJobQueue.add(job);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Dequeue a job for scheduling (used by Scheduler)
    public Job dequeueNewJob() throws InterruptedException {
        lock.lock();
        try {
            while (newJobQueue.isEmpty()) {
                notEmpty.await();
            }
            Job job = newJobQueue.poll();
            notFull.signalAll();
            return job;
        } finally {
            lock.unlock();
        }
    }

    // Enqueue a job that has been scheduled (used by Scheduler)
    public void enqueueScheduledJob(Job job) throws InterruptedException {
        lock.lock();
        try {
            while (scheduledJobQueue.size() == MAX_QUEUE_CAPACITY) {
                notFull.await();
            }
            scheduledJobQueue.add(job);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Dequeue a job for execution (used by Dispatcher)
    public Job dequeueScheduledJob() throws InterruptedException {
        lock.lock();
        try {
            while (scheduledJobQueue.isEmpty()) {
                notEmpty.await();
            }
            Job job = scheduledJobQueue.poll();
            notFull.signalAll();
            return job;
        } finally {
            lock.unlock();
        }
    }

    //  New: Used by Scheduler to check if it’s done collecting jobs for sorting
    public boolean isNewJobQueueEmpty() {
        lock.lock();
        try {
            return newJobQueue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    // Optional: Debugging helper
    public void listQueue() {
        lock.lock();
        try {
            System.out.println("Current new jobs in queue:");
            for (Job job : newJobQueue) {
                System.out.println("- " + job.getName());
            }
        } finally {
            lock.unlock();
        }
    }
}
