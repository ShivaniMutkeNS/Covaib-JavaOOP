package composition.analytics;

/**
 * Pipeline Status data class
 */
public class PipelineStatus {
    private final String pipelineId;
    private final PipelineState state;
    private final int processorCount;
    private final int sinkCount;
    private final PipelineMetrics metrics;
    private final long uptime;
    
    public PipelineStatus(String pipelineId, PipelineState state, int processorCount, 
                         int sinkCount, PipelineMetrics metrics, long uptime) {
        this.pipelineId = pipelineId;
        this.state = state;
        this.processorCount = processorCount;
        this.sinkCount = sinkCount;
        this.metrics = metrics;
        this.uptime = uptime;
    }
    
    public String getPipelineId() { return pipelineId; }
    public PipelineState getState() { return state; }
    public int getProcessorCount() { return processorCount; }
    public int getSinkCount() { return sinkCount; }
    public PipelineMetrics getMetrics() { return metrics; }
    public long getUptime() { return uptime; }
}
