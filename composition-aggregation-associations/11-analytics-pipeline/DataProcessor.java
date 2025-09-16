package composition.analytics;

import java.util.Map;

/**
 * Data Processor interface for processing data in the pipeline
 */
public interface DataProcessor {
    void initialize(Map<String, Object> configuration);
    ProcessingResult process(DataBatch batch);
    void cleanup();
    boolean isHealthy();
    String getProcessorName();
}
