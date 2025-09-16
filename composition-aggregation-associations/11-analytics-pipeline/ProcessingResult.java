package composition.analytics;

/**
 * Processing Result data class
 */
public class ProcessingResult {
    private final boolean success;
    private final DataBatch processedData;
    private final String errorMessage;
    private final long processingTime;
    
    public ProcessingResult(boolean success, DataBatch processedData, String errorMessage, long processingTime) {
        this.success = success;
        this.processedData = processedData;
        this.errorMessage = errorMessage;
        this.processingTime = processingTime;
    }
    
    public boolean isSuccess() { return success; }
    public DataBatch getProcessedData() { return processedData; }
    public String getErrorMessage() { return errorMessage; }
    public long getProcessingTime() { return processingTime; }
}
