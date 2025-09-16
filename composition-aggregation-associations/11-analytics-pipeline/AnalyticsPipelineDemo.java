package composition.analytics;

import java.util.*;

/**
 * MAANG-Level Analytics Pipeline Demo
 * Demonstrates runtime component swapping, data processing, and pipeline management
 */
public class AnalyticsPipelineDemo {
    
    public static void main(String[] args) {
        System.out.println("📊 MAANG-Level Analytics Pipeline System Demo");
        System.out.println("=============================================");
        
        // Create analytics pipeline
        AnalyticsPipeline pipeline = new AnalyticsPipeline("ANALYTICS_PIPELINE_001");
        
        // Add event listener for monitoring
        pipeline.addEventListener(new PipelineEventListener() {
            @Override
            public void onPipelineEvent(String pipelineId, String message, PipelineStatus status) {
                System.out.println("📢 [" + pipelineId + "] " + message);
            }
        });
        
        System.out.println("\n=== 1. Setting Up Data Sources ===");
        
        // Configure data source
        FileDataSource fileSource = new FileDataSource("/data/user_events.log");
        pipeline.setDataSource(fileSource);
        
        System.out.println("\n=== 2. Adding Data Processors ===");
        
        // Add filter processor
        DataFilter eventFilter = record -> {
            String eventType = (String) record.getValue("event_type");
            return eventType != null && !eventType.equals("page_view"); // Filter out page views
        };
        pipeline.addProcessor(new FilterProcessor(eventFilter));
        
        // Add transform processor
        DataTransformer enrichTransformer = record -> {
            Map<String, Object> data = new HashMap<>(record.getData());
            
            // Enrich with additional fields
            data.put("processed_at", System.currentTimeMillis());
            data.put("pipeline_id", "ANALYTICS_PIPELINE_001");
            
            // Normalize event types
            String eventType = (String) data.get("event_type");
            if (eventType != null) {
                data.put("event_category", categorizeEvent(eventType));
            }
            
            return new DataRecord(record.getId() + "_enriched", data);
        };
        pipeline.addProcessor(new TransformProcessor(enrichTransformer));
        
        // Add aggregation processor
        pipeline.addProcessor(new AggregationProcessor("event_category", "value", AggregationType.AVG));
        
        System.out.println("\n=== 3. Adding Data Sinks ===");
        
        // Add database sink
        pipeline.addSink(new DatabaseSink("jdbc:postgresql://localhost:5432/analytics", "processed_events"));
        
        // Add file sink
        pipeline.addSink(new FileSink("/output/processed_events.json", "json"));
        
        pipeline.displayPipelineStatus();
        
        System.out.println("\n=== 4. Pipeline Health Check ===");
        
        HealthCheckResult healthCheck = pipeline.performHealthCheck();
        System.out.println("Health Status: " + (healthCheck.isHealthy() ? "✅ Healthy" : "❌ Unhealthy"));
        System.out.println("Message: " + healthCheck.getMessage());
        
        if (!healthCheck.getIssues().isEmpty()) {
            System.out.println("Issues:");
            for (String issue : healthCheck.getIssues()) {
                System.out.println("   • " + issue);
            }
        }
        
        System.out.println("\n=== 5. Starting Pipeline ===");
        
        PipelineResult startResult = pipeline.start();
        System.out.println("Start Result: " + (startResult.isSuccess() ? "✅ Success" : "❌ Failed"));
        System.out.println("Message: " + startResult.getMessage());
        
        if (startResult.isSuccess()) {
            // Let pipeline run for a few seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            pipeline.displayPipelineStatus();
        }
        
        System.out.println("\n=== 6. Manual Data Processing ===");
        
        // Process individual records manually
        Map<String, Object> testData = new HashMap<>();
        testData.put("user_id", "user_12345");
        testData.put("event_type", "purchase");
        testData.put("value", 99.99);
        testData.put("timestamp", System.currentTimeMillis());
        
        DataRecord testRecord = new DataRecord("manual_test_001", testData);
        ProcessingResult manualResult = pipeline.processData(testRecord);
        
        System.out.println("Manual Processing: " + (manualResult.isSuccess() ? "✅ Success" : "❌ Failed"));
        if (!manualResult.isSuccess()) {
            System.out.println("Error: " + manualResult.getErrorMessage());
        }
        
        System.out.println("\n=== 7. Runtime Component Swapping ===");
        
        // Pause pipeline for component swapping
        pipeline.pause();
        
        // Swap data source
        System.out.println("\nSwapping to stream data source...");
        StreamDataSource streamSource = new StreamDataSource("user_activity_stream");
        pipeline.setDataSource(streamSource);
        
        // Add another processor
        DataTransformer timestampTransformer = record -> {
            Map<String, Object> data = new HashMap<>(record.getData());
            data.put("hour_of_day", new Date((Long) data.get("timestamp")).getHours());
            return new DataRecord(record.getId() + "_timestamped", data);
        };
        pipeline.insertProcessor(1, new TransformProcessor(timestampTransformer));
        
        // Resume pipeline
        pipeline.resume();
        
        // Let it run with new configuration
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        pipeline.displayPipelineStatus();
        
        System.out.println("\n=== 8. Error Handling Demonstration ===");
        
        // Add a processor that will cause errors
        DataTransformer errorTransformer = record -> {
            if (Math.random() < 0.3) { // 30% chance of error
                throw new RuntimeException("Simulated processing error");
            }
            return record;
        };
        pipeline.addProcessor(new TransformProcessor(errorTransformer));
        
        // Let it run and generate some errors
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n=== 9. Pipeline Configuration ===");
        
        // Set configuration parameters
        pipeline.setConfiguration("batch_size", 100);
        pipeline.setConfiguration("timeout_ms", 5000);
        pipeline.setConfiguration("retry_count", 3);
        
        System.out.println("Configuration updated");
        
        System.out.println("\n=== 10. Final Status and Metrics ===");
        
        pipeline.displayPipelineStatus();
        
        PipelineStatus finalStatus = pipeline.getPipelineStatus();
        System.out.println("\n📊 Final Pipeline Metrics:");
        System.out.println("   State: " + finalStatus.getState());
        System.out.println("   Uptime: " + finalStatus.getUptime() + "ms");
        System.out.println("   Processors: " + finalStatus.getProcessorCount());
        System.out.println("   Sinks: " + finalStatus.getSinkCount());
        
        PipelineMetrics metrics = finalStatus.getMetrics();
        System.out.println("   Batches Processed: " + metrics.getBatchesProcessed());
        System.out.println("   Records Processed: " + metrics.getRecordsProcessed());
        System.out.println("   Errors: " + metrics.getErrorCount());
        System.out.printf("   Avg Processing Time: %.2fms\n", metrics.getAverageProcessingTime());
        
        System.out.println("\n=== 11. Stopping Pipeline ===");
        
        PipelineResult stopResult = pipeline.stop();
        System.out.println("Stop Result: " + (stopResult.isSuccess() ? "✅ Success" : "❌ Failed"));
        System.out.println("Message: " + stopResult.getMessage());
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("✅ Analytics Pipeline successfully demonstrated:");
        System.out.println("   • Runtime component swapping (sources, processors, sinks)");
        System.out.println("   • Chain of responsibility pattern for data processing");
        System.out.println("   • Asynchronous batch processing");
        System.out.println("   • Comprehensive error handling and recovery");
        System.out.println("   • Real-time metrics collection");
        System.out.println("   • Pipeline lifecycle management");
        System.out.println("   • Health monitoring and diagnostics");
        System.out.println("   • Event-driven architecture");
        System.out.println("   • Configurable processing parameters");
        
        // Cleanup
        pipeline.shutdown();
    }
    
    private static String categorizeEvent(String eventType) {
        switch (eventType) {
            case "click":
            case "page_view":
                return "engagement";
            case "purchase":
            case "add_to_cart":
                return "commerce";
            case "signup":
            case "login":
            case "logout":
                return "authentication";
            default:
                return "other";
        }
    }
}
