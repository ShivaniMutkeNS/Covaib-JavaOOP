package composition.analytics;

import java.util.List;

/**
 * Health Check Result data class
 */
public class HealthCheckResult {
    private final boolean healthy;
    private final String message;
    private final List<String> issues;
    
    public HealthCheckResult(boolean healthy, String message, List<String> issues) {
        this.healthy = healthy;
        this.message = message;
        this.issues = issues;
    }
    
    public boolean isHealthy() { return healthy; }
    public String getMessage() { return message; }
    public List<String> getIssues() { return issues; }
}
