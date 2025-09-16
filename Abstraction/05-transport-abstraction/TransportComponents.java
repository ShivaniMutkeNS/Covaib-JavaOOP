package abstraction.transport;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Supporting components for the Transport abstraction system
 */

// Route Manager - handles route planning and journey logging
class RouteManager {
    private Map<String, Route> routeCache;
    private List<JourneyLog> journeyHistory;
    private ScheduledExecutorService scheduler;
    
    public RouteManager() {
        this.routeCache = new ConcurrentHashMap<>();
        this.journeyHistory = Collections.synchronizedList(new ArrayList<>());
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
    
    public boolean isRouteValid(Route route) {
        if (route == null || route.getOrigin() == null || route.getDestination() == null) {
            return false;
        }
        
        // Check if distance is reasonable
        if (route.getDistance() <= 0 || route.getDistance() > 50000) { // Max 50,000 km
            return false;
        }
        
        // Check route type compatibility
        return route.getRouteType() != null;
    }
    
    public Route optimizeRoute(Route originalRoute, Map<String, Object> preferences) {
        // Simulate route optimization
        Route optimizedRoute = new Route(
            "optimized_" + originalRoute.getRouteId(),
            originalRoute.getOrigin(),
            originalRoute.getDestination(),
            originalRoute.getDistance() * 0.95, // 5% optimization
            originalRoute.getRouteType()
        );
        
        // Set optimized duration
        optimizedRoute.setEstimatedDuration(originalRoute.getEstimatedDuration() * 0.9);
        
        // Cache the optimized route
        routeCache.put(optimizedRoute.getRouteId(), optimizedRoute);
        
        return optimizedRoute;
    }
    
    public void logJourney(JourneyRequest request, TransportResult result) {
        JourneyLog log = new JourneyLog(
            request.getRequestId(),
            request.getRoute(),
            request.getScheduledDeparture(),
            result.getCompletionTime(),
            result.isSuccess(),
            result.getActualDistance(),
            result.getFuelConsumed()
        );
        
        journeyHistory.add(log);
        
        // Keep only last 1000 journeys
        if (journeyHistory.size() > 1000) {
            journeyHistory.remove(0);
        }
    }
    
    public List<JourneyLog> getJourneyHistory(int limit) {
        int size = journeyHistory.size();
        int fromIndex = Math.max(0, size - limit);
        return new ArrayList<>(journeyHistory.subList(fromIndex, size));
    }
    
    public Route getCachedRoute(String routeId) {
        return routeCache.get(routeId);
    }
    
    public void shutdown() {
        scheduler.shutdown();
    }
}

// Journey Log class
class JourneyLog {
    private String journeyId;
    private Route route;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private boolean successful;
    private double actualDistance;
    private double fuelConsumed;
    
    public JourneyLog(String journeyId, Route route, LocalDateTime departureTime,
                     LocalDateTime arrivalTime, boolean successful, double actualDistance,
                     double fuelConsumed) {
        this.journeyId = journeyId;
        this.route = route;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.successful = successful;
        this.actualDistance = actualDistance;
        this.fuelConsumed = fuelConsumed;
    }
    
    // Getters
    public String getJourneyId() { return journeyId; }
    public Route getRoute() { return route; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public boolean isSuccessful() { return successful; }
    public double getActualDistance() { return actualDistance; }
    public double getFuelConsumed() { return fuelConsumed; }
}

// Abstract Fuel Manager
abstract class FuelManager {
    protected FuelType fuelType;
    protected double currentLevel;
    protected double capacity;
    protected double consumptionRate; // per km
    protected List<FuelReading> fuelHistory;
    
    public FuelManager(FuelType fuelType, double capacity, double consumptionRate) {
        this.fuelType = fuelType;
        this.capacity = capacity;
        this.consumptionRate = consumptionRate;
        this.currentLevel = capacity * 0.8; // Start at 80%
        this.fuelHistory = Collections.synchronizedList(new ArrayList<>());
    }
    
    public boolean hasSufficientFuel(double distance) {
        double requiredFuel = distance * consumptionRate;
        return currentLevel >= requiredFuel * 1.1; // 10% buffer
    }
    
    public void updateConsumption(double distance) {
        double consumed = distance * consumptionRate;
        currentLevel = Math.max(0, currentLevel - consumed);
        
        // Log fuel reading
        fuelHistory.add(new FuelReading(LocalDateTime.now(), currentLevel, consumed));
        
        // Keep only last 100 readings
        if (fuelHistory.size() > 100) {
            fuelHistory.remove(0);
        }
    }
    
    public abstract RefuelResult refuel(RefuelRequest request);
    public abstract boolean isFuelSystemOperational();
    
    // Getters
    public FuelType getFuelType() { return fuelType; }
    public double getCurrentFuelLevel() { return currentLevel; }
    public double getCapacity() { return capacity; }
    public double getConsumptionRate() { return consumptionRate; }
    public List<FuelReading> getFuelHistory() { return new ArrayList<>(fuelHistory); }
    
