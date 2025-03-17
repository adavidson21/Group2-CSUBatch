package org.example.uiController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final QueueManager job_queue;
    private Scheduler scheduler;
    private Dispatcher dispatcher;
    private Thread dispatcherThread;
    private SchedulingPolicy currentPolicy = SchedulingPolicy.FCFS;

    // Batch mode variables
    private boolean batchMode = false;
    private List<Job> batchJobs = new ArrayList<>();

    public UIController(Scanner scanner) {
        this.userInput = scanner;
        this.job_queue = new QueueManager();
        this.scheduler = new Scheduler(currentPolicy, job_queue);
        this.dispatcher = new Dispatcher(scheduler.getScheduledJobQueue());

        dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
    }

    public void generateUI() {
        System.out.println("Welcome to the CSUBatch Scheduling Application");
        System.out.println("Thank you for downloading.");
        System.out.println("This system acts as a job scheduler where jobs can be added to a queue and scheduled based on a selected policy.");
        System.out.println("Commands: run <jobName> <execTimeMs> <priority>, list, policy_change <FCFS|SJF|Priority>, help, exit");
        System.out.println("Batch Mode: batch_start, batch_execute");
    }

    public void userInteraction() {
        System.out.println("Please Enter Command: ");
        String command;
        String[] commandarr;

        while (true) {
            System.out.print("> ");
            command = userInput.nextLine().trim();
            commandarr = command.split(" ");

            if (command.equalsIgnoreCase("exit")) break;
            if (commandarr.length == 0) continue;

            switch (commandarr[0].toLowerCase()) {
                case "run":
                    handleRun(commandarr);
                    break;
                case "list":
                    handleList();
                    break;
                case "policy_change":
                    handlePolicyChange(commandarr);
                    break;
                case "help":
                    printHelp();
                    break;
                case "batch_start":
                    batchMode = true;
                    batchJobs.clear();
                    System.out.println("Batch mode started. Jobs will be held until 'batch_execute'.");
                    break;
                case "batch_execute":
                    batchMode = false;
                    for (Job job : batchJobs) {
                        try {
                            job_queue.enqueueJob(job);
                            System.out.println("Batch job added: " + job.getName());
                        } catch (InterruptedException e) {
                            System.out.println("Failed to enqueue job: " + job.getName());
                        }
                    }
                    batchJobs.clear();
                    System.out.println("Batch execution started.");
                    break;
                default:
                    System.out.println("Sorry, command unrecognized. Type 'help' for list of commands.");
            }
        }

        scheduler.stopScheduler();
        System.out.println("System ending...");
        this.endThread("Dispatcher", dispatcherThread);
    }

    private void handleRun(String[] commandarr) {
        if (commandarr.length != 4) {
            System.out.println("Invalid run command. Usage: run <job name> <job time> <priority>");
            return;
        }

        try {
            String job_name = commandarr[1];
            long job_time = Long.parseLong(commandarr[2]);
            int job_priority = Integer.parseInt(commandarr[3]);

            LocalDateTime currentDate = LocalDateTime.now();
            Job userSubmitted = new Job(job_name, job_priority, job_time, currentDate);

            if (batchMode) {
                batchJobs.add(userSubmitted);
                System.out.println("Job staged for batch: " + job_name);
            } else {
                job_queue.enqueueJob(userSubmitted);
                System.out.println("Job added: " + job_name);
            }

        } catch (NumberFormatException | InterruptedException e) {
            System.out.println("Time and priority must be valid numbers.");
        }
    }

    private void handleList() {
        System.out.println("Scheduling Policy: " + scheduler.getPolicy());
        System.out.println("Job_Name | CPU_Time | Priority | Arrival_Time | State");
        job_queue.listQueue();
    }

    private void handlePolicyChange(String[] commandarr) {
        if (commandarr.length != 2) {
            System.out.println("Invalid command. Usage: policy_change <FCFS|SJF|Priority>");
            return;
        }

        try {
            SchedulingPolicy newPolicy = SchedulingPolicy.valueOf(commandarr[1].toUpperCase());
            currentPolicy = newPolicy;

            scheduler.stopScheduler();
            scheduler = new Scheduler(currentPolicy, job_queue);
            dispatcher = new Dispatcher(scheduler.getScheduledJobQueue());
            dispatcherThread = new Thread(dispatcher);
            dispatcherThread.setDaemon(true);
            dispatcherThread.start();

            System.out.println("Scheduling policy changed to: " + currentPolicy);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid policy. Choose from: FCFS, SJF, Priority.");
        }
    }

    private void printHelp() {
        System.out.println("Command List:");
        System.out.println("run <job name> <job time> <priority> - Add a job to the system.");
        System.out.println("list - Print out the current job queue.");
        System.out.println("policy_change <policy> - Change scheduling policy (FCFS, SJF, Priority).");
        System.out.println("batch_start - Enter batch mode to queue multiple jobs without execution.");
        System.out.println("batch_execute - Submit all batched jobs to the scheduler.");
        System.out.println("exit - End the system.");
    }

    void endThread(String className, Thread thread) {
        try {
            if (thread != null) {
                System.out.printf("%s: Returning thread to resource pool. Exiting process.%n", className);
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
