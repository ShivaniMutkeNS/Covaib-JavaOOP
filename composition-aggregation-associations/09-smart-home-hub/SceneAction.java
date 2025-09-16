package composition.smarthome;

/**
 * Scene Action data class for scene management
 */
public class SceneAction {
    private final String deviceId;
    private final String command;
    private final Object[] parameters;
    private final long delayMs;
    
    public SceneAction(String deviceId, String command, Object[] parameters, long delayMs) {
        this.deviceId = deviceId;
        this.command = command;
        this.parameters = parameters;
        this.delayMs = delayMs;
    }
    
    public SceneAction(String deviceId, String command, Object[] parameters) {
        this(deviceId, command, parameters, 0);
    }
    
    public String getDeviceId() { return deviceId; }
    public String getCommand() { return command; }
    public Object[] getParameters() { return parameters; }
    public long getDelayMs() { return delayMs; }
}
