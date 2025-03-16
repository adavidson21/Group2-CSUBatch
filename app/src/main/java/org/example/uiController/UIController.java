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
    private final QueueManager jobQueue;
    private final Scheduler scheduler;
    private final Dispatcher dispatcher;

    /**
     * The UIController constructor.
     * Initializes the QueueManager, Scheduler, and Dispatcher to encapsulate dependencies
     * and to allow for dependency injection for testing if needed.
     * @param scanner The scanner.
     */
    public UIController(Scanner scanner) {
        this.userInput = scanner;
        this.jobQueue = new QueueManager();
        this.scheduler = new Scheduler(SchedulingPolicy.FCFS);
        this.dispatcher = new Dispatcher(jobQueue);
    }

    /**
     * Prints the initial welcome and instructions for the application.
     */
    public void generateUI(){
        System.out.println("Welcome to the CSUBatch Scheduling Application");
        System.out.println("Thank you for downloading.");
        System.out.println("This System is meant to act as a scheduling application where jobs can be added to a queue that will be arranged based \n on the selected priority.");
        System.out.println("Commands: run, list, policy_change, help, exit");
    }

    /**
     * User interaction handler that parses commands as they are inputted by the user.
     */
    public void userInteraction(){
        System.out.println("Please Enter a Command:");
        String commandLine = userInput.nextLine();
        String[] commandArr = commandLine.split(" "); //an array in place so the commands that are more than one word can be parsed.

        while(!"exit".equalsIgnoreCase(commandArr[0])){
            String commandName = commandArr[0].toLowerCase();
            switch(commandName){
                case "run":
                    handleRunCommand(commandArr);
                    break;
                case "list":
                    handleListCommand();
                    break;
                case "policy_change":
                    handlePolicyChangeCommand(commandArr);
                    break;
                case "help":
                    handleHelpCommand();
                    break;
                default:
                    System.out.println("Sorry, the entered command " + commandName+ " is not recognized. Please try again or type 'help' for a list of commands.");
                    break;
            }

            System.out.println("\nPlease Enter Command:");
            commandLine = userInput.nextLine();
            commandArr = commandLine.split(" ");
            if (commandArr.length == 0) {
                commandArr = new String[]{" "};  // Set default to avoid array index issues
            }
        }
        System.out.println("System ending...");
    }

    /**
     * Handles the run command when it is submitted by the user.
     * Ensures that the user enters the run command format correctly
     * If not valid, an error is thrown that does not crash the system.
     * @param command The command.
     */
    private void handleRunCommand(String[] command){
        if (command.length != 4) {
            System.out.println("Invalid run command, please try again. \nUsage: run <jobName> <cpuTime> <priority>");
            return;
        }
        try {
            String jobName = command[1];
            int jobTime = Integer.parseInt(command[2]);
            int jobPriority = Integer.parseInt(command[3]);
            LocalDateTime currentDate = LocalDateTime.now();
            Job userSubmittiedJob = new Job(jobName, jobPriority, jobTime, currentDate);

            jobQueue.enqueueJob(userSubmittiedJob);
            System.out.println("Job '" + jobName + "' added to the queue.");

        } catch (NumberFormatException e) {
            System.out.println("Error: time and priority must be integers. Please try again.");
        } catch (InterruptedException e) {
            System.out.println("Unexpected error while enqueuing job. Please try again.");
        }
    }

    /**
     * Handles the list command when it is submitted by the user.
     * Lists current scheduling policy and all jobs in the job queue.
     */
    private void handleListCommand(){
        System.out.println("Scheduling Policy: " + scheduler.getPolicy());
        System.out.println("Job_Name CPU_Time Priority Arrival_Time State");
        jobQueue.listQueue();
    }

    /**
     * Handles the policy_change command when it is submitted by the user.
     * @param command The commands.
     */
    private void handlePolicyChangeCommand(String[] command){
        if(command.length != 2){
            System.out.println("Invalid policy_change command, please try again. \nUsage: policy_change <policy>");
        }
        else{
            //TODO: Add policy change functionality here.
            System.out.println("policy change successful");
        }
    }

    /**
     * Handles the help command when it is submitted by the user.
     */
    private void handleHelpCommand(){
        System.out.println("Available Commands:");
        System.out.println("run <job name> <job time> <priority> - Will add a job to the system");
        System.out.println("list - Print out the current job queue.");
        System.out.println("policy_change <policy> - will change the policy to the new entered one and restructure queue.");
        System.out.println("exit - End System processes and perform benchmark on close");
    }
}
