import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a data source for analytics processing
 * Contains metadata and configuration for data ingestion
 */
public class DataSource {
    private String sourceId;
    private String name;
    private String description;
    private DataSourceType type;
    private String connectionString;
    private Map<String, Object> configuration;
    private LocalDateTime lastUpdated;
    private boolean isActive;
    private long recordCount;
    private String[] availableFields;
    private Map<String, String> fieldTypes;
    
    public DataSource(String sourceId, String name, DataSourceType type) {
        this.sourceId = sourceId;
        this.name = name;
        this.type = type;
        this.configuration = new HashMap<>();
        this.fieldTypes = new HashMap<>();
        this.lastUpdated = LocalDateTime.now();
        this.isActive = true;
        this.recordCount = 0;
        this.availableFields = new String[0];
    }
    
    public void updateConfiguration(String key, Object value) {
        configuration.put(key, value);
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void setFieldType(String fieldName, String dataType) {
        fieldTypes.put(fieldName, dataType);
    }
    
    public void updateRecordCount(long count) {
        this.recordCount = count;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public boolean isConfigured() {
        return !configuration.isEmpty() && availableFields.length > 0;
    }
    
    public boolean hasField(String fieldName) {
        for (String field : availableFields) {
            if (field.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }
    
    public String getFieldType(String fieldName) {
        return fieldTypes.getOrDefault(fieldName, "STRING");
    }
    
    // Getters and Setters
    public String getSourceId() { return sourceId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public DataSourceType getType() { return type; }
    public String getConnectionString() { return connectionString; }
    public Map<String, Object> getConfiguration() { return new HashMap<>(configuration); }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public boolean isActive() { return isActive; }
    public long getRecordCount() { return recordCount; }
    public String[] getAvailableFields() { return availableFields.clone(); }
    public Map<String, String> getFieldTypes() { return new HashMap<>(fieldTypes); }
    
    public void setDescription(String description) { this.description = description; }
    public void setConnectionString(String connectionString) { this.connectionString = connectionString; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setAvailableFields(String[] fields) { this.availableFields = fields.clone(); }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %d records", name, type.getDisplayName(), recordCount);
    }
}

/**
 * Enumeration of supported data source types
 */
enum DataSourceType {
    DATABASE("Database", "Relational database connection"),
    FILE("File", "File-based data source"),
    API("API", "REST API endpoint"),
    STREAM("Stream", "Real-time data stream"),
    WAREHOUSE("Data Warehouse", "Enterprise data warehouse"),
    CLOUD("Cloud Storage", "Cloud-based storage service");
    
    private final String displayName;
    private final String description;
    
    DataSourceType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}
