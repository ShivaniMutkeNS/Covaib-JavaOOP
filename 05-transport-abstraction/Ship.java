package abstraction.transport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Ship implementation of Transport - represents watercraft
 */
public class Ship extends Transport {
    
    private double currentSpeed;
    private double maxSpeed;
    private MarineNavigationSystem marineNav;
    private WeatherRadar weatherRadar;
    private PortAuthority portAuthority;
    private VesselTrafficService vts;
    private SeaState currentSeaState;
    
    public Ship(String vehicleId, String vehicleName, Map<String, Object> configuration) {
        super(vehicleId, vehicleName, TransportType.WATERCRAFT, configuration);
        
        this.maxSpeed = (Double) configuration.getOrDefault("max_speed", 25.0); // knots
        this.currentSpeed = 0.0;
        this.currentSeaState = SeaState.CALM;
        
        this.marineNav = new MarineNavigationSystem();
        this.weatherRadar = new WeatherRadar();
        this.portAuthority = new PortAuthority();
        this.vts = new VesselTrafficService();
    }
    
    @Override
    protected void initialize() {
        System.out.println("Initializing Ship: " + vehicleName);
        
        // Initialize marine-specific systems
        marineNav.initialize();
        weatherRadar.initialize();
        portAuthority.connect();
        vts.connect();
        
        // Perform pre-departure checks
        performPreDepartureChecks();
        
        System.out.println("Ship initialization complete");
    }
    
    @Override
    protected FuelManager createFuelManager() {
        FuelType fuelType = FuelType.valueOf(
            configuration.getOrDefault("fuel_type", "DIESEL").toString()
        );
        double tankCapacity = (Double) configuration.getOrDefault("fuel_capacity", 5000.0);
        double consumptionRate = (Double) configuration.getOrDefault("consumption_rate", 0.15);
        
        return new ShipFuelManager(fuelType, tankCapacity, consumptionRate);
    }
    
