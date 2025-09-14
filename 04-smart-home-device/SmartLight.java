package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Smart Light Implementation with dimming, color control, and circadian rhythm
 */
public class SmartLight extends Device {
    
    private int brightness; // 0-100
    private String color; // Hex color code
    private boolean isDimmable;
    private boolean supportsColor;
    private boolean circadianRhythm;
    private int maxWattage;
    
    public SmartLight(String deviceId, String deviceName, Map<String, Object> properties) {
        super(deviceId, deviceName, DeviceType.LIGHT, properties);
        
        this.brightness = 0;
        this.color = "#FFFFFF"; // Default white
        this.isDimmable = (Boolean) properties.getOrDefault("dimmable", true);
        this.supportsColor = (Boolean) properties.getOrDefault("supports_color", false);
        this.circadianRhythm = (Boolean) properties.getOrDefault("circadian_rhythm", false);
        this.maxWattage = ((Number) properties.getOrDefault("max_wattage", 60)).intValue();
    }
    
    @Override
    public CompletableFuture<DeviceOperationResult> turnOn() throws DeviceException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState == DeviceState.ON) {
                    return DeviceOperationResult.success("turn_on", "Light is already on");
                }
                
                // Simulate light turning on
                Thread.sleep(100);
                
                setCurrentState(DeviceState.ON);
                if (brightness == 0) {
                    brightness = 80; // Default brightness
                }
                
                // Apply circadian rhythm if enabled
                if (circadianRhythm) {
                    applyCircadianSettings();
                }
                
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("brightness", brightness);
                resultData.put("color", color);
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
                    return DeviceOperationResult.success("turn_off", "Light is already off");
                }
                
                // Simulate light turning off with fade effect
                if (isDimmable) {
                    fadeOut();
                } else {
                    Thread.sleep(50);
                }
                
                setCurrentState(DeviceState.OFF);
                brightness = 0;
                
                return DeviceOperationResult.success("turn_off", "Light turned off successfully");
                
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
        properties.put("brightness", brightness);
        properties.put("color", color);
        properties.put("is_dimmable", isDimmable);
        properties.put("supports_color", supportsColor);
        properties.put("circadian_rhythm", circadianRhythm);
        properties.put("max_wattage", maxWattage);
        properties.put("power_consumption", calculatePowerConsumption());
        
        status.setProperties(properties);
        status.setFirmwareVersion("SmartLight_v2.1.3");
        
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
                
                // Update brightness
                if (settings.containsKey("brightness")) {
                    int newBrightness = ((Number) settings.get("brightness")).intValue();
                    if (setBrightness(newBrightness)) {
                        settingsChanged = true;
                    }
                }
                
                // Update color
                if (settings.containsKey("color") && supportsColor) {
                    String newColor = (String) settings.get("color");
                    if (setColor(newColor)) {
                        settingsChanged = true;
                    }
                }
                
                // Update circadian rhythm setting
                if (settings.containsKey("circadian_rhythm")) {
                    circadianRhythm = (Boolean) settings.get("circadian_rhythm");
                    settingsChanged = true;
                }
                
                if (settingsChanged) {
                    // Apply changes if light is on
                    if (currentState == DeviceState.ON) {
                        Thread.sleep(200); // Simulate adjustment time
                    }
                    
                    Map<String, Object> resultData = new HashMap<>();
                    resultData.put("brightness", brightness);
                    resultData.put("color", color);
                    resultData.put("circadian_rhythm", circadianRhythm);
                    
                    return DeviceOperationResult.success("update_settings", resultData);
                } else {
                    return DeviceOperationResult.success("update_settings", "No valid settings to update");
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return DeviceOperationResult.failure("update_settings", "Settings update interrupted", "INTERRUPTED");
            } catch (Exception e) {
                return DeviceOperationResult.failure("update_settings", "Failed to update settings: " + e.getMessage(), "SETTINGS_ERROR");
            }
        });
    }
    
    private boolean setBrightness(int newBrightness) {
        if (!isDimmable) {
            return false;
        }
        
        if (newBrightness < 0 || newBrightness > 100) {
            return false;
        }
        
        this.brightness = newBrightness;
        
        // Turn off if brightness is 0
        if (newBrightness == 0 && currentState == DeviceState.ON) {
            setCurrentState(DeviceState.OFF);
        } else if (newBrightness > 0 && currentState == DeviceState.OFF) {
            setCurrentState(DeviceState.ON);
        }
        
        return true;
    }
    
    private boolean setColor(String newColor) {
        if (!supportsColor) {
            return false;
        }
        
        // Validate hex color format
        if (newColor == null || !newColor.matches("^#[0-9A-Fa-f]{6}$")) {
            return false;
        }
        
        this.color = newColor.toUpperCase();
        return true;
    }
    
    private void applyCircadianSettings() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        
        // Adjust color temperature based on time of day
        if (supportsColor) {
            if (hour >= 6 && hour < 12) {
                // Morning: Cool white
                color = "#F0F8FF";
            } else if (hour >= 12 && hour < 18) {
                // Afternoon: Neutral white
                color = "#FFFFFF";
            } else {
                // Evening/Night: Warm white
                color = "#FFE4B5";
            }
        }
        
        // Adjust brightness based on time
        if (isDimmable) {
            if (hour >= 22 || hour < 6) {
                // Night time: Lower brightness
                brightness = Math.min(brightness, 30);
            } else if (hour >= 6 && hour < 8) {
                // Early morning: Gradual increase
                brightness = Math.min(brightness, 60);
            }
        }
    }
    
    private void fadeOut() throws InterruptedException {
        int originalBrightness = brightness;
        
        // Fade out over 500ms
        for (int i = originalBrightness; i >= 0; i -= 10) {
            brightness = i;
            Thread.sleep(50);
        }
        
        brightness = 0;
    }
    
    private double calculatePowerConsumption() {
        if (currentState == DeviceState.OFF) {
            return 1.0; // Standby power
        }
        
        // Power consumption based on brightness and max wattage
        double powerFactor = brightness / 100.0;
        return 1.0 + (maxWattage * powerFactor); // Standby + operational power
    }
    
    // Smart Light specific methods
    public void fadeIn(int targetBrightness, int durationMs) throws DeviceException {
        if (!isDimmable) {
            throw new DeviceException("Light does not support dimming");
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                int steps = durationMs / 50; // 50ms per step
                int brightnessStep = (targetBrightness - brightness) / steps;
                
                for (int i = 0; i < steps; i++) {
                    brightness = Math.max(0, Math.min(100, brightness + brightnessStep));
                    Thread.sleep(50);
                }
                
                brightness = targetBrightness;
                if (brightness > 0 && currentState == DeviceState.OFF) {
                    setCurrentState(DeviceState.ON);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    public void strobe(int count, int intervalMs) throws DeviceException {
        if (currentState == DeviceState.OFF) {
            throw new DeviceException("Cannot strobe when light is off");
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                int originalBrightness = brightness;
                
                for (int i = 0; i < count; i++) {
                    brightness = 0;
                    Thread.sleep(intervalMs / 2);
                    brightness = originalBrightness;
                    Thread.sleep(intervalMs / 2);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    // Getters
    public int getBrightness() { return brightness; }
    public String getColor() { return color; }
    public boolean isDimmable() { return isDimmable; }
    public boolean supportsColor() { return supportsColor; }
    public boolean hasCircadianRhythm() { return circadianRhythm; }
    public int getMaxWattage() { return maxWattage; }
}
