package org.example.perfEvaluator;

import org.example.common.Job;
import org.example.queueManager.QueueManager;
import org.example.scheduler.Scheduler;
import org.example.scheduler.SchedulingPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for the PerfEvaluator class.
 */
public class PerfEvaluatorTest {
    private PerfEvaluator evaluator;

    @BeforeEach
    public void setUp() {
        Scheduler scheduler = new Scheduler(SchedulingPolicy.FCFS, new QueueManager());
        evaluator = new PerfEvaluator(scheduler);
    }

    @Test
    @DisplayName("Should successfully add completed jobs to the evaluator's list")
    public void Evaluator_CompletedJob_ShouldBeAddedToEvaluator() {
        // Arrange
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0);
        Job job = new Job("TestJob", 1, 1000, now);
        job.setActualProcessingStartTime(now.plusSeconds(1));
        job.setActualCompletionTime(now.plusSeconds(2));

        // Act
        evaluator.addCompletedJob(job);

        // Assert
        assertEquals(1, evaluator.getCompletedJobs().size());
        assertEquals("TestJob", evaluator.getCompletedJobs().get(0).getName());
    }

    @Test
    @DisplayName("Should calculate correct average and max response time for a single job")
    public void Evaluator_SingleJob_ShouldCalculateCorrectly() {
        // Arrange
        LocalDateTime arrival = LocalDateTime.of(2023, 1, 1, 0, 0);
        Job job = new Job("Job1", 1, 2000, arrival);
        job.setActualProcessingStartTime(arrival.plusSeconds(1));  // Response = 1s
        job.setActualCompletionTime(arrival.plusSeconds(3));       // Turnaround = 3s, Wait = 1s

        evaluator.addCompletedJob(job);

        // Act
        evaluator.printMetrics();

        // Assert
        assertFalse(evaluator.getCompletedJobs().isEmpty());
        assertEquals(3000.0, evaluator.perfMetrics.getAverageTurnaroundTime(), 0.001);
        assertEquals(1000.0, evaluator.perfMetrics.getAverageResponseTime(), 0.001);
        assertEquals(1000.0, evaluator.perfMetrics.getAverageWaitTime(), 0.001);
        assertEquals(1000.0, evaluator.perfMetrics.getMaxResponseTime(), 0.001);
        assertEquals(0.33, evaluator.perfMetrics.getThroughput(), 0.01); // 1 job / 3s
    }

    @Test
    @DisplayName("Should calculate throughput, average and max response time accurately for multiple jobs")
    public void Evaluator_MultipleJobs_ShouldCalculateMetricsPrecisely() {
        // Arrange
        LocalDateTime base = LocalDateTime.of(2023, 1, 1, 0, 0);

        Job job1 = new Job("Job1", 1, 2000, base);
        job1.setActualProcessingStartTime(base.plusSeconds(1));   // Response = 1s
        job1.setActualCompletionTime(base.plusSeconds(3));        // Turnaround = 3s, Wait = 1s
        evaluator.addCompletedJob(job1);

        Job job2 = new Job("Job2", 1, 1000, base.plusSeconds(1));
        job2.setActualProcessingStartTime(base.plusSeconds(2));   // Response = 1s
        job2.setActualCompletionTime(base.plusSeconds(3));        // Turnaround = 2s, Wait = 1s
        evaluator.addCompletedJob(job2);

        // Act
        evaluator.printMetrics();

        // Assert
        assertEquals(2500.0, evaluator.perfMetrics.getAverageTurnaroundTime(), 0.001); // (3000 + 2000) / 2
        assertEquals(1000.0, evaluator.perfMetrics.getAverageResponseTime(), 0.001);   // (1000 + 1000) / 2
        assertEquals(1000.0, evaluator.perfMetrics.getAverageWaitTime(), 0.001);       // (1000 + 1000) / 2
        assertEquals(1000.0, evaluator.perfMetrics.getMaxResponseTime(), 0.001);
        assertEquals(0.67, evaluator.perfMetrics.getThroughput(), 0.01); // 2 jobs / 3s
    }

    @Test
    @DisplayName("Should calculate throughput properly for multiple jobs")
    public void Evaluator_ThroughputCalculation_ShouldBeAccurate() {
        // Arrange
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0);

        for (int i = 0; i < 5; i++) {
            Job job = new Job("Job" + i, 1, 1000, now.plusSeconds(i));
            job.setActualProcessingStartTime(now.plusSeconds(i + 1));
            job.setActualCompletionTime(now.plusSeconds(i + 2));
            evaluator.addCompletedJob(job);
        }

        // Act
        evaluator.printMetrics();

        // Assert
        assertEquals(5, evaluator.getCompletedJobs().size());
        assertEquals(1000.0, evaluator.perfMetrics.getAverageResponseTime(), 0.001);
        assertEquals(1000.0, evaluator.perfMetrics.getAverageWaitTime(), 0.001);
        assertEquals(2000.0, evaluator.perfMetrics.getAverageTurnaroundTime(), 0.001);
        assertEquals(1000.0, evaluator.perfMetrics.getMaxResponseTime(), 0.001);
        assertEquals(0.83, evaluator.perfMetrics.getThroughput(), 0.01); // 5 jobs / 6s
    }
}
