package composition.analytics;

import java.util.*;

/**
 * Filter Processor implementation for filtering data records
 */
public class FilterProcessor implements DataProcessor {
    private final DataFilter filter;
    private boolean isHealthy;
    
    public FilterProcessor(DataFilter filter) {
        this.filter = filter;
        this.isHealthy = true;
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("🔍 Initializing filter processor");
        isHealthy = true;
    }
    
    @Override
    public ProcessingResult process(DataBatch batch) {
        long startTime = System.currentTimeMillis();
        
        try {
            DataBatch filteredBatch = batch.filter(filter);
            long processingTime = System.currentTimeMillis() - startTime;
            
            System.out.printf("🔍 Filtered %d records to %d records\n", 
                            batch.size(), filteredBatch.size());
            
            return new ProcessingResult(true, filteredBatch, null, processingTime);
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            return new ProcessingResult(false, null, "Filter processing failed: " + e.getMessage(), processingTime);
        }
    }
    
    @Override
    public void cleanup() {
        System.out.println("🔍 Cleaning up filter processor");
    }
    
    @Override
    public boolean isHealthy() {
        return isHealthy;
    }
    
    @Override
    public String getProcessorName() {
        return "Filter Processor";
    }
}
