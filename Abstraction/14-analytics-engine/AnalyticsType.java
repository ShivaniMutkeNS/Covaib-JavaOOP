/**
 * Enumeration of different analytics types with their characteristics
 * Defines the scope, complexity, and processing requirements for analytics
 */
public enum AnalyticsType {
    DESCRIPTIVE("Descriptive Analytics", "What happened?", 1, true, false),
    DIAGNOSTIC("Diagnostic Analytics", "Why did it happen?", 2, true, true),
    PREDICTIVE("Predictive Analytics", "What will happen?", 3, false, true),
    PRESCRIPTIVE("Prescriptive Analytics", "What should we do?", 4, false, true),
    REAL_TIME("Real-time Analytics", "What is happening now?", 2, true, false),
    BEHAVIORAL("Behavioral Analytics", "How do users behave?", 3, true, true),
    FINANCIAL("Financial Analytics", "What are the financial metrics?", 2, true, false),
    OPERATIONAL("Operational Analytics", "How are operations performing?", 2, true, false);
    
    private final String displayName;
    private final String description;
    private final int complexityLevel; // 1-4 scale
    private final boolean supportsHistorical;
    private final boolean requiresModelTraining;
    
    AnalyticsType(String displayName, String description, int complexityLevel, 
                  boolean supportsHistorical, boolean requiresModelTraining) {
        this.displayName = displayName;
        this.description = description;
        this.complexityLevel = complexityLevel;
        this.supportsHistorical = supportsHistorical;
        this.requiresModelTraining = requiresModelTraining;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getComplexityLevel() {
        return complexityLevel;
    }
    
    public boolean supportsHistorical() {
        return supportsHistorical;
    }
    
    public boolean requiresModelTraining() {
        return requiresModelTraining;
    }
    
    public boolean isAdvanced() {
        return complexityLevel >= 3;
    }
    
    public String getComplexityDescription() {
        switch (complexityLevel) {
            case 1: return "Basic";
            case 2: return "Intermediate";
            case 3: return "Advanced";
            case 4: return "Expert";
            default: return "Unknown";
        }
    }
    
    @Override
    public String toString() {
        return displayName + " (" + getComplexityDescription() + ")";
    }
}
