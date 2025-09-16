package composition.smarthome;

import java.util.List;

/**
 * Scene Execution Result data class
 */
public class SceneExecutionResult {
    private final boolean success;
    private final String message;
    private final List<DeviceControlResult> actionResults;
    
    public SceneExecutionResult(boolean success, String message, List<DeviceControlResult> actionResults) {
        this.success = success;
        this.message = message;
        this.actionResults = actionResults;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<DeviceControlResult> getActionResults() { return actionResults; }
}
