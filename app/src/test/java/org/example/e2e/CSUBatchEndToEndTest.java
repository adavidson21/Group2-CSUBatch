package org.example.e2e;

import org.example.CSUBatchTestBase;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CSUBatchEndToEndTest extends CSUBatchTestBase {
    @Test
    @DisplayName("Successfully completes a full end to end flow of UI interactions.")
    public void E2E_UIControllerFlow() throws InterruptedException {
        // Arrange
        setUserInput(
                "run fishing 1 10\n" +  // valid run
                        "run fish hello ten\n"    +  // invalid run
                        "list\n"                  +  // valid list
                        "help\n"                  +  // valid help
                        "policy_change fcfs\n"    +  // valid policy change
                        "policy_change foo\n"     +  // invalid policy change
                        "invalid input\n"         +  // invalid command
                        "exit\n"
        );

        // Act
        UI.userInteraction();
        String output = getOutput();

        // Assert
        assertTrue(output.contains("Job 'fishing' added to the queue"));    // "run fishing 100 10\n"
//        assertTrue(output.contains("fishing 1 seconds 10"));                      // "list\n"
        assertTrue(output.contains("Available Commands:"));                 // "help\n"
        assertTrue(output.contains("policy change successful"));            // "policy_change fcfs\n"

        assertFalse(output.contains("Job 'fish' added to the queue"));      // "run fish hello ten\n"
        assertTrue(output.contains("Error: time and priority must be integers. Please try again."));

        assertTrue(output.contains("not recognized"));                      // "invalid input\n"
        assertTrue(output.contains("System ending..."));                    // "exit\n"
    }
}
