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
    private final List<Job> mutateList = new ArrayList<>();
    private final List<Job> originalList = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private SchedulingPolicy policy;

    public Scheduler(SchedulingPolicy policy, QueueManager queue) {
        this.policy = policy;
        this.jobQueue = queue;
    }

    /**
     * Adds a new job to the scheduler in a thread-safe manner.
     */
    public void addJob(Job job) throws InterruptedException {
        lock.lock();
        try {
            originalList.add(job);
            jobQueue.enqueueJob(job);
            condition.signal(); // Notify scheduler that a job is available
        } finally {
            lock.unlock();
        }
    }
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
    private void manageFCFS() throws InterruptedException{
        updateList();
        jobQueue.empty();
        for(Job job : originalList){
            jobQueue.enqueueJob(job);
        }
    }
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

    public void setPolicy(SchedulingPolicy newPolicy ) throws InterruptedException{
        this.policy = newPolicy;
        switch(policy){
            case FCFS -> manageFCFS();
            case Priority -> managePriority();
            case SJF -> manageSJF();
            default -> {
            }

        }

    }
    /**
     * Stops the scheduler gracefully.
     */
    public SchedulingPolicy getPolicy(){
        return policy;
    }
    @Override
    public void run(){

    }
}
