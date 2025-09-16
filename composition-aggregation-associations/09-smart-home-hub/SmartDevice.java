package composition.smarthome;

/**
 * Smart Device Interface for IoT devices
 */
public interface SmartDevice {
    void initialize();
    void disconnect();
    DeviceControlResult executeCommand(String command, Object... parameters);
    String getDeviceId();
    String getName();
    String getType();
    String getStatus();
    boolean isOnline();
    void setHub(SmartHomeHub hub);
    DeviceCapabilities getCapabilities();
}
