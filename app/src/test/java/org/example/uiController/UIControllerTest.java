package org.example.uiController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UIControllerTest {
    private UIController UI;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream)); // capture output
    }

    /**
     * Sets the user input to the specified value.
     * @param input The user input.
     */
    private void setUserInput(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner mockScanner = new Scanner(inputStream);
        UI = new UIController(mockScanner);
    }

    /**
     * Cleans up after tests have completed.
     */
    @AfterAll
    public static void tearDown() {
        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should successfully create the UI System and then move to take user input.")
    public void Commands_GenerateUI_ShouldDisplayGreeting() {
        // Arrange
        setUserInput(""); // No input needed for this test

        // Act
        UI.generateUI();
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("Welcome to the CSUBatch Scheduling Application"));
    }

    @Test
    @DisplayName("Should successfully add the job to the queue when a valid 'run' command is entered.")
    public void Commands_ValidRun_ShouldAddJobToQueue() {
        // Arrange
        String jobName = "test-job";
        setUserInput("run " + jobName + " 100 10\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("Job '" + jobName + "' added to the queue."));
    }

    @Test
    @DisplayName("Should display an error message when ana invalid 'run' command (wrong format) is entered.")
    public void Commands_InvalidRun_ShouldNotAddJobToQueue() {
        // Arrange
        setUserInput("run fish hello ten\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("Error: time and priority must be integers. Please try again."));
    }

    @Test
    @DisplayName("Should successfully display an empty message when the 'list' command is entered without jobs queued.")
    public void Commands_ValidList_ShouldDisplayEmptyQueue() {
        // Arrange
        setUserInput("list\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Scheduling Policy: "));
        assertTrue(output.contains("Job_Name CPU_Time Priority Arrival_Time State"));
        assertTrue(output.contains("Queue Currently Empty."));
    }

    @Test
    @DisplayName("Should successfully list the job queue when the 'list' command is entered with jobs queued.")
    public void Commands_ValidList_ShouldDisplayQueuedJobs() {
        // Arrange
        setUserInput("run jobname 100 10\nlist\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Scheduling Policy: "));
        assertTrue(output.contains("Job_Name CPU_Time Priority Arrival_Time State"));
        assertTrue(output.contains("jobname"));
    }

    @Test
    @DisplayName("Should successfully display the 'help' options when the 'help' command is entered.")
    public void Commands_ValidHelp_ShouldDisplayHelpOptions() {
        // Arrange
        setUserInput("help\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Available Commands:"));
        assertTrue(output.contains("run <job name> <job time> <priority>"));
    }

    @Test
    @DisplayName("Should successfully display the policy change Test 'policy_change' command")
    public void Commands_ValidPolicyChange_ShouldSuccessfullyChangePolicy() {
        // Arrange
        setUserInput("policy_change fcfs\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("policy change successful"));
    }

    @Test
    @DisplayName("Should display an error message when the input is not a valid command")
    public void Command_InvalidInput_ShouldDisplayErrorMessage() {
        // Arrange
        String command = "invalid";
        setUserInput(command + "\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("Sorry, the entered command " + command + " is not recognized. Please try again or type 'help' for a list of commands."));
    }
}