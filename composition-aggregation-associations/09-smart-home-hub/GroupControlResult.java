package composition.smarthome;

import java.util.List;

/**
 * Group Control Result data class
 */
public class GroupControlResult {
    private final boolean success;
    private final String message;
    private final List<DeviceControlResult> deviceResults;
    
    public GroupControlResult(boolean success, String message, List<DeviceControlResult> deviceResults) {
        this.success = success;
        this.message = message;
        this.deviceResults = deviceResults;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<DeviceControlResult> getDeviceResults() { return deviceResults; }
}
