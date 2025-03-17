package org.example.uiController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private void setUserInput(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner mockScanner = new Scanner(inputStream);
        UI = new UIController(mockScanner);
    }

    @AfterAll
    public static void tearDown() {
        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should successfully create the UI System and then move to take user input.")
    public void testGenerate() {
        setUserInput("");
        UI.generateUI();
        assertTrue(outputStream.toString().contains("Welcome to the CSUBatch Scheduling Application"));
    }

    @Test
    @DisplayName("Test valid 'run' command")
    public void testValidRunCommand() {
        setUserInput("run fishing 100 10\nexit\n");
        UI.userInteraction();
        assertTrue(outputStream.toString().contains("Job added: fishing"));
    }

    @Test
    @DisplayName("Test invalid 'run' command (wrong format)")
    public void testInvalidRunCommand() {
        setUserInput("run fish hello ten\nexit\n");
        UI.userInteraction();
        assertTrue(outputStream.toString().contains("Time and priority must be valid numbers."));
    }

    @Test
    @DisplayName("Test 'list' command")
    public void testListCommand() {
        setUserInput("list\nexit\n");
        UI.userInteraction();
        assertTrue(outputStream.toString().contains("Scheduling Policy:"));
    }

    @Test
    @DisplayName("Test 'help' command")
    public void testHelpCommand() {
        setUserInput("help\nexit\n");
        UI.userInteraction();
        String output = outputStream.toString();
        assertTrue(output.contains("Command List:"));
        assertTrue(output.contains("run <job name> <job time> <priority>"));
    }

    @Test
    @DisplayName("Test 'policy_change' command")
    public void testPolicyChangeCommand() throws Exception {
        setUserInput("policy_change FCFS\nexit\n"); // FIXED: use uppercase FCFS
        UI.userInteraction();

        String output = outputStream.toString();
        Files.write(Paths.get("policy_output.txt"), output.getBytes()); // Optional for debugging

        assertTrue(output.contains("Scheduling policy changed to: FCFS"));
    }

    @Test
    @DisplayName("Test invalid input command")
    public void testInvalidInput() {
        setUserInput("invalid input\nexit\n");
        UI.userInteraction();
        assertTrue(outputStream.toString().contains("Sorry, command unrecognized"));
    }

    @Test
    @DisplayName("Test full interaction flow")
    public void testFullInteraction() throws Exception {
        // FIXED: changed "fcfs" to "FCFS"
        setUserInput("run fishing 100 10\nlist\nhelp\npolicy_change FCFS\nrun fish hello ten\ninvalid input\nexit\n");
        UI.userInteraction();

        String output = outputStream.toString();
        Files.write(Paths.get("full_output.txt"), output.getBytes()); // Optional for debugging

        assertTrue(output.contains("Job added: fishing"));
        assertTrue(output.contains("Scheduling Policy:"));
        assertTrue(output.contains("Command List:"));
        assertTrue(output.contains("Scheduling policy changed to: FCFS"));
        assertTrue(output.contains("Time and priority must be valid numbers."));
        assertTrue(output.contains("Sorry, command unrecognized"));
        assertTrue(output.contains("System ending..."));
    }
}
