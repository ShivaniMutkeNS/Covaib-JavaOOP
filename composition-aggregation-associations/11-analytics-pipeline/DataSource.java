package composition.analytics;

import java.util.Map;

/**
 * Data Source interface for analytics pipeline
 */
public interface DataSource {
    void initialize(Map<String, Object> configuration);
    DataBatch fetchData();
    void stop();
    boolean isHealthy();
    String getSourceName();
    DataSourceMetrics getMetrics();
}
