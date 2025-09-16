package composition.smarthome;

/**
 * Device Capabilities data class
 */
public class DeviceCapabilities {
    private final String[] supportedCommands;
    private final String[] availableProperties;
    
    public DeviceCapabilities(String[] supportedCommands, String[] availableProperties) {
        this.supportedCommands = supportedCommands.clone();
        this.availableProperties = availableProperties.clone();
    }
    
    public String[] getSupportedCommands() { return supportedCommands.clone(); }
    public String[] getAvailableProperties() { return availableProperties.clone(); }
    
    public boolean supportsCommand(String command) {
        for (String cmd : supportedCommands) {
            if (cmd.equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }
}
