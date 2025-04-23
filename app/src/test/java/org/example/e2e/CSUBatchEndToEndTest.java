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
                        "test demo fcfs 1 2 1 2\n" + // valid test command
                        "exit\n"
        );

        // Act
        UI.userInteraction();
        String output = getOutput();

        // Assert
        assertTrue(output.contains("Job 'fishing' added to the queue"));    // "run fishing 100 10\n"
        assertTrue(output.contains("fishing 1 seconds 10"));                // "list\n"
        assertTrue(output.contains("Available Commands:"));                 // "help\n"
        assertTrue(output.contains("policy change successful"));            // "policy_change fcfs\n"

        assertFalse(output.contains("Job 'fish' added to the queue"));      // "run fish hello ten\n"
        assertTrue(output.contains("Error: time and priority must be integers. Please try again."));

        assertTrue(output.contains("not recognized"));                      // "invalid input\n"

        assertTrue(output.contains("Performance Metrics for DEMO"));
        assertTrue(output.contains("Total number of jobs completed:"));
        assertTrue(output.contains("Average response time"));
        assertTrue(output.contains("Max response time"));
        assertTrue(output.contains("Throughput"));

        assertTrue(output.contains("Checking for perf metric availability and shutting down..."));                    // "exit\n"
    }
    @Test
    @DisplayName("Successfully completes a full end to end flow of scheduling actions to verify policy changes and queue order.")
    public void E2E_SchedulerFlow() throws InterruptedException {
        // Arrange
        setUserInput(
                "run jobA 3 1\n" +                  // valid run (CPU=3s, Priority=1)
                        "run jobB 1 5\n" +          // valid run (CPU=1s, Priority=5)
                        "list\n" +                  // valid list
                        "policy_change sjf\n" +     // switch to Shortest Job First
                        "list\n" +                  // valid list
                        "policy_change Priority\n" + // switch to Priority
                        "list\n" +                  // valid list
                        "exit\n"
        );

        // Act
        UI.userInteraction();
        String output = getOutput();

        // Assert
        assertTrue(output.contains("Job 'jobA' added to the queue"));
        assertTrue(output.contains("Job 'jobB' added to the queue"));

        int sjfPolicyIndex = output.indexOf("Scheduling Policy: SJF");
        assertTrue(sjfPolicyIndex >= 0); // Validate change to SJF
        String afterSJFOutput = output.substring(sjfPolicyIndex);
        int jobBPosSJF = afterSJFOutput.indexOf("jobB");
        int jobAPosSJF = afterSJFOutput.indexOf("jobA");
        assertTrue(jobBPosSJF >= 0);
        assertTrue(jobAPosSJF >= 0);
        assertTrue(jobBPosSJF < jobAPosSJF); // Validate jobB comes before jobA

        int priorityPolicyIndex = output.indexOf("Scheduling Policy: PRIORITY");
        assertTrue(priorityPolicyIndex >= 0); // Validate change to priority

        String afterPriorityOutput = output.substring(priorityPolicyIndex);

        int jobAPosPriority = afterPriorityOutput.indexOf("jobA");
        int jobBPosPriority = afterPriorityOutput.indexOf("jobB");

        assertTrue(jobAPosPriority >= 0);
        assertTrue(jobBPosPriority >= 0);
        assertTrue(jobAPosPriority < jobBPosPriority); // Validate jobA comes before jobB

        assertTrue(output.contains("Checking for perf metric availability and shutting down..."));
    }

    @Test
    @DisplayName("Successfully runs the 'test' command and prints performance metrics.")
    public void E2E_PerfEvaluationFlow() throws InterruptedException {
        // Arrange
        setUserInput(
                "test benchmark fcfs 2 2 1 2\n" +  // valid test command (2 jobs, priorities 1-2, CPU time 1-2s)
                "exit\n"
        );

        // Act
        UI.userInteraction();
        String output = getOutput();

        // Assert
        assertTrue(output.contains("Performance Metrics for BENCHMARK"));
        assertTrue(output.contains("Total number of jobs completed: 2"));
        assertTrue(output.contains("Average response time"));
        assertTrue(output.contains("Max response time"));
        assertTrue(output.contains("Throughput"));
        assertTrue(output.contains("Checking for perf metric availability and shutting down..."));
    }
}
