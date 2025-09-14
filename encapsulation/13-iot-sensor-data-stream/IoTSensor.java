
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * IoT Sensor Data Stream
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating raw sensor values
 * 2. Exposing processed/normalized values through getters
 * 3. Preventing external tampering of readings
 * 4. Implementing data validation and processing
 */
public class IoTSensor {
    // Encapsulated sensor data
    private final String sensorId;
    private final String sensorType;
    private final String location;
    private final SensorConfig config;
    
    // Raw sensor readings (private)
    private final List<RawReading> rawReadings;
    private final List<ProcessedReading> processedReadings;
    
    // Data processing
    private final DataProcessor dataProcessor;
    private final DataValidator dataValidator;
    
    // Thread safety
    private final Object lock = new Object();
    
    /**
     * Constructor
     */
    public IoTSensor(String sensorId, String sensorType, String location) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.location = location;
        this.config = new SensorConfig();
        this.rawReadings = new CopyOnWriteArrayList<>();
        this.processedReadings = new CopyOnWriteArrayList<>();
        this.dataProcessor = new DataProcessor();
        this.dataValidator = new DataValidator();
    }
    
    /**
     * Add raw sensor reading
     * @param value Raw sensor value
     * @param timestamp Timestamp of reading
     * @return true if reading was added successfully
     */
    public boolean addRawReading(double value, LocalDateTime timestamp) {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        
        RawReading reading = new RawReading(value, timestamp);
        
        // Validate reading
        if (!dataValidator.isValidReading(reading, config)) {
            return false;
        }
        
        // Add raw reading
        synchronized (lock) {
            rawReadings.add(reading);
            
            // Process reading
            ProcessedReading processed = dataProcessor.processReading(reading, config);
            if (processed != null) {
                processedReadings.add(processed);
            }
            
            // Maintain reading history limit
            maintainReadingHistory();
        }
        
        return true;
    }
    
    /**
     * Get processed readings (read-only)
     * @return Unmodifiable list of processed readings
     */
    public List<ProcessedReading> getProcessedReadings() {
        return Collections.unmodifiableList(processedReadings);
    }
    
    /**
     * Get latest processed reading
     * @return Latest processed reading or null if none
     */
    public ProcessedReading getLatestProcessedReading() {
        return processedReadings.isEmpty() ? null : processedReadings.get(processedReadings.size() - 1);
    }
    
    /**
     * Get processed readings within time range
     * @param startTime Start time
     * @param endTime End time
     * @return List of processed readings in time range
     */
    public List<ProcessedReading> getProcessedReadingsInRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return Collections.emptyList();
        }
        
        return processedReadings.stream()
                .filter(reading -> !reading.getTimestamp().isBefore(startTime) && 
                                 !reading.getTimestamp().isAfter(endTime))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get average value over time period
     * @param minutes Number of minutes to average over
     * @return Average value or null if no data
     */
    public Double getAverageValue(int minutes) {
        if (minutes <= 0 || processedReadings.isEmpty()) {
            return null;
        }
        
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(minutes);
        return processedReadings.stream()
                .filter(reading -> !reading.getTimestamp().isBefore(cutoff))
                .mapToDouble(ProcessedReading::getProcessedValue)
                .average()
                .orElse(Double.NaN);
    }
    
    /**
     * Get maximum value over time period
     * @param minutes Number of minutes to check
     * @return Maximum value or null if no data
     */
    public Double getMaxValue(int minutes) {
        if (minutes <= 0 || processedReadings.isEmpty()) {
            return null;
        }
        
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(minutes);
        return processedReadings.stream()
                .filter(reading -> !reading.getTimestamp().isBefore(cutoff))
                .mapToDouble(ProcessedReading::getProcessedValue)
                .max()
                .orElse(Double.NaN);
    }
    
    /**
     * Get minimum value over time period
     * @param minutes Number of minutes to check
     * @return Minimum value or null if no data
     */
    public Double getMinValue(int minutes) {
        if (minutes <= 0 || processedReadings.isEmpty()) {
            return null;
        }
        
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(minutes);
        return processedReadings.stream()
                .filter(reading -> !reading.getTimestamp().isBefore(cutoff))
                .mapToDouble(ProcessedReading::getProcessedValue)
                .min()
                .orElse(Double.NaN);
    }
    
    /**
     * Get sensor status
     * @return Sensor status
     */
    public SensorStatus getSensorStatus() {
        if (processedReadings.isEmpty()) {
            return SensorStatus.NO_DATA;
        }
        
        ProcessedReading latest = getLatestProcessedReading();
        if (latest == null) {
            return SensorStatus.NO_DATA;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (latest.getTimestamp().isBefore(now.minusMinutes(config.getMaxDataAgeMinutes()))) {
            return SensorStatus.STALE_DATA;
        }
        
        if (latest.getProcessedValue() < config.getMinValidValue() || 
            latest.getProcessedValue() > config.getMaxValidValue()) {
            return SensorStatus.INVALID_DATA;
        }
        
        return SensorStatus.ACTIVE;
    }
    
    /**
     * Get sensor statistics
     * @return Sensor statistics
     */
    public SensorStatistics getSensorStatistics() {
        if (processedReadings.isEmpty()) {
            return new SensorStatistics(0, 0.0, 0.0, 0.0, 0.0);
        }
        
        double[] values = processedReadings.stream()
                .mapToDouble(ProcessedReading::getProcessedValue)
                .toArray();
        
        double sum = Arrays.stream(values).sum();
        double average = sum / values.length;
        double min = Arrays.stream(values).min().orElse(0.0);
        double max = Arrays.stream(values).max().orElse(0.0);
        
        return new SensorStatistics(values.length, average, min, max, sum);
    }
    
    /**
     * Get sensor configuration (read-only)
     * @return Sensor configuration
     */
    public SensorConfig getSensorConfig() {
        return config;
    }
    
    /**
     * Get sensor information
     * @return Sensor information
     */
    public String getSensorInfo() {
        return String.format("Sensor{id='%s', type='%s', location='%s', status=%s, readings=%d}", 
            sensorId, sensorType, location, getSensorStatus(), processedReadings.size());
    }
    
    /**
     * Maintain reading history within limits
     */
    private void maintainReadingHistory() {
        int maxReadings = config.getMaxReadings();
        if (rawReadings.size() > maxReadings) {
            int toRemove = rawReadings.size() - maxReadings;
            for (int i = 0; i < toRemove; i++) {
                rawReadings.remove(0);
            }
        }
        
        if (processedReadings.size() > maxReadings) {
            int toRemove = processedReadings.size() - maxReadings;
            for (int i = 0; i < toRemove; i++) {
                processedReadings.remove(0);
            }
        }
    }
    
    // Getters
    public String getSensorId() { return sensorId; }
    public String getSensorType() { return sensorType; }
    public String getLocation() { return location; }
    
    /**
     * Raw sensor reading (immutable)
     */
    public static class RawReading {
        private final double value;
        private final LocalDateTime timestamp;
        
        public RawReading(double value, LocalDateTime timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
        
        public double getValue() { return value; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("RawReading{value=%.2f, timestamp=%s}", value, timestamp);
        }
    }
    
    /**
     * Processed sensor reading (immutable)
     */
    public static class ProcessedReading {
        private final double rawValue;
        private final double processedValue;
        private final LocalDateTime timestamp;
        private final String processingMethod;
        private final boolean isValid;
        
        public ProcessedReading(double rawValue, double processedValue, LocalDateTime timestamp, 
                              String processingMethod, boolean isValid) {
            this.rawValue = rawValue;
            this.processedValue = processedValue;
            this.timestamp = timestamp;
            this.processingMethod = processingMethod;
            this.isValid = isValid;
        }
        
        public double getRawValue() { return rawValue; }
        public double getProcessedValue() { return processedValue; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getProcessingMethod() { return processingMethod; }
        public boolean isValid() { return isValid; }
        
        @Override
        public String toString() {
            return String.format("ProcessedReading{raw=%.2f, processed=%.2f, timestamp=%s, method='%s', valid=%s}", 
                rawValue, processedValue, timestamp, processingMethod, isValid);
        }
    }
    
    /**
     * Sensor status
     */
    public enum SensorStatus {
        ACTIVE, NO_DATA, STALE_DATA, INVALID_DATA
    }
    
    /**
     * Sensor statistics
     */
    public static class SensorStatistics {
        private final int readingCount;
        private final double average;
        private final double min;
        private final double max;
        private final double sum;
        
        public SensorStatistics(int readingCount, double average, double min, double max, double sum) {
            this.readingCount = readingCount;
            this.average = average;
            this.min = min;
            this.max = max;
            this.sum = sum;
        }
        
        public int getReadingCount() { return readingCount; }
        public double getAverage() { return average; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public double getSum() { return sum; }
        
        @Override
        public String toString() {
            return String.format("SensorStatistics{count=%d, avg=%.2f, min=%.2f, max=%.2f, sum=%.2f}", 
                readingCount, average, min, max, sum);
        }
    }
    
    /**
     * Sensor configuration
     */
    public static class SensorConfig {
        private final double minValidValue;
        private final double maxValidValue;
        private final int maxReadings;
        private final int maxDataAgeMinutes;
        private final boolean enableNormalization;
        private final boolean enableFiltering;
        
        public SensorConfig() {
            this.minValidValue = -100.0;
            this.maxValidValue = 100.0;
            this.maxReadings = 1000;
            this.maxDataAgeMinutes = 60;
            this.enableNormalization = true;
            this.enableFiltering = true;
        }
        
        public double getMinValidValue() { return minValidValue; }
        public double getMaxValidValue() { return maxValidValue; }
        public int getMaxReadings() { return maxReadings; }
        public int getMaxDataAgeMinutes() { return maxDataAgeMinutes; }
        public boolean isEnableNormalization() { return enableNormalization; }
        public boolean isEnableFiltering() { return enableFiltering; }
    }
    
    /**
     * Data processor for sensor readings
     */
    private static class DataProcessor {
        public ProcessedReading processReading(RawReading rawReading, SensorConfig config) {
            double processedValue = rawReading.getValue();
            String processingMethod = "none";
            boolean isValid = true;
            
            // Apply normalization if enabled
            if (config.isEnableNormalization()) {
                processedValue = normalizeValue(processedValue, config);
                processingMethod = "normalized";
            }
            
            // Apply filtering if enabled
            if (config.isEnableFiltering()) {
                processedValue = filterValue(processedValue);
                processingMethod = "filtered";
            }
            
            // Validate processed value
            if (processedValue < config.getMinValidValue() || 
                processedValue > config.getMaxValidValue()) {
                isValid = false;
            }
            
            return new ProcessedReading(
                rawReading.getValue(),
                processedValue,
                rawReading.getTimestamp(),
                processingMethod,
                isValid
            );
        }
        
        private double normalizeValue(double value, SensorConfig config) {
            // Simple normalization to 0-1 range
            double range = config.getMaxValidValue() - config.getMinValidValue();
            return (value - config.getMinValidValue()) / range;
        }
        
        private double filterValue(double value) {
            // Simple low-pass filter
            return value * 0.9; // Dampen high-frequency noise
        }
    }
    
    /**
     * Data validator for sensor readings
     */
    private static class DataValidator {
        public boolean isValidReading(RawReading reading, SensorConfig config) {
            if (reading == null || reading.getTimestamp() == null) {
                return false;
            }
            
            // Check if reading is within valid range
            double value = reading.getValue();
            if (value < config.getMinValidValue() || value > config.getMaxValidValue()) {
                return false;
            }
            
            // Check if reading is not too old
            LocalDateTime now = LocalDateTime.now();
            if (reading.getTimestamp().isBefore(now.minusMinutes(config.getMaxDataAgeMinutes()))) {
                return false;
            }
            
            return true;
        }
    }
    
    @Override
    public String toString() {
        return getSensorInfo();
    }
}
