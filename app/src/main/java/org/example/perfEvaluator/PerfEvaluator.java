package org.example.perfEvaluator;


import org.example.common.Job;
import org.example.dispatcher.Dispatcher;
import org.example.scheduler.Scheduler;

import java.time.LocalDateTime;

/**
 * The PerfEvaluator handles the collation of performance metrics for job execution.
 */
public class PerfEvaluator {
    Scheduler scheduler;
    Dispatcher dispatcher;
    PerfTestParams testParams;

    public PerfEvaluator(PerfTestParams testParams, Scheduler scheduler, Dispatcher dispatcher) {
        this.testParams = testParams;
        this.scheduler = scheduler;
        this.dispatcher = dispatcher;
    }

    private int calcAvgResponseTime() {
        return 0;
    }

    public void printMetrics() {
        System.out.println("Performance metrics for " + testParams.benchmarkName());
        System.out.println("Average response time: ");
        System.out.println("Throughput: ");
    }

    /**
     * Generate jobs for the performance evaluation.
     *
     * @throws InterruptedException throws if the thread is interrupted
     */
    public void generateJobs() throws InterruptedException {
        for (int i = 0; i < testParams.numJobs(); i++) {
            // allow the job to have a priority between 0 and the inclusive maximum priority level
            int priority = (int) (Math.random() * testParams.priorityLevels() + 1);
            // allow the job to have a CPU time between the inclusive minimum and maximum CPU time
            long cpuTime = (int) (Math.random() * (testParams.maxCpuTime() - testParams.minCpuTime()) + testParams.minCpuTime());
            String jobName = "Job_" + (i + 1);
            LocalDateTime creationTime = LocalDateTime.now();
            Job job = new Job(jobName, priority, cpuTime, creationTime);
            System.out.println("Generate job: " + job.getName() + " with priority " + job.getExecutionPriority() + " and execution time " + job.getExecutionTime());
            scheduler.addJob(job);
        }
    }

    /**
     * Run the performance evaluation.
     *
     * @throws InterruptedException throws if the thread is interrupted
     */
    public void run() throws InterruptedException {
        scheduler.setPolicy(this.testParams.policy());
        this.generateJobs();

        this.printMetrics();
    }


}
