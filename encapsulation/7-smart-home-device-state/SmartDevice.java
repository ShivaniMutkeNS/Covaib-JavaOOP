
import java.time.LocalDateTime;
import java.util.*;

/**
 * Smart Home Device State
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating device state (ON/OFF, temperature, mode)
 * 2. Ensuring state transitions are only valid (e.g., AC cannot go ON if main power is OFF)
 * 3. Hiding raw fields, exposing controlled methods
 * 4. Implementing state machine with validation
 */
public class SmartDevice {
    // Encapsulated device state
    private DeviceState currentState;
    private DeviceType deviceType;
    private String deviceId;
    private String deviceName;
    
    // State-specific data
    private double temperature;
    private DeviceMode mode;
    private int brightness;
    private int volume;
    private String currentProgram;
    
    // Power management
    private boolean mainPowerOn;
    private LocalDateTime lastStateChange;
    
    // State history for audit
    private final List<StateTransition> stateHistory;
    
    // State machine rules
    private final StateMachine stateMachine;
    
    /**
     * Constructor
     */
    public SmartDevice(String deviceId, String deviceName, DeviceType deviceType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.currentState = DeviceState.OFF;
        this.mainPowerOn = true; // Assume main power is on by default
        this.lastStateChange = LocalDateTime.now();
        this.stateHistory = new ArrayList<>();
        this.stateMachine = new StateMachine();
        
        // Initialize device-specific defaults
        initializeDeviceDefaults();
        
        // Log initial state
        logStateTransition(DeviceState.OFF, "Device initialized");
    }
    
    /**
     * Turn device ON
     * @return true if successful, false if invalid transition
     */
    public boolean turnOn() {
        return changeState(DeviceState.ON, "Device turned on");
    }
    
    /**
     * Turn device OFF
     * @return true if successful, false if invalid transition
     */
    public boolean turnOff() {
        return changeState(DeviceState.OFF, "Device turned off");
    }
    
    /**
     * Set device to standby mode
     * @return true if successful, false if invalid transition
     */
    public boolean setStandby() {
        return changeState(DeviceState.STANDBY, "Device set to standby");
    }
    
    /**
     * Set device to maintenance mode
     * @return true if successful, false if invalid transition
     */
    public boolean setMaintenance() {
        return changeState(DeviceState.MAINTENANCE, "Device set to maintenance mode");
    }
    
    /**
     * Set temperature (for temperature-controlled devices)
     * @param temperature Temperature to set
     * @return true if successful, false if invalid
     */
    public boolean setTemperature(double temperature) {
        if (!isTemperatureControlled()) {
            logStateTransition(currentState, "Temperature control not available for this device type");
            return false;
        }
        
        if (!isValidTemperature(temperature)) {
            logStateTransition(currentState, "Invalid temperature: " + temperature);
            return false;
        }
        
        if (currentState != DeviceState.ON) {
            logStateTransition(currentState, "Cannot set temperature when device is not ON");
            return false;
        }
        
        this.temperature = temperature;
        logStateTransition(currentState, "Temperature set to " + temperature);
        return true;
    }
    
    /**
     * Set device mode
     * @param mode Mode to set
     * @return true if successful, false if invalid
     */
    public boolean setMode(DeviceMode mode) {
        if (!isValidModeForDevice(mode)) {
            logStateTransition(currentState, "Invalid mode for device type: " + mode);
            return false;
        }
        
        if (currentState != DeviceState.ON) {
            logStateTransition(currentState, "Cannot set mode when device is not ON");
            return false;
        }
        
        this.mode = mode;
        logStateTransition(currentState, "Mode set to " + mode);
        return true;
    }
    
    /**
     * Set brightness (for light devices)
     * @param brightness Brightness level (0-100)
     * @return true if successful, false if invalid
     */
    public boolean setBrightness(int brightness) {
        if (!isLightDevice()) {
            logStateTransition(currentState, "Brightness control not available for this device type");
            return false;
        }
        
        if (brightness < 0 || brightness > 100) {
            logStateTransition(currentState, "Invalid brightness level: " + brightness);
            return false;
        }
        
        if (currentState != DeviceState.ON) {
            logStateTransition(currentState, "Cannot set brightness when device is not ON");
            return false;
        }
        
        this.brightness = brightness;
        logStateTransition(currentState, "Brightness set to " + brightness);
        return true;
    }
    
