package org.example;

import org.example.uiController.UIController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Base class that is to be extended by specific unit test classes.
 * Contains base functionality that may be needed across different test classes.
 */
public class CSUBatchTestBase {
    protected UIController UI;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Gets the output from the output stream.
     * @return The output string.
     */
    public String getOutput(){
        return outputStream.toString();
    }

    /**
     * Sets up the stream in order to capture the output.
     */
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Cleans up (resets output stream) after tests have completed.
     */
    @AfterAll
    public static void tearDown() {
        System.setOut(System.out);
    }

    /**
     * Sets the user input to the specified value.
     * @param input The user input.
     */
    protected void setUserInput(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Scanner mockScanner = new Scanner(inputStream);
        UI = new UIController(mockScanner);
    }
}
