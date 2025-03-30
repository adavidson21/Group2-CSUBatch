package org.example.perfEvaluator;

/**
 * The PerfMetrics class encapsulates the available performance metrics for a run of the PerfEvaluator.
 */
public class PerfMetrics {
    private final int averageResponseTime;
    private final int throughput;

    public PerfMetrics(int averageResponseTime, int throughput) {
        // average response time of the system after processing n jobs
        this.averageResponseTime = averageResponseTime;
        // throughput of the system after processing n jobs
        this.throughput = throughput;
    }

    public int getAverageResponseTime() {
        return averageResponseTime;
    }

    public int getThroughput() {
        return throughput;
    }
}
