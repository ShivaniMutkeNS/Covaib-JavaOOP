package composition.analytics;

/**
 * Pipeline Error data class
 */
public class PipelineError {
    private final String pipelineId;
    private final String errorType;
    private final String message;
    private final Throwable exception;
    private final long timestamp;
    
    public PipelineError(String pipelineId, String errorType, String message, Throwable exception) {
        this.pipelineId = pipelineId;
        this.errorType = errorType;
        this.message = message;
        this.exception = exception;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getPipelineId() { return pipelineId; }
    public String getErrorType() { return errorType; }
    public String getMessage() { return message; }
    public Throwable getException() { return exception; }
    public long getTimestamp() { return timestamp; }
}
