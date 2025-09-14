/**
 * Enumeration representing different job priority levels
 * Higher priority jobs are executed before lower priority ones
 */
public enum JobPriority {
    CRITICAL(5, "Critical", "System-critical tasks that must run immediately"),
    HIGH(4, "High", "Important tasks that should run soon"),
    NORMAL(3, "Normal", "Standard priority tasks"),
    LOW(2, "Low", "Background tasks that can wait"),
    DEFERRED(1, "Deferred", "Tasks that run when system is idle");
    
    private final int level;
    private final String displayName;
    private final String description;
    
    JobPriority(int level, String displayName, String description) {
        this.level = level;
        this.displayName = displayName;
        this.description = description;
    }
    
    public int getLevel() { return level; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    
    public boolean isHigherThan(JobPriority other) {
        return this.level > other.level;
    }
    
    public boolean isLowerThan(JobPriority other) {
        return this.level < other.level;
    }
    
    @Override
    public String toString() {
        return String.format("%s (Level %d) - %s", displayName, level, description);
    }
}
