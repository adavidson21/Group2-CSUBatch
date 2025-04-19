package org.example.perfEvaluator;

import org.example.scheduler.SchedulingPolicy;

/**
 * The PerfTestParams class encapsulates the parameters for a performance test.
 */
public record PerfTestParams(
        String benchmarkName, SchedulingPolicy policy, int numJobs, int priorityLevels,
        int maxCpuTime, int minCpuTime
) {
}
