package org.example;

import org.example.queueManager.QueueManager;
import org.example.scheduler.Scheduler;
import org.example.scheduler.SchedulingPolicy;
import org.example.uiController.UIController;

import java.util.Scanner;

public class CSUBatchApplication {
    public static void main(String[] args) {
        System.out.println("System Loading...");
        System.out.println("Welcome to the CSUBatch Scheduling Application");
        System.out.println("Thank you for downloading.");
        System.out.println("This system is meant to act as a scheduling application where jobs can be added to a queue that will be arranged based on the selected priority.");
        System.out.println("Commands: run <job_name> <job_time> <job_priority>, list, policy_change, help, exit");

        Scanner scanner = new Scanner(System.in);

        // Default policy: FCFS
        SchedulingPolicy policy = SchedulingPolicy.FCFS;
        QueueManager queueManager = new QueueManager();
        Scheduler scheduler = new Scheduler(policy, queueManager);
        UIController UI = new UIController(scheduler, queueManager);
        UI.startDispatcher();

        boolean running = true;
        while (running) {
            System.out.print("\nPlease Enter Command: ");
            String input = scanner.nextLine().trim();

            String[] tokens = input.split("\\s+");

            if (tokens.length == 0 || tokens[0].isEmpty()) continue;

            switch (tokens[0].toLowerCase()) {
                case "run":
                    if (tokens.length != 4) {
                        System.out.println("Usage: run <job_name> <job_time> <job_priority>");
                    } else {
                        try {
                            String name = tokens[1];
                            int time = Integer.parseInt(tokens[2]);
                            int priority = Integer.parseInt(tokens[3]);
                            UI.submitJob(new org.example.common.Job(name, priority, time));
                            System.out.println("Job added: " + name);
                        } catch (NumberFormatException e) {
                            System.out.println("Time and priority must be valid numbers.");
                        }
                    }
                    break;

                case "list":
                    System.out.println("Scheduling Policy: " + scheduler.getPolicy());
                    queueManager.listQueue();
                    break;

                case "policy_change":
                    if (tokens.length != 2) {
                        System.out.println("Usage: policy_change <FCFS|SJF|Priority>");
                    } else {
                        try {
                            policy = SchedulingPolicy.valueOf(tokens[1].toUpperCase());
                            scheduler.stop(); // Stop current scheduler thread
                            scheduler = new Scheduler(policy, queueManager); // New scheduler with updated policy
                            System.out.println("Scheduling policy changed to: " + policy);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid policy. Choose FCFS, SJF, or Priority.");
                        }
                    }
                    break;

                case "help":
                    System.out.println("Command List:");
                    System.out.println("- run <job_name> <job_time> <job_priority>");
                    System.out.println("- list");
                    System.out.println("- policy_change <FCFS|SJF|Priority>");
                    System.out.println("- help");
                    System.out.println("- exit");
                    break;

                case "exit":
                    scheduler.stop();
                    running = false;
                    System.out.println("System ending...");
                    break;

                default:
                    System.out.println("Sorry, command unrecognized. Type 'help' to see command options.");
            }
        }

        scanner.close();
    }
}
