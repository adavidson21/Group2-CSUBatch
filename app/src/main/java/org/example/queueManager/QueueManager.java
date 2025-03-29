package org.example.queueManager;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.example.common.Job;

/**
 * The QueueManager is a wrapper around a BlockingQueue and manages the queue.
 */
public class QueueManager {
  private final Queue<Job> jobQueue = new LinkedList<>();
  private final int MAX_QUEUE_CAPACITY = 10;
  private final Lock lock = new ReentrantLock();
  private final Condition notEmpty = lock.newCondition();
  private final Condition notFull = lock.newCondition();


  /**
   * Adds a job to the queue. Used by Submitter which gets job data from Scheduler.
   * @param job The job to add.
   * @throws InterruptedException If the thread is interrupted while waiting for space on queue to become available.
   */
  public void enqueueJob(Job job) throws InterruptedException {
    //acquire lock
    lock.lock();
    try {
      while (jobQueue.size() == MAX_QUEUE_CAPACITY) {
        // queue is full, must wait for space to become available
        notFull.await();
      }
      // space is available, adding job to queue
      jobQueue.add(job);
      // let calling thread know that queue is not empty
      notEmpty.signal();
    } finally {
      // release lock so other thread can access queue
      lock.unlock();
    }
  }

    /**
     * Removes a job from the head of the queue. Used by Dispatcher.
     * @throws InterruptedException If the thread is interrupted while waiting for a job to become available.
     */
  public Job dequeueJob() throws InterruptedException {
    //acquire lock
    lock.lock();
    try {
      while (jobQueue.isEmpty()) {
        // queue is empty, must wait for job to become available
        notEmpty.await();
      }
      // job is available, removing job from queue
      Job job = jobQueue.remove();
      // let calling thread know that queue is not full
      notFull.signal();
      return job;
    } finally {
      // release lock so other thread can access queue
      lock.unlock();
    }
  }

    /**
     * Gets the current size of the queue.
     * @return The size of the queue.
     */
  public int getQueueSize() {
    return jobQueue.size();
  }

  public void listQueue() {
    if (!jobQueue.isEmpty()) {
      int i = 1;
      for (Job currJob : jobQueue) {
        System.out.println(i + ". " + currJob.getName() + " " + currJob.getExecutionTime() / 1000 + " seconds " + currJob.getExecutionPriority() + " " + currJob.getArrivalTime());
        i++;
      }
    } else {
      System.out.println("Queue Currently Empty.");
    }
  }

}
