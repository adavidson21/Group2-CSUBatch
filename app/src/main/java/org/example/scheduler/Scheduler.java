package org.example.scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.example.common.Job;
import org.example.queueManager.QueueManager;

/**
 * The Scheduler governs a thread that enforces scheduling policies for submitted jobs.
 * It is responsible for managing the 3 scheduling policies: FCFS, SJF, and Priority.
 */
public class Scheduler implements Runnable{
    private final QueueManager jobQueue;
    private final ArrayList<Job> mutateList = new ArrayList<>();
    private final ArrayList<Job> originalList = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private SchedulingPolicy policy;

    /**
     * Scheduler constructor.
     * @param policy The scheduling policy.
     * @param queue The queue manager instance.
     */
    public Scheduler(SchedulingPolicy policy, QueueManager queue) {
        this.policy = policy;
        this.jobQueue = queue;
    }

    /**
     * Adds a new job to the scheduler in a thread-safe manner.
     * @param job The Job.
     * @throws InterruptedException An exception when the processing is interrupted.
     */
    public void addJob(Job job) throws InterruptedException {
        lock.lock();
        try {
            originalList.add(job);
            jobQueue.enqueueJob(job);
            switch(policy){
                case FCFS -> manageFCFS();
                case PRIORITY -> managePriority();
                case SJF -> manageSJF();
                default -> {
                }
            }
            condition.signal(); // Notify scheduler that a job is available
        } finally {
            lock.unlock();
        }
    }

    /**
     * Updates the list of jobs.
     */
    private void updateList(){
        List<Job> jobsToRemove = new ArrayList<>();
        for(Job job : originalList ){
            if(!jobQueue.checkForJob(job)){
                jobsToRemove.add(job);
            }
        }
        for(Job job : jobsToRemove){
            originalList.remove(job);
        }
    }

    /**
     * Manages the first come first serve scheduling policy.
     * @throws InterruptedException An exception when interrupted.
     */
    private void manageFCFS() throws InterruptedException{
        updateList();
        jobQueue.empty();
        for(Job job : originalList){
            jobQueue.enqueueJob(job);
        }
    }

    /**
     * Manages the shortest job first scheduling policy.
     * @throws InterruptedException An exception when interrupted.
     */
    private void manageSJF() throws InterruptedException{
        updateList();
        jobQueue.empty();
        for(Job job : originalList){
            mutateList.add(job); //adds all jobs to a mutable list so we can reupload to queue in sorted order.
        }
        mutateList.sort(Comparator.comparingLong(Job::getExecutionTime));
        for(Job job : mutateList){
            jobQueue.enqueueJob(job);
        }
        mutateList.clear();
    }

    /**
     * Manages the priority scheduling policy.
     * @throws InterruptedException An exception when interrupted.
     */
    private void managePriority() throws InterruptedException{
        updateList();
        jobQueue.empty();
        for(Job job : originalList){
            mutateList.add(job); //adds all jobs to a mutable list so we can reupload to queue in sorted order.
        }
        mutateList.sort(Comparator.comparingInt(Job::getExecutionPriority));
        for(Job job : mutateList){
            jobQueue.enqueueJob(job);
        }
        mutateList.clear();
    }

    /**
     * Sets the scheduling policy.
     * @param newPolicy The new scheduling policy to be set.
     * @throws InterruptedException An exception when interrupted.
     */
    public void setPolicy(SchedulingPolicy newPolicy ) throws InterruptedException{
        this.policy = newPolicy;
        switch(policy){
            case FCFS -> manageFCFS();
            case PRIORITY -> managePriority();
            case SJF -> manageSJF();
            default -> {
            }

        }
    }

    /**
     * Stops the scheduler gracefully.
     * @return The scheduling policy.
     */
    public SchedulingPolicy getPolicy(){
        return policy;
    }
    
    @Override
    public void run(){

    }
}
