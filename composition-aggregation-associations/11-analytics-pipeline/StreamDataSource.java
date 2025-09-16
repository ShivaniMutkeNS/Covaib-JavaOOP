package composition.analytics;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Stream Data Source implementation (simulates Kafka/Kinesis)
 */
public class StreamDataSource implements DataSource {
    private final String streamName;
    private boolean isRunning;
    private final AtomicLong recordsRead;
    private final Random random;
    
    public StreamDataSource(String streamName) {
        this.streamName = streamName;
        this.isRunning = false;
        this.recordsRead = new AtomicLong(0);
        this.random = new Random();
    }
    
    @Override
    public void initialize(Map<String, Object> configuration) {
        System.out.println("🌊 Initializing stream data source: " + streamName);
        isRunning = true;
    }
    
    @Override
    public DataBatch fetchData() {
        if (!isRunning) {
            return null;
        }
        
        // Simulate real-time streaming data
        List<DataRecord> records = new ArrayList<>();
        int batchSize = 1 + random.nextInt(5); // 1-5 records per batch (streaming)
        
        for (int i = 0; i < batchSize; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("device_id", "device_" + (100 + random.nextInt(900)));
            data.put("metric_type", getRandomMetricType());
            data.put("timestamp", System.currentTimeMillis());
            data.put("value", random.nextGaussian() * 10 + 50); // Normal distribution around 50
            data.put("location", getRandomLocation());
            data.put("status", random.nextBoolean() ? "active" : "inactive");
            
            records.add(new DataRecord("stream_" + recordsRead.incrementAndGet(), data));
        }
        
        return new DataBatch(records);
    }
    
    private String getRandomMetricType() {
        String[] metricTypes = {"temperature", "humidity", "pressure", "cpu_usage", "memory_usage", "network_io"};
        return metricTypes[random.nextInt(metricTypes.length)];
    }
    
    private String getRandomLocation() {
        String[] locations = {"datacenter_1", "datacenter_2", "edge_node_1", "edge_node_2", "mobile_device"};
        return locations[random.nextInt(locations.length)];
    }
    
    @Override
    public void stop() {
        System.out.println("🌊 Stopping stream data source: " + streamName);
        isRunning = false;
    }
    
    @Override
    public boolean isHealthy() {
        return isRunning;
    }
    
    @Override
    public String getSourceName() {
        return "Stream Source (" + streamName + ")";
    }
    
    @Override
    public DataSourceMetrics getMetrics() {
        return new DataSourceMetrics(recordsRead.get(), 0, isRunning);
    }
}
