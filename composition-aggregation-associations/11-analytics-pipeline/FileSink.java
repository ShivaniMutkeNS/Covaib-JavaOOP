package composition.analytics;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * File Sink implementation for writing data to files
 */
public class FileSink implements DataSink {
    private final String filePath;
    private final String format;
    private boolean isHealthy;
    private final AtomicLong recordsWritten;
    
    public FileSink(String filePath, String format) {
        this.filePath = filePath;
        this.format = format;
        this.isHealthy = true;
        this.recordsWritten = new AtomicLong(0);
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("📄 Initializing file sink: " + filePath + " (" + format + ")");
        isHealthy = true;
    }
    
    @Override
    public SinkResult write(DataBatch batch) {
        try {
            // Simulate file write
            for (DataRecord record : batch.getRecords()) {
                String formattedRecord = formatRecord(record);
                System.out.printf("📄 Writing to %s: %s\n", filePath, formattedRecord);
                recordsWritten.incrementAndGet();
            }
            
            return new SinkResult(true, batch.size(), null);
            
        } catch (Exception e) {
            return new SinkResult(false, 0, "File write failed: " + e.getMessage());
        }
    }
    
    private String formatRecord(DataRecord record) {
        switch (format.toLowerCase()) {
            case "json":
                return "{\"id\":\"" + record.getId() + "\",\"data\":" + record.getData() + "}";
            case "csv":
                return record.getId() + "," + record.getData().toString();
            default:
                return record.toString();
        }
    }
    
    @Override
    public void cleanup() {
        System.out.println("📄 Cleaning up file sink");
    }
    
    @Override
    public boolean isHealthy() {
        return isHealthy;
    }
    
    @Override
    public String getSinkName() {
        return "File Sink (" + filePath + ")";
    }
}