    public double getFuelPercentage() {
        return (currentLevel / capacity) * 100.0;
    }
}

// Fuel Reading class
class FuelReading {
    private LocalDateTime timestamp;
    private double fuelLevel;
    private double consumed;
    
    public FuelReading(LocalDateTime timestamp, double fuelLevel, double consumed) {
        this.timestamp = timestamp;
        this.fuelLevel = fuelLevel;
        this.consumed = consumed;
    }
    
    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getFuelLevel() { return fuelLevel; }
    public double getConsumed() { return consumed; }
}

// Maintenance Manager
class MaintenanceManager {
    private String vehicleId;
    private double totalMileage;
    private LocalDateTime lastMaintenanceDate;
    private LocalDateTime nextMaintenanceDate;
    private List<MaintenanceRecord> maintenanceHistory;
    private Map<MaintenanceType, Double> maintenanceIntervals;
    
    public MaintenanceManager(String vehicleId) {
        this.vehicleId = vehicleId;
        this.totalMileage = 0.0;
        this.lastMaintenanceDate = LocalDateTime.now().minusMonths(1);
        this.maintenanceHistory = Collections.synchronizedList(new ArrayList<>());
        this.maintenanceIntervals = initializeMaintenanceIntervals();
        calculateNextMaintenanceDate();
    }
    
    private Map<MaintenanceType, Double> initializeMaintenanceIntervals() {
        Map<MaintenanceType, Double> intervals = new HashMap<>();
        intervals.put(MaintenanceType.ROUTINE, 5000.0); // Every 5000 km
        intervals.put(MaintenanceType.PREVENTIVE, 10000.0); // Every 10000 km
        intervals.put(MaintenanceType.INSPECTION, 15000.0); // Every 15000 km
        return intervals;
    }
    
    public void updateUsage(double distance) {
        totalMileage += distance;
        calculateNextMaintenanceDate();
    }
    
    public boolean isMaintenanceDue() {
        return LocalDateTime.now().isAfter(nextMaintenanceDate) || 
               isMaintenanceDueByMileage();
    }
    
    private boolean isMaintenanceDueByMileage() {
        if (maintenanceHistory.isEmpty()) {
            return totalMileage >= maintenanceIntervals.get(MaintenanceType.ROUTINE);
        }
        
        MaintenanceRecord lastMaintenance = maintenanceHistory.get(maintenanceHistory.size() - 1);
        double mileageSinceLastMaintenance = totalMileage - lastMaintenance.getMileageAtMaintenance();
        
        return mileageSinceLastMaintenance >= maintenanceIntervals.get(MaintenanceType.ROUTINE);
    }
    
    public void recordMaintenance(MaintenanceRequest request, MaintenanceResult result) {
        MaintenanceRecord record = new MaintenanceRecord(
            request.getRequestId(),
            request.getMaintenanceType(),
            request.getDescription(),
            LocalDateTime.now(),
            totalMileage,
            result.isSuccess(),
            result.getCost()
        );
        
        maintenanceHistory.add(record);
        
        if (result.isSuccess()) {
            lastMaintenanceDate = LocalDateTime.now();
            calculateNextMaintenanceDate();
        }
    }
    
    private void calculateNextMaintenanceDate() {
        // Calculate based on time (3 months) or mileage (5000 km), whichever comes first
        LocalDateTime timeBasedNext = lastMaintenanceDate.plusMonths(3);
        
        double remainingMileage = maintenanceIntervals.get(MaintenanceType.ROUTINE);
        if (!maintenanceHistory.isEmpty()) {
            MaintenanceRecord lastMaintenance = maintenanceHistory.get(maintenanceHistory.size() - 1);
            remainingMileage = maintenanceIntervals.get(MaintenanceType.ROUTINE) - 
                              (totalMileage - lastMaintenance.getMileageAtMileage());
        }
        
        // Estimate when mileage-based maintenance will be due (assuming 1000 km/month)
        LocalDateTime mileageBasedNext = LocalDateTime.now().plusDays((long)(remainingMileage / 33.33)); // ~1000km/month
        
        nextMaintenanceDate = timeBasedNext.isBefore(mileageBasedNext) ? timeBasedNext : mileageBasedNext;
    }
    
    // Getters
    public double getTotalMileage() { return totalMileage; }
    public LocalDateTime getLastMaintenanceDate() { return lastMaintenanceDate; }
    public LocalDateTime getNextMaintenanceDate() { return nextMaintenanceDate; }
    public List<MaintenanceRecord> getMaintenanceHistory() { return new ArrayList<>(maintenanceHistory); }
}

// Maintenance Record class
class MaintenanceRecord {
    private String maintenanceId;
    private MaintenanceType maintenanceType;
    private String description;
    private LocalDateTime maintenanceDate;
    private double mileageAtMaintenance;
    private boolean successful;
    private double cost;
    
