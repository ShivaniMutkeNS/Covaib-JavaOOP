package composition.smarthome;

/**
 * Voice Command data class
 */
public class VoiceCommand {
    private final String target;
    private final String action;
    private final Object[] parameters;
    private final boolean isGroupCommand;
    
    public VoiceCommand(String target, String action, Object[] parameters, boolean isGroupCommand) {
        this.target = target;
        this.action = action;
        this.parameters = parameters;
        this.isGroupCommand = isGroupCommand;
    }
    
    public String getTarget() { return target; }
    public String getAction() { return action; }
    public Object[] getParameters() { return parameters; }
    public boolean isGroupCommand() { return isGroupCommand; }
}
