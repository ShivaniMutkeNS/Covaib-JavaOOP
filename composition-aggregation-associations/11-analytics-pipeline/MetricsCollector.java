package composition.analytics;

/**
 * Metrics Collector interface for collecting pipeline metrics
 */
public interface MetricsCollector {
    void recordPipelineStart();
    void recordPipelineStop();
    void recordBatchReceived(int recordCount);
    void recordProcessorExecution(String processorName, long processingTime);
    void recordSinkWrite(String sinkName, int recordCount);
    void recordError(String errorType);
    PipelineMetrics getMetrics();
    String getCollectorName();
}
