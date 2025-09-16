package composition.computer;

import java.util.*;

/**
 * System Monitor for tracking performance metrics
 */
public class SystemMonitor {
    private final List<TaskExecution> taskHistory;
    private boolean isMonitoring;
    private long totalTasks;
    private double averagePerformance;
    
    public SystemMonitor() {
        this.taskHistory = new ArrayList<>();
        this.isMonitoring = false;
        this.totalTasks = 0;
        this.averagePerformance = 0.0;
    }
    
    public void startMonitoring() {
        isMonitoring = true;
        System.out.println("System monitoring started");
    }
    
    public void stopMonitoring() {
        isMonitoring = false;
        System.out.println("System monitoring stopped");
    }
    
    public void recordTaskExecution(ComputeTask task, long executionTime) {
        if (isMonitoring) {
            TaskExecution execution = new TaskExecution(task.getName(), executionTime, System.currentTimeMillis());
            taskHistory.add(execution);
            totalTasks++;
            
            // Keep only last 100 executions
            if (taskHistory.size() > 100) {
                taskHistory.remove(0);
            }
        }
    }
    
    public void displayMetrics() {
        System.out.println("\n--- System Performance Metrics ---");
        System.out.println("Total Tasks Executed: " + totalTasks);
        
        if (!taskHistory.isEmpty()) {
            double avgTime = taskHistory.stream()
                .mapToLong(TaskExecution::getExecutionTime)
                .average()
                .orElse(0.0);
            
            System.out.printf("Average Execution Time: %.2fms\n", avgTime);
            System.out.println("Recent Tasks: " + Math.min(5, taskHistory.size()));
            
            taskHistory.stream()
                .skip(Math.max(0, taskHistory.size() - 5))
                .forEach(execution -> 
                    System.out.printf("- %s: %dms\n", execution.getTaskName(), execution.getExecutionTime()));
        }
    }
    
    private static class TaskExecution {
        private final String taskName;
        private final long executionTime;
        private final long timestamp;
        
        public TaskExecution(String taskName, long executionTime, long timestamp) {
            this.taskName = taskName;
            this.executionTime = executionTime;
            this.timestamp = timestamp;
        }
        
        public String getTaskName() { return taskName; }
        public long getExecutionTime() { return executionTime; }
        public long getTimestamp() { return timestamp; }
    }
}
