package composition.analytics;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * File Data Source implementation
 */
public class FileDataSource implements DataSource {
    private final String filePath;
    private boolean isRunning;
    private final AtomicLong recordsRead;
    private final Random random;
    
    public FileDataSource(String filePath) {
        this.filePath = filePath;
        this.isRunning = false;
        this.recordsRead = new AtomicLong(0);
        this.random = new Random();
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("📁 Initializing file data source: " + filePath);
        isRunning = true;
    }
    
    @Override
    public DataBatch fetchData() {
        if (!isRunning) {
            return null;
        }
        
        // Simulate reading data from file
        List<DataRecord> records = new ArrayList<>();
        int batchSize = 5 + random.nextInt(10); // 5-15 records per batch
        
        for (int i = 0; i < batchSize; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("user_id", "user_" + (1000 + random.nextInt(9000)));
            data.put("event_type", getRandomEventType());
            data.put("timestamp", System.currentTimeMillis() - random.nextInt(3600000)); // Last hour
            data.put("value", random.nextDouble() * 100);
            data.put("session_id", "session_" + random.nextInt(1000));
            
            records.add(new DataRecord("record_" + recordsRead.incrementAndGet(), data));
        }
        
        return new DataBatch(records);
    }
    
    private String getRandomEventType() {
        String[] eventTypes = {"page_view", "click", "purchase", "signup", "login", "logout"};
        return eventTypes[random.nextInt(eventTypes.length)];
    }
    
    @Override
    public void stop() {
        System.out.println("📁 Stopping file data source: " + filePath);
        isRunning = false;
    }
    
    @Override
    public boolean isHealthy() {
        return isRunning;
    }
    
    @Override
    public String getSourceName() {
        return "File Source (" + filePath + ")";
    }
    
    @Override
    public DataSourceMetrics getMetrics() {
        return new DataSourceMetrics(recordsRead.get(), 0, isRunning);
    }
}
