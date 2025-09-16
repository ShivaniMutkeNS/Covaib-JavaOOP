package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract Smart Home Device with enterprise IoT features
 * Supports scheduling, energy monitoring, security, and remote management
 */
public abstract class Device {
    
    public enum DeviceState {
        OFF, ON, STANDBY, ERROR, MAINTENANCE, UPDATING
    }
    
    public enum DeviceType {
        LIGHT, FAN, THERMOSTAT, CAMERA, LOCK, SENSOR, SPEAKER, DISPLAY
    }
    
    protected String deviceId;
    protected String deviceName;
    protected DeviceType deviceType;
    protected DeviceState currentState;
    protected boolean isConnected;
    protected boolean supportsScheduling;
    protected boolean supportsEnergyMonitoring;
    protected Map<String, Object> deviceProperties;
    protected DeviceScheduler scheduler;
    protected SecurityManager securityManager;
    protected EnergyMonitor energyMonitor;
    
    public Device(String deviceId, String deviceName, DeviceType deviceType, 
                 Map<String, Object> properties) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceProperties = properties;
        this.currentState = DeviceState.OFF;
        this.isConnected = false;
        this.supportsScheduling = (Boolean) properties.getOrDefault("supports_scheduling", false);
        this.supportsEnergyMonitoring = (Boolean) properties.getOrDefault("supports_energy_monitoring", false);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        if (supportsScheduling) {
            this.scheduler = new DeviceScheduler(deviceId);
        }
        this.securityManager = new SecurityManager(deviceId);
        if (supportsEnergyMonitoring) {
            this.energyMonitor = new EnergyMonitor(deviceId);
        }
    }
    
    /**
     * Abstract method to turn on the device
     * @return DeviceOperationResult with operation status
     * @throws DeviceException if operation fails
     */
    public abstract CompletableFuture<DeviceOperationResult> turnOn() throws DeviceException;
    
    /**
     * Abstract method to turn off the device
     * @return DeviceOperationResult with operation status
     * @throws DeviceException if operation fails
     */
    public abstract CompletableFuture<DeviceOperationResult> turnOff() throws DeviceException;
    
    /**
     * Abstract method to get current device status
     * @return DeviceStatus with comprehensive device information
     * @throws DeviceException if status retrieval fails
     */
    public abstract DeviceStatus getStatus() throws DeviceException;
    
    /**
     * Schedule device operation (only if supported)
     * @param scheduleRequest Schedule details
     * @return ScheduleResult with scheduling status
     * @throws DeviceException if scheduling fails or not supported
     */
    public ScheduleResult schedule(ScheduleRequest scheduleRequest) throws DeviceException {
        if (!supportsScheduling) {
            throw new DeviceException("Device " + deviceId + " does not support scheduling");
        }
        
        return scheduler.addSchedule(scheduleRequest);
    }
    
    /**
     * Template method for device operation workflow
     */
    public final DeviceOperationResult performOperation(DeviceOperation operation) {
        try {
            // Step 1: Security validation
            if (!securityManager.validateOperation(operation)) {
                return DeviceOperationResult.failure(operation.getOperationId(), 
                    "Security validation failed", "SECURITY_ERROR");
            }
            
            // Step 2: Pre-operation hooks
            preOperationHook(operation);
            
            // Step 3: Check device connectivity
            if (!isConnected && !connectToDevice()) {
                return DeviceOperationResult.failure(operation.getOperationId(), 
                    "Device not connected", "CONNECTION_ERROR");
            }
            
            // Step 4: Execute operation
            CompletableFuture<DeviceOperationResult> futureResult = executeOperation(operation);
            DeviceOperationResult result = futureResult.get(); // In real world, use callbacks
            
            // Step 5: Update energy monitoring
            if (supportsEnergyMonitoring && result.isSuccess()) {
                energyMonitor.recordOperation(operation, result);
            }
            
            // Step 6: Post-operation hooks
            postOperationHook(operation, result);
            
            return result;
            
        } catch (Exception e) {
            return handleOperationError(e, operation);
        }
    }
    
    private CompletableFuture<DeviceOperationResult> executeOperation(DeviceOperation operation) 
            throws DeviceException {
        switch (operation.getType()) {
            case TURN_ON:
                return turnOn();
            case TURN_OFF:
                return turnOff();
            case GET_STATUS:
                DeviceStatus status = getStatus();
                return CompletableFuture.completedFuture(
                    DeviceOperationResult.success(operation.getOperationId(), status));
            case UPDATE_SETTINGS:
                return updateDeviceSettings(operation.getParameters());
            default:
                throw new DeviceException("Unsupported operation: " + operation.getType());
        }
    }
    
    /**
     * Update device settings (can be overridden by specific devices)
     */
    protected CompletableFuture<DeviceOperationResult> updateDeviceSettings(Map<String, Object> settings) 
            throws DeviceException {
        // Default implementation - update properties
        if (settings != null) {
            deviceProperties.putAll(settings);
        }
        
        return CompletableFuture.completedFuture(
            DeviceOperationResult.success("update_settings", "Settings updated successfully"));
    }
    
    /**
     * Connect to device (can be overridden for specific connection protocols)
     */
    protected boolean connectToDevice() {
        try {
            // Simulate connection process
            Thread.sleep(100);
            isConnected = true;
            System.out.println("Connected to device: " + deviceId);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    // Hook methods for customization
    protected void preOperationHook(DeviceOperation operation) {
        System.out.println("Pre-operation: " + operation.getType() + " on device " + deviceId);
    }
    
    protected void postOperationHook(DeviceOperation operation, DeviceOperationResult result) {
        System.out.println("Post-operation: " + operation.getType() + " completed with status: " + 
                         result.isSuccess());
    }
    
    private DeviceOperationResult handleOperationError(Exception e, DeviceOperation operation) {
        System.err.println("Operation failed for device " + deviceId + ": " + e.getMessage());
        return DeviceOperationResult.failure(operation.getOperationId(), e.getMessage(), "OPERATION_ERROR");
    }
    
    // Utility methods
    public void disconnect() {
        isConnected = false;
        if (scheduler != null) {
            scheduler.shutdown();
        }
        System.out.println("Disconnected from device: " + deviceId);
    }
    
    // Getters and setters
    public String getDeviceId() { return deviceId; }
    public String getDeviceName() { return deviceName; }
    public DeviceType getDeviceType() { return deviceType; }
    public DeviceState getCurrentState() { return currentState; }
    public boolean isConnected() { return isConnected; }
    public boolean supportsScheduling() { return supportsScheduling; }
    public boolean supportsEnergyMonitoring() { return supportsEnergyMonitoring; }
    public Map<String, Object> getDeviceProperties() { return deviceProperties; }
    public EnergyMonitor getEnergyMonitor() { return energyMonitor; }
    
    protected void setCurrentState(DeviceState state) { 
        this.currentState = state; 
    }
}
