package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Smart Thermostat Implementation with climate control, scheduling, and learning algorithms
 */
public class SmartThermostat extends Device {
    
    public enum ThermostatMode {
        OFF, HEAT, COOL, AUTO, ECO, AWAY
    }
    
    public enum FanMode {
        AUTO, ON, CIRCULATE
    }
    
    private double currentTemperature;
    private double targetTemperature;
    private double humidity;
    private ThermostatMode thermostatMode;
    private FanMode fanMode;
    private boolean isHeating;
    private boolean isCooling;
    private boolean learningEnabled;
    private Map<Integer, Double> scheduleTemperatures; // Hour -> Temperature
    
    public SmartThermostat(String deviceId, String deviceName, Map<String, Object> properties) {
        super(deviceId, deviceName, DeviceType.THERMOSTAT, properties);
        
        this.currentTemperature = 22.0; // Default 22°C
        this.targetTemperature = 22.0;
        this.humidity = 45.0; // Default 45%
        this.thermostatMode = ThermostatMode.AUTO;
        this.fanMode = FanMode.AUTO;
        this.isHeating = false;
        this.isCooling = false;
        this.learningEnabled = (Boolean) properties.getOrDefault("learning_enabled", true);
        this.scheduleTemperatures = new HashMap<>();
        
        // Initialize default schedule
        initializeDefaultSchedule();
    }
    
