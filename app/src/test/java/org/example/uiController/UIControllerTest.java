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
    public void testGenerate() {
        setUserInput(""); // No input needed for this test
        UI.generateUI();

        String expectedOutput = "Welcome to the CSUBatch Scheduling Application";

        assertTrue(outputStream.toString().contains(expectedOutput.trim()));
    }

    @Test
    @DisplayName("Test valid 'run' command")
    public void testValidRunCommand() {
        // Arrange
        setUserInput("run fishing 100 10\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("Job: fishing added to queue"));
    }

    @Test
    @DisplayName("Test invalid 'run' command (wrong format)")
    public void testInvalidRunCommand() {
        // Arrange
        setUserInput("run fish hello ten\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("Sorry time and priority must be able to be converted to integer try again"));
    }

    @Test
    @DisplayName("Test 'list' command")
    public void testListCommand() {
        // Arrange
        setUserInput("list\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        // TODO: Uncomment assertion/update expectation once implemented.
        // assertTrue(outputStream.toString().contains("Listing all jobs"));
    }

    @Test
    @DisplayName("Test 'help' command")
    public void testHelpCommand() {
        // Arrange
        setUserInput("help\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("User has enter help"));
        assertTrue(outputStream.toString().contains("Command List:"));
        assertTrue(outputStream.toString().contains("run <job name> <job time> <priority>"));
    }

    @Test
    @DisplayName("Test 'policy_change' command")
    public void testPolicyChangeCommand() {
        // Arrange
        setUserInput("policy_change fcfs\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("policy change successful"));
    }

    @Test
    @DisplayName("Test invalid input command")
    public void testInvalidInput() {
        // Arrange
        setUserInput("invalid input\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        assertTrue(outputStream.toString().contains("Sorry command unrecognized try again"));
    }

    @Test
    @DisplayName("Test full interaction flow")
    public void testFullInteraction() {
        // Arrange
        setUserInput("run fishing 100 10\nlist\nhelp\npolicy_change fcfs\nrun fish hello ten\ninvalid input\nexit\n");

        // Act
        UI.userInteraction();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Job: fishing added to queue"));
        //   TODO: Add assertions for functionality as it is developed.
        //   assertTrue(output.contains("Listing all jobs"));
        assertTrue(output.contains("User has enter help"));
        assertTrue(output.contains("policy change successful"));
        assertTrue(output.contains("Sorry time and priority must be able to be converted to integer try again"));
        assertTrue(output.contains("Sorry command unrecognized try again"));
        assertTrue(output.contains("System ending..."));
    }
}