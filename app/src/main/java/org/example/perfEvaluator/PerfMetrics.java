package org.example.perfEvaluator;

/**
 * The PerfMetrics class encapsulates the available performance metrics for a run of the PerfEvaluator.
 */
public class PerfMetrics {
    private long averageResponseTime;
    private double throughput;
    private long maxResponseTime;

    public void setAverageResponseTime(long averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public void setMaxResponseTime(long maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }


    public long getMaxResponseTime() {
        return maxResponseTime;
    }

    public long getAverageResponseTime() {
        return averageResponseTime;
    }

    public double getThroughput() {
        return throughput;
    }
}
