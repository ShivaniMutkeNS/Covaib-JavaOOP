package abstraction.transport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data models and enums for the Transport abstraction system
 */

// Transport type enumeration
enum TransportType {
    ROAD_VEHICLE, AIRCRAFT, WATERCRAFT, RAIL_VEHICLE, SPACECRAFT
}

// Vehicle state enumeration
enum VehicleState {
    IDLE, MOVING, MAINTENANCE, REFUELING, ERROR, EMERGENCY_STOP
}

// Journey request class
class JourneyRequest {
    private String requestId;
    private Route route;
    private int passengerCount;
    private double cargoWeight;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private Map<String, Object> specialRequirements;
    private String requestedBy;
    
    public JourneyRequest(String requestId, Route route, int passengerCount, 
                         double cargoWeight, LocalDateTime scheduledDeparture, 
                         String requestedBy) {
        this.requestId = requestId;
        this.route = route;
        this.passengerCount = passengerCount;
        this.cargoWeight = cargoWeight;
        this.scheduledDeparture = scheduledDeparture;
        this.requestedBy = requestedBy;
    }
    
    public double getEstimatedDistance() {
        return route != null ? route.getDistance() : 0.0;
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public Route getRoute() { return route; }
    public int getPassengerCount() { return passengerCount; }
    public double getCargoWeight() { return cargoWeight; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getScheduledArrival() { return scheduledArrival; }
    public Map<String, Object> getSpecialRequirements() { return specialRequirements; }
    public String getRequestedBy() { return requestedBy; }
    
    // Setters
    public void setScheduledArrival(LocalDateTime scheduledArrival) { this.scheduledArrival = scheduledArrival; }
    public void setSpecialRequirements(Map<String, Object> specialRequirements) { this.specialRequirements = specialRequirements; }
}

// Route class
class Route {
    private String routeId;
    private Location origin;
    private Location destination;
    private List<Location> waypoints;
    private double distance;
    private double estimatedDuration;
    private RouteType routeType;
    private Map<String, Object> routeConditions;
    
    public Route(String routeId, Location origin, Location destination, 
                double distance, RouteType routeType) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.routeType = routeType;
    }
    
    // Getters
    public String getRouteId() { return routeId; }
    public Location getOrigin() { return origin; }
    public Location getDestination() { return destination; }
    public List<Location> getWaypoints() { return waypoints; }
    public double getDistance() { return distance; }
    public double getEstimatedDuration() { return estimatedDuration; }
    public RouteType getRouteType() { return routeType; }
    public Map<String, Object> getRouteConditions() { return routeConditions; }
    
    // Setters
    public void setWaypoints(List<Location> waypoints) { this.waypoints = waypoints; }
    public void setEstimatedDuration(double estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public void setRouteConditions(Map<String, Object> routeConditions) { this.routeConditions = routeConditions; }
}

// Location class
class Location {
    private double latitude;
    private double longitude;
    private double altitude;
    private String name;
    private String address;
    
    public Location(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.altitude = 0.0;
    }
    
    public Location(double latitude, double longitude, double altitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.name = name;
    }
    
    // Getters
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getAltitude() { return altitude; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    
    // Setters
    public void setAddress(String address) { this.address = address; }
    
    @Override
    public String toString() {
        return name + " (" + latitude + ", " + longitude + 
               (altitude != 0.0 ? ", " + altitude + "m" : "") + ")";
    }
}

// Route type enumeration
enum RouteType {
    HIGHWAY, CITY_ROADS, AIRWAYS, WATERWAYS, RAILWAY, SPACE_ROUTE
}

// Transport result class
class TransportResult {
    private boolean success;
    private String message;
    private LocalDateTime completionTime;
    private double actualDistance;
    private double actualDuration;
    private double fuelConsumed;
    private Map<String, Object> journeyData;
    
    private TransportResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.completionTime = LocalDateTime.now();
    }
    
    public static TransportResult success(String message) {
        return new TransportResult(true, message);
    }
    
    public static TransportResult failure(String message) {
        return new TransportResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public double getActualDistance() { return actualDistance; }
    public double getActualDuration() { return actualDuration; }
    public double getFuelConsumed() { return fuelConsumed; }
    public Map<String, Object> getJourneyData() { return journeyData; }
    
    // Setters
    public void setActualDistance(double actualDistance) { this.actualDistance = actualDistance; }
    public void setActualDuration(double actualDuration) { this.actualDuration = actualDuration; }
    public void setFuelConsumed(double fuelConsumed) { this.fuelConsumed = fuelConsumed; }
    public void setJourneyData(Map<String, Object> journeyData) { this.journeyData = journeyData; }
}

// Navigation result class
class NavigationResult {
    private boolean success;
    private String message;
    private double distanceCovered;
    private LocalDateTime navigationTime;
    
    private NavigationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.navigationTime = LocalDateTime.now();
    }
    
    public static NavigationResult success(String message, double distanceCovered) {
        NavigationResult result = new NavigationResult(true, message);
        result.distanceCovered = distanceCovered;
        return result;
    }
    
    public static NavigationResult failure(String message) {
        return new NavigationResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getDistanceCovered() { return distanceCovered; }
    public LocalDateTime getNavigationTime() { return navigationTime; }
}

// Maintenance request class
class MaintenanceRequest {
    private String requestId;
    private MaintenanceType maintenanceType;
    private String description;
    private boolean isEmergency;
    private LocalDateTime scheduledTime;
    private String requestedBy;
    private Map<String, Object> maintenanceDetails;
    
    public MaintenanceRequest(String requestId, MaintenanceType maintenanceType, 
                            String description, String requestedBy) {
        this.requestId = requestId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.requestedBy = requestedBy;
        this.isEmergency = false;
        this.scheduledTime = LocalDateTime.now();
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public String getDescription() { return description; }
    public boolean isEmergency() { return isEmergency; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public String getRequestedBy() { return requestedBy; }
    public Map<String, Object> getMaintenanceDetails() { return maintenanceDetails; }
    
    // Setters
    public void setEmergency(boolean emergency) { isEmergency = emergency; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public void setMaintenanceDetails(Map<String, Object> maintenanceDetails) { this.maintenanceDetails = maintenanceDetails; }
}

// Maintenance type enumeration
enum MaintenanceType {
    ROUTINE, PREVENTIVE, CORRECTIVE, EMERGENCY, INSPECTION
}

// Maintenance result class
class MaintenanceResult {
    private boolean success;
    private String message;
    private LocalDateTime completionTime;
    private double cost;
    private List<String> workPerformed;
    private LocalDateTime nextMaintenanceDate;
    
    private MaintenanceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.completionTime = LocalDateTime.now();
    }
    
    public static MaintenanceResult success(String message) {
        return new MaintenanceResult(true, message);
    }
    
    public static MaintenanceResult failure(String message) {
        return new MaintenanceResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public double getCost() { return cost; }
    public List<String> getWorkPerformed() { return workPerformed; }
    public LocalDateTime getNextMaintenanceDate() { return nextMaintenanceDate; }
    
    // Setters
    public void setCost(double cost) { this.cost = cost; }
    public void setWorkPerformed(List<String> workPerformed) { this.workPerformed = workPerformed; }
    public void setNextMaintenanceDate(LocalDateTime nextMaintenanceDate) { this.nextMaintenanceDate = nextMaintenanceDate; }
}

// Refuel request class
class RefuelRequest {
    private String requestId;
    private FuelType fuelType;
    private double requestedAmount;
    private String fuelStationId;
    private String requestedBy;
    
    public RefuelRequest(String requestId, FuelType fuelType, double requestedAmount, 
                        String fuelStationId, String requestedBy) {
        this.requestId = requestId;
        this.fuelType = fuelType;
        this.requestedAmount = requestedAmount;
        this.fuelStationId = fuelStationId;
        this.requestedBy = requestedBy;
    }
    
    // Getters
    public String getRequestId() { return requestId; }
    public FuelType getFuelType() { return fuelType; }
    public double getRequestedAmount() { return requestedAmount; }
    public String getFuelStationId() { return fuelStationId; }
    public String getRequestedBy() { return requestedBy; }
}

// Fuel type enumeration
enum FuelType {
    GASOLINE, DIESEL, ELECTRIC, HYBRID, HYDROGEN, JET_FUEL, ROCKET_FUEL
}

// Refuel result class
class RefuelResult {
    private boolean success;
    private String message;
    private double actualAmount;
    private double cost;
    private LocalDateTime refuelTime;
    
    private RefuelResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.refuelTime = LocalDateTime.now();
    }
    
    public static RefuelResult success(String message, double actualAmount, double cost) {
        RefuelResult result = new RefuelResult(true, message);
        result.actualAmount = actualAmount;
        result.cost = cost;
        return result;
    }
    
    public static RefuelResult failure(String message) {
        return new RefuelResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getActualAmount() { return actualAmount; }
    public double getCost() { return cost; }
    public LocalDateTime getRefuelTime() { return refuelTime; }
}

// Vehicle status class
class VehicleStatus {
    private String vehicleId;
    private String vehicleName;
    private TransportType transportType;
    private VehicleState currentState;
    private double currentSpeed;
    private double fuelLevel;
    private LocalDateTime nextMaintenanceDate;
    private LocalDateTime statusTime;
    
    public VehicleStatus(String vehicleId, String vehicleName, TransportType transportType,
                        VehicleState currentState, double currentSpeed, double fuelLevel,
                        LocalDateTime nextMaintenanceDate, LocalDateTime statusTime) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.transportType = transportType;
        this.currentState = currentState;
        this.currentSpeed = currentSpeed;
        this.fuelLevel = fuelLevel;
        this.nextMaintenanceDate = nextMaintenanceDate;
        this.statusTime = statusTime;
    }
    
    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getVehicleName() { return vehicleName; }
    public TransportType getTransportType() { return transportType; }
    public VehicleState getCurrentState() { return currentState; }
    public double getCurrentSpeed() { return currentSpeed; }
    public double getFuelLevel() { return fuelLevel; }
    public LocalDateTime getNextMaintenanceDate() { return nextMaintenanceDate; }
    public LocalDateTime getStatusTime() { return statusTime; }
}

// Check result classes
class PreJourneyCheckResult {
    private boolean success;
    private String message;
    
    private PreJourneyCheckResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static PreJourneyCheckResult success(String message) {
        return new PreJourneyCheckResult(true, message);
    }
    
    public static PreJourneyCheckResult failure(String message) {
        return new PreJourneyCheckResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

class PostMaintenanceCheckResult {
    private boolean success;
    private String message;
    
    private PostMaintenanceCheckResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static PostMaintenanceCheckResult success(String message) {
        return new PostMaintenanceCheckResult(true, message);
    }
    
    public static PostMaintenanceCheckResult failure(String message) {
        return new PostMaintenanceCheckResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

class SafetyCheckResult {
    private boolean success;
    private String message;
    private List<String> issues;
    
    private SafetyCheckResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static SafetyCheckResult success(String message) {
        return new SafetyCheckResult(true, message);
    }
    
    public static SafetyCheckResult failure(String message) {
        return new SafetyCheckResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }
}

// Custom exception for transport operations
class TransportException extends RuntimeException {
    public TransportException(String message) {
        super(message);
    }
    
    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
