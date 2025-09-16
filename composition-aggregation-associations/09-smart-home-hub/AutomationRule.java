package composition.smarthome;

/**
 * Automation Rule Interface for Smart Home Hub
 */
public interface AutomationRule {
    String getName();
    boolean isEnabled();
    void setEnabled(boolean enabled);
    boolean matches(String deviceId, String trigger, Object data);
    void execute(SmartHomeHub hub);
    String getDescription();
}
