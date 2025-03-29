package org.example.uiController;

import org.example.common.Job;
import org.example.dispatcher.Dispatcher;
import org.example.queueManager.QueueManager;
import org.example.scheduler.Scheduler;

public class UIController {
    private final Scheduler scheduler;
    private final QueueManager queueManager;
    private Dispatcher dispatcher;

    public UIController(Scheduler scheduler, QueueManager queueManager) {
        this.scheduler = scheduler;
        this.queueManager = queueManager;
        this.dispatcher = new Dispatcher(queueManager);
    }

    public void submitJob(Job job) {
        try {
            queueManager.enqueueJob(job);
            System.out.println("Job submitted: " + job.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Failed to submit job: " + job.getName());
        }
    }

    public void startDispatcher() {
        if (dispatcher != null) {
            Thread dispatcherThread = new Thread(dispatcher);
            dispatcherThread.start();
            System.out.println("Dispatcher started.");
        }
    }
}
