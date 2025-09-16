package composition.analytics;

import java.util.Map;

/**
 * Data Record class representing a single data point
 */
public class DataRecord {
    private final String id;
    private final Map<String, Object> data;
    private final long timestamp;
    
    public DataRecord(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getId() { return id; }
    public Map<String, Object> getData() { return data; }
    public long getTimestamp() { return timestamp; }
    
    public Object getValue(String key) {
        return data.get(key);
    }
    
    public void setValue(String key, Object value) {
        data.put(key, value);
    }
}
