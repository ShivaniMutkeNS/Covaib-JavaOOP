
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo class to demonstrate IoT Sensor Data Stream
 */
public class IoTSensorDemo {
    public static void main(String[] args) {
        System.out.println("=== IoT Sensor Data Stream Demo ===\n");
        
        // Test basic sensor operations
        testBasicSensorOperations();
        
        // Test data processing
        testDataProcessing();
        
        // Test sensor statistics
        testSensorStatistics();
        
        // Test sensor status
        testSensorStatus();
        
        // Test data validation
        testDataValidation();
    }
    
    private static void testBasicSensorOperations() {
        System.out.println("=== Basic Sensor Operations Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_001", "Temperature", "Room A");
        System.out.println("Sensor created: " + sensor);
        
        // Add some readings
        boolean added1 = sensor.addRawReading(25.5, LocalDateTime.now());
        System.out.println("Added reading 25.5°C: " + (added1 ? "SUCCESS" : "FAILED"));
        
        boolean added2 = sensor.addRawReading(26.0, LocalDateTime.now());
        System.out.println("Added reading 26.0°C: " + (added2 ? "SUCCESS" : "FAILED"));
        
        boolean added3 = sensor.addRawReading(24.8, LocalDateTime.now());
        System.out.println("Added reading 24.8°C: " + (added3 ? "SUCCESS" : "FAILED"));
        
        // Get processed readings
        List<IoTSensor.ProcessedReading> readings = sensor.getProcessedReadings();
        System.out.println("Processed readings: " + readings.size());
        
        for (IoTSensor.ProcessedReading reading : readings) {
            System.out.println("  " + reading);
        }
        
        // Get latest reading
        IoTSensor.ProcessedReading latest = sensor.getLatestProcessedReading();
        if (latest != null) {
            System.out.println("Latest reading: " + latest);
        }
        
        System.out.println();
    }
    
    private static void testDataProcessing() {
        System.out.println("=== Data Processing Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_002", "Humidity", "Room B");
        
        // Add readings with different values
        double[] values = {30.0, 35.0, 40.0, 45.0, 50.0, 55.0, 60.0, 65.0, 70.0, 75.0};
        
        for (double value : values) {
            boolean added = sensor.addRawReading(value, LocalDateTime.now());
            System.out.println("Added reading " + value + "%: " + (added ? "SUCCESS" : "FAILED"));
        }
        
        // Display processed readings
        System.out.println("\nProcessed readings:");
        for (IoTSensor.ProcessedReading reading : sensor.getProcessedReadings()) {
            System.out.println("  Raw: " + reading.getRawValue() + 
                             ", Processed: " + reading.getProcessedValue() + 
                             ", Method: " + reading.getProcessingMethod() + 
                             ", Valid: " + reading.isValid());
        }
        
        System.out.println();
    }
    
    private static void testSensorStatistics() {
        System.out.println("=== Sensor Statistics Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_003", "Pressure", "Room C");
        
        // Add readings
        double[] values = {1013.25, 1014.50, 1015.75, 1016.00, 1017.25, 1018.50, 1019.75, 1020.00};
        
        for (double value : values) {
            sensor.addRawReading(value, LocalDateTime.now());
        }
        
        // Get statistics
        IoTSensor.SensorStatistics stats = sensor.getSensorStatistics();
        System.out.println("Sensor statistics: " + stats);
        
        // Get average over different time periods
        Double avg5min = sensor.getAverageValue(5);
        System.out.println("Average over 5 minutes: " + (avg5min != null ? avg5min : "No data"));
        
        Double avg10min = sensor.getAverageValue(10);
        System.out.println("Average over 10 minutes: " + (avg10min != null ? avg10min : "No data"));
        
        // Get min/max values
        Double minValue = sensor.getMinValue(5);
        System.out.println("Minimum over 5 minutes: " + (minValue != null ? minValue : "No data"));
        
        Double maxValue = sensor.getMaxValue(5);
        System.out.println("Maximum over 5 minutes: " + (maxValue != null ? maxValue : "No data"));
        
        System.out.println();
    }
    
    private static void testSensorStatus() {
        System.out.println("=== Sensor Status Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_004", "Light", "Room D");
        
        // Test no data status
        IoTSensor.SensorStatus status = sensor.getSensorStatus();
        System.out.println("Status with no data: " + status);
        
        // Add valid reading
        sensor.addRawReading(500.0, LocalDateTime.now());
        status = sensor.getSensorStatus();
        System.out.println("Status with valid data: " + status);
        
        // Add invalid reading
        sensor.addRawReading(150.0, LocalDateTime.now()); // Assuming this is invalid
        status = sensor.getSensorStatus();
        System.out.println("Status with invalid data: " + status);
        
        // Test stale data (simulate old reading)
        sensor.addRawReading(600.0, LocalDateTime.now().minusHours(2));
        status = sensor.getSensorStatus();
        System.out.println("Status with stale data: " + status);
        
        System.out.println();
    }
    
