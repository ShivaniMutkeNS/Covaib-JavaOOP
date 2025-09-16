import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base class for all analytics engine implementations
 * Defines core analytics operations and data processing capabilities
 */
public abstract class AnalyticsEngine {
    protected String engineName;
    protected AnalyticsType supportedType;
    protected List<DataSource> dataSources;
    protected Map<String, Object> configuration;
    protected boolean isInitialized;
    protected long totalProcessingTime;
    protected int analysesCompleted;
    protected double averageConfidence;
    
    public AnalyticsEngine(String engineName, AnalyticsType supportedType) {
        this.engineName = engineName;
        this.supportedType = supportedType;
        this.dataSources = new java.util.ArrayList<>();
        this.configuration = new java.util.HashMap<>();
        this.isInitialized = false;
        this.totalProcessingTime = 0;
        this.analysesCompleted = 0;
        this.averageConfidence = 0.0;
    }
    
    // Abstract methods that must be implemented by concrete engines
    public abstract AnalyticsResult performAnalysis(String analysisName, Map<String, Object> parameters);
    public abstract boolean validateData(DataSource dataSource);
    public abstract void trainModel(List<DataSource> trainingSources);
    public abstract String getEngineCapabilities();
    
    // Concrete methods with default implementation
    public void initialize() {
        if (!isInitialized) {
            System.out.println("🔧 Initializing " + engineName + " for " + supportedType.getDisplayName());
            loadConfiguration();
            validateConfiguration();
            this.isInitialized = true;
            System.out.println("✅ " + engineName + " initialized successfully");
        }
    }
    
    public void addDataSource(DataSource dataSource) {
        if (validateData(dataSource)) {
            dataSources.add(dataSource);
            System.out.println("📊 Added data source: " + dataSource.getName());
        } else {
            System.out.println("❌ Invalid data source: " + dataSource.getName());
        }
    }
    
    public void removeDataSource(String sourceId) {
        dataSources.removeIf(ds -> ds.getSourceId().equals(sourceId));
        System.out.println("🗑️ Removed data source: " + sourceId);
    }
    
    public AnalyticsResult runAnalysis(String analysisName, Map<String, Object> parameters) {
        if (!isInitialized) {
            initialize();
        }
        
        long startTime = System.currentTimeMillis();
        System.out.println("🚀 Starting analysis: " + analysisName);
        
        try {
            AnalyticsResult result = performAnalysis(analysisName, parameters);
            long processingTime = System.currentTimeMillis() - startTime;
            
            result.markSuccessful(processingTime);
            updateStatistics(result);
            
            System.out.println("✅ Analysis completed: " + analysisName + " (" + processingTime + "ms)");
            return result;
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            AnalyticsResult errorResult = new AnalyticsResult(
                UUID.randomUUID().toString(), analysisName, supportedType);
            errorResult.markFailed(e.getMessage(), processingTime);
            
            System.out.println("❌ Analysis failed: " + analysisName + " - " + e.getMessage());
            return errorResult;
        }
    }
    
    protected void loadConfiguration() {
        // Default configuration loading
        configuration.put("maxRecords", 1000000);
        configuration.put("timeout", 30000);
        configuration.put("cacheEnabled", true);
        configuration.put("parallelProcessing", true);
    }
    
    protected void validateConfiguration() {
        if (dataSources.isEmpty()) {
            System.out.println("⚠️ Warning: No data sources configured");
        }
        
        if (supportedType.requiresModelTraining() && !isModelTrained()) {
            System.out.println("⚠️ Warning: Model training required for " + supportedType.getDisplayName());
        }
    }
    
    protected boolean isModelTrained() {
        return configuration.containsKey("modelTrained") && 
               (Boolean) configuration.get("modelTrained");
    }
    
    protected void updateStatistics(AnalyticsResult result) {
        analysesCompleted++;
        totalProcessingTime += result.getProcessingTimeMs();
        
        // Update average confidence
        double totalConfidence = averageConfidence * (analysesCompleted - 1) + result.getConfidenceScore();
        averageConfidence = totalConfidence / analysesCompleted;
    }
    
    public void configure(String key, Object value) {
        configuration.put(key, value);
        System.out.println("⚙️ Configuration updated: " + key + " = " + value);
    }
    
    public Object getConfiguration(String key) {
        return configuration.get(key);
    }
    
    public void printStatistics() {
        System.out.println("\n📈 ENGINE STATISTICS: " + engineName);
        System.out.println("=".repeat(50));
        System.out.println("Type: " + supportedType.getDisplayName());
        System.out.println("Status: " + (isInitialized ? "Initialized" : "Not Initialized"));
        System.out.println("Data Sources: " + dataSources.size());
        System.out.println("Analyses Completed: " + analysesCompleted);
        System.out.println("Total Processing Time: " + totalProcessingTime + "ms");
        System.out.println("Average Processing Time: " + 
                          (analysesCompleted > 0 ? totalProcessingTime / analysesCompleted : 0) + "ms");
        System.out.println("Average Confidence: " + String.format("%.1f%%", averageConfidence * 100));
        System.out.println("Capabilities: " + getEngineCapabilities());
        
        if (!dataSources.isEmpty()) {
            System.out.println("\nData Sources:");
            for (DataSource ds : dataSources) {
                System.out.println("  • " + ds.toString());
            }
        }
        System.out.println();
    }
    
    public boolean canProcess(AnalyticsType type) {
        return this.supportedType == type;
    }
    
    public boolean hasDataSource(String sourceId) {
        return dataSources.stream().anyMatch(ds -> ds.getSourceId().equals(sourceId));
    }
    
    public DataSource getDataSource(String sourceId) {
        return dataSources.stream()
                         .filter(ds -> ds.getSourceId().equals(sourceId))
                         .findFirst()
                         .orElse(null);
    }
    
    // Getters
    public String getEngineName() { return engineName; }
    public AnalyticsType getSupportedType() { return supportedType; }
    public List<DataSource> getDataSources() { return new java.util.ArrayList<>(dataSources); }
    public boolean isInitialized() { return isInitialized; }
    public long getTotalProcessingTime() { return totalProcessingTime; }
    public int getAnalysesCompleted() { return analysesCompleted; }
    public double getAverageConfidence() { return averageConfidence; }
    public double getAverageProcessingTime() { 
        return analysesCompleted > 0 ? (double) totalProcessingTime / analysesCompleted : 0.0; 
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %d analyses, %.1f%% avg confidence", 
            engineName, supportedType.getDisplayName(), analysesCompleted, averageConfidence * 100);
    }
}
