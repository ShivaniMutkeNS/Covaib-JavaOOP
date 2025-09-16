package composition.analytics;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Database Sink implementation for writing data to database
 */
public class DatabaseSink implements DataSink {
    private final String databaseUrl;
    private final String tableName;
    private boolean isHealthy;
    private final AtomicLong recordsWritten;
    
    public DatabaseSink(String databaseUrl, String tableName) {
        this.databaseUrl = databaseUrl;
        this.tableName = tableName;
        this.isHealthy = true;
        this.recordsWritten = new AtomicLong(0);
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("🗄️ Initializing database sink: " + databaseUrl + "/" + tableName);
        isHealthy = true;
    }
    
    @Override
    public SinkResult write(DataBatch batch) {
        try {
            // Simulate database write
            for (DataRecord record : batch.getRecords()) {
                // Simulate SQL insert
                System.out.printf("🗄️ INSERT INTO %s VALUES (%s)\n", tableName, record.getId());
                recordsWritten.incrementAndGet();
            }
            
            return new SinkResult(true, batch.size(), null);
            
        } catch (Exception e) {
            return new SinkResult(false, 0, "Database write failed: " + e.getMessage());
        }
    }
    
    @Override
    public void cleanup() {
        System.out.println("🗄️ Cleaning up database sink");
    }
    
    @Override
    public boolean isHealthy() {
        return isHealthy;
    }
    
    @Override
    public String getSinkName() {
        return "Database Sink (" + tableName + ")";
    }
}
