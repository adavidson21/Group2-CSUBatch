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

import static org.example.constants.ConsoleMessages.*;

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
        System.out.println(WELCOME_MESSAGE);
        System.out.println(SYSTEM_INTRO);
        System.out.println(COMMANDS_OVERVIEW);
    }

    public void userInteraction() {
        System.out.println(PROMPT_MESSAGE);

        while (true) {
            System.out.print("> ");
            String commandLine = userInput.nextLine().trim();
            if (commandLine.isEmpty()) {
                continue; // skip blank lines
            }

            String[] commandArr = commandLine.split("\\s+");
            Command cmd = CommandParser.parseCommand(commandArr[0]);
            if (cmd == Command.EXIT) {
                break;
            }

            switch (cmd) {
                case RUN:
                    handleRun(commandArr);
                    break;
                case LIST:
                    handleList();
                    break;
                case POLICY_CHANGE:
                    handlePolicyChange(commandArr);
                    break;
                case HELP:
                    System.out.println(HELP_BLOCK);
                    break;
                case BATCH_START:
                    batchMode = true;
                    batchJobs.clear();
                    System.out.println(BATCH_START_MESSAGE);
                    break;
                case BATCH_EXECUTE:
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
                    System.out.println(BATCH_EXECUTION_MESSAGE);
                    break;
                default:
                    System.out.println(UNKNOWN_COMMAND_MESSAGE);
                    break;
            }
        }

        scheduler.stopScheduler();
        dispatcher.requestStop();
        System.out.println(SYSTEM_ENDING_MESSAGE);
        this.endThread("Dispatcher", dispatcherThread);
    }

    private void handleRun(String[] commandarr) {
        if (commandarr.length != 4) {
            System.out.println(INVALID_RUN_MESSAGE);
            return;
        }

        try {
            String jobName = commandarr[1];
            int jobTime = Integer.parseInt(commandarr[2]);
            int jobPriority = Integer.parseInt(commandarr[3]);
            Job userSubmitted = new Job(jobName, jobPriority, jobTime, LocalDateTime.now());

            if (batchMode) {
                batchJobs.add(userSubmitted);
                System.out.println(JOB_STAGED_MESSAGE + jobName);
            } else {
                job_queue.enqueueJob(userSubmitted);
                System.out.println(JOB_ADDED_MESSAGE + jobName);
            }

        } catch (NumberFormatException | InterruptedException e) {
            System.out.println(INVALID_RUN_FORMAT);
        }
    }

    private void handleList() {
        System.out.println("Scheduling Policy: " + scheduler.getPolicy());
        System.out.println(LIST_HEADER);
        job_queue.listQueue();
    }

    private void handlePolicyChange(String[] commandarr) {
        if (commandarr.length != 2) {
            System.out.println(INVALID_POLICY_CHANGE);
            return;
        }

        try {
            SchedulingPolicy newPolicy = SchedulingPolicy.valueOf(commandarr[1].toUpperCase());
            currentPolicy = newPolicy;

            scheduler.stopScheduler();
            dispatcher.requestStop();
            scheduler = new Scheduler(currentPolicy, job_queue);
            dispatcher = new Dispatcher(scheduler.getScheduledJobQueue());
            dispatcherThread = new Thread(dispatcher);
            dispatcherThread.setDaemon(true);
            dispatcherThread.start();

            System.out.println(POLICY_CHANGE_MESSAGE + currentPolicy);
        } catch (IllegalArgumentException e) {
            System.out.println(INVALID_POLICY);
        }
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