    public MaintenanceRecord(String maintenanceId, MaintenanceType maintenanceType,
                           String description, LocalDateTime maintenanceDate,
                           double mileageAtMaintenance, boolean successful, double cost) {
        this.maintenanceId = maintenanceId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.maintenanceDate = maintenanceDate;
        this.mileageAtMaintenance = mileageAtMaintenance;
        this.successful = successful;
        this.cost = cost;
    }
    
    // Getters
    public String getMaintenanceId() { return maintenanceId; }
    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public String getDescription() { return description; }
    public LocalDateTime getMaintenanceDate() { return maintenanceDate; }
    public double getMileageAtMaintenance() { return mileageAtMaintenance; }
    public double getMileageAtMileage() { return mileageAtMaintenance; }
    public boolean isSuccessful() { return successful; }
    public double getCost() { return cost; }
}

// Safety System
class SafetySystem {
    private TransportType transportType;
    private boolean emergencyStopActive;
    private List<SafetyAlert> activeAlerts;
    private Map<String, Boolean> safetyChecks;
    
    public SafetySystem(TransportType transportType) {
        this.transportType = transportType;
        this.emergencyStopActive = false;
        this.activeAlerts = Collections.synchronizedList(new ArrayList<>());
        this.safetyChecks = initializeSafetyChecks();
    }
    
    private Map<String, Boolean> initializeSafetyChecks() {
        Map<String, Boolean> checks = new HashMap<>();
        checks.put("brakes", true);
        checks.put("lights", true);
        checks.put("engine", true);
        checks.put("fuel_system", true);
        checks.put("navigation", true);
        checks.put("communication", true);
        
        // Transport-specific checks
        switch (transportType) {
            case ROAD_VEHICLE:
                checks.put("tires", true);
                checks.put("steering", true);
                break;
            case AIRCRAFT:
                checks.put("wings", true);
                checks.put("landing_gear", true);
                checks.put("pressurization", true);
                break;
            case WATERCRAFT:
                checks.put("hull_integrity", true);
                checks.put("life_jackets", true);
                break;
            case RAIL_VEHICLE:
                checks.put("coupling", true);
                checks.put("track_sensors", true);
                break;
            case SPACECRAFT:
                checks.put("life_support", true);
                checks.put("heat_shield", true);
                break;
        }
        
        return checks;
    }
    
    public SafetyCheckResult performSafetyCheck() {
        List<String> issues = new ArrayList<>();
        
        for (Map.Entry<String, Boolean> check : safetyChecks.entrySet()) {
            if (!check.getValue()) {
                issues.add(check.getKey() + " system failure");
            }
        }
        
        // Simulate random safety issues (5% chance)
        if (Math.random() < 0.05) {
            String[] possibleIssues = {"low_battery", "sensor_malfunction", "minor_leak"};
            issues.add(possibleIssues[(int)(Math.random() * possibleIssues.length)]);
        }
        
        if (issues.isEmpty()) {
            return SafetyCheckResult.success("All safety systems operational");
        } else {
            SafetyCheckResult result = SafetyCheckResult.failure("Safety issues detected");
            result.setIssues(issues);
            return result;
        }
    }
    
    public void activateEmergencyStop() {
        emergencyStopActive = true;
        SafetyAlert alert = new SafetyAlert(
            "EMERGENCY_STOP",
            "Emergency stop activated",
            SafetyAlert.Severity.CRITICAL,
            LocalDateTime.now()
        );
        activeAlerts.add(alert);
    }
    
    public void deactivateEmergencyStop() {
        emergencyStopActive = false;
        // Remove emergency stop alerts
        activeAlerts.removeIf(alert -> "EMERGENCY_STOP".equals(alert.getAlertType()));
    }
    
    public boolean isMaintenanceSafe() {
        return !emergencyStopActive && 
               safetyChecks.get("engine") && 
               safetyChecks.get("fuel_system");
    }
    
    public void updateSafetyStatus(String system, boolean status) {
        safetyChecks.put(system, status);
        
        if (!status) {
            SafetyAlert alert = new SafetyAlert(
                "SYSTEM_FAILURE",
                system + " system failure detected",
                SafetyAlert.Severity.HIGH,
                LocalDateTime.now()
            );
            activeAlerts.add(alert);
        }
    }
    
    // Getters
    public boolean isEmergencyStopActive() { return emergencyStopActive; }
    public List<SafetyAlert> getActiveAlerts() { return new ArrayList<>(activeAlerts); }
    public Map<String, Boolean> getSafetyChecks() { return new HashMap<>(safetyChecks); }
}

// Safety Alert class
class SafetyAlert {
    private String alertType;
    private String message;
    private Severity severity;
    private LocalDateTime timestamp;
    
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public SafetyAlert(String alertType, String message, Severity severity, LocalDateTime timestamp) {
        this.alertType = alertType;
        this.message = message;
        this.severity = severity;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getAlertType() { return alertType; }
    public String getMessage() { return message; }
    public Severity getSeverity() { return severity; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
