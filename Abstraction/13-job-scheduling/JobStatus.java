/**
 * Enumeration representing different job execution states
 */
public enum JobStatus {
    PENDING("Pending", "Job is waiting to be executed"),
    RUNNING("Running", "Job is currently executing"),
    COMPLETED("Completed", "Job finished successfully"),
    FAILED("Failed", "Job execution failed"),
    CANCELLED("Cancelled", "Job was cancelled before completion"),
    PAUSED("Paused", "Job execution is temporarily suspended"),
    RETRYING("Retrying", "Job is being retried after failure"),
    SCHEDULED("Scheduled", "Job is scheduled for future execution");
    
    private final String displayName;
    private final String description;
    
    JobStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }
    
    public boolean isActive() {
        return this == RUNNING || this == RETRYING;
    }
    
    public boolean canTransitionTo(JobStatus newStatus) {
        switch (this) {
            case PENDING:
                return newStatus == RUNNING || newStatus == CANCELLED || newStatus == SCHEDULED;
            case RUNNING:
                return newStatus == COMPLETED || newStatus == FAILED || newStatus == PAUSED || newStatus == CANCELLED;
            case PAUSED:
                return newStatus == RUNNING || newStatus == CANCELLED;
            case FAILED:
                return newStatus == RETRYING || newStatus == CANCELLED;
            case RETRYING:
                return newStatus == RUNNING || newStatus == FAILED || newStatus == CANCELLED;
            case SCHEDULED:
                return newStatus == PENDING || newStatus == CANCELLED;
            default:
                return false; // Terminal states cannot transition
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s", displayName, description);
    }
}
