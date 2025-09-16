package composition.smarthome;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level Smart Home Hub System using Composition
 * Demonstrates: Strategy Pattern, Command Pattern, Observer Pattern, IoT Device Management
 */
public class SmartHomeHub {
    private final Map<String, SmartDevice> devices;
    private final Map<String, DeviceGroup> groups;
    private final List<AutomationRule> automationRules;
    private final List<HubEventListener> listeners;
    private final SecurityManager securityManager;
    private final ExecutorService automationExecutor;
    private boolean isOnline;
    private final String hubId;
    
    public SmartHomeHub(String hubId) {
        this.hubId = hubId;
        this.devices = new ConcurrentHashMap<>();
        this.groups = new HashMap<>();
        this.automationRules = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.securityManager = new SecurityManager();
        this.automationExecutor = Executors.newFixedThreadPool(5);
        this.isOnline = true;
    }
    
    // Runtime device addition/removal - core composition flexibility
    public void addDevice(SmartDevice device) {
        devices.put(device.getDeviceId(), device);
        device.setHub(this);
        device.initialize();
        
        notifyListeners("Device added: " + device.getName() + " (" + device.getType() + ")");
        
        // Check if device triggers any automation rules
        checkAutomationRules(device.getDeviceId(), "device_added", null);
    }
    
    public void removeDevice(String deviceId) {
        SmartDevice device = devices.remove(deviceId);
        if (device != null) {
            device.disconnect();
            notifyListeners("Device removed: " + device.getName());
            
            // Remove device from all groups
            groups.values().forEach(group -> group.removeDevice(deviceId));
        }
    }
    
    public DeviceControlResult controlDevice(String deviceId, String command, Object... parameters) {
        SmartDevice device = devices.get(deviceId);
        if (device == null) {
            return new DeviceControlResult(false, "Device not found: " + deviceId, null);
        }
        
        if (!device.isOnline()) {
            return new DeviceControlResult(false, "Device offline: " + device.getName(), null);
        }
        
        // Security check
        if (!securityManager.isCommandAuthorized(deviceId, command)) {
            return new DeviceControlResult(false, "Command not authorized", null);
        }
        
        DeviceControlResult result = device.executeCommand(command, parameters);
        
        if (result.isSuccess()) {
            notifyListeners("Command executed: " + command + " on " + device.getName());
            
            // Trigger automation rules
            checkAutomationRules(deviceId, command, result.getResponse());
        }
        
        return result;
    }
    
    // Device grouping for bulk operations
    public void createGroup(String groupName, List<String> deviceIds) {
        DeviceGroup group = new DeviceGroup(groupName);
        
        for (String deviceId : deviceIds) {
            SmartDevice device = devices.get(deviceId);
            if (device != null) {
                group.addDevice(device);
            }
        }
        
        groups.put(groupName, group);
        notifyListeners("Device group created: " + groupName + " with " + group.getDeviceCount() + " devices");
    }
    
    public GroupControlResult controlGroup(String groupName, String command, Object... parameters) {
        DeviceGroup group = groups.get(groupName);
        if (group == null) {
            return new GroupControlResult(false, "Group not found: " + groupName, new ArrayList<>());
        }
        
        List<DeviceControlResult> results = new ArrayList<>();
        
        for (SmartDevice device : group.getDevices()) {
            DeviceControlResult result = controlDevice(device.getDeviceId(), command, parameters);
            results.add(result);
        }
        
        boolean allSuccessful = results.stream().allMatch(DeviceControlResult::isSuccess);
        String message = allSuccessful ? "All devices in group executed successfully" : "Some devices failed";
        
        return new GroupControlResult(allSuccessful, message, results);
    }
    
    // Automation rules
    public void addAutomationRule(AutomationRule rule) {
        automationRules.add(rule);
        notifyListeners("Automation rule added: " + rule.getName());
    }
    
    public void removeAutomationRule(String ruleName) {
        automationRules.removeIf(rule -> {
            if (rule.getName().equals(ruleName)) {
                notifyListeners("Automation rule removed: " + ruleName);
                return true;
            }
            return false;
        });
    }
    
    private void checkAutomationRules(String deviceId, String trigger, Object data) {
        automationExecutor.submit(() -> {
            for (AutomationRule rule : automationRules) {
                if (rule.isEnabled() && rule.matches(deviceId, trigger, data)) {
                    try {
                        rule.execute(this);
                        notifyListeners("Automation rule triggered: " + rule.getName());
                    } catch (Exception e) {
                        notifyListeners("Automation rule failed: " + rule.getName() + " - " + e.getMessage());
                    }
                }
            }
        });
    }
    
