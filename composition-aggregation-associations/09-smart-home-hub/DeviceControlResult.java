package composition.smarthome;

/**
 * Device Control Result data class
 */
public class DeviceControlResult {
    private final boolean success;
    private final String message;
    private final Object response;
    
    public DeviceControlResult(boolean success, String message, Object response) {
        this.success = success;
        this.message = message;
        this.response = response;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Object getResponse() { return response; }
}
