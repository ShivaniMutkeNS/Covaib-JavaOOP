package composition.analytics;

/**
 * Pipeline Result data class
 */
public class PipelineResult {
    private final boolean success;
    private final String message;
    private final PipelineStatus status;
    
    public PipelineResult(boolean success, String message, PipelineStatus status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public PipelineStatus getStatus() { return status; }
}
