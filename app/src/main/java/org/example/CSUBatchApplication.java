/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.util.Scanner;

import org.example.uiController.UIController;

public class CSUBatchApplication {
    public String getGreeting() {
        return "System Loading...";
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(new CSUBatchApplication().getGreeting());
         UIController UI = new UIController(new Scanner(System.in));
         UI.generateUI();
         UI.userInteraction();

        // CODE FOR TESTING DISPATCHER, IGNORE FOR NOW
        // WILL DELETE ONCE WE HAVE MORE OF THE PROJECT IMPLEMENTED
//        QueueManager queueManager = new QueueManager();
//        try {
//            // example of adding job to queue for testing dispatcher
//            queueManager.enqueueJob(new Job("Job1", 1, 1000));
//            queueManager.enqueueJob(new Job("Job2", 1, 1000));
//            queueManager.enqueueJob(new Job("Job3", 1, 1000));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Dispatcher dispatcher = new Dispatcher(queueManager);
//        Thread dispatcherThread = new Thread(dispatcher);
//        dispatcherThread.start();
    }
}
