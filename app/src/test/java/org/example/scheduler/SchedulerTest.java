package org.example.scheduler;

import org.example.common.Job;
import org.example.queueManager.QueueManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Scheduler class.
 */
public class SchedulerTest {
    private QueueManager queueManager;
    private Scheduler scheduler;

    @BeforeEach
    void setup() {
        queueManager = new QueueManager();
        scheduler = new Scheduler(SchedulingPolicy.FCFS, queueManager); // default to FCFS
    }

    @Test
    @DisplayName("Should add a single Job to the scheduler.")
    void Scheduler_AddSingleJob_ShouldAddJob() throws InterruptedException {
        // Arrange
        Job job = new Job("TestJob", 1, 2000L, null);

        // Act
        scheduler.addJob(job);

        // Assert
        assertTrue(queueManager.checkForJob(job));
    }

    @Test
    @DisplayName("Should follow new policy when changed to SJF.")
    void Scheduler_ChangePolicy_ShouldFollowSJFOnChange() throws InterruptedException {
        // Arrange
        Job jobFast = new Job("FastJob", 1, 1000L, null);    // shortest time
        Job jobMedium = new Job("MediumJob", 1, 3000L, null);
        Job jobSlow = new Job("SlowJob", 1, 5000L, null);

        scheduler.addJob(jobSlow);
        scheduler.addJob(jobMedium);
        scheduler.addJob(jobFast);

        scheduler.setPolicy(SchedulingPolicy.SJF);

        // Act
        Job firstOut = queueManager.dequeueJob();
        Job secondOut = queueManager.dequeueJob();
        Job thirdOut = queueManager.dequeueJob();

        // Assert
        assertEquals("FastJob", firstOut.getName());
        assertEquals("MediumJob", secondOut.getName());
        assertEquals("SlowJob", thirdOut.getName());
    }

    @Test
    @DisplayName("Should follow new policy when changed to Priority.")
    void Scheduler_ChangePolicy_ShouldFollowPriorityOnChange() throws InterruptedException {
        // Create and add Jobs with distinct priorities
        Job jobHighPriority = new Job("HighPriorityJob", 1, 2000L, null);
        Job jobMediumPriority = new Job("MediumPriorityJob", 2, 2000L, null);
        Job jobLowPriority = new Job("LowPriorityJob", 3, 2000L, null);

        scheduler.addJob(jobLowPriority);
        scheduler.addJob(jobMediumPriority);
        scheduler.addJob(jobHighPriority);

        // Change policy to Priority
        scheduler.setPolicy(SchedulingPolicy.Priority);

        // Act
        Job firstOut = queueManager.dequeueJob();
        Job secondOut = queueManager.dequeueJob();
        Job thirdOut = queueManager.dequeueJob();

        // Assert
        assertEquals("HighPriorityJob", firstOut.getName());
        assertEquals("MediumPriorityJob", secondOut.getName());
        assertEquals("LowPriorityJob", thirdOut.getName());
    }

    @ParameterizedTest
    @EnumSource(SchedulingPolicy.class)
    @DisplayName("Should reflect policy change for the chosen policy")
    void Scheduler_ChangePolicy_ShouldReflectChange(SchedulingPolicy newPolicy) throws InterruptedException {
        // Arrange / Act
        scheduler.setPolicy(newPolicy);

        // Assert
        assertEquals(newPolicy, scheduler.getPolicy());
    }
}
