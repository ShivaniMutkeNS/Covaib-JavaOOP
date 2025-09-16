import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the result of an analytics computation
 * Contains metrics, insights, and metadata about the analysis
 */
public class AnalyticsResult {
    private String resultId;
    private String analysisName;
    private AnalyticsType analyticsType;
    private LocalDateTime computedAt;
    private long processingTimeMs;
    private Map<String, Object> metrics;
    private List<String> insights;
    private Map<String, Object> metadata;
    private double confidenceScore;
    private String[] dataSourcesUsed;
    private long recordsProcessed;
    private boolean isSuccessful;
    private String errorMessage;
    
    public AnalyticsResult(String resultId, String analysisName, AnalyticsType analyticsType) {
        this.resultId = resultId;
        this.analysisName = analysisName;
        this.analyticsType = analyticsType;
        this.computedAt = LocalDateTime.now();
        this.metrics = new HashMap<>();
        this.insights = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.confidenceScore = 0.0;
        this.dataSourcesUsed = new String[0];
        this.recordsProcessed = 0;
        this.isSuccessful = false;
    }
    
    public void addMetric(String name, Object value) {
        metrics.put(name, value);
    }
    
    public void addInsight(String insight) {
        insights.add(insight);
    }
    
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public void markSuccessful(long processingTimeMs) {
        this.isSuccessful = true;
        this.processingTimeMs = processingTimeMs;
        this.errorMessage = null;
    }
    
    public void markFailed(String errorMessage, long processingTimeMs) {
        this.isSuccessful = false;
        this.errorMessage = errorMessage;
        this.processingTimeMs = processingTimeMs;
    }
    
    public Object getMetric(String name) {
        return metrics.get(name);
    }
    
    public boolean hasMetric(String name) {
        return metrics.containsKey(name);
    }
    
    public double getNumericMetric(String name, double defaultValue) {
        Object value = metrics.get(name);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    public String getStringMetric(String name, String defaultValue) {
        Object value = metrics.get(name);
        return value != null ? value.toString() : defaultValue;
    }
    
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("📊 Analytics Result: ").append(analysisName).append("\n");
        summary.append("Type: ").append(analyticsType.getDisplayName()).append("\n");
        summary.append("Status: ").append(isSuccessful ? "✅ Success" : "❌ Failed").append("\n");
        summary.append("Processing Time: ").append(processingTimeMs).append("ms\n");
        summary.append("Records Processed: ").append(recordsProcessed).append("\n");
        summary.append("Confidence: ").append(String.format("%.1f%%", confidenceScore * 100)).append("\n");
        
        if (!metrics.isEmpty()) {
            summary.append("\nKey Metrics:\n");
            metrics.entrySet().stream()
                   .limit(5)
                   .forEach(entry -> summary.append("  • ").append(entry.getKey())
                                           .append(": ").append(entry.getValue()).append("\n"));
        }
        
        if (!insights.isEmpty()) {
            summary.append("\nTop Insights:\n");
            insights.stream()
                   .limit(3)
                   .forEach(insight -> summary.append("  • ").append(insight).append("\n"));
        }
        
        return summary.toString();
    }
    
    // Getters
    public String getResultId() { return resultId; }
    public String getAnalysisName() { return analysisName; }
    public AnalyticsType getAnalyticsType() { return analyticsType; }
    public LocalDateTime getComputedAt() { return computedAt; }
    public long getProcessingTimeMs() { return processingTimeMs; }
    public Map<String, Object> getMetrics() { return new HashMap<>(metrics); }
    public List<String> getInsights() { return new ArrayList<>(insights); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public double getConfidenceScore() { return confidenceScore; }
    public String[] getDataSourcesUsed() { return dataSourcesUsed.clone(); }
    public long getRecordsProcessed() { return recordsProcessed; }
    public boolean isSuccessful() { return isSuccessful; }
    public String getErrorMessage() { return errorMessage; }
    
    // Setters
    public void setConfidenceScore(double confidenceScore) { 
        this.confidenceScore = Math.max(0.0, Math.min(1.0, confidenceScore)); 
    }
    public void setDataSourcesUsed(String[] dataSourcesUsed) { 
        this.dataSourcesUsed = dataSourcesUsed.clone(); 
    }
    public void setRecordsProcessed(long recordsProcessed) { 
        this.recordsProcessed = recordsProcessed; 
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)", 
            resultId.substring(0, Math.min(8, resultId.length())), 
            analysisName, 
            analyticsType.getDisplayName(),
            isSuccessful ? "Success" : "Failed");
    }
}
