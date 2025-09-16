package composition.analytics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Default Metrics Collector implementation
 */
public class DefaultMetricsCollector implements MetricsCollector {
    private final PipelineMetrics metrics;
    private final AtomicLong startCount;
    private final AtomicLong stopCount;
    
    public DefaultMetricsCollector() {
        this.metrics = new PipelineMetrics();
        this.startCount = new AtomicLong(0);
        this.stopCount = new AtomicLong(0);
    }
    
    @Override
    public void recordPipelineStart() {
        startCount.incrementAndGet();
        System.out.println("📈 Pipeline start recorded");
    }
    
    @Override
    public void recordPipelineStop() {
        stopCount.incrementAndGet();
        System.out.println("📈 Pipeline stop recorded");
    }
    
    @Override
    public void recordBatchReceived(int recordCount) {
        metrics.incrementBatchesProcessed();
        metrics.addRecordsProcessed(recordCount);
    }
    
    @Override
    public void recordProcessorExecution(String processorName, long processingTime) {
        metrics.addProcessingTime(processingTime);
    }
    
    @Override
    public void recordSinkWrite(String sinkName, int recordCount) {
        // Additional sink-specific metrics could be tracked here
    }
    
    @Override
    public void recordError(String errorType) {
        metrics.incrementErrorCount();
    }
    
    @Override
    public PipelineMetrics getMetrics() {
        return metrics;
    }
    
    @Override
    public String getCollectorName() {
        return "Default Metrics Collector";
    }
}