    private static void testDataValidation() {
        System.out.println("=== Data Validation Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_005", "Temperature", "Room E");
        
        // Test valid readings
        boolean valid1 = sensor.addRawReading(20.0, LocalDateTime.now());
        System.out.println("Valid reading 20.0°C: " + (valid1 ? "SUCCESS" : "FAILED"));
        
        boolean valid2 = sensor.addRawReading(30.0, LocalDateTime.now());
        System.out.println("Valid reading 30.0°C: " + (valid2 ? "SUCCESS" : "FAILED"));
        
        // Test invalid readings
        boolean invalid1 = sensor.addRawReading(150.0, LocalDateTime.now()); // Too high
        System.out.println("Invalid reading 150.0°C: " + (invalid1 ? "SUCCESS" : "FAILED"));
        
        boolean invalid2 = sensor.addRawReading(-150.0, LocalDateTime.now()); // Too low
        System.out.println("Invalid reading -150.0°C: " + (invalid2 ? "SUCCESS" : "FAILED"));
        
        boolean invalid3 = sensor.addRawReading(25.0, LocalDateTime.now().minusHours(3)); // Too old
        System.out.println("Old reading 25.0°C: " + (invalid3 ? "SUCCESS" : "FAILED"));
        
        boolean invalid4 = sensor.addRawReading(25.0, null); // Null timestamp
        System.out.println("Reading with null timestamp: " + (invalid4 ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    /**
     * Test time range filtering
     */
    private static void testTimeRangeFiltering() {
        System.out.println("=== Time Range Filtering Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_006", "Temperature", "Room F");
        
        // Add readings at different times
        LocalDateTime now = LocalDateTime.now();
        sensor.addRawReading(20.0, now.minusMinutes(30));
        sensor.addRawReading(21.0, now.minusMinutes(20));
        sensor.addRawReading(22.0, now.minusMinutes(10));
        sensor.addRawReading(23.0, now);
        
        // Get readings in different time ranges
        LocalDateTime startTime = now.minusMinutes(25);
        LocalDateTime endTime = now.minusMinutes(5);
        
        List<IoTSensor.ProcessedReading> rangeReadings = sensor.getProcessedReadingsInRange(startTime, endTime);
        System.out.println("Readings in range " + startTime + " to " + endTime + ": " + rangeReadings.size());
        
        for (IoTSensor.ProcessedReading reading : rangeReadings) {
            System.out.println("  " + reading);
        }
        
        System.out.println();
    }
    
    /**
     * Test sensor configuration
     */
    private static void testSensorConfiguration() {
        System.out.println("=== Sensor Configuration Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_007", "Temperature", "Room G");
        IoTSensor.SensorConfig config = sensor.getSensorConfig();
        
        System.out.println("Sensor configuration:");
        System.out.println("  Min valid value: " + config.getMinValidValue());
        System.out.println("  Max valid value: " + config.getMaxValidValue());
        System.out.println("  Max readings: " + config.getMaxReadings());
        System.out.println("  Max data age (minutes): " + config.getMaxDataAgeMinutes());
        System.out.println("  Enable normalization: " + config.isEnableNormalization());
        System.out.println("  Enable filtering: " + config.isEnableFiltering());
        
        System.out.println();
    }
    
    /**
     * Test sensor with multiple readings
     */
    private static void testMultipleReadings() {
        System.out.println("=== Multiple Readings Test ===");
        
        IoTSensor sensor = new IoTSensor("SENSOR_008", "Temperature", "Room H");
        
        // Add many readings
        for (int i = 0; i < 50; i++) {
            double value = 20.0 + (i * 0.5);
            sensor.addRawReading(value, LocalDateTime.now());
        }
        
        System.out.println("Added 50 readings");
        System.out.println("Total processed readings: " + sensor.getProcessedReadings().size());
        
        // Get statistics
        IoTSensor.SensorStatistics stats = sensor.getSensorStatistics();
        System.out.println("Statistics: " + stats);
        
        // Get average over different periods
        Double avg5min = sensor.getAverageValue(5);
        System.out.println("Average over 5 minutes: " + (avg5min != null ? avg5min : "No data"));
        
        Double avg10min = sensor.getAverageValue(10);
        System.out.println("Average over 10 minutes: " + (avg10min != null ? avg10min : "No data"));
        
        System.out.println();
    }
}
