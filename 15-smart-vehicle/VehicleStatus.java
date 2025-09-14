import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents the current status and operational state of a smart vehicle
 * Contains real-time metrics, diagnostics, and system information
 */
public class VehicleStatus {
    private String vehicleId;
    private LocalDateTime lastUpdated;
    private OperationalState operationalState;
    private double currentSpeedKmh;
    private double fuelLevel; // Percentage or kWh remaining
    private double batteryLevel; // For electric/hybrid vehicles
    private double engineTemperature;
    private double tirePressure; // Average tire pressure
    private int odometerKm;
    private Map<String, Object> diagnostics;
    private Map<String, Boolean> systemStatus;
    private String currentLocation;
    private String destination;
    private double remainingRangeKm;
    private boolean maintenanceRequired;
    private String[] activeWarnings;
    private double averageFuelConsumption;
    
    public VehicleStatus(String vehicleId) {
        this.vehicleId = vehicleId;
        this.lastUpdated = LocalDateTime.now();
        this.operationalState = OperationalState.PARKED;
        this.currentSpeedKmh = 0.0;
        this.fuelLevel = 100.0;
        this.batteryLevel = 100.0;
        this.engineTemperature = 20.0;
        this.tirePressure = 32.0;
        this.odometerKm = 0;
        this.diagnostics = new HashMap<>();
        this.systemStatus = new HashMap<>();
        this.remainingRangeKm = 500.0;
        this.maintenanceRequired = false;
        this.activeWarnings = new String[0];
        this.averageFuelConsumption = 8.0;
        initializeSystemStatus();
    }
    
    private void initializeSystemStatus() {
        systemStatus.put("engine", true);
        systemStatus.put("brakes", true);
        systemStatus.put("lights", true);
        systemStatus.put("airbags", true);
        systemStatus.put("abs", true);
        systemStatus.put("navigation", true);
        systemStatus.put("communication", true);
        systemStatus.put("sensors", true);
    }
    
    public void updateStatus() {
        this.lastUpdated = LocalDateTime.now();
        
        // Simulate realistic status changes
        if (operationalState == OperationalState.DRIVING) {
            // Consume fuel/battery while driving
            if (fuelLevel > 0) {
                fuelLevel = Math.max(0, fuelLevel - (currentSpeedKmh * 0.001));
            }
            if (batteryLevel > 0) {
                batteryLevel = Math.max(0, batteryLevel - (currentSpeedKmh * 0.0008));
            }
            
            // Update odometer
            odometerKm += (int)(currentSpeedKmh * 0.1);
            
            // Engine temperature varies with speed
            engineTemperature = 85 + (currentSpeedKmh * 0.2) + (Math.random() * 10 - 5);
            
            // Update remaining range
            remainingRangeKm = fuelLevel * 5 + batteryLevel * 3;
        } else {
            // Cool down when not driving
            engineTemperature = Math.max(20, engineTemperature - 2);
        }
        
        // Check for maintenance needs
        checkMaintenanceRequirements();
        
        // Update diagnostics
        updateDiagnostics();
    }
    
    private void checkMaintenanceRequirements() {
        // Check various maintenance conditions
        if (odometerKm > 0 && odometerKm % 10000 == 0) {
            maintenanceRequired = true;
            addWarning("Regular maintenance due at " + odometerKm + " km");
        }
        
        if (tirePressure < 28) {
            addWarning("Low tire pressure detected");
        }
        
        if (fuelLevel < 10) {
            addWarning("Low fuel level");
        }
        
        if (batteryLevel < 15) {
            addWarning("Low battery level");
        }
        
        if (engineTemperature > 110) {
            addWarning("Engine overheating");
        }
    }
    
    private void updateDiagnostics() {
        diagnostics.put("engine_hours", odometerKm / 50.0);
        diagnostics.put("brake_wear_percentage", Math.min(100, odometerKm * 0.01));
        diagnostics.put("battery_health_percentage", Math.max(70, 100 - (odometerKm * 0.002)));
        diagnostics.put("emission_level", engineTemperature > 90 ? "Normal" : "Low");
        diagnostics.put("system_efficiency", calculateSystemEfficiency());
    }
    
    private double calculateSystemEfficiency() {
        double baseEfficiency = 85.0;
        
        // Reduce efficiency based on various factors
        if (engineTemperature > 100) baseEfficiency -= 5;
        if (tirePressure < 30) baseEfficiency -= 3;
        if (fuelLevel < 20) baseEfficiency -= 2;
        if (maintenanceRequired) baseEfficiency -= 8;
        
        return Math.max(50, baseEfficiency);
    }
    
