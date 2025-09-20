package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data models for smart home device operations
 */

// Device Operation
class DeviceOperation {
    public enum OperationType {
        TURN_ON, TURN_OFF, GET_STATUS, UPDATE_SETTINGS, SCHEDULE, CANCEL_SCHEDULE
    }
    
    private String operationId;
    private OperationType type;
    private Map<String, Object> parameters;
    private String userId;
    private LocalDateTime requestTime;
    
    public DeviceOperation(String operationId, OperationType type, Map<String, Object> parameters, String userId) {
        this.operationId = operationId;
        this.type = type;
        this.parameters = parameters;
        this.userId = userId;
        this.requestTime = LocalDateTime.now();
    }
    
    // Getters
    public String getOperationId() { return operationId; }
    public OperationType getType() { return type; }
    public Map<String, Object> getParameters() { return parameters; }
    public String getUserId() { return userId; }
    public LocalDateTime getRequestTime() { return requestTime; }
}

// Device Operation Result
class DeviceOperationResult {
    private String operationId;
    private boolean success;
    private String message;
    private String errorCode;
    private Object data;
    private LocalDateTime completionTime;
    
    private DeviceOperationResult(String operationId, boolean success, String message, 
                                 String errorCode, Object data) {
        this.operationId = operationId;
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
        this.completionTime = LocalDateTime.now();
    }
    
    public static DeviceOperationResult success(String operationId, Object data) {
        return new DeviceOperationResult(operationId, true, "Operation completed successfully", null, data);
    }
    
    public static DeviceOperationResult success(String operationId, String message) {
        return new DeviceOperationResult(operationId, true, message, null, null);
    }
    
    public static DeviceOperationResult failure(String operationId, String message, String errorCode) {
        return new DeviceOperationResult(operationId, false, message, errorCode, null);
    }
    
    // Getters
    public String getOperationId() { return operationId; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getErrorCode() { return errorCode; }
    public Object getData() { return data; }
    public LocalDateTime getCompletionTime() { return completionTime; }
}

// Device Status
class DeviceStatus {
    private String deviceId;
    private Device.DeviceState state;
    private boolean isOnline;
    private double batteryLevel;
    private String firmwareVersion;
    private LocalDateTime lastUpdate;
    private Map<String, Object> properties;
    private List<String> activeAlerts;
    private EnergyUsage energyUsage;
    
    public DeviceStatus(String deviceId, Device.DeviceState state, boolean isOnline) {
        this.deviceId = deviceId;
        this.state = state;
        this.isOnline = isOnline;
        this.lastUpdate = LocalDateTime.now();
        this.batteryLevel = -1; // -1 indicates not applicable
    }
    
    // Getters and setters
    public String getDeviceId() { return deviceId; }
    public Device.DeviceState getState() { return state; }
    public boolean isOnline() { return isOnline; }
    public double getBatteryLevel() { return batteryLevel; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public Map<String, Object> getProperties() { return properties; }
    public List<String> getActiveAlerts() { return activeAlerts; }
    public EnergyUsage getEnergyUsage() { return energyUsage; }
    
    public void setBatteryLevel(double batteryLevel) { this.batteryLevel = batteryLevel; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    public void setActiveAlerts(List<String> activeAlerts) { this.activeAlerts = activeAlerts; }
    public void setEnergyUsage(EnergyUsage energyUsage) { this.energyUsage = energyUsage; }
}

// Schedule Request
class ScheduleRequest {
    public enum ScheduleType {
        ONE_TIME, DAILY, WEEKLY, MONTHLY, CUSTOM
    }
    
    private String scheduleId;
    private ScheduleType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DeviceOperation.OperationType operation;
    private Map<String, Object> operationParameters;
    private String cronExpression; // For custom schedules
    private boolean enabled;
    
    public ScheduleRequest(String scheduleId, ScheduleType type, LocalDateTime startTime, 
                          DeviceOperation.OperationType operation) {
        this.scheduleId = scheduleId;
        this.type = type;
        this.startTime = startTime;
        this.operation = operation;
        this.enabled = true;
    }
    
    // Getters and setters
    public String getScheduleId() { return scheduleId; }
    public ScheduleType getType() { return type; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public DeviceOperation.OperationType getOperation() { return operation; }
    public Map<String, Object> getOperationParameters() { return operationParameters; }
    public String getCronExpression() { return cronExpression; }
    public boolean isEnabled() { return enabled; }
    
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setOperationParameters(Map<String, Object> parameters) { this.operationParameters = parameters; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

// Schedule Result
class ScheduleResult {
    private String scheduleId;
    private boolean success;
    private String message;
    private LocalDateTime nextExecution;
    
    private ScheduleResult(String scheduleId, boolean success, String message, LocalDateTime nextExecution) {
        this.scheduleId = scheduleId;
        this.success = success;
        this.message = message;
        this.nextExecution = nextExecution;
    }
    
    public static ScheduleResult success(String scheduleId, LocalDateTime nextExecution) {
        return new ScheduleResult(scheduleId, true, "Schedule created successfully", nextExecution);
    }
    
    public static ScheduleResult failure(String scheduleId, String message) {
        return new ScheduleResult(scheduleId, false, message, null);
    }
    
    // Getters
    public String getScheduleId() { return scheduleId; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LocalDateTime getNextExecution() { return nextExecution; }
}

// Energy Usage
class EnergyUsage {
    private double currentPowerWatts;
    private double totalEnergyKwh;
    private double dailyEnergyKwh;
    private double monthlyEnergyKwh;
    private LocalDateTime lastMeasurement;
    
    public EnergyUsage(double currentPowerWatts, double totalEnergyKwh) {
        this.currentPowerWatts = currentPowerWatts;
        this.totalEnergyKwh = totalEnergyKwh;
        this.lastMeasurement = LocalDateTime.now();
    }
    
    // Getters and setters
    public double getCurrentPowerWatts() { return currentPowerWatts; }
    public double getTotalEnergyKwh() { return totalEnergyKwh; }
    public double getDailyEnergyKwh() { return dailyEnergyKwh; }
    public double getMonthlyEnergyKwh() { return monthlyEnergyKwh; }
    public LocalDateTime getLastMeasurement() { return lastMeasurement; }
    
    public void setCurrentPowerWatts(double watts) { 
        this.currentPowerWatts = watts; 
        this.lastMeasurement = LocalDateTime.now();
    }
    public void setTotalEnergyKwh(double kwh) { this.totalEnergyKwh = kwh; }
    public void setDailyEnergyKwh(double kwh) { this.dailyEnergyKwh = kwh; }
    public void setMonthlyEnergyKwh(double kwh) { this.monthlyEnergyKwh = kwh; }
}

// Custom exceptions
class DeviceException extends Exception {
    private String errorCode;
    
    public DeviceException(String message) { 
        super(message); 
    }
    
    public DeviceException(String message, String errorCode) { 
        super(message); 
        this.errorCode = errorCode;
    }
    
    public DeviceException(String message, Throwable cause) { 
        super(message, cause); 
    }
    
    public String getErrorCode() { return errorCode; }
}

class DeviceConnectionException extends DeviceException {
    public DeviceConnectionException(String message) { super(message); }
}

class DeviceSecurityException extends DeviceException {
    public DeviceSecurityException(String message) { super(message); }
}

class DeviceSchedulingException extends DeviceException {
    public DeviceSchedulingException(String message) { super(message); }
}
