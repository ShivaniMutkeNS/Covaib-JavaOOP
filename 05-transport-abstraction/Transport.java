package abstraction.transport;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract Transport class defining the template for all transport vehicles
 * Uses Template Method pattern to enforce common workflow while allowing customization
 */
public abstract class Transport {
    
    protected String vehicleId;
    protected String vehicleName;
    protected TransportType transportType;
    protected VehicleState currentState;
    protected RouteManager routeManager;
    protected FuelManager fuelManager;
    protected MaintenanceManager maintenanceManager;
    protected SafetySystem safetySystem;
    protected Map<String, Object> configuration;
    
    // Constructor
    public Transport(String vehicleId, String vehicleName, TransportType type, 
                    Map<String, Object> configuration) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.transportType = type;
        this.configuration = configuration;
        this.currentState = VehicleState.IDLE;
        
        // Initialize components
        this.routeManager = new RouteManager();
        this.fuelManager = createFuelManager();
        this.maintenanceManager = new MaintenanceManager(vehicleId);
        this.safetySystem = new SafetySystem(type);
        
        initialize();
    }
    
    // Template method for transport operations
    public final CompletableFuture<TransportResult> executeJourney(JourneyRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Pre-journey checks (template method steps)
                PreJourneyCheckResult preCheck = performPreJourneyChecks(request);
                if (!preCheck.isSuccess()) {
                    return TransportResult.failure("Pre-journey checks failed: " + preCheck.getMessage());
                }
                
                // Start journey
                TransportResult startResult = startJourney(request);
                if (!startResult.isSuccess()) {
                    return startResult;
                }
                
                // Navigate (abstract - implemented by concrete classes)
                NavigationResult navResult = navigate(request.getRoute());
                if (!navResult.isSuccess()) {
                    emergencyStop();
                    return TransportResult.failure("Navigation failed: " + navResult.getMessage());
                }
                
                // Complete journey
                TransportResult completeResult = completeJourney(request);
                
                // Post-journey operations
                performPostJourneyOperations(request, completeResult);
                
                return completeResult;
                
            } catch (Exception e) {
                emergencyStop();
                return TransportResult.failure("Journey execution failed: " + e.getMessage());
            }
        });
    }
    
    // Template method for maintenance operations
    public final CompletableFuture<MaintenanceResult> performMaintenance(MaintenanceRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Safety checks before maintenance
                if (!safetySystem.isMaintenanceSafe()) {
                    return MaintenanceResult.failure("Vehicle not safe for maintenance");
                }
                
                // Stop vehicle if running
                if (currentState == VehicleState.MOVING) {
                    stop();
                }
                
                currentState = VehicleState.MAINTENANCE;
                
                // Perform vehicle-specific maintenance
                MaintenanceResult result = performVehicleSpecificMaintenance(request);
                
                // Update maintenance records
                maintenanceManager.recordMaintenance(request, result);
                
                // Post-maintenance checks
                if (result.isSuccess()) {
                    PostMaintenanceCheckResult postCheck = performPostMaintenanceChecks();
                    if (postCheck.isSuccess()) {
                        currentState = VehicleState.IDLE;
                    } else {
                        currentState = VehicleState.ERROR;
                        result = MaintenanceResult.failure("Post-maintenance checks failed: " + 
                                                         postCheck.getMessage());
                    }
                }
                
                return result;
                
            } catch (Exception e) {
                currentState = VehicleState.ERROR;
                return MaintenanceResult.failure("Maintenance failed: " + e.getMessage());
            }
        });
    }
    
    // Abstract methods to be implemented by concrete transport classes
    protected abstract void initialize();
    protected abstract FuelManager createFuelManager();
    protected abstract NavigationResult navigate(Route route);
    protected abstract TransportResult startJourney(JourneyRequest request);
    protected abstract TransportResult completeJourney(JourneyRequest request);
    protected abstract MaintenanceResult performVehicleSpecificMaintenance(MaintenanceRequest request);
    protected abstract double calculateJourneyCost(Route route);
    protected abstract double getMaxSpeed();
    protected abstract double getCurrentSpeed();
    protected abstract void adjustSpeed(double targetSpeed);
    
    // Concrete methods with default implementations (can be overridden)
    protected PreJourneyCheckResult performPreJourneyChecks(JourneyRequest request) {
        try {
            // Check vehicle state
            if (currentState != VehicleState.IDLE) {
                return PreJourneyCheckResult.failure("Vehicle not in idle state: " + currentState);
            }
            
            // Check fuel/energy levels
            if (!fuelManager.hasSufficientFuel(request.getEstimatedDistance())) {
                return PreJourneyCheckResult.failure("Insufficient fuel for journey");
            }
            
            // Check maintenance status
            if (maintenanceManager.isMaintenanceDue()) {
                return PreJourneyCheckResult.failure("Vehicle maintenance is due");
            }
            
            // Safety system checks
            SafetyCheckResult safetyCheck = safetySystem.performSafetyCheck();
            if (!safetyCheck.isSuccess()) {
                return PreJourneyCheckResult.failure("Safety check failed: " + safetyCheck.getMessage());
            }
            
            // Route validation
            if (!routeManager.isRouteValid(request.getRoute())) {
                return PreJourneyCheckResult.failure("Invalid route provided");
            }
            
            return PreJourneyCheckResult.success("All pre-journey checks passed");
            
        } catch (Exception e) {
            return PreJourneyCheckResult.failure("Pre-journey check error: " + e.getMessage());
        }
    }
    
    protected void performPostJourneyOperations(JourneyRequest request, TransportResult result) {
        try {
            // Update fuel consumption
            fuelManager.updateConsumption(request.getRoute().getDistance());
            
            // Update mileage/usage
            maintenanceManager.updateUsage(request.getRoute().getDistance());
            
            // Log journey
            routeManager.logJourney(request, result);
            
            // Reset to idle state
            currentState = VehicleState.IDLE;
            
        } catch (Exception e) {
            System.err.println("Post-journey operations failed: " + e.getMessage());
        }
    }
    
    protected PostMaintenanceCheckResult performPostMaintenanceChecks() {
        try {
            // Check all systems are operational
            SafetyCheckResult safetyCheck = safetySystem.performSafetyCheck();
            if (!safetyCheck.isSuccess()) {
                return PostMaintenanceCheckResult.failure("Safety check failed: " + safetyCheck.getMessage());
            }
            
            // Check fuel system
            if (!fuelManager.isFuelSystemOperational()) {
                return PostMaintenanceCheckResult.failure("Fuel system not operational");
            }
            
            return PostMaintenanceCheckResult.success("All post-maintenance checks passed");
            
        } catch (Exception e) {
            return PostMaintenanceCheckResult.failure("Post-maintenance check error: " + e.getMessage());
        }
    }
    
    // Common operations
    public void stop() {
        try {
            adjustSpeed(0);
            currentState = VehicleState.IDLE;
        } catch (Exception e) {
            currentState = VehicleState.ERROR;
            throw new TransportException("Failed to stop vehicle: " + e.getMessage());
        }
    }
    
    public void emergencyStop() {
        try {
            safetySystem.activateEmergencyStop();
            adjustSpeed(0);
            currentState = VehicleState.EMERGENCY_STOP;
        } catch (Exception e) {
            currentState = VehicleState.ERROR;
            System.err.println("Emergency stop failed: " + e.getMessage());
        }
    }
    
    public CompletableFuture<RefuelResult> refuel(RefuelRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState == VehicleState.MOVING) {
                    return RefuelResult.failure("Cannot refuel while moving");
                }
                
                return fuelManager.refuel(request);
                
            } catch (Exception e) {
                return RefuelResult.failure("Refuel failed: " + e.getMessage());
            }
        });
    }
    
    // Getters and status methods
    public String getVehicleId() { return vehicleId; }
    public String getVehicleName() { return vehicleName; }
    public TransportType getTransportType() { return transportType; }
    public VehicleState getCurrentState() { return currentState; }
    
    public VehicleStatus getStatus() {
        return new VehicleStatus(
            vehicleId,
            vehicleName,
            transportType,
            currentState,
            getCurrentSpeed(),
            fuelManager.getCurrentFuelLevel(),
            maintenanceManager.getNextMaintenanceDate(),
            LocalDateTime.now()
        );
    }
    
    public RouteManager getRouteManager() { return routeManager; }
    public FuelManager getFuelManager() { return fuelManager; }
    public MaintenanceManager getMaintenanceManager() { return maintenanceManager; }
    public SafetySystem getSafetySystem() { return safetySystem; }
    
    // Configuration
    public boolean supportsFeature(String feature) {
        return configuration.containsKey(feature) && 
               Boolean.TRUE.equals(configuration.get(feature));
    }
    
    public Object getConfigurationValue(String key) {
        return configuration.get(key);
    }
}
