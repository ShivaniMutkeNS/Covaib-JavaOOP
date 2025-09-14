import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Descriptive analytics engine implementation
 * Focuses on summarizing historical data and providing statistical insights
 */
public class DescriptiveAnalyticsEngine extends AnalyticsEngine {
    private Map<String, Double> aggregationCache;
    private boolean enableCaching;
    
    public DescriptiveAnalyticsEngine(String engineName) {
        super(engineName, AnalyticsType.DESCRIPTIVE);
        this.aggregationCache = new HashMap<>();
        this.enableCaching = true;
    }
    
    @Override
    public AnalyticsResult performAnalysis(String analysisName, Map<String, Object> parameters) {
        AnalyticsResult result = new AnalyticsResult(UUID.randomUUID().toString(), analysisName, supportedType);
        
        // Simulate data processing
        long recordsProcessed = simulateDataProcessing(parameters);
        result.setRecordsProcessed(recordsProcessed);
        result.setDataSourcesUsed(dataSources.stream().map(DataSource::getName).toArray(String[]::new));
        
        // Calculate descriptive statistics
        calculateBasicStatistics(result, parameters);
        calculateDistributionMetrics(result, parameters);
        generateDescriptiveInsights(result);
        
        result.setConfidenceScore(0.95); // High confidence for descriptive analytics
        return result;
    }
    
    private void calculateBasicStatistics(AnalyticsResult result, Map<String, Object> parameters) {
        // Simulate statistical calculations
        String metric = (String) parameters.getOrDefault("metric", "revenue");
        
        // Generate realistic statistical values
        double mean = 1000 + Math.random() * 5000;
        double median = mean * (0.8 + Math.random() * 0.4);
        double stdDev = mean * (0.1 + Math.random() * 0.3);
        double min = mean - 3 * stdDev;
        double max = mean + 3 * stdDev;
        
        result.addMetric("mean_" + metric, Math.round(mean * 100.0) / 100.0);
        result.addMetric("median_" + metric, Math.round(median * 100.0) / 100.0);
        result.addMetric("std_deviation_" + metric, Math.round(stdDev * 100.0) / 100.0);
        result.addMetric("min_" + metric, Math.round(min * 100.0) / 100.0);
        result.addMetric("max_" + metric, Math.round(max * 100.0) / 100.0);
        result.addMetric("range_" + metric, Math.round((max - min) * 100.0) / 100.0);
        
        // Cache results if enabled
        if (enableCaching) {
            String cacheKey = analysisName + "_" + metric;
            aggregationCache.put(cacheKey + "_mean", mean);
            aggregationCache.put(cacheKey + "_median", median);
        }
    }
    
    private void calculateDistributionMetrics(AnalyticsResult result, Map<String, Object> parameters) {
        String metric = (String) parameters.getOrDefault("metric", "revenue");
        
        // Calculate quartiles and percentiles
        double q1 = result.getNumericMetric("median_" + metric, 0) * 0.75;
        double q3 = result.getNumericMetric("median_" + metric, 0) * 1.25;
        double p90 = result.getNumericMetric("max_" + metric, 0) * 0.9;
        double p95 = result.getNumericMetric("max_" + metric, 0) * 0.95;
        
        result.addMetric("q1_" + metric, Math.round(q1 * 100.0) / 100.0);
        result.addMetric("q3_" + metric, Math.round(q3 * 100.0) / 100.0);
        result.addMetric("iqr_" + metric, Math.round((q3 - q1) * 100.0) / 100.0);
        result.addMetric("p90_" + metric, Math.round(p90 * 100.0) / 100.0);
        result.addMetric("p95_" + metric, Math.round(p95 * 100.0) / 100.0);
        
        // Calculate skewness and kurtosis (simplified)
        double skewness = (Math.random() - 0.5) * 2; // -1 to 1
        double kurtosis = 1 + Math.random() * 4; // 1 to 5
        
        result.addMetric("skewness_" + metric, Math.round(skewness * 100.0) / 100.0);
        result.addMetric("kurtosis_" + metric, Math.round(kurtosis * 100.0) / 100.0);
    }
    
