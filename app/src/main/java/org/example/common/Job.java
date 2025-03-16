package org.example.common;

import java.time.LocalDateTime;

public class Job {
    private final String name;
    private final int executionPriority;
    private final long executionTimeMs;
    private boolean isCompleted = false;
    private final LocalDateTime arrival;

    public Job(String name, long executionTimeMs) {
        /* Sets the execution priority to 1 (highest priority) by default.
        In the case where priority is not specified or "Priority" scheduling policy is not used,
        this ensures that the priority value does not affect whatever scheduling policy is used.
        */
        this(name, 1, executionTimeMs, null);
    }

    public Job(String name, int executionPriority, long executionTimeMs, LocalDateTime date) {
        this.name = name;
        this.executionPriority = executionPriority;
        this.executionTimeMs = executionTimeMs;
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

    public long getExecutionTimeMs() {
        return this.executionTimeMs;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
