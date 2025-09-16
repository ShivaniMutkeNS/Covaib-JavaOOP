package abstraction.transport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Airplane implementation of Transport - represents aircraft
 */
public class Airplane extends Transport {
    
    private double currentSpeed;
    private double maxSpeed;
    private double currentAltitude;
    private double cruisingAltitude;
    private double maxAltitude;
    private FlightManagementSystem fms;
    private AirTrafficControl atc;
    private WeatherSystem weatherSystem;
    private FlightPhase currentPhase;
    
    public Airplane(String vehicleId, String vehicleName, Map<String, Object> configuration) {
        super(vehicleId, vehicleName, TransportType.AIRCRAFT, configuration);
        
        this.maxSpeed = (Double) configuration.getOrDefault("max_speed", 900.0);
        this.cruisingAltitude = (Double) configuration.getOrDefault("cruising_altitude", 35000.0);
        this.maxAltitude = (Double) configuration.getOrDefault("max_altitude", 42000.0);
        this.currentSpeed = 0.0;
        this.currentAltitude = 0.0;
        this.currentPhase = FlightPhase.ON_GROUND;
        
        this.fms = new FlightManagementSystem();
        this.atc = new AirTrafficControl();
        this.weatherSystem = new WeatherSystem();
    }
    
    @Override
    protected void initialize() {
        System.out.println("Initializing Airplane: " + vehicleName);
        
        // Initialize aircraft-specific systems
        fms.initialize();
        atc.connect();
        weatherSystem.connect();
        
        // Perform pre-flight checks
        performPreFlightChecks();
        
        System.out.println("Airplane initialization complete");
    }
    
    @Override
    protected FuelManager createFuelManager() {
        FuelType fuelType = FuelType.JET_FUEL;
        double tankCapacity = (Double) configuration.getOrDefault("fuel_capacity", 50000.0);
        double consumptionRate = (Double) configuration.getOrDefault("consumption_rate", 3.5);
        
        return new AirplaneFuelManager(fuelType, tankCapacity, consumptionRate);
    }
    
