package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Smart Fan Implementation with speed control, oscillation, and timer functions
 */
public class SmartFan extends Device {
    
    public enum FanMode {
        NORMAL, ECO, TURBO, SLEEP, AUTO
    }
    
    private int speed; // 0-10 speed levels
    private boolean isOscillating;
    private FanMode currentMode;
    private int timerMinutes; // Auto-off timer
    private boolean hasRemote;
    private int maxSpeed;
    private double airflowCfm; // Cubic feet per minute
    
    public SmartFan(String deviceId, String deviceName, Map<String, Object> properties) {
        super(deviceId, deviceName, DeviceType.FAN, properties);
        
        this.speed = 0;
        this.isOscillating = false;
        this.currentMode = FanMode.NORMAL;
        this.timerMinutes = 0;
        this.hasRemote = (Boolean) properties.getOrDefault("has_remote", true);
        this.maxSpeed = ((Number) properties.getOrDefault("max_speed", 10)).intValue();
        this.airflowCfm = ((Number) properties.getOrDefault("max_airflow_cfm", 2000.0)).doubleValue();
    }
    
    @Override
    public CompletableFuture<DeviceOperationResult> turnOn() throws DeviceException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState == DeviceState.ON) {
                    return DeviceOperationResult.success("turn_on", "Fan is already running");
                }
                
                // Simulate fan startup
                Thread.sleep(150);
                
                setCurrentState(DeviceState.ON);
                if (speed == 0) {
                    speed = 3; // Default medium speed
                }
                
                // Apply mode-specific settings
                applyModeSettings();
                
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("speed", speed);
                resultData.put("mode", currentMode);
                resultData.put("oscillating", isOscillating);
                resultData.put("airflow_cfm", calculateCurrentAirflow());
                resultData.put("power_consumption", calculatePowerConsumption());
                
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
                    return DeviceOperationResult.success("turn_off", "Fan is already off");
                }
                
                // Simulate fan wind-down
                windDown();
                
                setCurrentState(DeviceState.OFF);
                speed = 0;
                isOscillating = false;
                timerMinutes = 0;
                
                return DeviceOperationResult.success("turn_off", "Fan turned off successfully");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return DeviceOperationResult.failure("turn_off", "Operation interrupted", "INTERRUPTED");
            }
        });
    }
    
    @Override
    public DeviceStatus getStatus() throws DeviceException {
        DeviceStatus status = new DeviceStatus(deviceId, currentState, isConnected);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("speed", speed);
        properties.put("mode", currentMode.toString());
        properties.put("oscillating", isOscillating);
        properties.put("timer_minutes", timerMinutes);
        properties.put("max_speed", maxSpeed);
        properties.put("has_remote", hasRemote);
        properties.put("airflow_cfm", calculateCurrentAirflow());
        properties.put("power_consumption", calculatePowerConsumption());
        properties.put("noise_level_db", calculateNoiseLevel());
        
        status.setProperties(properties);
        status.setFirmwareVersion("SmartFan_v1.8.2");
        
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
                
                // Update speed
                if (settings.containsKey("speed")) {
                    int newSpeed = ((Number) settings.get("speed")).intValue();
                    if (setSpeed(newSpeed)) {
                        settingsChanged = true;
                    }
                }
                
                // Update oscillation
                if (settings.containsKey("oscillating")) {
                    boolean newOscillating = (Boolean) settings.get("oscillating");
                    if (setOscillation(newOscillating)) {
                        settingsChanged = true;
                    }
                }
                
                // Update mode
                if (settings.containsKey("mode")) {
                    String modeStr = (String) settings.get("mode");
                    try {
                        FanMode newMode = FanMode.valueOf(modeStr.toUpperCase());
                        if (setMode(newMode)) {
                            settingsChanged = true;
                        }
                    } catch (IllegalArgumentException e) {
                        return DeviceOperationResult.failure("update_settings", 
                            "Invalid fan mode: " + modeStr, "INVALID_MODE");
                    }
                }
                
                // Update timer
                if (settings.containsKey("timer_minutes")) {
                    int newTimer = ((Number) settings.get("timer_minutes")).intValue();
                    if (setTimer(newTimer)) {
                        settingsChanged = true;
                    }
                }
                
                if (settingsChanged) {
                    // Apply changes if fan is running
                    if (currentState == DeviceState.ON) {
                        Thread.sleep(300); // Simulate adjustment time
                        applyModeSettings();
                    }
                    
                    Map<String, Object> resultData = new HashMap<>();
                    resultData.put("speed", speed);
                    resultData.put("mode", currentMode);
                    resultData.put("oscillating", isOscillating);
                    resultData.put("timer_minutes", timerMinutes);
                    resultData.put("airflow_cfm", calculateCurrentAirflow());
                    
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
    
    private boolean setSpeed(int newSpeed) {
        if (newSpeed < 0 || newSpeed > maxSpeed) {
            return false;
        }
        
        this.speed = newSpeed;
        
        // Turn off if speed is 0
        if (newSpeed == 0 && currentState == DeviceState.ON) {
            setCurrentState(DeviceState.OFF);
        } else if (newSpeed > 0 && currentState == DeviceState.OFF) {
            setCurrentState(DeviceState.ON);
        }
        
        return true;
    }
    
    private boolean setOscillation(boolean oscillating) {
        if (currentState == DeviceState.OFF && oscillating) {
            return false; // Cannot oscillate when off
        }
        
        this.isOscillating = oscillating;
        return true;
    }
    
    private boolean setMode(FanMode newMode) {
        this.currentMode = newMode;
        return true;
    }
    
    private boolean setTimer(int minutes) {
        if (minutes < 0 || minutes > 480) { // Max 8 hours
            return false;
        }
        
        this.timerMinutes = minutes;
        
        // Start timer if fan is on and timer > 0
        if (currentState == DeviceState.ON && minutes > 0) {
            startTimer(minutes);
        }
        
        return true;
    }
    
    private void applyModeSettings() {
        switch (currentMode) {
            case ECO:
                // Reduce speed for energy efficiency
                speed = Math.min(speed, 5);
                break;
            case TURBO:
                // Maximum performance
                speed = maxSpeed;
                break;
            case SLEEP:
                // Quiet operation
                speed = Math.min(speed, 2);
                isOscillating = false;
                break;
            case AUTO:
                // Automatic speed based on time of day
                LocalDateTime now = LocalDateTime.now();
                int hour = now.getHour();
                if (hour >= 22 || hour < 6) {
                    speed = 2; // Night mode
                } else if (hour >= 12 && hour < 18) {
                    speed = 6; // Afternoon
                } else {
                    speed = 4; // Default
                }
                break;
            case NORMAL:
            default:
                // No special adjustments
                break;
        }
    }
    
    private void windDown() throws InterruptedException {
        int originalSpeed = speed;
        
        // Gradually reduce speed over 1 second
        for (int i = originalSpeed; i > 0; i--) {
            speed = i;
            Thread.sleep(100);
        }
        
        speed = 0;
    }
    
    private void startTimer(int minutes) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(minutes * 60 * 1000); // Convert to milliseconds
                
                // Auto turn off
                if (currentState == DeviceState.ON) {
                    setCurrentState(DeviceState.OFF);
                    speed = 0;
                    isOscillating = false;
                    timerMinutes = 0;
                    System.out.println("Fan " + deviceId + " auto-turned off after timer expired");
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    private double calculateCurrentAirflow() {
        if (currentState == DeviceState.OFF) {
            return 0.0;
        }
        
        double speedFactor = (double) speed / maxSpeed;
        double modeMultiplier = 1.0;
        
        switch (currentMode) {
            case ECO:
                modeMultiplier = 0.8;
                break;
            case TURBO:
                modeMultiplier = 1.2;
                break;
            case SLEEP:
                modeMultiplier = 0.6;
                break;
        }
        
        return airflowCfm * speedFactor * modeMultiplier;
    }
    
    private double calculatePowerConsumption() {
        if (currentState == DeviceState.OFF) {
            return 2.0; // Standby power
        }
        
        // Base power consumption based on speed
        double basePower = 15.0 + (speed * 8.0); // 15W base + 8W per speed level
        
        // Mode adjustments
        switch (currentMode) {
            case ECO:
                basePower *= 0.7; // 30% reduction
                break;
            case TURBO:
                basePower *= 1.3; // 30% increase
                break;
            case SLEEP:
                basePower *= 0.5; // 50% reduction
                break;
        }
        
        // Oscillation adds power
        if (isOscillating) {
            basePower += 5.0;
        }
        
        return basePower;
    }
    
    private double calculateNoiseLevel() {
        if (currentState == DeviceState.OFF) {
            return 0.0;
        }
        
        // Base noise level increases with speed
        double noiseLevel = 25.0 + (speed * 3.0); // 25dB base + 3dB per speed level
        
        // Mode adjustments
        switch (currentMode) {
            case SLEEP:
                noiseLevel -= 10.0; // Quieter operation
                break;
            case TURBO:
                noiseLevel += 5.0; // Louder at max performance
                break;
        }
        
        return Math.max(0.0, noiseLevel);
    }
    
    // Smart Fan specific methods
    public void oscillateFor(int seconds) throws DeviceException {
        if (currentState == DeviceState.OFF) {
            throw new DeviceException("Cannot oscillate when fan is off");
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                boolean originalOscillation = isOscillating;
                isOscillating = true;
                
                Thread.sleep(seconds * 1000);
                
                isOscillating = originalOscillation;
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    public void rampUpSpeed(int targetSpeed, int durationSeconds) throws DeviceException {
        if (targetSpeed < 0 || targetSpeed > maxSpeed) {
            throw new DeviceException("Invalid target speed: " + targetSpeed);
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                int startSpeed = speed;
                int steps = durationSeconds * 2; // 500ms per step
                int speedDiff = targetSpeed - startSpeed;
                
                for (int i = 1; i <= steps; i++) {
                    int newSpeed = startSpeed + (speedDiff * i / steps);
                    speed = newSpeed;
                    
                    if (speed > 0 && currentState == DeviceState.OFF) {
                        setCurrentState(DeviceState.ON);
                    }
                    
                    Thread.sleep(500);
                }
                
                speed = targetSpeed;
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    // Getters
    public int getSpeed() { return speed; }
    public boolean isOscillating() { return isOscillating; }
    public FanMode getCurrentMode() { return currentMode; }
    public int getTimerMinutes() { return timerMinutes; }
    public boolean hasRemote() { return hasRemote; }
    public int getMaxSpeed() { return maxSpeed; }
    public double getAirflowCfm() { return airflowCfm; }
}