    private void addWarning(String warning) {
        // Add warning to active warnings array
        String[] newWarnings = new String[activeWarnings.length + 1];
        System.arraycopy(activeWarnings, 0, newWarnings, 0, activeWarnings.length);
        newWarnings[activeWarnings.length] = warning;
        this.activeWarnings = newWarnings;
    }
    
    public void clearWarnings() {
        this.activeWarnings = new String[0];
    }
    
    public boolean hasWarnings() {
        return activeWarnings.length > 0;
    }
    
    public boolean isSystemHealthy() {
        return !maintenanceRequired && activeWarnings.length == 0 && 
               systemStatus.values().stream().allMatch(status -> status);
    }
    
    public String getHealthSummary() {
        if (isSystemHealthy()) {
            return "All systems operational";
        } else {
            StringBuilder summary = new StringBuilder();
            if (maintenanceRequired) summary.append("Maintenance required. ");
            if (activeWarnings.length > 0) {
                summary.append(activeWarnings.length).append(" active warnings. ");
            }
            
            long failedSystems = systemStatus.values().stream().mapToLong(status -> status ? 0 : 1).sum();
            if (failedSystems > 0) {
                summary.append(failedSystems).append(" system(s) offline.");
            }
            
            return summary.toString().trim();
        }
    }
    
    // Getters and Setters
    public String getVehicleId() { return vehicleId; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public OperationalState getOperationalState() { return operationalState; }
    public double getCurrentSpeedKmh() { return currentSpeedKmh; }
    public double getFuelLevel() { return fuelLevel; }
    public double getBatteryLevel() { return batteryLevel; }
    public double getEngineTemperature() { return engineTemperature; }
    public double getTirePressure() { return tirePressure; }
    public int getOdometerKm() { return odometerKm; }
    public Map<String, Object> getDiagnostics() { return new HashMap<>(diagnostics); }
    public Map<String, Boolean> getSystemStatus() { return new HashMap<>(systemStatus); }
    public String getCurrentLocation() { return currentLocation; }
    public String getDestination() { return destination; }
    public double getRemainingRangeKm() { return remainingRangeKm; }
    public boolean isMaintenanceRequired() { return maintenanceRequired; }
    public String[] getActiveWarnings() { return activeWarnings.clone(); }
    public double getAverageFuelConsumption() { return averageFuelConsumption; }
    
    public void setOperationalState(OperationalState state) { 
        this.operationalState = state; 
        updateStatus();
    }
    public void setCurrentSpeedKmh(double speed) { 
        this.currentSpeedKmh = Math.max(0, speed); 
        updateStatus();
    }
    public void setFuelLevel(double level) { 
        this.fuelLevel = Math.max(0, Math.min(100, level)); 
    }
    public void setBatteryLevel(double level) { 
        this.batteryLevel = Math.max(0, Math.min(100, level)); 
    }
    public void setTirePressure(double pressure) { 
        this.tirePressure = Math.max(0, pressure); 
    }
    public void setCurrentLocation(String location) { 
        this.currentLocation = location; 
    }
    public void setDestination(String destination) { 
        this.destination = destination; 
    }
    public void setSystemStatus(String system, boolean status) { 
        systemStatus.put(system, status); 
    }
    
    @Override
    public String toString() {
        return String.format("Vehicle %s: %s at %.1f km/h (Fuel: %.1f%%, Battery: %.1f%%)", 
            vehicleId, operationalState.getDisplayName(), currentSpeedKmh, fuelLevel, batteryLevel);
    }
}

/**
 * Enumeration of vehicle operational states
 */
enum OperationalState {
    PARKED("Parked", "Vehicle is stationary and parked"),
    IDLE("Idle", "Engine running but vehicle stationary"),
    DRIVING("Driving", "Vehicle is in motion"),
    CHARGING("Charging", "Electric vehicle is charging"),
    MAINTENANCE("Maintenance", "Vehicle is undergoing maintenance"),
    EMERGENCY("Emergency", "Emergency state - immediate attention required"),
    AUTONOMOUS("Autonomous", "Vehicle operating in autonomous mode");
    
    private final String displayName;
    private final String description;
    
    OperationalState(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    
    public boolean canTransitionTo(OperationalState newState) {
        switch (this) {
            case PARKED:
                return newState != EMERGENCY;
            case IDLE:
                return newState == DRIVING || newState == PARKED || newState == CHARGING || newState == AUTONOMOUS;
            case DRIVING:
                return newState == IDLE || newState == PARKED || newState == EMERGENCY;
            case CHARGING:
                return newState == PARKED || newState == IDLE;
            case MAINTENANCE:
                return newState == PARKED;
            case EMERGENCY:
                return newState == MAINTENANCE || newState == PARKED;
            case AUTONOMOUS:
                return newState == PARKED || newState == IDLE || newState == EMERGENCY;
            default:
                return false;
        }
    }
}
