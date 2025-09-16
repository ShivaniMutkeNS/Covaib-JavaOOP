package composition.smarthome;

/**
 * Motion-based Lighting Automation Rule
 */
public class MotionBasedLightingRule implements AutomationRule {
    private final String name;
    private boolean enabled;
    private final String securityDeviceId;
    private final String lightDeviceId;
    
    public MotionBasedLightingRule(String name, String securityDeviceId, String lightDeviceId) {
        this.name = name;
        this.securityDeviceId = securityDeviceId;
        this.lightDeviceId = lightDeviceId;
        this.enabled = true;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public boolean isEnabled() { return enabled; }
    
    @Override
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    @Override
    public boolean matches(String deviceId, String trigger, Object data) {
        return deviceId.equals(securityDeviceId) && trigger.equals("simulate_motion");
    }
    
    @Override
    public void execute(SmartHomeHub hub) {
        hub.controlDevice(lightDeviceId, "turn_on");
        System.out.println("🤖 Automation: Motion detected, turning on lights");
    }
    
    @Override
    public String getDescription() {
        return "Turn on lights when motion is detected";
    }
}
