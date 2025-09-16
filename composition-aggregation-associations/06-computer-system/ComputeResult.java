package composition.computer;

/**
 * Compute Result data class
 */
public class ComputeResult {
    private final boolean success;
    private final String message;
    private final double performanceScore;
    
    public ComputeResult(boolean success, String message, double performanceScore) {
        this.success = success;
        this.message = message;
        this.performanceScore = performanceScore;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getPerformanceScore() { return performanceScore; }
}
