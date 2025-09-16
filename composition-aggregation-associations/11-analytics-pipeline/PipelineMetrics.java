package composition.analytics;

/**
 * Pipeline Metrics data class
 */
public class PipelineMetrics {
    private long batchesProcessed;
    private long recordsProcessed;
    private long errorCount;
    private double totalProcessingTime;
    
    public PipelineMetrics() {
        this.batchesProcessed = 0;
        this.recordsProcessed = 0;
        this.errorCount = 0;
        this.totalProcessingTime = 0.0;
    }
    
    public void incrementBatchesProcessed() { batchesProcessed++; }
    public void addRecordsProcessed(long count) { recordsProcessed += count; }
    public void incrementErrorCount() { errorCount++; }
    public void addProcessingTime(double time) { totalProcessingTime += time; }
    
    public long getBatchesProcessed() { return batchesProcessed; }
    public long getRecordsProcessed() { return recordsProcessed; }
    public long getErrorCount() { return errorCount; }
    public double getTotalProcessingTime() { return totalProcessingTime; }
    
    public double getAverageProcessingTime() {
        return batchesProcessed > 0 ? totalProcessingTime / batchesProcessed : 0.0;
    }
}