    @Override
    protected NavigationResult navigate(Route route) {
        try {
            System.out.println("Starting marine navigation from " + route.getOrigin().getName() + 
                             " to " + route.getDestination().getName());
            
            // Marine navigation phases: Port departure -> Open water navigation -> Port arrival
            double totalDistance = 0.0;
            
            // Phase 1: Departure from port
            NavigationResult departureResult = performPortDeparture();
            if (!departureResult.isSuccess()) {
                return departureResult;
            }
            
            // Phase 2: Open water navigation
            NavigationResult openWaterResult = performOpenWaterNavigation(route);
            if (!openWaterResult.isSuccess()) {
                return openWaterResult;
            }
            totalDistance += openWaterResult.getDistanceCovered();
            
            // Phase 3: Arrival at destination port
            NavigationResult arrivalResult = performPortArrival();
            if (!arrivalResult.isSuccess()) {
                return arrivalResult;
            }
            
            currentSpeed = 0.0;
            
            return NavigationResult.success("Voyage completed successfully", totalDistance);
            
        } catch (Exception e) {
            return NavigationResult.failure("Marine navigation failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performPortDeparture() {
        try {
            System.out.println("Preparing for port departure");
            
            // Get departure clearance from port authority
            if (!portAuthority.requestDepartureClearance()) {
                return NavigationResult.failure("Departure clearance denied by port authority");
            }
            
            // Check weather conditions
            MarineWeatherConditions weather = weatherRadar.getCurrentWeather();
            if (!weather.isSuitableForDeparture()) {
                return NavigationResult.failure("Weather conditions unsuitable for departure");
            }
            
            // Start engines and begin departure
            adjustSpeed(5.0); // Slow departure speed
            
            // Navigate through harbor
            Thread.sleep(3000); // Simulate harbor navigation time
            
            System.out.println("Successfully departed from port");
            return NavigationResult.success("Port departure completed", 3.0);
            
        } catch (Exception e) {
            return NavigationResult.failure("Port departure failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performOpenWaterNavigation(Route route) {
        try {
            System.out.println("Beginning open water navigation");
            
            // Set cruise speed based on sea conditions
            MarineWeatherConditions weather = weatherRadar.getCurrentWeather();
            currentSeaState = weather.getSeaState();
            
            double cruiseSpeed = calculateOptimalSpeed(weather);
            adjustSpeed(cruiseSpeed);
            
            // Navigate the main route
            double totalDistance = 0.0;
            double remainingDistance = route.getDistance();
            
            // Update marine navigation system
            marineNav.setRoute(route);
            
            while (remainingDistance > 0) {
                // Check for weather updates
                weather = weatherRadar.getCurrentWeather();
                currentSeaState = weather.getSeaState();
                
                // Adjust speed based on sea conditions
                double optimalSpeed = calculateOptimalSpeed(weather);
                adjustSpeed(optimalSpeed);
                
                // Check for vessel traffic
                if (vts.hasTrafficAlert()) {
                    adjustSpeed(currentSpeed * 0.7); // Reduce speed for traffic
                    System.out.println("Reducing speed due to vessel traffic");
                }
                
                // Navigate for 1 hour segments
                double distanceThisSegment = Math.min(remainingDistance, currentSpeed);
                totalDistance += distanceThisSegment;
                remainingDistance -= distanceThisSegment;
                
                // Update navigation progress
                marineNav.updateProgress(totalDistance / route.getDistance());
                
                Thread.sleep(100); // 100ms represents 1 hour of sailing
                
                // Safety checks
                if (safetySystem.isEmergencyStopActive()) {
                    return NavigationResult.failure("Emergency stop during navigation");
                }
                
                // Check for severe weather
                if (weather.requiresCourseChange()) {
                    if (requestCourseChange()) {
                        remainingDistance *= 1.15; // 15% longer route
                        System.out.println("Course change approved due to severe weather");
                    } else {
                        return NavigationResult.failure("Unable to avoid severe weather");
                    }
                }
            }
            
            return NavigationResult.success("Open water navigation completed", totalDistance);
            
        } catch (Exception e) {
            return NavigationResult.failure("Open water navigation failed: " + e.getMessage());
        }
    }
    
    private NavigationResult performPortArrival() {
        try {
            System.out.println("Approaching destination port");
            
            // Request port entry clearance
            if (!portAuthority.requestArrivalClearance()) {
                return NavigationResult.failure("Port entry clearance denied");
            }
            
            // Get pilot assistance for harbor navigation
            if (!portAuthority.requestPilotAssistance()) {
                return NavigationResult.failure("Pilot assistance not available");
            }
            
            // Reduce speed for harbor approach
            adjustSpeed(3.0);
            
            // Navigate to berth
            Thread.sleep(2000); // Simulate harbor approach time
            
            // Dock at berth
            adjustSpeed(0.0);
            
            System.out.println("Successfully arrived and docked at port");
            return NavigationResult.success("Port arrival completed", 2.0);
            
        } catch (Exception e) {
            return NavigationResult.failure("Port arrival failed: " + e.getMessage());
        }
    }
    
    private double calculateOptimalSpeed(MarineWeatherConditions weather) {
        double baseSpeed = maxSpeed * 0.8; // Normal cruise speed
        
        // Adjust for sea state
        switch (weather.getSeaState()) {
            case CALM:
                baseSpeed *= 1.0;
                break;
            case SLIGHT:
                baseSpeed *= 0.9;
                break;
            case MODERATE:
                baseSpeed *= 0.7;
                break;
            case ROUGH:
                baseSpeed *= 0.5;
                break;
            case VERY_ROUGH:
                baseSpeed *= 0.3;
                break;
            case HIGH:
                baseSpeed *= 0.2;
                break;
        }
        
        // Adjust for wind conditions
        if (weather.getWindSpeed() > 50) { // High winds
            baseSpeed *= 0.6;
        } else if (weather.getWindSpeed() > 30) { // Moderate winds
            baseSpeed *= 0.8;
        }
        
        return Math.max(baseSpeed, 2.0); // Minimum safe speed
    }
    
    private boolean requestCourseChange() {
        return vts.isConnected() && Math.random() > 0.2; // 80% approval rate
    }
    
    @Override
    protected TransportResult startJourney(JourneyRequest request) {
        try {
            System.out.println("Starting ship voyage: " + request.getRequestId());
            
            // Start main engines
            if (!startEngines()) {
                return TransportResult.failure("Failed to start engines");
            }
            
            // Final pre-departure checks
            if (!performFinalPreDepartureChecks()) {
                return TransportResult.failure("Pre-departure checks failed");
            }
            
            currentState = VehicleState.MOVING;
            
            double estimatedCost = calculateJourneyCost(request.getRoute());
            
            TransportResult result = TransportResult.success("Voyage started successfully");
            Map<String, Object> journeyData = new HashMap<>();
            journeyData.put("estimated_cost", estimatedCost);
            journeyData.put("fuel_at_start", fuelManager.getCurrentFuelLevel());
            journeyData.put("estimated_voyage_time", request.getRoute().getDistance() / (maxSpeed * 0.8));
            journeyData.put("sea_state", currentSeaState.toString());
            result.setJourneyData(journeyData);
            
            return result;
            
        } catch (Exception e) {
            return TransportResult.failure("Failed to start voyage: " + e.getMessage());
        }
    }
    
    @Override
    protected TransportResult completeJourney(JourneyRequest request) {
        try {
            System.out.println("Completing ship voyage: " + request.getRequestId());
            
            // Shutdown engines
            shutdownEngines();
            
            currentState = VehicleState.IDLE;
            
            TransportResult result = TransportResult.success("Voyage completed successfully");
            result.setActualDistance(request.getRoute().getDistance());
            result.setFuelConsumed(request.getRoute().getDistance() * fuelManager.getConsumptionRate());
            
            // Calculate voyage time
            double avgSpeed = maxSpeed * 0.6; // Average speed considering weather
            result.setActualDuration(request.getRoute().getDistance() / avgSpeed);
            
            return result;
            
        } catch (Exception e) {
            return TransportResult.failure("Failed to complete voyage: " + e.getMessage());
        }
    }
    
    @Override
    protected MaintenanceResult performVehicleSpecificMaintenance(MaintenanceRequest request) {
        try {
            System.out.println("Performing ship maintenance: " + request.getDescription());
            
            MaintenanceResult result = MaintenanceResult.success("Ship maintenance completed");
            
            switch (request.getMaintenanceType()) {
                case ROUTINE:
                    performRoutineShipMaintenance(result);
                    break;
                case PREVENTIVE:
                    performPreventiveShipMaintenance(result);
                    break;
                case CORRECTIVE:
                    performCorrectiveShipMaintenance(request, result);
                    break;
                case EMERGENCY:
                    performEmergencyShipMaintenance(request, result);
                    break;
                case INSPECTION:
                    performShipInspection(result);
                    break;
            }
            
            return result;
            
        } catch (Exception e) {
            return MaintenanceResult.failure("Ship maintenance failed: " + e.getMessage());
        }
    }
    
    private void performRoutineShipMaintenance(MaintenanceResult result) {
        String[] tasks = {
            "Engine maintenance", "Hull inspection", "Navigation equipment check",
            "Safety equipment inspection", "Fuel system maintenance", "Deck equipment service"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(25000.0 + Math.random() * 15000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusMonths(2));
    }
    
    private void performPreventiveShipMaintenance(MaintenanceResult result) {
        String[] tasks = {
            "Dry dock inspection", "Propeller maintenance", "Hull cleaning and painting",
            "Engine overhaul", "Navigation system upgrade", "Safety certification renewal"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(100000.0 + Math.random() * 50000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusYears(1));
    }
    
    private void performCorrectiveShipMaintenance(MaintenanceRequest request, MaintenanceResult result) {
        String[] tasks = {
            "Repair: " + request.getDescription(),
            "Component replacement", "System testing", "Sea trial preparation"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(40000.0 + Math.random() * 60000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusMonths(1));
    }
    
    private void performEmergencyShipMaintenance(MaintenanceRequest request, MaintenanceResult result) {
        String[] tasks = {
            "Emergency repair: " + request.getDescription(),
            "Critical system restoration", "Safety verification", "Emergency sea trial"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(80000.0 + Math.random() * 70000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusWeeks(2));
    }
    
    private void performShipInspection(MaintenanceResult result) {
        String[] tasks = {
            "Port state control inspection", "Flag state inspection", "Classification survey",
            "Safety management audit", "Environmental compliance check", "Crew certification review"
        };
        
        result.setWorkPerformed(java.util.Arrays.asList(tasks));
        result.setCost(15000.0 + Math.random() * 10000.0);
        result.setNextMaintenanceDate(LocalDateTime.now().plusMonths(6));
    }
    
    @Override
    protected double calculateJourneyCost(Route route) {
        double fuelCost = route.getDistance() * fuelManager.getConsumptionRate() * 1.2; // $1.2 per liter marine fuel
        double portFees = 5000.0; // Port charges
        double canalFees = 0.0;
        
        // Add canal fees if route crosses major shipping lanes
        if (route.getDistance() > 1000) {
            canalFees = 10000.0; // Suez/Panama canal fees
        }
        
        double crewCost = route.getDistance() / (maxSpeed * 0.8) * 200.0; // $200 per hour crew cost
        double maintenanceCost = route.getDistance() * 0.5; // $0.5 per nautical mile
        
        return fuelCost + portFees + canalFees + crewCost + maintenanceCost;
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
            if (fuelManager.getCurrentFuelLevel() <= fuelManager.getCapacity() * 0.05) {
                return false;
            }
            
            System.out.println("Starting ship engines...");
            Thread.sleep(3000); // Engine start time
            
            // Check engine systems
            if (Math.random() < 0.02) { // 2% chance of engine failure
                return false;
            }
            
            System.out.println("Ship engines started successfully");
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private void shutdownEngines() {
        currentSpeed = 0.0;
        System.out.println("Ship engines shut down");
    }
    
    private void performPreDepartureChecks() {
        System.out.println("Performing pre-departure checks...");
        
        // Check ship systems
        safetySystem.updateSafetyStatus("engine", Math.random() > 0.03);
        safetySystem.updateSafetyStatus("navigation", Math.random() > 0.02);
        safetySystem.updateSafetyStatus("communication", Math.random() > 0.01);
        safetySystem.updateSafetyStatus("fuel_system", Math.random() > 0.02);
        safetySystem.updateSafetyStatus("hull_integrity", Math.random() > 0.01);
        safetySystem.updateSafetyStatus("life_jackets", Math.random() > 0.005);
        
        System.out.println("Pre-departure checks complete");
    }
    
    private boolean performFinalPreDepartureChecks() {
        // Final checks before departure
        SafetyCheckResult safetyCheck = safetySystem.performSafetyCheck();
        if (!safetyCheck.isSuccess()) {
            return false;
        }
        
        // Check weather
        MarineWeatherConditions weather = weatherRadar.getCurrentWeather();
        if (!weather.isSuitableForDeparture()) {
            return false;
        }
        
        // Check port authority clearance
        return portAuthority.isConnected();
    }
    
    // Ship-specific getters
    public SeaState getCurrentSeaState() { return currentSeaState; }
    public MarineNavigationSystem getMarineNav() { return marineNav; }
    public WeatherRadar getWeatherRadar() { return weatherRadar; }
    public PortAuthority getPortAuthority() { return portAuthority; }
    public VesselTrafficService getVts() { return vts; }
}

// Sea state enumeration
enum SeaState {
    CALM, SLIGHT, MODERATE, ROUGH, VERY_ROUGH, HIGH
}

// Ship-specific Fuel Manager
class ShipFuelManager extends FuelManager {
    
    public ShipFuelManager(FuelType fuelType, double capacity, double consumptionRate) {
        super(fuelType, capacity, consumptionRate);
    }
    
    @Override
    public RefuelResult refuel(RefuelRequest request) {
        try {
            if (!isCompatibleFuelType(request.getFuelType())) {
                return RefuelResult.failure("Incompatible fuel type for ship. Expected: " + fuelType);
            }
            
            double availableSpace = capacity - currentLevel;
            double actualAmount = Math.min(request.getRequestedAmount(), availableSpace);
            
            if (actualAmount <= 0) {
                return RefuelResult.failure("Fuel tanks are already full");
            }
            
            // Simulate ship refueling (very slow process)
            try {
                Thread.sleep((long)(actualAmount / 50)); // 1ms per 50 liters
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return RefuelResult.failure("Refueling interrupted");
            }
            
            currentLevel += actualAmount;
            double cost = actualAmount * getMarineFuelPrice();
            
            return RefuelResult.success("Ship refueling completed", actualAmount, cost);
            
        } catch (Exception e) {
            return RefuelResult.failure("Ship refueling failed: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isFuelSystemOperational() {
        // Check fuel pumps, fuel transfer system, etc.
        return currentLevel >= 0 && Math.random() > 0.01; // 1% chance of fuel system failure
    }
    
    private boolean isCompatibleFuelType(FuelType requestedType) {
        return requestedType.equals(fuelType) || 
               (fuelType == FuelType.DIESEL && requestedType == FuelType.DIESEL);
    }
    
    private double getMarineFuelPrice() {
        // Marine fuel prices
        switch (fuelType) {
            case DIESEL: return 1.2;
            case GASOLINE: return 1.4;
            default: return 1.3;
        }
    }
}

// Marine Navigation System
class MarineNavigationSystem {
    private Route currentRoute;
    private double progress;
    private boolean isInitialized;
    
    public void initialize() {
        isInitialized = true;
        progress = 0.0;
        System.out.println("Marine Navigation System initialized");
    }
    
    public void setRoute(Route route) {
        this.currentRoute = route;
        this.progress = 0.0;
    }
    
    public void updateProgress(double newProgress) {
        this.progress = Math.max(0.0, Math.min(1.0, newProgress));
    }
    
    public double getProgress() { return progress; }
    public Route getCurrentRoute() { return currentRoute; }
    public boolean isInitialized() { return isInitialized; }
}

// Weather Radar
class WeatherRadar {
    private boolean isInitialized;
    
    public void initialize() {
        isInitialized = true;
        System.out.println("Weather Radar initialized");
    }
    
    public MarineWeatherConditions getCurrentWeather() {
        if (!isInitialized) {
            return new MarineWeatherConditions(SeaState.UNKNOWN, 0, false, false, false, 
                                             "Weather radar not initialized");
        }
        
        // Simulate marine weather conditions
        SeaState[] seaStates = SeaState.values();
        SeaState seaState = seaStates[(int)(Math.random() * (seaStates.length - 1))]; // Exclude UNKNOWN
        
        double windSpeed = Math.random() * 60; // 0-60 knots
        boolean suitableForDeparture = seaState.ordinal() <= 3 && windSpeed < 40; // Up to ROUGH, wind < 40 knots
        boolean requiresCourseChange = seaState.ordinal() >= 4 || windSpeed > 45; // VERY_ROUGH+ or high winds
        boolean stormWarning = windSpeed > 50 || seaState.ordinal() >= 5;
        
        return new MarineWeatherConditions(seaState, windSpeed, suitableForDeparture, 
                                         requiresCourseChange, stormWarning, "Current marine weather");
    }
    
    public boolean isInitialized() { return isInitialized; }
}

// Marine Weather Conditions
class MarineWeatherConditions {
    private SeaState seaState;
    private double windSpeed;
    private boolean suitableForDeparture;
    private boolean requiresCourseChange;
    private boolean stormWarning;
    private String description;
    
    public MarineWeatherConditions(SeaState seaState, double windSpeed, boolean suitableForDeparture,
                                 boolean requiresCourseChange, boolean stormWarning, String description) {
        this.seaState = seaState;
        this.windSpeed = windSpeed;
        this.suitableForDeparture = suitableForDeparture;
        this.requiresCourseChange = requiresCourseChange;
        this.stormWarning = stormWarning;
        this.description = description;
    }
    
    public SeaState getSeaState() { return seaState; }
    public double getWindSpeed() { return windSpeed; }
    public boolean isSuitableForDeparture() { return suitableForDeparture; }
    public boolean requiresCourseChange() { return requiresCourseChange; }
    public boolean hasStormWarning() { return stormWarning; }
    public String getDescription() { return description; }
}

// Port Authority
class PortAuthority {
    private boolean isConnected;
    
    public void connect() {
        isConnected = Math.random() > 0.05; // 95% success rate
        System.out.println("Port Authority " + (isConnected ? "connected" : "connection failed"));
    }
    
    public boolean requestDepartureClearance() {
        return isConnected && Math.random() > 0.1; // 90% approval rate
    }
    
    public boolean requestArrivalClearance() {
        return isConnected && Math.random() > 0.05; // 95% approval rate
    }
    
    public boolean requestPilotAssistance() {
        return isConnected && Math.random() > 0.15; // 85% availability
    }
    
    public boolean isConnected() { return isConnected; }
}

// Vessel Traffic Service
class VesselTrafficService {
    private boolean isConnected;
    
    public void connect() {
        isConnected = Math.random() > 0.1; // 90% success rate
        System.out.println("Vessel Traffic Service " + (isConnected ? "connected" : "connection failed"));
    }
    
    public boolean hasTrafficAlert() {
        return isConnected && Math.random() < 0.2; // 20% chance of traffic alert
    }
    
    public boolean isConnected() { return isConnected; }
}
