package composition.analytics;

import java.util.*;

/**
 * Data Batch class representing a collection of data records
 */
public class DataBatch {
    private final List<DataRecord> records;
    private final long batchId;
    private final long createdAt;
    
    public DataBatch(List<DataRecord> records) {
        this.records = new ArrayList<>(records);
        this.batchId = System.currentTimeMillis();
        this.createdAt = System.currentTimeMillis();
    }
    
    public List<DataRecord> getRecords() { return new ArrayList<>(records); }
    public long getBatchId() { return batchId; }
    public long getCreatedAt() { return createdAt; }
    public int size() { return records.size(); }
    public boolean isEmpty() { return records.isEmpty(); }
    
    public void addRecord(DataRecord record) {
        records.add(record);
    }
    
    public void removeRecord(DataRecord record) {
        records.remove(record);
    }
    
    public DataBatch filter(DataFilter filter) {
        List<DataRecord> filteredRecords = new ArrayList<>();
        for (DataRecord record : records) {
            if (filter.accept(record)) {
                filteredRecords.add(record);
            }
        }
        return new DataBatch(filteredRecords);
    }
}