    @Override
    protected NavigationResult navigate(Route route) {
        try {
            System.out.println("Starting airplane navigation from " + route.getOrigin().getName() + 
                             " to " + route.getDestination().getName());
            
            // Flight phases: Taxi -> Takeoff -> Climb -> Cruise -> Descent -> Landing -> Taxi
            double totalDistance = 0.0;
            
            // Phase 1: Taxi to runway
            NavigationResult taxiResult = performTaxi("TO_RUNWAY");
            if (!taxiResult.isSuccess()) {
                return taxiResult;
            }
            
            // Phase 2: Takeoff
            NavigationResult takeoffResult = performTakeoff();
            if (!takeoffResult.isSuccess()) {
                return takeoffResult;
            }
            
            // Phase 3: Climb to cruising altitude
            NavigationResult climbResult = performClimb();
            if (!climbResult.isSuccess()) {
                return climbResult;
            }
            
            // Phase 4: Cruise (main navigation)
            NavigationResult cruiseResult = performCruise(route);
            if (!cruiseResult.isSuccess()) {
                return cruiseResult;
            }
            totalDistance += cruiseResult.getDistanceCovered();
            
            // Phase 5: Descent
            NavigationResult descentResult = performDescent();
            if (!descentResult.isSuccess()) {
                return descentResult;
            }
            
            // Phase 6: Landing
            NavigationResult landingResult = performLanding();
            if (!landingResult.isSuccess()) {
                return landingResult;
            }
            
            // Phase 7: Taxi to gate
            NavigationResult taxiToGateResult = performTaxi("TO_GATE");
            if (!taxiToGateResult.isSuccess()) {
                return taxiToGateResult;
            }
            
            currentPhase = FlightPhase.ON_GROUND;
            currentSpeed = 0.0;
            currentAltitude = 0.0;
            
            return NavigationResult.success("Flight completed successfully", totalDistance);
            
        } catch (Exception e) {
            return NavigationResult.failure("Flight navigation failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performTaxi(String direction) {
        try {
            currentPhase = FlightPhase.TAXI;
            System.out.println("Taxiing " + direction.toLowerCase().replace("_", " "));
            
            // Get taxi clearance from ATC
            if (!atc.requestTaxiClearance(direction)) {
                return NavigationResult.failure("Taxi clearance denied by ATC");
            }
            
            adjustSpeed(20.0); // Taxi speed
            Thread.sleep(2000); // Simulate taxi time
            
            return NavigationResult.success("Taxi completed", 2.0);
            
        } catch (Exception e) {
            return NavigationResult.failure("Taxi failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performTakeoff() {
        try {
            currentPhase = FlightPhase.TAKEOFF;
            System.out.println("Initiating takeoff sequence");
            
            // Get takeoff clearance
            if (!atc.requestTakeoffClearance()) {
                return NavigationResult.failure("Takeoff clearance denied by ATC");
            }
            
            // Check weather conditions
            WeatherConditions weather = weatherSystem.getCurrentWeather();
            if (!weather.isSuitableForTakeoff()) {
                return NavigationResult.failure("Weather conditions unsuitable for takeoff");
            }
            
            // Accelerate to takeoff speed
            double takeoffSpeed = maxSpeed * 0.4; // Typical takeoff speed
            adjustSpeed(takeoffSpeed);
            
            // Simulate takeoff roll
            Thread.sleep(3000);
            
            // Lift off
            currentAltitude = 100.0;
            System.out.println("Aircraft airborne");
            
            return NavigationResult.success("Takeoff completed", 5.0);
            
        } catch (Exception e) {
            return NavigationResult.failure("Takeoff failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performClimb() {
        try {
            currentPhase = FlightPhase.CLIMB;
            System.out.println("Climbing to cruising altitude: " + cruisingAltitude + " ft");
            
            // Gradual climb to cruising altitude
            double climbRate = 2000.0; // feet per minute
            double climbTime = (cruisingAltitude - currentAltitude) / climbRate;
            
            while (currentAltitude < cruisingAltitude) {
                currentAltitude += climbRate / 6; // Simulate climb in 10-second intervals
                adjustSpeed(Math.min(currentSpeed + 50, maxSpeed * 0.8));
                
                Thread.sleep(100); // 100ms represents 10 seconds
                
                // Safety check
                if (safetySystem.isEmergencyStopActive()) {
                    return NavigationResult.failure("Emergency during climb");
                }
            }
            
            currentAltitude = cruisingAltitude;
            System.out.println("Reached cruising altitude: " + cruisingAltitude + " ft");
            
            return NavigationResult.success("Climb completed", climbTime * currentSpeed / 60);
            
        } catch (Exception e) {
            return NavigationResult.failure("Climb failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performCruise(Route route) {
        try {
            currentPhase = FlightPhase.CRUISE;
            System.out.println("Cruising at " + cruisingAltitude + " ft");
            
            // Set cruise speed
            adjustSpeed(maxSpeed * 0.85); // Typical cruise speed
            
            // Navigate the main route distance
            double totalDistance = 0.0;
            double remainingDistance = route.getDistance();
            
            // Update FMS with route
            fms.setFlightPlan(route);
            
            while (remainingDistance > 0) {
                // Check weather conditions
                WeatherConditions weather = weatherSystem.getCurrentWeather();
                if (weather.requiresRouteDeviation()) {
                    // Request route deviation from ATC
                    if (atc.requestRouteDeviation()) {
                        remainingDistance *= 1.1; // 10% longer route
                        System.out.println("Route deviation approved due to weather");
                    }
                }
                
                // Fly for 1 minute segments
                double distanceThisSegment = Math.min(remainingDistance, currentSpeed / 60.0);
                totalDistance += distanceThisSegment;
                remainingDistance -= distanceThisSegment;
                
                // Update FMS progress
                fms.updateProgress(totalDistance / route.getDistance());
                
                Thread.sleep(50); // 50ms represents 1 minute of flight
                
                // Safety and fuel checks
                if (safetySystem.isEmergencyStopActive()) {
                    return NavigationResult.failure("Emergency during cruise");
                }
                
                if (!fuelManager.hasSufficientFuel(remainingDistance)) {
                    return NavigationResult.failure("Insufficient fuel for remaining distance");
                }
            }
            
            return NavigationResult.success("Cruise phase completed", totalDistance);
            
        } catch (Exception e) {
            return NavigationResult.failure("Cruise failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performDescent() {
        try {
            currentPhase = FlightPhase.DESCENT;
            System.out.println("Beginning descent");
            
            // Get descent clearance
            if (!atc.requestDescentClearance()) {
                return NavigationResult.failure("Descent clearance denied by ATC");
            }
            
            // Gradual descent
            double descentRate = 1500.0; // feet per minute
            
            while (currentAltitude > 3000.0) { // Descent to approach altitude
                currentAltitude -= descentRate / 6; // 10-second intervals
                adjustSpeed(Math.max(currentSpeed - 30, maxSpeed * 0.4));
                
                Thread.sleep(100);
                
                if (safetySystem.isEmergencyStopActive()) {
                    return NavigationResult.failure("Emergency during descent");
                }
            }
            
            currentAltitude = 3000.0;
            System.out.println("Descent completed, ready for approach");
            
            return NavigationResult.success("Descent completed", 50.0);
            
        } catch (Exception e) {
            return NavigationResult.failure("Descent failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performLanding() {
        try {
            currentPhase = FlightPhase.LANDING;
            System.out.println("Initiating landing sequence");
            
            // Get landing clearance
            if (!atc.requestLandingClearance()) {
                return NavigationResult.failure("Landing clearance denied by ATC");
            }
            
            // Check weather for landing
            WeatherConditions weather = weatherSystem.getCurrentWeather();
            if (!weather.isSuitableForLanding()) {
                return NavigationResult.failure("Weather conditions unsuitable for landing");
            }
            
            // Final approach
            while (currentAltitude > 0) {
                currentAltitude -= 500; // Rapid descent on approach
                adjustSpeed(Math.max(currentSpeed - 20, maxSpeed * 0.3));
                
                Thread.sleep(200);
                
                if (currentAltitude <= 0) {
                    currentAltitude = 0;
                    break;
                }
            }
            
            // Touchdown and rollout
            adjustSpeed(20.0); // Taxi speed after landing
            System.out.println("Aircraft landed successfully");
            
            return NavigationResult.success("Landing completed", 8.0);
            
        } catch (Exception e) {
            return NavigationResult.failure("Landing failed: " + e.getMessage());
        }
    }
    
    @Override
    protected TransportResult startJourney(JourneyRequest request) {
        try {
            System.out.println("Starting airplane journey: " + request.getRequestId());
            
            // Start engines
            if (!startEngines()) {
                return TransportResult.failure("Failed to start engines");
            }
            
            // Final pre-flight checks
            if (!performFinalPreFlightChecks()) {
                return TransportResult.failure("Pre-flight checks failed");
            }
            
            currentState = VehicleState.MOVING;
            currentPhase = FlightPhase.ON_GROUND;
            
            double estimatedCost = calculateJourneyCost(request.getRoute());
            
            TransportResult result = TransportResult.success("Flight journey started");
            Map<String, Object> journeyData = new HashMap<>();
            journeyData.put("estimated_cost", estimatedCost);
            journeyData.put("fuel_at_start", fuelManager.getCurrentFuelLevel());
            journeyData.put("cruising_altitude", cruisingAltitude);
            journeyData.put("flight_time_estimate", request.getRoute().getDistance() / (maxSpeed * 0.85));
            result.setJourneyData(journeyData);
            
            return result;
            
        } catch (Exception e) {
            return TransportResult.failure("Failed to start flight: " + e.getMessage());
        }
    }
    
    @Override
    protected TransportResult completeJourney(JourneyRequest request) {
        try {
            System.out.println("Completing airplane journey: " + request.getRequestId());
            
            // Shutdown engines
            shutdownEngines();
            
            currentState = VehicleState.IDLE;
            currentPhase = FlightPhase.ON_GROUND;
            
            TransportResult result = TransportResult.success("Flight completed successfully");
            result.setActualDistance(request.getRoute().getDistance());
            result.setFuelConsumed(request.getRoute().getDistance() * fuelManager.getConsumptionRate());
            
            // Calculate flight time (including taxi, takeoff, climb, cruise, descent, landing)
            double cruiseTime = request.getRoute().getDistance() / (maxSpeed * 0.85);
            double totalFlightTime = cruiseTime + 0.5; // Add 30 minutes for other phases
            result.setActualDuration(totalFlightTime);
            
            return result;
            
        } catch (Exception e) {
            return TransportResult.failure("Failed to complete flight: " + e.getMessage());
        }
    }
    
    @Override
    protected MaintenanceResult performVehicleSpecificMaintenance(MaintenanceRequest request) {
        try {
            System.out.println("Performing aircraft maintenance: " + request.getDescription());
            
            MaintenanceResult result = MaintenanceResult.success("Aircraft maintenance completed");
            
            switch (request.getMaintenanceType()) {
                case ROUTINE:
                    performRoutineAircraftMaintenance(result);
                    break;
                case PREVENTIVE:
                    performPreventiveAircraftMaintenance(result);
                    break;
                case CORRECTIVE:
                    performCorrectiveAircraftMaintenance(request, result);
                    break;
                case EMERGENCY:
                    performEmergencyAircraftMaintenance(request, result);
                    break;
                case INSPECTION:
                    performAircraftInspection(result);
                    break;
            }
            
            return result;
            
        } catch (Exception e) {
            return MaintenanceResult.failure("Aircraft maintenance failed: " + e.getMessage());
        }
    }
    
    private void performRoutineAircraftMaintenance(MaintenanceResult result) {
        String[] tasks = {
            "Engine inspection", "Hydraulic system check", "Avionics test",
            "Landing gear inspection", "Fuel system check", "Flight control test"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(15000.0 + Math.random() * 10000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusDays(30));
    }
    
    private void performPreventiveAircraftMaintenance(MaintenanceResult result) {
        String[] tasks = {
            "Engine overhaul", "Structural inspection", "Avionics upgrade",
            "Interior refurbishment", "Safety equipment check", "Navigation system calibration"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(50000.0 + Math.random() * 30000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusMonths(6));
    }
    
    private void performCorrectiveAircraftMaintenance(MaintenanceRequest request, MaintenanceResult result) {
        String[] tasks = {
            "Repair: " + request.getDescription(),
            "Component replacement", "System recertification", "Test flight preparation"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(25000.0 + Math.random() * 40000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusDays(15));
    }
    
    private void performEmergencyAircraftMaintenance(MaintenanceRequest request, MaintenanceResult result) {
        String[] tasks = {
            "Emergency repair: " + request.getDescription(),
            "Critical system check", "Safety certification", "Immediate airworthiness test"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(75000.0 + Math.random() * 50000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusDays(7));
    }
    
    private void performAircraftInspection(MaintenanceResult result) {
        String[] tasks = {
            "Annual inspection", "Airworthiness certification", "Documentation review",
            "Compliance verification", "Performance test", "Safety audit"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(20000.0 + Math.random() * 15000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusYears(1));
    }
    
    @Override
    protected double calculateJourneyCost(Route route) {
        double fuelCost = route.getDistance() * fuelManager.getConsumptionRate() * 0.8; // $0.8 per liter jet fuel
        double airportFees = 2000.0; // Landing and takeoff fees
        double airspaceFees = route.getDistance() * 0.15; // $0.15 per km
        double crewCost = route.getDistance() / (maxSpeed * 0.85) * 500.0; // $500 per hour
        double maintenanceCost = route.getDistance() * 0.25; // $0.25 per km
        
        return fuelCost + airportFees + airspaceFees + crewCost + maintenanceCost;
    }
    
    @Override
    protected double getMaxSpeed() {
        return maxSpeed;
    }
    
    @Override
    protected double getCurrentSpeed() {
        return currentSpeed;
    }
    
    @Override
    protected void adjustSpeed(double targetSpeed) {
        targetSpeed = Math.max(0, Math.min(targetSpeed, maxSpeed));
        currentSpeed = targetSpeed;
    }
    
    private boolean startEngines() {
        try {
            // Check fuel
            if (fuelManager.getCurrentFuelLevel() <= fuelManager.getCapacity() * 0.1) {
                return false;
            }
            
            // Simulate engine start sequence
            System.out.println("Starting engines...");
            Thread.sleep(5000); // Engine start time
            
            // Check engine systems
            if (Math.random() < 0.01) { // 1% chance of engine failure
                return false;
            }
            
            System.out.println("All engines started successfully");
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private void shutdownEngines() {
        currentSpeed = 0.0;
        System.out.println("Engines shut down");
    }
    
    private void performPreFlightChecks() {
        System.out.println("Performing pre-flight checks...");
        
        // Check aircraft systems
        safetySystem.updateSafetyStatus("engine", Math.random() > 0.02);
        safetySystem.updateSafetyStatus("navigation", Math.random() > 0.01);
        safetySystem.updateSafetyStatus("communication", Math.random() > 0.01);
        safetySystem.updateSafetyStatus("fuel_system", Math.random() > 0.01);
        safetySystem.updateSafetyStatus("pressurization", Math.random() > 0.02);
        safetySystem.updateSafetyStatus("landing_gear", Math.random() > 0.01);
        
        System.out.println("Pre-flight checks complete");
    }
    
    private boolean performFinalPreFlightChecks() {
        // Final checks before departure
        SafetyCheckResult safetyCheck = safetySystem.performSafetyCheck();
        if (!safetyCheck.isSuccess()) {
            return false;
        }
        
        // Check weather
        WeatherConditions weather = weatherSystem.getCurrentWeather();
        if (!weather.isSuitableForFlight()) {
            return false;
        }
        
        // Check ATC clearance
        return atc.isConnected();
    }
    
    // Aircraft-specific getters
    public double getCurrentAltitude() { return currentAltitude; }
    public double getCruisingAltitude() { return cruisingAltitude; }
    public FlightPhase getCurrentPhase() { return currentPhase; }
    public FlightManagementSystem getFms() { return fms; }
    public AirTrafficControl getAtc() { return atc; }
    public WeatherSystem getWeatherSystem() { return weatherSystem; }
}

// Flight phase enumeration
enum FlightPhase {
    ON_GROUND, TAXI, TAKEOFF, CLIMB, CRUISE, DESCENT, LANDING
}

// Airplane-specific Fuel Manager
class AirplaneFuelManager extends FuelManager {
    
    public AirplaneFuelManager(FuelType fuelType, double capacity, double consumptionRate) {
        super(fuelType, capacity, consumptionRate);
    }
    
    @Override
    public RefuelResult refuel(RefuelRequest request) {
        try {
            if (!request.getFuelType().equals(FuelType.JET_FUEL)) {
                return RefuelResult.failure("Aircraft requires jet fuel only");
            }
            
            double availableSpace = capacity - currentLevel;
            double actualAmount = Math.min(request.getRequestedAmount(), availableSpace);
            
            if (actualAmount <= 0) {
                return RefuelResult.failure("Fuel tanks are already full");
            }
            
            // Simulate aircraft refueling (slower than cars)
            try {
                Thread.sleep((long)(actualAmount / 10)); // 1ms per 10 liters
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return RefuelResult.failure("Refueling interrupted");
            }
            
            currentLevel += actualAmount;
            double cost = actualAmount * 0.8; // $0.8 per liter jet fuel
            
            return RefuelResult.success("Aircraft refueling completed", actualAmount, cost);
            
        } catch (Exception e) {
            return RefuelResult.failure("Aircraft refueling failed: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isFuelSystemOperational() {
        // Check fuel pumps, fuel distribution system, etc.
        return currentLevel >= 0 && Math.random() > 0.005; // 0.5% chance of fuel system failure
    }
}

// Flight Management System
class FlightManagementSystem {
    private Route flightPlan;
    private double progress;
    private boolean isInitialized;
    
    public void initialize() {
        isInitialized = true;
        progress = 0.0;
        System.out.println("Flight Management System initialized");
    }
    
    public void setFlightPlan(Route route) {
        this.flightPlan = route;
        this.progress = 0.0;
    }
    
    public void updateProgress(double newProgress) {
        this.progress = Math.max(0.0, Math.min(1.0, newProgress));
    }
    
    public double getProgress() { return progress; }
    public Route getFlightPlan() { return flightPlan; }
    public boolean isInitialized() { return isInitialized; }
}

// Air Traffic Control
class AirTrafficControl {
    private boolean isConnected;
    
    public void connect() {
        isConnected = Math.random() > 0.02; // 98% success rate
        System.out.println("ATC " + (isConnected ? "connected" : "connection failed"));
    }
    
    public boolean requestTaxiClearance(String direction) {
        return isConnected && Math.random() > 0.05; // 95% approval rate
    }
    
    public boolean requestTakeoffClearance() {
        return isConnected && Math.random() > 0.1; // 90% approval rate
    }
    
    public boolean requestLandingClearance() {
        return isConnected && Math.random() > 0.05; // 95% approval rate
    }
    
    public boolean requestDescentClearance() {
        return isConnected && Math.random() > 0.03; // 97% approval rate
    }
    
    public boolean requestRouteDeviation() {
        return isConnected && Math.random() > 0.2; // 80% approval rate
    }
    
    public boolean isConnected() { return isConnected; }
}

// Weather System
class WeatherSystem {
    private boolean isConnected;
    
    public void connect() {
        isConnected = Math.random() > 0.05; // 95% success rate
        System.out.println("Weather system " + (isConnected ? "connected" : "connection failed"));
    }
    
    public WeatherConditions getCurrentWeather() {
        if (!isConnected) {
            return new WeatherConditions(false, false, false, "No weather data available");
        }
        
        // Simulate weather conditions
        boolean suitableForTakeoff = Math.random() > 0.1; // 90% suitable
        boolean suitableForLanding = Math.random() > 0.15; // 85% suitable
        boolean requiresDeviation = Math.random() < 0.2; // 20% requires deviation
        
        return new WeatherConditions(suitableForTakeoff, suitableForLanding, requiresDeviation, 
                                   "Current weather conditions");
    }
    
    public boolean isConnected() { return isConnected; }
}

// Weather conditions class
class WeatherConditions {
    private boolean suitableForTakeoff;
    private boolean suitableForLanding;
    private boolean requiresRouteDeviation;
    private String description;
    
    public WeatherConditions(boolean suitableForTakeoff, boolean suitableForLanding, 
                           boolean requiresRouteDeviation, String description) {
        this.suitableForTakeoff = suitableForTakeoff;
        this.suitableForLanding = suitableForLanding;
        this.requiresRouteDeviation = requiresRouteDeviation;
        this.description = description;
    }
    
    public boolean isSuitableForTakeoff() { return suitableForTakeoff; }
    public boolean isSuitableForLanding() { return suitableForLanding; }
    public boolean isSuitableForFlight() { return suitableForTakeoff && suitableForLanding; }
    public boolean requiresRouteDeviation() { return requiresRouteDeviation; }
    public String getDescription() { return description; }
}
