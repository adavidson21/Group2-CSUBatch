package org.example.constants;

public final class ConsoleMessages {

    public static final String WELCOME_MESSAGE = "Welcome to the CSUBatch Scheduling Application\nThank you for downloading.";
    public static final String SYSTEM_INTRO = "This system acts as a job scheduler where jobs can be added to a queue and scheduled based on a selected policy.";
    public static final String COMMANDS_OVERVIEW = "Commands: run <jobName> <execTimeMs> <priority>, list, policy_change <FCFS|SJF|Priority>, help, exit\n\"Batch Mode: batch_start, batch_execute\"";

    public static final String PROMPT_MESSAGE = "Please Enter Command: ";
    public static final String UNKNOWN_COMMAND_MESSAGE = "Sorry, the entered command is not recognized. Please try again or type 'help' for a list of commands.";

    public static final String SYSTEM_ENDING_MESSAGE = "System ending...";

    public static final String BATCH_START_MESSAGE = "Batch mode started. Jobs will be held until 'batch_execute'.";
    public static final String BATCH_EXECUTION_MESSAGE = "Batch execution started.";

    public static final String JOB_STAGED_MESSAGE = "Job staged for batch: ";
    public static final String JOB_ADDED_MESSAGE = "Job has been added to the queue: ";

    public static final String POLICY_CHANGE_MESSAGE = "Policy change successful. Scheduling policy changed to: ";

    public static final String LIST_HEADER = "Job_Name | CPU_Time | Priority | Arrival_Time | State";
    public static final String QUEUE_EMPTY_MESSAGE = "Queue is Empty.";

    public static final String INVALID_RUN_MESSAGE = "Invalid run command. Usage: run <job name> <job time> <priority>";
    public static final String INVALID_RUN_FORMAT = "Error: time and priority must be integers. Please try again.";
    public static final String INVALID_POLICY_CHANGE = "Invalid policy change command. Usage: policy_change <FCFS|SJF|Priority>";
    public static final String INVALID_POLICY = "Invalid policy. Choose from: FCFS, SJF, Priority.";

    //region Help Messages
    public static final String HELP_HEADER = "Command List:";
    public static final String HELP_RUN = "run <job name> <job time> <priority> - Add a job to the system.";
    public static final String HELP_LIST = "list - Print out the current job queue.";
    public static final String HELP_POLICY = "policy_change <policy> - Change scheduling policy (FCFS, SJF, Priority).";
    public static final String HELP_BATCH_START = "batch_start - Enter batch mode to queue multiple jobs without execution.";
    public static final String HELP_BATCH_EXECUTE = "batch_execute - Submit all batched jobs to the scheduler.";
    public static final String HELP_EXIT = "exit - End the system.";

    public static final String HELP_BLOCK =
            HELP_HEADER + "\n" +
                    HELP_RUN + "\n" +
                    HELP_LIST + "\n" +
                    HELP_POLICY + "\n" +
                    HELP_BATCH_START + "\n" +
                    HELP_BATCH_EXECUTE + "\n" +
                    HELP_EXIT;
    //endregion
}