    /**
     * Set volume (for audio devices)
     * @param volume Volume level (0-100)
     * @return true if successful, false if invalid
     */
    public boolean setVolume(int volume) {
        if (!isAudioDevice()) {
            logStateTransition(currentState, "Volume control not available for this device type");
            return false;
        }
        
        if (volume < 0 || volume > 100) {
            logStateTransition(currentState, "Invalid volume level: " + volume);
            return false;
        }
        
        if (currentState != DeviceState.ON) {
            logStateTransition(currentState, "Cannot set volume when device is not ON");
            return false;
        }
        
        this.volume = volume;
        logStateTransition(currentState, "Volume set to " + volume);
        return true;
    }
    
    /**
     * Set program (for programmable devices)
     * @param program Program to set
     * @return true if successful, false if invalid
     */
    public boolean setProgram(String program) {
        if (!isProgrammableDevice()) {
            logStateTransition(currentState, "Program control not available for this device type");
            return false;
        }
        
        if (program == null || program.trim().isEmpty()) {
            logStateTransition(currentState, "Invalid program: " + program);
            return false;
        }
        
        if (currentState != DeviceState.ON) {
            logStateTransition(currentState, "Cannot set program when device is not ON");
            return false;
        }
        
        this.currentProgram = program;
        logStateTransition(currentState, "Program set to " + program);
        return true;
    }
    
    /**
     * Turn main power ON
     * @return true if successful
     */
    public boolean turnMainPowerOn() {
        this.mainPowerOn = true;
        logStateTransition(currentState, "Main power turned on");
        return true;
    }
    
    /**
     * Turn main power OFF
     * @return true if successful
     */
    public boolean turnMainPowerOff() {
        this.mainPowerOn = false;
        // Turn off device if main power is off
        if (currentState == DeviceState.ON) {
            changeState(DeviceState.OFF, "Device turned off due to main power loss");
        }
        logStateTransition(currentState, "Main power turned off");
        return true;
    }
    
    /**
     * Get current state
     * @return Current device state
     */
    public DeviceState getCurrentState() {
        return currentState;
    }
    
    /**
     * Get device type
     * @return Device type
     */
    public DeviceType getDeviceType() {
        return deviceType;
    }
    
    /**
     * Get device ID
     * @return Device ID
     */
    public String getDeviceId() {
        return deviceId;
    }
    
    /**
     * Get device name
     * @return Device name
     */
    public String getDeviceName() {
        return deviceName;
    }
    
    /**
     * Get current temperature
     * @return Current temperature
     */
    public double getTemperature() {
        return temperature;
    }
    
    /**
     * Get current mode
     * @return Current mode
     */
    public DeviceMode getMode() {
        return mode;
    }
    
    /**
     * Get current brightness
     * @return Current brightness
     */
    public int getBrightness() {
        return brightness;
    }
    
    /**
     * Get current volume
     * @return Current volume
     */
    public int getVolume() {
        return volume;
    }
    
    /**
     * Get current program
     * @return Current program
     */
    public String getCurrentProgram() {
        return currentProgram;
    }
    
    /**
     * Check if main power is on
     * @return true if main power is on
     */
    public boolean isMainPowerOn() {
        return mainPowerOn;
    }
    
    /**
     * Get last state change time
     * @return Last state change time
     */
    public LocalDateTime getLastStateChange() {
        return lastStateChange;
    }
    
    /**
     * Get state history
     * @return List of state transitions
     */
    public List<StateTransition> getStateHistory() {
        return Collections.unmodifiableList(stateHistory);
    }
    
    /**
     * Get device status summary
     * @return Device status summary
     */
    public String getStatusSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Device: ").append(deviceName).append(" (").append(deviceId).append(")\n");
        summary.append("Type: ").append(deviceType).append("\n");
        summary.append("State: ").append(currentState).append("\n");
        summary.append("Main Power: ").append(mainPowerOn ? "ON" : "OFF").append("\n");
        summary.append("Last Change: ").append(lastStateChange).append("\n");
        
