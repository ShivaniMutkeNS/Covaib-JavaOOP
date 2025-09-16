package composition.analytics;

import java.util.Map;

/**
 * Data Sink interface for writing processed data
 */
public interface DataSink {
    void initialize(Map<String, Object> configuration);
    SinkResult write(DataBatch batch);
    void cleanup();
    boolean isHealthy();
    String getSinkName();
}
