package org.example.common;

import java.time.LocalDateTime;

public class Job {
    private final String name;
    private final int executionPriority;
    private final long executionTime;
    private boolean isCompleted = false;
    private final LocalDateTime arrival;
    private LocalDateTime actualCompletionTime;
    private LocalDateTime actualProcessingStartTime;

    public Job(String name, long executionTime) {
        /* Sets the execution priority to 1 (highest priority) by default.
        In the case where priority is not specified or "Priority" scheduling policy is not used,
        this ensures that the priority value does not affect whatever scheduling policy is used.
        */
        this(name, 1, executionTime, null);
    }

    public Job(String name, int executionPriority, long executionTime, LocalDateTime date) {
        this.name = name;
        this.executionPriority = executionPriority;
        this.executionTime = executionTime;
        this.arrival = date;
    }
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
}
