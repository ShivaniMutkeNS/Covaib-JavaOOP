package composition.smarthome;

import java.util.*;

/**
 * Security Manager for Smart Home Hub
 */
public class SecurityManager {
    private final Map<String, Set<String>> devicePermissions;
    private final Set<String> restrictedCommands;
    
    public SecurityManager() {
        this.devicePermissions = new HashMap<>();
        this.restrictedCommands = new HashSet<>();
        
        // Add some restricted commands that require special authorization
        restrictedCommands.add("trigger_alarm");
        restrictedCommands.add("disarm");
    }
    
    public boolean isCommandAuthorized(String deviceId, String command) {
        // For demo purposes, allow most commands
        // In real implementation, this would check user permissions, device access levels, etc.
        
        if (restrictedCommands.contains(command.toLowerCase())) {
            // Check if device has special permissions
            Set<String> permissions = devicePermissions.get(deviceId);
            return permissions != null && permissions.contains(command.toLowerCase());
        }
        
        return true; // Allow all other commands
    }
    
    public void grantPermission(String deviceId, String command) {
        devicePermissions.computeIfAbsent(deviceId, k -> new HashSet<>()).add(command.toLowerCase());
    }
    
    public void revokePermission(String deviceId, String command) {
        Set<String> permissions = devicePermissions.get(deviceId);
        if (permissions != null) {
            permissions.remove(command.toLowerCase());
        }
    }
}
