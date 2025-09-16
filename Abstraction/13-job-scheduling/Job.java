import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;

/**
 * Represents a job in the scheduling system with execution details and metadata
 */
public class Job {
    private String jobId;
    private String name;
    private String description;
    private JobPriority priority;
    private JobStatus status;
    private LocalDateTime createdTime;
    private LocalDateTime scheduledTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration estimatedDuration;
    private Duration actualDuration;
    private String owner;
    private String[] dependencies;
    private int maxRetries;
    private int retryCount;
    private String errorMessage;
    private double cpuRequirement; // CPU cores needed
    private double memoryRequirement; // Memory in GB
    private String[] tags;
    
    public Job(String name, String description, JobPriority priority, String owner) {
        this.jobId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.owner = owner;
        this.status = JobStatus.PENDING;
        this.createdTime = LocalDateTime.now();
        this.scheduledTime = LocalDateTime.now();
        this.estimatedDuration = Duration.ofMinutes(5); // Default 5 minutes
        this.maxRetries = 3;
        this.retryCount = 0;
        this.cpuRequirement = 1.0;
        this.memoryRequirement = 0.5;
        this.dependencies = new String[0];
        this.tags = new String[0];
    }
    
    public void start() {
        if (status.canTransitionTo(JobStatus.RUNNING)) {
            this.status = JobStatus.RUNNING;
            this.startTime = LocalDateTime.now();
            System.out.println("🚀 Job started: " + name);
        } else {
            System.out.println("❌ Cannot start job in current state: " + status);
        }
    }
    
    public void complete() {
        if (status.canTransitionTo(JobStatus.COMPLETED)) {
            this.status = JobStatus.COMPLETED;
            this.endTime = LocalDateTime.now();
            if (startTime != null) {
                this.actualDuration = Duration.between(startTime, endTime);
            }
            System.out.println("✅ Job completed: " + name);
        }
    }
    
    public void fail(String errorMessage) {
        if (status.canTransitionTo(JobStatus.FAILED)) {
            this.status = JobStatus.FAILED;
            this.endTime = LocalDateTime.now();
            this.errorMessage = errorMessage;
            if (startTime != null) {
                this.actualDuration = Duration.between(startTime, endTime);
            }
            System.out.println("❌ Job failed: " + name + " - " + errorMessage);
        }
    }
    
    public void cancel() {
        if (status.canTransitionTo(JobStatus.CANCELLED)) {
            this.status = JobStatus.CANCELLED;
            this.endTime = LocalDateTime.now();
            System.out.println("🚫 Job cancelled: " + name);
        }
    }
    
    public void pause() {
        if (status.canTransitionTo(JobStatus.PAUSED)) {
            this.status = JobStatus.PAUSED;
            System.out.println("⏸️ Job paused: " + name);
        }
    }
    
    public void retry() {
        if (retryCount < maxRetries && status.canTransitionTo(JobStatus.RETRYING)) {
            this.status = JobStatus.RETRYING;
            this.retryCount++;
            this.errorMessage = null;
            System.out.println("🔄 Job retry attempt " + retryCount + "/" + maxRetries + ": " + name);
        } else {
            System.out.println("❌ Cannot retry job: max retries exceeded or invalid state");
        }
    }
    
    public void schedule(LocalDateTime scheduledTime) {
        if (status.canTransitionTo(JobStatus.SCHEDULED)) {
            this.status = JobStatus.SCHEDULED;
            this.scheduledTime = scheduledTime;
            System.out.println("📅 Job scheduled for: " + scheduledTime);
        }
    }
    
    public boolean isReadyToRun() {
        return (status == JobStatus.PENDING || status == JobStatus.RETRYING) && 
               LocalDateTime.now().isAfter(scheduledTime);
    }
    
    public boolean hasDependencies() {
        return dependencies.length > 0;
    }
    
    public boolean hasTag(String tag) {
        for (String t : tags) {
            if (t.equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }
    
    public double getProgressPercentage() {
        if (status == JobStatus.COMPLETED) return 100.0;
        if (status == JobStatus.FAILED || status == JobStatus.CANCELLED) return 0.0;
        if (status == JobStatus.RUNNING && startTime != null) {
            Duration elapsed = Duration.between(startTime, LocalDateTime.now());
            if (estimatedDuration.toMillis() > 0) {
                return Math.min(100.0, (elapsed.toMillis() * 100.0) / estimatedDuration.toMillis());
            }
        }
        return 0.0;
    }
    
    // Getters
    public String getJobId() { return jobId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public JobPriority getPriority() { return priority; }
    public JobStatus getStatus() { return status; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Duration getEstimatedDuration() { return estimatedDuration; }
    public Duration getActualDuration() { return actualDuration; }
    public String getOwner() { return owner; }
    public String[] getDependencies() { return dependencies.clone(); }
    public int getMaxRetries() { return maxRetries; }
    public int getRetryCount() { return retryCount; }
    public String getErrorMessage() { return errorMessage; }
    public double getCpuRequirement() { return cpuRequirement; }
    public double getMemoryRequirement() { return memoryRequirement; }
    public String[] getTags() { return tags.clone(); }
    
    // Setters
    public void setPriority(JobPriority priority) { this.priority = priority; }
    public void setEstimatedDuration(Duration estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public void setDependencies(String[] dependencies) { this.dependencies = dependencies.clone(); }
    public void setCpuRequirement(double cpuRequirement) { this.cpuRequirement = cpuRequirement; }
    public void setMemoryRequirement(double memoryRequirement) { this.memoryRequirement = memoryRequirement; }
    public void setTags(String[] tags) { this.tags = tags.clone(); }
    
    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - %s", 
            jobId.substring(0, 8), name, priority.getDisplayName(), status.getDisplayName());
    }
    
    public String getDetailedInfo() {
        return String.format(
            "📋 Job Details:\n" +
            "ID: %s\n" +
            "Name: %s\n" +
            "Description: %s\n" +
            "Priority: %s\n" +
            "Status: %s\n" +
            "Owner: %s\n" +
            "Created: %s\n" +
            "Scheduled: %s\n" +
            "Progress: %.1f%%\n" +
            "CPU Required: %.1f cores\n" +
            "Memory Required: %.1f GB\n" +
            "Retries: %d/%d\n" +
            "Dependencies: %s\n" +
            "Tags: %s",
            jobId, name, description, priority.getDisplayName(), status.getDisplayName(),
            owner, createdTime, scheduledTime, getProgressPercentage(),
            cpuRequirement, memoryRequirement, retryCount, maxRetries,
            dependencies.length > 0 ? String.join(", ", dependencies) : "None",
            tags.length > 0 ? String.join(", ", tags) : "None"
        );
    }
}
