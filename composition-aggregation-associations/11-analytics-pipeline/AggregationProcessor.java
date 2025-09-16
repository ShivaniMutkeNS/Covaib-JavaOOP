package composition.analytics;

import java.util.*;

/**
 * Aggregation Processor implementation for aggregating data records
 */
public class AggregationProcessor implements DataProcessor {
    private final String groupByField;
    private final String aggregateField;
    private final AggregationType aggregationType;
    private boolean isHealthy;
    
    public AggregationProcessor(String groupByField, String aggregateField, AggregationType aggregationType) {
        this.groupByField = groupByField;
        this.aggregateField = aggregateField;
        this.aggregationType = aggregationType;
        this.isHealthy = true;
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("📊 Initializing aggregation processor");
        isHealthy = true;
    }
    
    @Override
    public ProcessingResult process(DataBatch batch) {
        long startTime = System.currentTimeMillis();
        
        try {
            Map<Object, List<DataRecord>> groups = new HashMap<>();
            
            // Group records by field
            for (DataRecord record : batch.getRecords()) {
                Object groupKey = record.getValue(groupByField);
                groups.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(record);
            }
            
            List<DataRecord> aggregatedRecords = new ArrayList<>();
            
            // Aggregate each group
            for (Map.Entry<Object, List<DataRecord>> entry : groups.entrySet()) {
                Object groupKey = entry.getKey();
                List<DataRecord> groupRecords = entry.getValue();
                
                double aggregatedValue = calculateAggregation(groupRecords);
                
                Map<String, Object> aggregatedData = new HashMap<>();
                aggregatedData.put(groupByField, groupKey);
                aggregatedData.put(aggregateField + "_" + aggregationType.name().toLowerCase(), aggregatedValue);
                aggregatedData.put("count", groupRecords.size());
                aggregatedData.put("timestamp", System.currentTimeMillis());
                
                aggregatedRecords.add(new DataRecord("agg_" + groupKey + "_" + System.currentTimeMillis(), aggregatedData));
            }
            
            DataBatch aggregatedBatch = new DataBatch(aggregatedRecords);
            long processingTime = System.currentTimeMillis() - startTime;
            
            System.out.printf("📊 Aggregated %d records into %d groups\n", 
                            batch.size(), aggregatedRecords.size());
            
            return new ProcessingResult(true, aggregatedBatch, null, processingTime);
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            return new ProcessingResult(false, null, "Aggregation processing failed: " + e.getMessage(), processingTime);
        }
    }
    
    private double calculateAggregation(List<DataRecord> records) {
        List<Double> values = new ArrayList<>();
        
        for (DataRecord record : records) {
            Object value = record.getValue(aggregateField);
            if (value instanceof Number) {
                values.add(((Number) value).doubleValue());
            }
        }
        
        if (values.isEmpty()) {
            return 0.0;
        }
        
        switch (aggregationType) {
            case SUM:
                return values.stream().mapToDouble(Double::doubleValue).sum();
            case AVG:
                return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            case MIN:
                return values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            case MAX:
                return values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            case COUNT:
                return values.size();
            default:
                return 0.0;
        }
    }
    
    @Override
    public void cleanup() {
        System.out.println("📊 Cleaning up aggregation processor");
    }
    
    @Override
    public boolean isHealthy() {
        return isHealthy;
    }
    
    @Override
    public String getProcessorName() {
        return "Aggregation Processor (" + aggregationType + " of " + aggregateField + " by " + groupByField + ")";
    }
}
