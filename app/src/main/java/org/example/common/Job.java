package org.example.common;

import java.time.LocalDateTime;

/**
 * A class that represents a Job in the system.
 */
public class Job {
    private final String name;
    private final int executionPriority;
    private final long executionTime;
    private boolean isCompleted = false;
    private final LocalDateTime arrival;
    private LocalDateTime actualCompletionTime;
    private LocalDateTime actualProcessingStartTime;

    /**
     * Job Constructor.
     * Sets the execution priority to 1 (highest priority) by default.
     * In the case where priority is not specified or "Priority" scheduling policy is not used,
     * this ensures that the priority value does not affect whatever scheduling policy is used.
     * @param name The name of the job.
     * @param executionTime The execution time of the job.
     */
    public Job(String name, long executionTime) {
        /* Sets the execution priority to 1 (highest priority) by default.
        In the case where priority is not specified or "Priority" scheduling policy is not used,
        this ensures that the priority value does not affect whatever scheduling policy is used.
        */
        this(name, 1, executionTime, null);
    }

    /**
     * Job Constructor.
     * @param name The name of the job.
     * @param executionPriority The execution priority of the job.
     * @param executionTime The execution time of the job.
     * @param date The date that represents the arrival (creation) time.
     */
    public Job(String name, int executionPriority, long executionTime, LocalDateTime date) {
        this.name = name;
        this.executionPriority = executionPriority;
        this.executionTime = executionTime;
        this.arrival = date;
    }
    //region Getters and Setters
    public LocalDateTime getArrivalTime(){
        return this.arrival;
    }
    public String getName() {
        return this.name;
    }

    public int getExecutionPriority() {
        return this.executionPriority;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setActualCompletionTime(LocalDateTime actualCompletionTime) {
        this.actualCompletionTime = actualCompletionTime;
    }

    public LocalDateTime getActualCompletionTime() {
        return this.actualCompletionTime;
    }

    public LocalDateTime getActualProcessingStartTime() {
        return this.actualProcessingStartTime;
    }

    public void setActualProcessingStartTime(LocalDateTime actualProcessingStartTime) {
        this.actualProcessingStartTime = actualProcessingStartTime;
    }
    //endregion
}
