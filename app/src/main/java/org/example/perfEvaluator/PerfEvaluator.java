package org.example.perfEvaluator;


import org.example.common.Job;
import org.example.scheduler.Scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * The PerfEvaluator handles the collation of performance metrics for job execution.
 */
public class PerfEvaluator {
    Scheduler scheduler;
    PerfTestParams testParams;
    PerfMetrics perfMetrics = new PerfMetrics();
    ArrayList<Job> completedJobs = new ArrayList<>();

    public PerfEvaluator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setTestParams(PerfTestParams testParams) {
        this.testParams = testParams;
    }

    private void calcResponseTimes() {
        Duration totalResponseDuration = Duration.ZERO;
        Duration maxResponseDuration = Duration.ZERO;
        System.out.println("Jobs completed size: " + this.completedJobs.size());
        for (Job job : completedJobs) {
            // calculate the average response time
            // response time =  actual completion time - arrival time
            Duration responseTime = Duration.between(job.getArrivalTime(), job.getActualCompletionTime());
            if (responseTime.compareTo(maxResponseDuration) > 0) {
                maxResponseDuration = responseTime;
            }
            totalResponseDuration = totalResponseDuration.plus(responseTime);
            System.out.println("current total response duration: " + totalResponseDuration.toSeconds());
        }
        Duration avgResponseTime = totalResponseDuration.dividedBy(completedJobs.size());
        this.perfMetrics.setAverageResponseTime(avgResponseTime.toMillis());
        this.perfMetrics.setMaxResponseTime(maxResponseDuration.toMillis());
    }

    private void calcThroughput() {
        // must iterate through and get the earliest arrival time
        LocalDateTime firstJobArrival = completedJobs.stream()
                .map(Job::getArrivalTime)
                .min(LocalDateTime::compareTo)
                .orElseThrow();
        // similarly, must get the completion time of the final job
        LocalDateTime lastJobCompletion = completedJobs.stream()
                .map(Job::getActualCompletionTime)
                .max(LocalDateTime::compareTo)
                .orElseThrow();
        Duration totalJobExecutionDuration = Duration.between(firstJobArrival, lastJobCompletion);
        // throughput tracked as jobs per second
        double throughput = completedJobs.size() / (double) totalJobExecutionDuration.toSeconds();
        throughput = Math.round(throughput * 100.0) / 100.0;
        this.perfMetrics.setThroughput(throughput);
    }

    public void printMetrics() {
        this.calcResponseTimes();
        this.calcThroughput();
        System.out.println("Performance metrics for " + testParams.benchmarkName());
        System.out.println("Average response time: " + this.perfMetrics.getAverageResponseTime() + "ms");
        System.out.println("Max response time: " + this.perfMetrics.getMaxResponseTime() + "ms");
        System.out.println("Throughput: " + this.perfMetrics.getThroughput() + " jobs per second");
    }

    public void addCompletedJob(Job job) {
        this.completedJobs.add(job);
    }

    public ArrayList<Job> getCompletedJobs() {
        return this.completedJobs;
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
    public void run(PerfTestParams testParams) throws InterruptedException {
        // setup
        this.setTestParams(testParams);
        scheduler.setPolicy(this.testParams.policy());
        // run the performance evaluation
        this.generateJobs();
    }


}