        if (isTemperatureControlled()) {
            summary.append("Temperature: ").append(temperature).append("°C\n");
        }
        if (mode != null) {
            summary.append("Mode: ").append(mode).append("\n");
        }
        if (isLightDevice()) {
            summary.append("Brightness: ").append(brightness).append("%\n");
        }
        if (isAudioDevice()) {
            summary.append("Volume: ").append(volume).append("%\n");
        }
        if (isProgrammableDevice() && currentProgram != null) {
            summary.append("Program: ").append(currentProgram).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Change device state with validation
     * @param newState New state to transition to
     * @param reason Reason for state change
     * @return true if successful, false if invalid transition
     */
    private boolean changeState(DeviceState newState, String reason) {
        if (!stateMachine.isValidTransition(currentState, newState, this)) {
            logStateTransition(currentState, "Invalid state transition: " + currentState + " -> " + newState);
            return false;
        }
        
        // DeviceState oldState = currentState; // Removed unused variable
        currentState = newState;
        lastStateChange = LocalDateTime.now();
        
        logStateTransition(newState, reason);
        return true;
    }
    
    /**
     * Log state transition
     * @param state State after transition
     * @param reason Reason for transition
     */
    private void logStateTransition(DeviceState state, String reason) {
        StateTransition transition = new StateTransition(
            state, reason, LocalDateTime.now()
        );
        stateHistory.add(transition);
    }
    
    /**
     * Initialize device-specific defaults
     */
    private void initializeDeviceDefaults() {
        switch (deviceType) {
            case AIR_CONDITIONER:
                temperature = 22.0;
                mode = DeviceMode.COOL;
                break;
            case HEATER:
                temperature = 20.0;
                mode = DeviceMode.HEAT;
                break;
            case LIGHT:
                brightness = 50;
                mode = DeviceMode.NORMAL;
                break;
            case SPEAKER:
                volume = 30;
                mode = DeviceMode.NORMAL;
                break;
            case WASHING_MACHINE:
                mode = DeviceMode.NORMAL;
                currentProgram = "Normal";
                break;
            case TV:
                volume = 50;
                brightness = 80;
                mode = DeviceMode.NORMAL;
                break;
            default:
                mode = DeviceMode.NORMAL;
                break;
        }
    }
    
    /**
     * Check if device is temperature controlled
     * @return true if temperature controlled
     */
    private boolean isTemperatureControlled() {
        return deviceType == DeviceType.AIR_CONDITIONER || 
               deviceType == DeviceType.HEATER;
    }
    
    /**
     * Check if device is a light device
     * @return true if light device
     */
    private boolean isLightDevice() {
        return deviceType == DeviceType.LIGHT || 
               deviceType == DeviceType.TV;
    }
    
    /**
     * Check if device is an audio device
     * @return true if audio device
     */
    private boolean isAudioDevice() {
        return deviceType == DeviceType.SPEAKER || 
               deviceType == DeviceType.TV;
    }
    
    /**
     * Check if device is programmable
     * @return true if programmable
     */
    private boolean isProgrammableDevice() {
        return deviceType == DeviceType.WASHING_MACHINE || 
               deviceType == DeviceType.DISHWASHER;
    }
    
    /**
     * Validate temperature for device type
     * @param temperature Temperature to validate
     * @return true if valid
     */
    private boolean isValidTemperature(double temperature) {
        switch (deviceType) {
            case AIR_CONDITIONER:
                return temperature >= 16.0 && temperature <= 30.0;
            case HEATER:
                return temperature >= 10.0 && temperature <= 35.0;
            default:
                return false;
        }
    }
    
    /**
     * Validate mode for device type
     * @param mode Mode to validate
     * @return true if valid
     */
    private boolean isValidModeForDevice(DeviceMode mode) {
        switch (deviceType) {
            case AIR_CONDITIONER:
                return mode == DeviceMode.COOL || mode == DeviceMode.HEAT || mode == DeviceMode.FAN;
            case HEATER:
                return mode == DeviceMode.HEAT || mode == DeviceMode.FAN;
            case LIGHT:
                return mode == DeviceMode.NORMAL || mode == DeviceMode.DIMMED || mode == DeviceMode.BRIGHT;
            case SPEAKER:
                return mode == DeviceMode.NORMAL || mode == DeviceMode.LOUD || mode == DeviceMode.QUIET;
            case WASHING_MACHINE:
                return mode == DeviceMode.NORMAL || mode == DeviceMode.DELICATE || mode == DeviceMode.HEAVY;
            case TV:
                return mode == DeviceMode.NORMAL || mode == DeviceMode.BRIGHT || mode == DeviceMode.DIMMED;
            default:
                return mode == DeviceMode.NORMAL;
        }
    }
    
    /**
     * Device states
     */
    public enum DeviceState {
        OFF, ON, STANDBY, MAINTENANCE
    }
    
    /**
     * Device types
     */
    public enum DeviceType {
        AIR_CONDITIONER, HEATER, LIGHT, SPEAKER, WASHING_MACHINE, DISHWASHER, TV, FAN
    }
    
    /**
     * Device modes
     */
    public enum DeviceMode {
        NORMAL, COOL, HEAT, FAN, DIMMED, BRIGHT, LOUD, QUIET, DELICATE, HEAVY
    }
    
    /**
     * State transition record
     */
    public static class StateTransition {
        private final DeviceState state;
        private final String reason;
        private final LocalDateTime timestamp;
        
        public StateTransition(DeviceState state, String reason, LocalDateTime timestamp) {
            this.state = state;
            this.reason = reason;
            this.timestamp = timestamp;
        }
        
        public DeviceState getState() { return state; }
        public String getReason() { return reason; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s: %s", timestamp, state, reason);
        }
    }
    
    /**
     * State machine for validating transitions
     */
    private static class StateMachine {
        private final Map<DeviceState, Set<DeviceState>> validTransitions;
        
        public StateMachine() {
            this.validTransitions = new HashMap<>();
            initializeTransitions();
        }
        
        private void initializeTransitions() {
            // OFF can transition to ON or MAINTENANCE
            validTransitions.put(DeviceState.OFF, EnumSet.of(DeviceState.ON, DeviceState.MAINTENANCE));
            
            // ON can transition to OFF, STANDBY, or MAINTENANCE
            validTransitions.put(DeviceState.ON, EnumSet.of(DeviceState.OFF, DeviceState.STANDBY, DeviceState.MAINTENANCE));
            
            // STANDBY can transition to ON, OFF, or MAINTENANCE
            validTransitions.put(DeviceState.STANDBY, EnumSet.of(DeviceState.ON, DeviceState.OFF, DeviceState.MAINTENANCE));
            
            // MAINTENANCE can transition to OFF
            validTransitions.put(DeviceState.MAINTENANCE, EnumSet.of(DeviceState.OFF));
        }
        
        public boolean isValidTransition(DeviceState currentState, DeviceState newState, SmartDevice device) {
            // Check if transition is valid
            Set<DeviceState> validStates = validTransitions.get(currentState);
            if (validStates == null || !validStates.contains(newState)) {
                return false;
            }
            
            // Check device-specific rules
            return validateDeviceSpecificRules(currentState, newState, device);
        }
        
        private boolean validateDeviceSpecificRules(DeviceState currentState, DeviceState newState, SmartDevice device) {
            // Rule: Cannot turn ON if main power is off
            if (newState == DeviceState.ON && !device.isMainPowerOn()) {
                return false;
            }
            
            // Rule: Cannot turn ON if device is in maintenance mode
            if (newState == DeviceState.ON && currentState == DeviceState.MAINTENANCE) {
                return false;
            }
            
            // Rule: AC cannot go ON if main power is OFF
            if (device.getDeviceType() == DeviceType.AIR_CONDITIONER && 
                newState == DeviceState.ON && !device.isMainPowerOn()) {
                return false;
            }
            
            // Rule: Heater cannot go ON if main power is OFF
            if (device.getDeviceType() == DeviceType.HEATER && 
                newState == DeviceState.ON && !device.isMainPowerOn()) {
                return false;
            }
            
            return true;
        }
    }
    
    @Override
    public String toString() {
        return String.format("SmartDevice{id='%s', name='%s', type=%s, state=%s, power=%s}", 
            deviceId, deviceName, deviceType, currentState, mainPowerOn ? "ON" : "OFF");
    }
}
