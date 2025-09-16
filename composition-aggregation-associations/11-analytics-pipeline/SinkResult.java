package composition.analytics;

/**
 * Sink Result data class
 */
public class SinkResult {
    private final boolean success;
    private final int recordsWritten;
    private final String errorMessage;
    
    public SinkResult(boolean success, int recordsWritten, String errorMessage) {
        this.success = success;
        this.recordsWritten = recordsWritten;
        this.errorMessage = errorMessage;
    }
    
    public boolean isSuccess() { return success; }
    public int getRecordsWritten() { return recordsWritten; }
    public String getErrorMessage() { return errorMessage; }
}
