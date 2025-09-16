package composition.analytics;

import java.util.*;

/**
 * Transform Processor implementation for transforming data records
 */
public class TransformProcessor implements DataProcessor {
    private final DataTransformer transformer;
    private boolean isHealthy;
    
    public TransformProcessor(DataTransformer transformer) {
        this.transformer = transformer;
        this.isHealthy = true;
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("🔄 Initializing transform processor");
        isHealthy = true;
    }
    
    @Override
    public ProcessingResult process(DataBatch batch) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<DataRecord> transformedRecords = new ArrayList<>();
            
            for (DataRecord record : batch.getRecords()) {
                DataRecord transformedRecord = transformer.transform(record);
                if (transformedRecord != null) {
                    transformedRecords.add(transformedRecord);
                }
            }
            
            DataBatch transformedBatch = new DataBatch(transformedRecords);
            long processingTime = System.currentTimeMillis() - startTime;
            
            System.out.printf("🔄 Transformed %d records\n", transformedRecords.size());
            
            return new ProcessingResult(true, transformedBatch, null, processingTime);
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            return new ProcessingResult(false, null, "Transform processing failed: " + e.getMessage(), processingTime);
        }
    }
    
    @Override
    public void cleanup() {
        System.out.println("🔄 Cleaning up transform processor");
    }
    
    @Override
    public boolean isHealthy() {
        return isHealthy;
    }
    
    @Override
    public String getProcessorName() {
        return "Transform Processor";
    }
}
