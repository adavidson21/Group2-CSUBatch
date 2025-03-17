package org.example.common;

import java.time.LocalDateTime;

public class Job {
    private final String name;
    private final int executionPriority;
    private final long executionTimeMs;
    private boolean isCompleted = false;
    private final LocalDateTime arrival;

    public Job(String name, long executionTimeMs) {
        this(name, 1, executionTimeMs, LocalDateTime.now());
    }

    public Job(String name, int executionPriority, long executionTimeMs) {
        this(name, executionPriority, executionTimeMs, LocalDateTime.now());
    }

    public Job(String name, int executionPriority, long executionTimeMs, LocalDateTime date) {
        this.name = name;
        this.executionPriority = executionPriority;
        this.executionTimeMs = executionTimeMs;
        this.arrival = (date != null) ? date : LocalDateTime.now();
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
