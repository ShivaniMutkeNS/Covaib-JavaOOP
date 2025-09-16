package composition.smarthome;

/**
 * Smart Thermostat Device Implementation
 */
public class SmartThermostat implements SmartDevice {
    private final String deviceId;
    private final String name;
    private SmartHomeHub hub;
    private boolean isOnline;
    private double currentTemperature;
    private double targetTemperature;
    private String mode; // heat, cool, auto, off
    private boolean isHeating;
    private boolean isCooling;
    
    public SmartThermostat(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOnline = false;
        this.currentTemperature = 22.0; // Celsius
        this.targetTemperature = 22.0;
        this.mode = "auto";
        this.isHeating = false;
        this.isCooling = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("🌡️ Initializing smart thermostat: " + name);
        isOnline = true;
        // Simulate reading current temperature
        currentTemperature = 20.0 + (Math.random() * 10); // 20-30°C
    }
    
    @Override
    public void disconnect() {
        System.out.println("🌡️ Disconnecting smart thermostat: " + name);
        isOnline = false;
    }
    
    @Override
    public DeviceControlResult executeCommand(String command, Object... parameters) {
        if (!isOnline) {
            return new DeviceControlResult(false, "Device offline", null);
        }
        
        switch (command.toLowerCase()) {
            case "set_temperature":
                if (parameters.length > 0 && parameters[0] instanceof Number) {
                    targetTemperature = ((Number) parameters[0]).doubleValue();
                    adjustHVAC();
                    System.out.printf("🌡️ %s target temperature set to %.1f°C\n", name, targetTemperature);
                    return new DeviceControlResult(true, "Temperature set", targetTemperature);
                }
                return new DeviceControlResult(false, "Invalid temperature value", null);
                
            case "set_mode":
                if (parameters.length > 0 && parameters[0] instanceof String) {
                    String newMode = (String) parameters[0];
                    if (isValidMode(newMode)) {
                        mode = newMode;
                        adjustHVAC();
                        System.out.println("🌡️ " + name + " mode set to " + mode);
                        return new DeviceControlResult(true, "Mode set", mode);
                    }
                }
                return new DeviceControlResult(false, "Invalid mode", null);
                
            case "get_temperature":
                return new DeviceControlResult(true, "Current temperature", currentTemperature);
                
            case "get_status":
                String status = String.format("Current: %.1f°C, Target: %.1f°C, Mode: %s", 
                                             currentTemperature, targetTemperature, mode);
                return new DeviceControlResult(true, "Status retrieved", status);
                
            default:
                return new DeviceControlResult(false, "Unknown command: " + command, null);
        }
    }
    
    private boolean isValidMode(String mode) {
        return mode.equals("heat") || mode.equals("cool") || mode.equals("auto") || mode.equals("off");
    }
    
    private void adjustHVAC() {
        if (mode.equals("off")) {
            isHeating = false;
            isCooling = false;
            return;
        }
        
        double tempDiff = targetTemperature - currentTemperature;
        
        if (mode.equals("heat") || (mode.equals("auto") && tempDiff > 1.0)) {
            isHeating = true;
            isCooling = false;
            System.out.println("🔥 Heating activated");
        } else if (mode.equals("cool") || (mode.equals("auto") && tempDiff < -1.0)) {
            isHeating = false;
            isCooling = true;
            System.out.println("❄️ Cooling activated");
        } else {
            isHeating = false;
            isCooling = false;
        }
    }
    
    @Override
    public String getDeviceId() { return deviceId; }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getType() { return "Smart Thermostat"; }
    
    @Override
    public String getStatus() {
        if (!isOnline) return "Offline";
        
        String hvacStatus = "";
        if (isHeating) hvacStatus = " (Heating)";
        else if (isCooling) hvacStatus = " (Cooling)";
        
        return String.format("%.1f°C → %.1f°C %s%s", currentTemperature, targetTemperature, mode, hvacStatus);
    }
    
    @Override
    public boolean isOnline() { return isOnline; }
    
    @Override
    public void setHub(SmartHomeHub hub) { this.hub = hub; }
    
    @Override
    public DeviceCapabilities getCapabilities() {
        return new DeviceCapabilities(
            new String[]{"set_temperature", "set_mode", "get_temperature", "get_status"},
            new String[]{"current_temperature", "target_temperature", "mode", "heating_status", "cooling_status"}
        );
    }
}
