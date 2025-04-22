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
        Job job = new Job("TestJob", 1, 1000, LocalDateTime.now());
        job.setActualCompletionTime(LocalDateTime.now().plusSeconds(1));

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
        LocalDateTime arrival = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalDateTime completion = arrival.plusSeconds(2);
        Job job = new Job("Job1", 1, 1000, arrival);
        job.setActualCompletionTime(completion);
        evaluator.addCompletedJob(job);

        // Act
        evaluator.printMetrics();

        // Assert
        assertFalse(evaluator.getCompletedJobs().isEmpty());
        assertEquals(2000.0, evaluator.perfMetrics.getAverageResponseTime(), 0.001);
        assertEquals(2000.0, evaluator.perfMetrics.getMaxResponseTime(), 0.001);
        assertEquals(0.5, evaluator.perfMetrics.getThroughput(), 0.001);
    }

    @Test
    @DisplayName("Should calculate throughput, average and max response time accurately for multiple jobs")
    public void Evaluator_MultipleJobs_ShouldCalculateMetricsPrecisely() {
        // Arrange
        LocalDateTime base = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

        Job job1 = new Job("Job1", 1, 1000, base);
        job1.setActualCompletionTime(base.plusSeconds(3)); // Response time = 3s
        evaluator.addCompletedJob(job1);

        Job job2 = new Job("Job2", 1, 1000, base.plusSeconds(1));
        job2.setActualCompletionTime(base.plusSeconds(4)); // Response time = 3s
        evaluator.addCompletedJob(job2);

        Job job3 = new Job("Job3", 1, 1000, base.plusSeconds(2));
        job3.setActualCompletionTime(base.plusSeconds(5)); // Response time = 3s
        evaluator.addCompletedJob(job3);

        // Act
        evaluator.printMetrics(); // triggers metric calculations

        // Assert
        assertEquals(3000.0, evaluator.perfMetrics.getAverageResponseTime(), 0.001); // Avg response time = 9000ms/3 = 3000ms
        assertEquals(3000.0, evaluator.perfMetrics.getMaxResponseTime(), 0.001); // Max response time = 3000ms
        assertEquals(0.6, evaluator.perfMetrics.getThroughput(), 0.001); // Throughput = 3 jobs / 5s = 0.6
    }


    @Test
    @DisplayName("Should calculate throughput properly for multiple jobs")
    public void Evaluator_ThroughputCalculation_ShouldBeAccurate() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 5; i++) {
            Job job = new Job("Job" + i, 1, 1000, now.plusSeconds(i));
            job.setActualCompletionTime(now.plusSeconds(i + 2));
            evaluator.addCompletedJob(job);
        }

        // Act
        evaluator.printMetrics();

        // Assert
        assertEquals(5, evaluator.getCompletedJobs().size());
    }
}
