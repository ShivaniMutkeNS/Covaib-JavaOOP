package composition.smarthome;

/**
 * Temperature-based Automation Rule
 */
public class TemperatureBasedRule implements AutomationRule {
    private final String name;
    private boolean enabled;
    private final String thermostatDeviceId;
    private final double triggerTemperature;
    private final String action;
    
    public TemperatureBasedRule(String name, String thermostatDeviceId, double triggerTemperature, String action) {
        this.name = name;
        this.thermostatDeviceId = thermostatDeviceId;
        this.triggerTemperature = triggerTemperature;
        this.action = action;
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
        if (!deviceId.equals(thermostatDeviceId) || !trigger.equals("set_temperature")) {
            return false;
        }
        
        if (data instanceof Double) {
            double temperature = (Double) data;
            return (action.equals("heat") && temperature < triggerTemperature) ||
                   (action.equals("cool") && temperature > triggerTemperature);
        }
        
        return false;
    }
    
    @Override
    public void execute(SmartHomeHub hub) {
        hub.controlDevice(thermostatDeviceId, "set_mode", action);
        System.out.println("🤖 Automation: Temperature-based rule triggered - setting mode to " + action);
    }
    
    @Override
    public String getDescription() {
        return String.format("Set thermostat to %s mode when temperature %s %.1f°C", 
                           action, action.equals("heat") ? "below" : "above", triggerTemperature);
    }
}
