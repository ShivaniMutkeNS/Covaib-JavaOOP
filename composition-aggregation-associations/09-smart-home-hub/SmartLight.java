package composition.smarthome;

/**
 * Smart Light Device Implementation
 */
public class SmartLight implements SmartDevice {
    private final String deviceId;
    private final String name;
    private SmartHomeHub hub;
    private boolean isOnline;
    private boolean isOn;
    private int brightness; // 0-100
    private String color; // RGB hex code
    private String mode; // normal, party, reading, etc.
    
    public SmartLight(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOnline = false;
        this.isOn = false;
        this.brightness = 100;
        this.color = "#FFFFFF"; // White
        this.mode = "normal";
    }
    
    @Override
    public void initialize() {
        System.out.println("💡 Initializing smart light: " + name);
        isOnline = true;
    }
    
    @Override
    public void disconnect() {
        System.out.println("💡 Disconnecting smart light: " + name);
        isOnline = false;
    }
    
    @Override
    public DeviceControlResult executeCommand(String command, Object... parameters) {
        if (!isOnline) {
            return new DeviceControlResult(false, "Device offline", null);
        }
        
        switch (command.toLowerCase()) {
            case "turn_on":
                isOn = true;
                System.out.println("💡 " + name + " turned ON");
                return new DeviceControlResult(true, "Light turned on", isOn);
                
            case "turn_off":
                isOn = false;
                System.out.println("💡 " + name + " turned OFF");
                return new DeviceControlResult(true, "Light turned off", isOn);
                
            case "set_brightness":
                if (parameters.length > 0 && parameters[0] instanceof Integer) {
                    brightness = Math.max(0, Math.min(100, (Integer) parameters[0]));
                    System.out.println("💡 " + name + " brightness set to " + brightness + "%");
                    return new DeviceControlResult(true, "Brightness set", brightness);
                }
                return new DeviceControlResult(false, "Invalid brightness value", null);
                
            case "set_color":
                if (parameters.length > 0 && parameters[0] instanceof String) {
                    color = (String) parameters[0];
                    System.out.println("💡 " + name + " color set to " + color);
                    return new DeviceControlResult(true, "Color set", color);
                }
                return new DeviceControlResult(false, "Invalid color value", null);
                
            case "set_mode":
                if (parameters.length > 0 && parameters[0] instanceof String) {
                    mode = (String) parameters[0];
                    System.out.println("💡 " + name + " mode set to " + mode);
                    return new DeviceControlResult(true, "Mode set", mode);
                }
                return new DeviceControlResult(false, "Invalid mode", null);
                
            default:
                return new DeviceControlResult(false, "Unknown command: " + command, null);
        }
    }
    
    @Override
    public String getDeviceId() { return deviceId; }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getType() { return "Smart Light"; }
    
    @Override
    public String getStatus() {
        if (!isOnline) return "Offline";
        return isOn ? "On (" + brightness + "%, " + color + ")" : "Off";
    }
    
    @Override
    public boolean isOnline() { return isOnline; }
    
    @Override
    public void setHub(SmartHomeHub hub) { this.hub = hub; }
    
    @Override
    public DeviceCapabilities getCapabilities() {
        return new DeviceCapabilities(
            new String[]{"turn_on", "turn_off", "set_brightness", "set_color", "set_mode"},
            new String[]{"brightness", "color", "mode", "power_state"}
        );
    }
}