    @Override
    public CompletableFuture<DeviceOperationResult> turnOn() throws DeviceException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState == DeviceState.ON) {
                    return DeviceOperationResult.success("turn_on", "Thermostat is already active");
                }
                
                // Simulate thermostat activation
                Thread.sleep(200);
                
                setCurrentState(DeviceState.ON);
                
                // Start climate control
                startClimateControl();
                
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("current_temperature", currentTemperature);
                resultData.put("target_temperature", targetTemperature);
                resultData.put("mode", thermostatMode);
                resultData.put("humidity", humidity);
                resultData.put("heating", isHeating);
                resultData.put("cooling", isCooling);
                
                return DeviceOperationResult.success("turn_on", resultData);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return DeviceOperationResult.failure("turn_on", "Operation interrupted", "INTERRUPTED");
            }
        });
    }
    
    @Override
    public CompletableFuture<DeviceOperationResult> turnOff() throws DeviceException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState == DeviceState.OFF) {
                    return DeviceOperationResult.success("turn_off", "Thermostat is already off");
                }
                
                // Stop heating/cooling
                stopClimateControl();
                
                setCurrentState(DeviceState.OFF);
                thermostatMode = ThermostatMode.OFF;
                
                return DeviceOperationResult.success("turn_off", "Thermostat turned off successfully");
                
            } catch (Exception e) {
                return DeviceOperationResult.failure("turn_off", "Failed to turn off: " + e.getMessage(), "OPERATION_ERROR");
            }
        });
    }
    
    @Override
    public DeviceStatus getStatus() throws DeviceException {
        DeviceStatus status = new DeviceStatus(deviceId, currentState, isConnected);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("current_temperature", currentTemperature);
        properties.put("target_temperature", targetTemperature);
        properties.put("humidity", humidity);
        properties.put("thermostat_mode", thermostatMode.toString());
        properties.put("fan_mode", fanMode.toString());
        properties.put("heating", isHeating);
        properties.put("cooling", isCooling);
        properties.put("learning_enabled", learningEnabled);
        properties.put("power_consumption", calculatePowerConsumption());
        properties.put("efficiency_rating", calculateEfficiency());
        
        status.setProperties(properties);
        status.setFirmwareVersion("SmartThermostat_v3.2.1");
        
        if (supportsEnergyMonitoring && energyMonitor != null) {
            status.setEnergyUsage(energyMonitor.getCurrentUsage());
        }
        
        return status;
    }
    
    @Override
    protected CompletableFuture<DeviceOperationResult> updateDeviceSettings(Map<String, Object> settings) 
            throws DeviceException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean settingsChanged = false;
                
                // Update target temperature
                if (settings.containsKey("target_temperature")) {
                    double newTarget = ((Number) settings.get("target_temperature")).doubleValue();
                    if (setTargetTemperature(newTarget)) {
                        settingsChanged = true;
                    }
                }
                
                // Update thermostat mode
                if (settings.containsKey("thermostat_mode")) {
                    String modeStr = (String) settings.get("thermostat_mode");
                    try {
                        ThermostatMode newMode = ThermostatMode.valueOf(modeStr.toUpperCase());
                        if (setThermostatMode(newMode)) {
                            settingsChanged = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return DeviceOperationResult.failure("update_settings", 
                            "Invalid thermostat mode: " + modeStr, "INVALID_MODE");
                    }
                }
                
                // Update fan mode
                if (settings.containsKey("fan_mode")) {
                    String fanModeStr = (String) settings.get("fan_mode");
                    try {
                        FanMode newFanMode = FanMode.valueOf(fanModeStr.toUpperCase());
                        if (setFanMode(newFanMode)) {
                            settingsChanged = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return DeviceOperationResult.failure("update_settings", 
                            "Invalid fan mode: " + fanModeStr, "INVALID_FAN_MODE");
                    }
                }
                
                // Update learning setting
                if (settings.containsKey("learning_enabled")) {
                    learningEnabled = (Boolean) settings.get("learning_enabled");
                    settingsChanged = true;
                }
                
                if (settingsChanged) {
                    // Apply changes if thermostat is active
                    if (currentState == DeviceState.ON) {
                        Thread.sleep(500); // Simulate adjustment time
                        adjustClimateControl();
                    }
                    
                    Map<String, Object> resultData = new HashMap<>();
                    resultData.put("target_temperature", targetTemperature);
                    resultData.put("thermostat_mode", thermostatMode);
                    resultData.put("fan_mode", fanMode);
                    resultData.put("learning_enabled", learningEnabled);
                    
                    return DeviceOperationResult.success("update_settings", resultData);
                } else {
                    return DeviceOperationResult.success("update_settings", "No valid settings to update");
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return DeviceOperationResult.failure("update_settings", "Settings update interrupted", "INTERRUPTED");
            } catch (Exception e) {
                return DeviceOperationResult.failure("update_settings", 
                    "Failed to update settings: " + e.getMessage(), "SETTINGS_ERROR");
            }
        });
    }
    
    private boolean setTargetTemperature(double temperature) {
        if (temperature < 10.0 || temperature > 35.0) { // Reasonable range
            return false;
        }
        
        this.targetTemperature = temperature;
        
        // Adjust climate control if active
        if (currentState == DeviceState.ON) {
            adjustClimateControl();
        }
        
        return true;
    }
    
    private boolean setThermostatMode(ThermostatMode mode) {
        this.thermostatMode = mode;
        
        if (mode == ThermostatMode.OFF) {
            stopClimateControl();
            setCurrentState(DeviceState.OFF);
        } else if (currentState == DeviceState.OFF) {
            setCurrentState(DeviceState.ON);
        }
        
        return true;
    }
    
    private boolean setFanMode(FanMode mode) {
        this.fanMode = mode;
        return true;
    }
    
    private void startClimateControl() {
        // Start background thread for climate control
        CompletableFuture.runAsync(() -> {
            while (currentState == DeviceState.ON && thermostatMode != ThermostatMode.OFF) {
                try {
                    // Simulate temperature reading
                    updateCurrentTemperature();
                    
                    // Adjust heating/cooling based on target
                    adjustClimateControl();
                    
                    // Apply learning if enabled
                    if (learningEnabled) {
                        applyLearning();
                    }
                    
                    // Apply schedule
                    applySchedule();
                    
                    Thread.sleep(30000); // Check every 30 seconds
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private void stopClimateControl() {
        isHeating = false;
        isCooling = false;
    }
    
    private void adjustClimateControl() {
        double temperatureDiff = targetTemperature - currentTemperature;
        double threshold = 0.5; // 0.5°C threshold
        
        switch (thermostatMode) {
            case HEAT:
                isHeating = temperatureDiff > threshold;
                isCooling = false;
                break;
                
            case COOL:
                isCooling = temperatureDiff < -threshold;
                isHeating = false;
                break;
                
            case AUTO:
                if (temperatureDiff > threshold) {
                    isHeating = true;
                    isCooling = false;
                } else if (temperatureDiff < -threshold) {
                    isCooling = true;
                    isHeating = false;
                } else {
                    isHeating = false;
                    isCooling = false;
                }
                break;
                
            case ECO:
                // Wider threshold for energy efficiency
                double ecoThreshold = 1.0;
                if (temperatureDiff > ecoThreshold) {
                    isHeating = true;
                    isCooling = false;
                } else if (temperatureDiff < -ecoThreshold) {
                    isCooling = true;
                    isHeating = false;
                } else {
                    isHeating = false;
                    isCooling = false;
                }
                break;
                
            case AWAY:
                // Maintain wider temperature range when away
                if (currentTemperature < 18.0) {
                    isHeating = true;
                    isCooling = false;
                } else if (currentTemperature > 28.0) {
                    isCooling = true;
                    isHeating = false;
                } else {
                    isHeating = false;
                    isCooling = false;
                }
                break;
                
            case OFF:
            default:
                isHeating = false;
                isCooling = false;
                break;
        }
    }
    
    private void updateCurrentTemperature() {
        // Simulate temperature changes based on heating/cooling
        if (isHeating) {
            currentTemperature += 0.1; // Gradual heating
        } else if (isCooling) {
            currentTemperature -= 0.1; // Gradual cooling
        } else {
            // Natural temperature drift
            double drift = (Math.random() - 0.5) * 0.05;
            currentTemperature += drift;
        }
        
        // Update humidity (inverse relationship with temperature changes)
        if (isHeating) {
            humidity = Math.max(30.0, humidity - 0.5);
        } else if (isCooling) {
            humidity = Math.min(70.0, humidity + 0.3);
        }
    }
    
    private void applyLearning() {
        // Simple learning algorithm: adjust target based on usage patterns
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        
        // Learn from user adjustments (simplified)
        if (scheduleTemperatures.containsKey(hour)) {
            double scheduledTemp = scheduleTemperatures.get(hour);
            double currentTarget = targetTemperature;
            
            // Gradually adjust schedule based on current preferences
            double learningRate = 0.1;
            double adjustedTemp = scheduledTemp + (currentTarget - scheduledTemp) * learningRate;
            scheduleTemperatures.put(hour, adjustedTemp);
        }
    }
    
    private void applySchedule() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        
        if (scheduleTemperatures.containsKey(hour)) {
            double scheduledTemp = scheduleTemperatures.get(hour);
            
            // Only apply if not manually overridden recently
            if (Math.abs(targetTemperature - scheduledTemp) > 2.0) {
                // Large difference suggests manual override, don't apply schedule
                return;
            }
            
            targetTemperature = scheduledTemp;
        }
    }
    
    private void initializeDefaultSchedule() {
        // Default 24-hour schedule
        scheduleTemperatures.put(6, 21.0);   // 6 AM - Wake up
        scheduleTemperatures.put(8, 20.0);   // 8 AM - Leave for work
        scheduleTemperatures.put(17, 22.0);  // 5 PM - Return home
        scheduleTemperatures.put(22, 20.0);  // 10 PM - Sleep
        scheduleTemperatures.put(23, 19.0);  // 11 PM - Deep sleep
    }
    
    private double calculatePowerConsumption() {
        double basePower = 5.0; // Base consumption for display and sensors
        
        if (currentState == DeviceState.OFF) {
            return basePower;
        }
        
        // Add power for heating/cooling
        if (isHeating) {
            basePower += 2500.0; // Heating element
        }
        if (isCooling) {
            basePower += 1800.0; // Cooling compressor
        }
        
        // Fan power based on mode
        switch (fanMode) {
            case ON:
                basePower += 150.0;
                break;
            case CIRCULATE:
                basePower += 75.0;
                break;
            case AUTO:
                if (isHeating || isCooling) {
                    basePower += 100.0;
                }
                break;
        }
        
        return basePower;
    }
    
    private double calculateEfficiency() {
        if (currentState == DeviceState.OFF) {
            return 0.0;
        }
        
        // Calculate efficiency based on how close we are to target
        double temperatureDiff = Math.abs(targetTemperature - currentTemperature);
        double efficiency = Math.max(0.0, 100.0 - (temperatureDiff * 20.0));
        
        // Adjust for mode
        switch (thermostatMode) {
            case ECO:
                efficiency *= 1.2; // Eco mode is more efficient
                break;
            case AWAY:
                efficiency *= 1.1; // Away mode saves energy
                break;
        }
        
        return Math.min(100.0, efficiency);
    }
    
    // Smart Thermostat specific methods
    public void setScheduleTemperature(int hour, double temperature) throws DeviceException {
        if (hour < 0 || hour > 23) {
            throw new DeviceException("Invalid hour: " + hour);
        }
        if (temperature < 10.0 || temperature > 35.0) {
            throw new DeviceException("Invalid temperature: " + temperature);
        }
        
        scheduleTemperatures.put(hour, temperature);
    }
    
    public void enableAwayMode(int hours) throws DeviceException {
        if (hours <= 0) {
            throw new DeviceException("Away mode duration must be positive");
        }
        
        ThermostatMode previousMode = thermostatMode;
        setThermostatMode(ThermostatMode.AWAY);
        
        // Auto-return to previous mode after specified hours
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(hours * 60 * 60 * 1000); // Convert to milliseconds
                
                if (thermostatMode == ThermostatMode.AWAY) {
                    setThermostatMode(previousMode);
                    System.out.println("Thermostat " + deviceId + " returned from away mode");
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    public void performMaintenanceCheck() {
        CompletableFuture.runAsync(() -> {
            try {
                setCurrentState(DeviceState.MAINTENANCE);
                
                // Simulate maintenance operations
                Thread.sleep(5000);
                
                // Check filters, sensors, etc.
                System.out.println("Thermostat " + deviceId + " maintenance check completed");
                
                setCurrentState(DeviceState.ON);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    // Getters
    public double getCurrentTemperature() { return currentTemperature; }
    public double getTargetTemperature() { return targetTemperature; }
    public double getHumidity() { return humidity; }
    public ThermostatMode getThermostatMode() { return thermostatMode; }
    public FanMode getFanMode() { return fanMode; }
    public boolean isHeating() { return isHeating; }
    public boolean isCooling() { return isCooling; }
    public boolean isLearningEnabled() { return learningEnabled; }
    public Map<Integer, Double> getScheduleTemperatures() { return new HashMap<>(scheduleTemperatures); }
}
