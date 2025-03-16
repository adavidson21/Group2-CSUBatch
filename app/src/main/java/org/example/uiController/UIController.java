package org.example.uiController;

import java.time.LocalDateTime;
import java.util.Scanner;

import org.example.common.Job;
import org.example.dispatcher.Dispatcher;
import org.example.queueManager.QueueManager;
import org.example.scheduler.Scheduler;
import org.example.scheduler.SchedulingPolicy;

/**
 * The UI Controller is responsible for handling the user interface of the application.
 */
public class UIController {
    private final Scanner userInput;
    private QueueManager job_queue = new QueueManager();
    private Dispatcher dispatcher = new Dispatcher(job_queue);
    private Thread dispatcherThread;
    private Scheduler scheduler = new Scheduler(SchedulingPolicy.FCFS);

    public UIController(Scanner scanner) {
        this.userInput = scanner;
    }

    public void generateUI() {
        System.out.println("Welcome to the CSUBatch Scheduling Application");
        System.out.println("Thank you for downloading.");
        System.out.println("This System is meant to act as a scheduling application where jobs can be added to a queue that will be arranged based \n on the selected priority. \n commands: run, list, policy_change, help, exit");
        //userInteraction();
    }

    public void userInteraction() {
        System.out.println("Please Enter Command: ");
        String command = userInput.nextLine();
        String[] commandarr = command.split(" "); //an array in place so the commands that are more than one word can be parsed.
        while (!"exit".equals(command)) {
            if (commandarr[0].equals("run")) {
                if (commandarr.length != 4) {
                    System.out.println("Invalid run command please try again");
                    break;
                } else {
                    try { //Try in place to ensure that the user enters the run command format correctly and if not throws an error that does not crash the system.
                        String job_name = commandarr[1];
                        String job_time = commandarr[2];
                        String job_priority = commandarr[3];
                        LocalDateTime currentDate = LocalDateTime.now();
                        int job_time_int = Integer.parseInt(job_time);
                        int job_priority_int = Integer.parseInt(job_priority);
                        Job userSubmitted = new Job(job_name, job_priority_int, job_time_int, currentDate);
                        /* TODO: The Scheduler needs to be doing this work on the next two lines, not the UIController
                           TODO: UIController just passes the params to the Scheduler */
                        job_queue.enqueueJob(userSubmitted);
                        System.out.println("Job: " + job_name + " added to queue");
                        if (dispatcherThread == null) {
                            dispatcherThread = this.startThread("Dispatcher", dispatcher);
                        }
                    } catch (InterruptedException | NumberFormatException e) {
                        System.out.println("Sorry time and priority must be able to be converted to integer try again");
                    }
                }
            } else if (command.equals("list")) {
                System.out.println("Scheduling Policy: " + scheduler.getPolicy());
                System.out.println(" Job_Name CPU_Time Priority Arrival_Time State");
                job_queue.listQueue();
            } else if (commandarr[0].equals("policy_change")) {
                if (commandarr.length != 2) {
                    System.out.println("invald policy_change command please try again");
                } else {
                    //Enter policy change functionality here.
                    System.out.println("policy change successful");
                }
            } else if (command.equals("help")) {
                System.out.println("User has enter help");
                System.out.println("Command List:");
                System.out.println("run <job name> <job time> <priority> - Will add a job to the system");
                System.out.println("list - Print out the current job queue.");
                System.out.println("policy_change <policy> - will change the policy to the new entered one and restructure queue.");
                System.out.println("exit - End System processes and perform benchmark on close");
            } else {
                System.out.println("Sorry command unrecognized try again");
            }
            System.out.println("Please Enter a command:");
            commandarr = null; //empties the array so new commands can be entered
            command = userInput.nextLine();
            commandarr = command.split(" "); //refills the array with new entries.
        }
        System.out.println("System ending...");
        // cleanup loose threads
        this.endThread("Dispatcher", dispatcherThread);

    }

    /**
     * Starts a new thread for either dispatcher or scheduler
     *
     * @param className - The name of the class that the thread is being started for
     * @param runnable  - The runnable object that the thread will execute (scheduler or dispatcher)
     */
    Thread startThread(String className, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.printf("%s: Started thread. %n", className);
        return thread;
    }

    /**
     * Ends a thread and returns it to the resource pool
     *
     * @param className - The name of the class that the thread is being ended for
     * @param thread    - The thread that is being returned to the resource pool
     */
    void endThread(String className, Thread thread) {
        try {
            if (thread != null) {
                System.out.printf("%s: Returning thread to resource pool. Exiting process. %n", className);
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
