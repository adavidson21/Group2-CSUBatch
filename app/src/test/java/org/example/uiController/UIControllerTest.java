package org.example.uiController;

import org.example.CSUBatchTestBase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UIController class.
 */
public class UIControllerTest extends CSUBatchTestBase {
    @Test
    @DisplayName("Should successfully create the UI System and then move to take user input.")
    public void Commands_GenerateUI_ShouldDisplayGreeting() {
        // Arrange
        setUserInput(""); // No input needed for this test

        // Act
        UI.generateUI();
        String output = getOutput();

        // Assert
        assertTrue(output.contains("Welcome to the CSUBatch Scheduling Application"));
    }

    @Test
    @DisplayName("Should successfully add the job to the queue when a valid 'run' command is entered.")
    public void Commands_ValidRun_ShouldAddJobToQueue() throws InterruptedException {
        // Arrange
        String jobName = "test-job";
        setUserInput("run " + jobName + " 100 10\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(getOutput().contains("Job '" + jobName + "' added to the queue."));
    }

    @Test
    @DisplayName("Should display an error message when an invalid 'run' command (wrong format) is entered.")
    public void Commands_InvalidRun_ShouldNotAddJobToQueue() throws InterruptedException {
        // Arrange
        setUserInput("run fish hello ten\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(getOutput().contains("Error: time and priority must be integers. Please try again."));
    }

    @Test
    @DisplayName("Should successfully display an empty message when the 'list' command is entered without jobs queued.")
    public void Commands_ValidList_ShouldDisplayEmptyQueue() throws InterruptedException {
        // Arrange
        setUserInput("list\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = getOutput();
        assertTrue(output.contains("Scheduling Policy: "));
        assertTrue(output.contains("Job_Name CPU_Time Priority Arrival_Time State"));
        assertTrue(output.contains("Queue Currently Empty."));
    }

    @Test
    @DisplayName("Should successfully list the job queue when the 'list' command is entered with jobs queued.")
    public void Commands_ValidList_ShouldDisplayQueuedJobs() throws InterruptedException {
        // Arrange
        setUserInput("run jobname 100 10\nlist\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = getOutput();
        assertTrue(output.contains("Scheduling Policy: "));
        assertTrue(output.contains("Job_Name CPU_Time Priority Arrival_Time State"));
        assertTrue(output.contains("jobname"));
    }

    @Test
    @DisplayName("Should successfully display the 'help' options when the 'help' command is entered.")
    public void Commands_ValidHelp_ShouldDisplayHelpOptions() throws InterruptedException {
        // Arrange
        setUserInput("help\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = getOutput();
        assertTrue(output.contains("Available Commands:"));
        assertTrue(output.contains("run <job name> <job time in seconds> <priority>"));
    }

    @Test
    @DisplayName("Should successfully display the policy change Test 'policy_change' command")
    public void Commands_ValidPolicyChange_ShouldSuccessfullyChangePolicy() throws InterruptedException {
        // Arrange
        setUserInput("policy_change fcfs\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(getOutput().contains("policy change successful"));
    }

    @Test
    @DisplayName("Should display an error message when the input is not a valid command")
    public void Command_InvalidInput_ShouldDisplayErrorMessage() throws InterruptedException {
        // Arrange
        String command = "invalid";
        setUserInput(command + "\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(getOutput().contains("Sorry, the entered command is not recognized. Please try again or type 'help' for a list of commands."));
    }

    @Test
    @DisplayName("Should enter batch mode when the 'batch_job' command is entered.")
    public void Command_BatchMode_ShouldEngageBatchMode() {
        // Arrange
        String[] command = {"batch_job", "5"};
        setUserInput("command\nexit\n");

        // Act
        UI.handleBatchJobCommand(command);

        // Assert
        assertTrue(getOutput().contains("Entering batch_job mode. Please see micro_benchmarks.log file for results."));
    }
}