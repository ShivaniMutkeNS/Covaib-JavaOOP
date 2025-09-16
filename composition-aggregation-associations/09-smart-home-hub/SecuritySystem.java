package composition.smarthome;

/**
 * Security System Device Implementation
 */
public class SecuritySystem implements SmartDevice {
    private final String deviceId;
    private final String name;
    private SmartHomeHub hub;
    private boolean isOnline;
    private boolean isArmed;
    private String armingMode; // home, away, disarmed
    private boolean motionDetected;
    private boolean doorOpen;
    private boolean alarmTriggered;
    
    public SecuritySystem(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOnline = false;
        this.isArmed = false;
        this.armingMode = "disarmed";
        this.motionDetected = false;
        this.doorOpen = false;
        this.alarmTriggered = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("🛡️ Initializing security system: " + name);
        isOnline = true;
    }
    
    @Override
    public void disconnect() {
        System.out.println("🛡️ Disconnecting security system: " + name);
        isOnline = false;
    }
    
    @Override
    public DeviceControlResult executeCommand(String command, Object... parameters) {
        if (!isOnline) {
            return new DeviceControlResult(false, "Device offline", null);
        }
        
        switch (command.toLowerCase()) {
            case "arm_home":
                armingMode = "home";
                isArmed = true;
                System.out.println("🛡️ " + name + " armed in HOME mode");
                return new DeviceControlResult(true, "System armed (home)", armingMode);
                
            case "arm_away":
                armingMode = "away";
                isArmed = true;
                System.out.println("🛡️ " + name + " armed in AWAY mode");
                return new DeviceControlResult(true, "System armed (away)", armingMode);
                
            case "disarm":
                armingMode = "disarmed";
                isArmed = false;
                alarmTriggered = false;
                System.out.println("🛡️ " + name + " disarmed");
                return new DeviceControlResult(true, "System disarmed", armingMode);
                
            case "trigger_alarm":
                if (isArmed) {
                    alarmTriggered = true;
                    System.out.println("🚨 ALARM TRIGGERED on " + name + "!");
                    return new DeviceControlResult(true, "Alarm triggered", true);
                }
                return new DeviceControlResult(false, "System not armed", false);
                
            case "simulate_motion":
                motionDetected = true;
                System.out.println("🚶 Motion detected by " + name);
                if (isArmed && armingMode.equals("away")) {
                    return executeCommand("trigger_alarm");
                }
                return new DeviceControlResult(true, "Motion detected", true);
                
            case "simulate_door":
                doorOpen = !doorOpen;
                System.out.println("🚪 Door " + (doorOpen ? "opened" : "closed") + " detected by " + name);
                if (isArmed && doorOpen) {
                    return executeCommand("trigger_alarm");
                }
                return new DeviceControlResult(true, "Door status changed", doorOpen);
                
            case "get_status":
                String status = String.format("Armed: %s, Mode: %s, Alarm: %s", 
                                             isArmed, armingMode, alarmTriggered ? "TRIGGERED" : "Normal");
                return new DeviceControlResult(true, "Status retrieved", status);
                
            default:
                return new DeviceControlResult(false, "Unknown command: " + command, null);
        }
    }
    
    @Override
    public String getDeviceId() { return deviceId; }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getType() { return "Security System"; }
    
    @Override
    public String getStatus() {
        if (!isOnline) return "Offline";
        
        String status = armingMode.toUpperCase();
        if (alarmTriggered) status += " - ALARM!";
        else if (motionDetected) status += " - Motion";
        else if (doorOpen) status += " - Door Open";
        
        return status;
    }
    
    @Override
    public boolean isOnline() { return isOnline; }
    
    @Override
    public void setHub(SmartHomeHub hub) { this.hub = hub; }
    
    @Override
    public DeviceCapabilities getCapabilities() {
        return new DeviceCapabilities(
            new String[]{"arm_home", "arm_away", "disarm", "trigger_alarm", "simulate_motion", "simulate_door", "get_status"},
            new String[]{"armed_status", "arming_mode", "motion_detected", "door_status", "alarm_status"}
        );
    }
}
