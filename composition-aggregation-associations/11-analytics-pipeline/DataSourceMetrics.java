package composition.analytics;

/**
 * Data Source Metrics data class
 */
public class DataSourceMetrics {
    private final long recordsRead;
    private final long errorCount;
    private final boolean isActive;
    
    public DataSourceMetrics(long recordsRead, long errorCount, boolean isActive) {
        this.recordsRead = recordsRead;
        this.errorCount = errorCount;
        this.isActive = isActive;
    }
    
    public long getRecordsRead() { return recordsRead; }
    public long getErrorCount() { return errorCount; }
    public boolean isActive() { return isActive; }
}
