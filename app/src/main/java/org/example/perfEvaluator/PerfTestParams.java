package org.example.perfEvaluator;

import org.example.scheduler.SchedulingPolicy;

/**
 * The PerfTestParams class encapsulates the parameters for a performance test.
 * @param benchmarkName The name of the benchmark.
 * @param policy The scheduling policy
 * @param numJobs The number of jobs.
 * @param priorityLevels The priority levels.
 * @param maxCpuTime The maximum CPU (processing) time.
 * @param minCpuTime The minimum CPU (processing) time.
 */
public record PerfTestParams(
        String benchmarkName, SchedulingPolicy policy, int numJobs, int priorityLevels,
        int maxCpuTime, int minCpuTime
) {
}