    // Voice assistant integration
    public DeviceControlResult processVoiceCommand(String voiceInput) {
        VoiceCommand command = VoiceCommandParser.parse(voiceInput);
        
        if (command == null) {
            return new DeviceControlResult(false, "Could not understand voice command", null);
        }
        
        notifyListeners("Voice command: " + voiceInput);
        
        if (command.isGroupCommand()) {
            GroupControlResult groupResult = controlGroup(command.getTarget(), command.getAction(), command.getParameters());
            return new DeviceControlResult(groupResult.isSuccess(), groupResult.getMessage(), groupResult);
        } else {
            return controlDevice(command.getTarget(), command.getAction(), command.getParameters());
        }
    }
    
    // Scene management
    public void createScene(String sceneName, List<SceneAction> actions) {
        SceneManager.createScene(sceneName, actions);
        notifyListeners("Scene created: " + sceneName);
    }
    
    public SceneExecutionResult executeScene(String sceneName) {
        List<SceneAction> actions = SceneManager.getScene(sceneName);
        if (actions == null) {
            return new SceneExecutionResult(false, "Scene not found: " + sceneName, new ArrayList<>());
        }
        
        notifyListeners("Executing scene: " + sceneName);
        List<DeviceControlResult> results = new ArrayList<>();
        
        for (SceneAction action : actions) {
            DeviceControlResult result = controlDevice(action.getDeviceId(), action.getCommand(), action.getParameters());
            results.add(result);
            
            // Add delay between actions if specified
            if (action.getDelayMs() > 0) {
                try {
                    Thread.sleep(action.getDelayMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        boolean allSuccessful = results.stream().allMatch(DeviceControlResult::isSuccess);
        return new SceneExecutionResult(allSuccessful, 
                                      allSuccessful ? "Scene executed successfully" : "Some actions failed", 
                                      results);
    }
    
    // Device discovery and status
    public List<SmartDevice> discoverDevices() {
        notifyListeners("Scanning for new devices...");
        
        // Simulate device discovery
        List<SmartDevice> discoveredDevices = new ArrayList<>();
        
        // This would typically scan the network for new devices
        // For demo purposes, we'll return empty list
        
        notifyListeners("Device discovery completed. Found " + discoveredDevices.size() + " new devices");
        return discoveredDevices;
    }
    
    public HubStatus getHubStatus() {
        int onlineDevices = (int) devices.values().stream().filter(SmartDevice::isOnline).count();
        int totalDevices = devices.size();
        int activeRules = (int) automationRules.stream().filter(AutomationRule::isEnabled).count();
        
        return new HubStatus(hubId, isOnline, totalDevices, onlineDevices, 
                           groups.size(), activeRules, System.currentTimeMillis());
    }
    
    public void addEventListener(HubEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(HubEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (HubEventListener listener : listeners) {
            listener.onHubEvent(hubId, message, getHubStatus());
        }
    }
    
    public void displayHubStatus() {
        System.out.println("\n=== Smart Home Hub Status ===");
        System.out.println("Hub ID: " + hubId);
        System.out.println("Status: " + (isOnline ? "Online" : "Offline"));
        System.out.println("Total Devices: " + devices.size());
        System.out.println("Online Devices: " + devices.values().stream().filter(SmartDevice::isOnline).count());
        System.out.println("Device Groups: " + groups.size());
        System.out.println("Automation Rules: " + automationRules.size() + 
                         " (" + automationRules.stream().filter(AutomationRule::isEnabled).count() + " enabled)");
        
        System.out.println("\nConnected Devices:");
        devices.values().forEach(device -> {
            String status = device.isOnline() ? "🟢" : "🔴";
            System.out.printf("  %s %s (%s) - %s\n", 
                            status, device.getName(), device.getType(), device.getStatus());
        });
        
        if (!groups.isEmpty()) {
            System.out.println("\nDevice Groups:");
            groups.forEach((name, group) -> 
                System.out.printf("  📁 %s: %d devices\n", name, group.getDeviceCount()));
        }
        
        if (!automationRules.isEmpty()) {
            System.out.println("\nAutomation Rules:");
            automationRules.forEach(rule -> {
                String status = rule.isEnabled() ? "✅" : "❌";
                System.out.printf("  %s %s\n", status, rule.getName());
            });
        }
    }
    
    public void shutdown() {
        automationExecutor.shutdown();
        try {
            if (!automationExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                automationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            automationExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Disconnect all devices
        devices.values().forEach(SmartDevice::disconnect);
        isOnline = false;
        
        notifyListeners("Hub shutdown completed");
    }
    
    // Getters
    public Map<String, SmartDevice> getDevices() { return new HashMap<>(devices); }
    public Map<String, DeviceGroup> getGroups() { return new HashMap<>(groups); }
    public List<AutomationRule> getAutomationRules() { return new ArrayList<>(automationRules); }
    public boolean isOnline() { return isOnline; }
    public String getHubId() { return hubId; }
}
