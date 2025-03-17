package org.example.e2e;

import org.example.CSUBatchTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.constants.ConsoleMessages.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSUBatchEndToEndTest extends CSUBatchTestBase {
    @Test
    @DisplayName("Successfully completes a full end to end flow of UI interactions.")
    public void E2E_UIControllerFlow() {
        //TODO: Add Batch commands

        // Arrange
        setUserInput(
                "run fishing 100 10\n" + // valid run
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
        assertTrue(output.contains(JOB_ADDED_MESSAGE));         // "run fishing 100 10\n"
        assertTrue(output.contains(INVALID_RUN_FORMAT));        // "run fish hello ten\n"
        assertTrue(output.contains(LIST_HEADER));               // "list\n"
        // TODO: Validate jobs that are listed from list command
        assertTrue(output.contains(HELP_BLOCK));                // "help\n"
        assertTrue(output.contains(POLICY_CHANGE_MESSAGE));     // "policy_change fcfs\n"
        assertTrue(output.contains(UNKNOWN_COMMAND_MESSAGE));   // "invalid input\n"
        assertTrue(output.contains(SYSTEM_ENDING_MESSAGE));     // "exit\n"
    }
}