    private void generateDescriptiveInsights(AnalyticsResult result) {
        Map<String, Object> metrics = result.getMetrics();
        
        // Generate insights based on calculated metrics
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("mean_")) {
                String metric = key.substring(5);
                double mean = (Double) entry.getValue();
                double median = result.getNumericMetric("median_" + metric, 0);
                
                if (mean > median * 1.1) {
                    result.addInsight("The " + metric + " distribution is right-skewed with mean (" + 
                                    String.format("%.2f", mean) + ") higher than median (" + 
                                    String.format("%.2f", median) + ")");
                } else if (mean < median * 0.9) {
                    result.addInsight("The " + metric + " distribution is left-skewed with mean (" + 
                                    String.format("%.2f", mean) + ") lower than median (" + 
                                    String.format("%.2f", median) + ")");
                } else {
                    result.addInsight("The " + metric + " distribution is approximately symmetric");
                }
                
                double stdDev = result.getNumericMetric("std_deviation_" + metric, 0);
                double cv = stdDev / mean; // Coefficient of variation
                
                if (cv > 0.5) {
                    result.addInsight("High variability detected in " + metric + " (CV: " + 
                                    String.format("%.2f", cv) + ")");
                } else if (cv < 0.1) {
                    result.addInsight("Low variability detected in " + metric + " (CV: " + 
                                    String.format("%.2f", cv) + ")");
                }
            }
        }
        
        // Add general insights
        result.addInsight("Data summary completed with " + result.getRecordsProcessed() + " records analyzed");
        result.addInsight("Statistical analysis shows " + metrics.size() + " key metrics computed");
    }
    
    @Override
    public boolean validateData(DataSource dataSource) {
        if (!dataSource.isActive()) {
            System.out.println("❌ Data source is not active: " + dataSource.getName());
            return false;
        }
        
        if (dataSource.getRecordCount() == 0) {
            System.out.println("❌ Data source has no records: " + dataSource.getName());
            return false;
        }
        
        if (!dataSource.isConfigured()) {
            System.out.println("❌ Data source is not properly configured: " + dataSource.getName());
            return false;
        }
        
        // Check for required numeric fields
        String[] fields = dataSource.getAvailableFields();
        boolean hasNumericField = false;
        for (String field : fields) {
            String type = dataSource.getFieldType(field);
            if ("NUMERIC".equals(type) || "INTEGER".equals(type) || "DOUBLE".equals(type)) {
                hasNumericField = true;
                break;
            }
        }
        
        if (!hasNumericField) {
            System.out.println("❌ Data source lacks numeric fields for statistical analysis: " + dataSource.getName());
            return false;
        }
        
        return true;
    }
    
    @Override
    public void trainModel(List<DataSource> trainingSources) {
        // Descriptive analytics doesn't require model training
        System.out.println("ℹ️ Descriptive analytics does not require model training");
        configuration.put("modelTrained", true);
    }
    
    @Override
    public String getEngineCapabilities() {
        return "Statistical summaries, distribution analysis, aggregations, trend identification";
    }
    
    public void setCachingEnabled(boolean enabled) {
        this.enableCaching = enabled;
        if (!enabled) {
            aggregationCache.clear();
        }
        System.out.println("🗄️ Caching " + (enabled ? "enabled" : "disabled"));
    }
    
    public Map<String, Double> getCachedResults() {
        return new HashMap<>(aggregationCache);
    }
    
    public void clearCache() {
        aggregationCache.clear();
        System.out.println("🗑️ Aggregation cache cleared");
    }
    
    private long simulateDataProcessing(Map<String, Object> parameters) {
        // Simulate processing time based on data size
        long baseRecords = 10000;
        String timeRange = (String) parameters.getOrDefault("timeRange", "month");
        
        switch (timeRange.toLowerCase()) {
            case "day": return baseRecords;
            case "week": return baseRecords * 7;
            case "month": return baseRecords * 30;
            case "quarter": return baseRecords * 90;
            case "year": return baseRecords * 365;
            default: return baseRecords;
        }
    }
    
    @Override
    public void printStatistics() {
        super.printStatistics();
        System.out.println("Cache Enabled: " + enableCaching);
        System.out.println("Cached Results: " + aggregationCache.size());
    }
}
