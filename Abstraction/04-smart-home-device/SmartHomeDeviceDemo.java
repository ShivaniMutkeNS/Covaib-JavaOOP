package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class showcasing polymorphic usage of different smart home devices
 * Demonstrates how client code remains unchanged regardless of device type
 */
public class SmartHomeDeviceDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Smart Home Device Abstraction Demo ===\n");
        
        // Create different smart home devices
        Device[] devices = createSmartDevices();
        
        // Test each device polymorphically
        for (Device device : devices) {
            System.out.println("Testing device: " + device.getClass().getSimpleName());
            System.out.println("Device ID: " + device.getDeviceId());
            System.out.println("Device Name: " + device.getDeviceName());
            System.out.println("Device Type: " + device.getDeviceType());
            System.out.println("Supports Scheduling: " + device.supportsScheduling());
            System.out.println("Supports Energy Monitoring: " + device.supportsEnergyMonitoring());
            
            try {
                // Test complete device workflow using template method
                testDeviceWorkflow(device);
                
                // Test device-specific features
                testDeviceSpecificFeatures(device);
                
                // Display energy monitoring if supported
                if (device.supportsEnergyMonitoring()) {
                    displayEnergyUsage(device);
                }
                
                // Disconnect device
                device.disconnect();
                
            } catch (Exception e) {
                System.err.println("Error testing device: " + e.getMessage());
            }
            
            System.out.println("-".repeat(70));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static Device[] createSmartDevices() {
        // Smart Light configuration
        Map<String, Object> lightConfig = new HashMap<>();
        lightConfig.put("dimmable", true);
        lightConfig.put("supports_color", true);
        lightConfig.put("circadian_rhythm", true);
        lightConfig.put("max_wattage", 75);
        lightConfig.put("supports_scheduling", true);
        lightConfig.put("supports_energy_monitoring", true);
        
        // Smart Fan configuration
        Map<String, Object> fanConfig = new HashMap<>();
        fanConfig.put("has_remote", true);
        fanConfig.put("max_speed", 8);
        fanConfig.put("max_airflow_cfm", 1800.0);
        fanConfig.put("supports_scheduling", true);
        fanConfig.put("supports_energy_monitoring", true);
        
        // Smart Thermostat configuration
        Map<String, Object> thermostatConfig = new HashMap<>();
        thermostatConfig.put("learning_enabled", true);
        thermostatConfig.put("supports_scheduling", true);
        thermostatConfig.put("supports_energy_monitoring", true);
        
        return new Device[] {
            new SmartLight("light_001", "Living Room Light", lightConfig),
            new SmartFan("fan_001", "Bedroom Ceiling Fan", fanConfig),
            new SmartThermostat("thermostat_001", "Main Thermostat", thermostatConfig)
        };
    }
    
    private static void testDeviceWorkflow(Device device) {
        try {
            System.out.println("\n1. Testing basic device operations...");
            
            // Test turn on
            DeviceOperation turnOnOp = new DeviceOperation("turn_on_001", 
                DeviceOperation.OperationType.TURN_ON, null, "admin");
            DeviceOperationResult turnOnResult = device.performOperation(turnOnOp);
            
            if (turnOnResult.isSuccess()) {
                System.out.println("   ✓ Device turned on successfully");
                if (turnOnResult.getData() != null) {
                    System.out.println("   Data: " + turnOnResult.getData());
                }
            } else {
                System.out.println("   ✗ Turn on failed: " + turnOnResult.getMessage());
                return;
            }
            
            // Test get status
            DeviceOperation statusOp = new DeviceOperation("status_001", 
                DeviceOperation.OperationType.GET_STATUS, null, "admin");
            DeviceOperationResult statusResult = device.performOperation(statusOp);
            
            if (statusResult.isSuccess()) {
                System.out.println("   ✓ Status retrieved successfully");
                DeviceStatus status = (DeviceStatus) statusResult.getData();
                System.out.println("   State: " + status.getState());
                System.out.println("   Online: " + status.isOnline());
                System.out.println("   Firmware: " + status.getFirmwareVersion());
            }
            
            // Test settings update
            System.out.println("\n2. Testing settings update...");
            Map<String, Object> newSettings = createDeviceSpecificSettings(device);
            DeviceOperation updateOp = new DeviceOperation("update_001", 
                DeviceOperation.OperationType.UPDATE_SETTINGS, newSettings, "admin");
            DeviceOperationResult updateResult = device.performOperation(updateOp);
            
            if (updateResult.isSuccess()) {
                System.out.println("   ✓ Settings updated successfully");
                System.out.println("   Result: " + updateResult.getData());
            } else {
                System.out.println("   ✗ Settings update failed: " + updateResult.getMessage());
            }
            
            // Test scheduling if supported
            if (device.supportsScheduling()) {
                testScheduling(device);
            }
            
            // Test turn off
            System.out.println("\n3. Testing turn off...");
            DeviceOperation turnOffOp = new DeviceOperation("turn_off_001", 
                DeviceOperation.OperationType.TURN_OFF, null, "admin");
            DeviceOperationResult turnOffResult = device.performOperation(turnOffOp);
            
            if (turnOffResult.isSuccess()) {
                System.out.println("   ✓ Device turned off successfully");
            } else {
                System.out.println("   ✗ Turn off failed: " + turnOffResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Workflow test failed: " + e.getMessage());
        }
    }
    
    private static Map<String, Object> createDeviceSpecificSettings(Device device) {
        Map<String, Object> settings = new HashMap<>();
        
        if (device instanceof SmartLight) {
            settings.put("brightness", 60);
            settings.put("color", "#FF6B35");
            settings.put("circadian_rhythm", true);
        } else if (device instanceof SmartFan) {
            settings.put("speed", 5);
            settings.put("oscillating", true);
            settings.put("mode", "ECO");
            settings.put("timer_minutes", 120);
        } else if (device instanceof SmartThermostat) {
            settings.put("target_temperature", 24.0);
            settings.put("thermostat_mode", "AUTO");
            settings.put("fan_mode", "AUTO");
            settings.put("learning_enabled", true);
        }
        
        return settings;
    }
    
    private static void testScheduling(Device device) {
        try {
            System.out.println("\n   Testing scheduling...");
            
            // Create a schedule for 1 minute from now
            LocalDateTime scheduleTime = LocalDateTime.now().plusMinutes(1);
            ScheduleRequest scheduleRequest = new ScheduleRequest(
                "schedule_001", 
                ScheduleRequest.ScheduleType.ONE_TIME, 
                scheduleTime, 
                DeviceOperation.OperationType.TURN_ON
            );
            
            ScheduleResult scheduleResult = device.schedule(scheduleRequest);
            
            if (scheduleResult.isSuccess()) {
                System.out.println("   ✓ Schedule created successfully");
                System.out.println("   Next execution: " + scheduleResult.getNextExecution());
            } else {
                System.out.println("   ✗ Scheduling failed: " + scheduleResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Scheduling test failed: " + e.getMessage());
        }
    }
    
    private static void testDeviceSpecificFeatures(Device device) {
        System.out.println("\n4. Testing device-specific features...");
        
        try {
            if (device instanceof SmartLight) {
                testSmartLightFeatures((SmartLight) device);
            } else if (device instanceof SmartFan) {
                testSmartFanFeatures((SmartFan) device);
            } else if (device instanceof SmartThermostat) {
                testSmartThermostatFeatures((SmartThermostat) device);
            }
        } catch (Exception e) {
            System.err.println("   Device-specific test failed: " + e.getMessage());
        }
    }
    
    private static void testSmartLightFeatures(SmartLight light) throws DeviceException {
        System.out.println("   Testing Smart Light specific features:");
        
        // Turn on first
        light.turnOn().get();
        
        // Test fade in
        System.out.println("   - Testing fade in to 80% brightness...");
        light.fadeIn(80, 1000);
        Thread.sleep(1200);
        System.out.println("     Current brightness: " + light.getBrightness() + "%");
        
        // Test color change (if supported)
        if (light.supportsColor()) {
            System.out.println("   - Current color: " + light.getColor());
        }
        
        // Test strobe effect
        System.out.println("   - Testing strobe effect (3 times)...");
        light.strobe(3, 200);
        Thread.sleep(1000);
        
        System.out.println("   ✓ Smart Light features tested");
    }
    
    private static void testSmartFanFeatures(SmartFan fan) throws DeviceException {
        System.out.println("   Testing Smart Fan specific features:");
        
        // Turn on first
        fan.turnOn().get();
        
        // Test speed ramping
        System.out.println("   - Testing speed ramp up to level 6...");
        fan.rampUpSpeed(6, 2);
        Thread.sleep(2500);
        System.out.println("     Current speed: " + fan.getSpeed());
        System.out.println("     Current mode: " + fan.getCurrentMode());
        
        // Test oscillation
        System.out.println("   - Testing oscillation for 3 seconds...");
        fan.oscillateFor(3);
        Thread.sleep(3500);
        
        System.out.println("   ✓ Smart Fan features tested");
    }
    
    private static void testSmartThermostatFeatures(SmartThermostat thermostat) throws DeviceException {
        System.out.println("   Testing Smart Thermostat specific features:");
        
        // Turn on first
        thermostat.turnOn().get();
        
        System.out.println("   - Current temperature: " + thermostat.getCurrentTemperature() + "°C");
        System.out.println("   - Target temperature: " + thermostat.getTargetTemperature() + "°C");
        System.out.println("   - Humidity: " + thermostat.getHumidity() + "%");
        System.out.println("   - Mode: " + thermostat.getThermostatMode());
        System.out.println("   - Heating: " + thermostat.isHeating());
        System.out.println("   - Cooling: " + thermostat.isCooling());
        
        // Test schedule setting
        System.out.println("   - Setting schedule temperature for current hour...");
        thermostat.setScheduleTemperature(LocalDateTime.now().getHour(), 23.0);
        
        // Test away mode
        System.out.println("   - Testing away mode (will auto-return in 1 minute for demo)...");
        thermostat.enableAwayMode(1); // 1 hour, but demo purposes
        
        System.out.println("   ✓ Smart Thermostat features tested");
    }
    
    private static void displayEnergyUsage(Device device) {
        System.out.println("\n5. Energy Usage Information:");
        
        EnergyMonitor monitor = device.getEnergyMonitor();
        if (monitor != null) {
            EnergyUsage usage = monitor.getCurrentUsage();
            System.out.println("   Current Power: " + String.format("%.1f", usage.getCurrentPowerWatts()) + " W");
            System.out.println("   Total Energy: " + String.format("%.3f", usage.getTotalEnergyKwh()) + " kWh");
            System.out.println("   Daily Energy: " + String.format("%.3f", usage.getDailyEnergyKwh()) + " kWh");
            System.out.println("   Last Measurement: " + usage.getLastMeasurement());
            
            // Show recent readings
            var recentReadings = monitor.getRecentReadings(3);
            if (!recentReadings.isEmpty()) {
                System.out.println("   Recent Readings:");
                for (var reading : recentReadings) {
                    System.out.println("     " + reading.getTimestamp() + 
                                     ": " + String.format("%.1f", reading.getPowerWatts()) + 
                                     "W (" + reading.getOperation() + ")");
                }
            }
            
            // Show average consumption
            double avgPower = monitor.getAveragePowerConsumption(1);
            System.out.println("   Average Power (1h): " + String.format("%.1f", avgPower) + " W");
        }
    }
}
