package abstraction.transport;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class showcasing polymorphic usage of different transport vehicles
 * Demonstrates how client code remains unchanged regardless of transport type
 */
public class TransportDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Transport Abstraction Demo ===\n");
        
        // Create different transport vehicles
        Transport[] transports = createTransportVehicles();
        
        // Test each transport polymorphically
        for (Transport transport : transports) {
            System.out.println("Testing transport: " + transport.getClass().getSimpleName());
            System.out.println("Vehicle ID: " + transport.getVehicleId());
            System.out.println("Vehicle Name: " + transport.getVehicleName());
            System.out.println("Transport Type: " + transport.getTransportType());
            
            try {
                // Test complete transport workflow using template method
                testTransportWorkflow(transport);
                
                // Test transport-specific features
                testTransportSpecificFeatures(transport);
                
                // Test maintenance workflow
                testMaintenanceWorkflow(transport);
                
                // Display vehicle status
                displayVehicleStatus(transport);
                
            } catch (Exception e) {
                System.err.println("Error testing transport: " + e.getMessage());
            }
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static Transport[] createTransportVehicles() {
        // Car configuration
        Map<String, Object> carConfig = new HashMap<>();
        carConfig.put("max_speed", 200.0);
        carConfig.put("fuel_type", "GASOLINE");
        carConfig.put("tank_capacity", 60.0);
        carConfig.put("consumption_rate", 0.08);
        carConfig.put("gear_count", 6);
        carConfig.put("automatic", true);
        
        // Airplane configuration
        Map<String, Object> airplaneConfig = new HashMap<>();
        airplaneConfig.put("max_speed", 850.0);
        airplaneConfig.put("cruising_altitude", 35000.0);
        airplaneConfig.put("max_altitude", 42000.0);
        airplaneConfig.put("fuel_capacity", 45000.0);
        airplaneConfig.put("consumption_rate", 3.2);
        
        // Ship configuration
        Map<String, Object> shipConfig = new HashMap<>();
        shipConfig.put("max_speed", 22.0);
        shipConfig.put("fuel_type", "DIESEL");
        shipConfig.put("fuel_capacity", 8000.0);
        shipConfig.put("consumption_rate", 0.18);
        
        return new Transport[] {
            new Car("car_001", "Tesla Model S", carConfig),
            new Airplane("plane_001", "Boeing 737-800", airplaneConfig),
            new Ship("ship_001", "MV Atlantic Explorer", shipConfig)
        };
    }
    
    private static void testTransportWorkflow(Transport transport) {
        try {
            System.out.println("\n1. Testing transport journey workflow...");
            
            // Create a journey request
            JourneyRequest journeyRequest = createJourneyRequest(transport);
            
            // Execute journey using template method
            System.out.println("   Starting journey execution...");
            var journeyFuture = transport.executeJourney(journeyRequest);
            
            // Wait for journey completion (with timeout simulation)
            TransportResult journeyResult = journeyFuture.get();
            
            if (journeyResult.isSuccess()) {
                System.out.println("   ✓ Journey completed successfully");
                System.out.println("   Message: " + journeyResult.getMessage());
                System.out.println("   Completion Time: " + journeyResult.getCompletionTime());
                
                if (journeyResult.getActualDistance() > 0) {
                    System.out.println("   Distance: " + String.format("%.1f", journeyResult.getActualDistance()) + " km");
                }
                if (journeyResult.getActualDuration() > 0) {
                    System.out.println("   Duration: " + String.format("%.2f", journeyResult.getActualDuration()) + " hours");
                }
                if (journeyResult.getFuelConsumed() > 0) {
                    System.out.println("   Fuel Consumed: " + String.format("%.2f", journeyResult.getFuelConsumed()) + " liters");
                }
                
                // Display journey data if available
                if (journeyResult.getJourneyData() != null) {
                    System.out.println("   Journey Data:");
                    journeyResult.getJourneyData().forEach((key, value) -> 
                        System.out.println("     " + key + ": " + value));
                }
            } else {
                System.out.println("   ✗ Journey failed: " + journeyResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Journey workflow test failed: " + e.getMessage());
        }
    }
    
    private static JourneyRequest createJourneyRequest(Transport transport) {
        // Create route based on transport type
        Route route = createRouteForTransport(transport);
        
        return new JourneyRequest(
            "journey_" + System.currentTimeMillis(),
            route,
            2, // passengers
            100.0, // cargo weight
            LocalDateTime.now().plusMinutes(5),
            "demo_user"
        );
    }
    
    private static Route createRouteForTransport(Transport transport) {
        switch (transport.getTransportType()) {
            case ROAD_VEHICLE:
                return new Route(
                    "road_route_001",
                    new Location(40.7128, -74.0060, "New York City"),
                    new Location(34.0522, -118.2437, "Los Angeles"),
                    4500.0, // km
                    RouteType.HIGHWAY
                );
                
            case AIRCRAFT:
                return new Route(
                    "air_route_001",
                    new Location(51.4700, -0.4543, 10.0, "London Heathrow"),
                    new Location(40.6413, -73.7781, 15.0, "New York JFK"),
                    5550.0, // km
                    RouteType.AIRWAYS
                );
                
            case WATERCRAFT:
                return new Route(
                    "sea_route_001",
                    new Location(51.5074, -0.1278, "Port of London"),
                    new Location(40.7128, -74.0060, "Port of New York"),
                    5500.0, // nautical miles converted to km
                    RouteType.WATERWAYS
                );
                
            default:
                return new Route(
                    "default_route_001",
                    new Location(0.0, 0.0, "Origin"),
                    new Location(1.0, 1.0, "Destination"),
                    100.0,
                    RouteType.HIGHWAY
                );
        }
    }
    
    private static void testTransportSpecificFeatures(Transport transport) {
        System.out.println("\n2. Testing transport-specific features...");
        
        try {
            if (transport instanceof Car) {
                testCarFeatures((Car) transport);
            } else if (transport instanceof Airplane) {
                testAirplaneFeatures((Airplane) transport);
            } else if (transport instanceof Ship) {
                testShipFeatures((Ship) transport);
            }
        } catch (Exception e) {
            System.err.println("   Transport-specific test failed: " + e.getMessage());
        }
    }
    
    private static void testCarFeatures(Car car) {
        System.out.println("   Testing Car specific features:");
        System.out.println("   - Max Speed: " + car.getMaxSpeed() + " km/h");
        System.out.println("   - Current Speed: " + car.getCurrentSpeed() + " km/h");
        System.out.println("   - Current Gear: " + car.getCurrentGear());
        System.out.println("   - Automatic Transmission: " + car.isAutomatic());
        System.out.println("   - GPS Initialized: " + car.getGpsSystem().isInitialized());
        System.out.println("   - Traffic System Connected: " + car.getTrafficSystem().isConnected());
        System.out.println("   ✓ Car features tested");
    }
    
    private static void testAirplaneFeatures(Airplane airplane) {
        System.out.println("   Testing Airplane specific features:");
        System.out.println("   - Max Speed: " + airplane.getMaxSpeed() + " km/h");
        System.out.println("   - Current Speed: " + airplane.getCurrentSpeed() + " km/h");
        System.out.println("   - Current Altitude: " + airplane.getCurrentAltitude() + " ft");
        System.out.println("   - Cruising Altitude: " + airplane.getCruisingAltitude() + " ft");
        System.out.println("   - Flight Phase: " + airplane.getCurrentPhase());
        System.out.println("   - FMS Initialized: " + airplane.getFms().isInitialized());
        System.out.println("   - ATC Connected: " + airplane.getAtc().isConnected());
        System.out.println("   - Weather System Connected: " + airplane.getWeatherSystem().isConnected());
        System.out.println("   ✓ Airplane features tested");
    }
    
    private static void testShipFeatures(Ship ship) {
        System.out.println("   Testing Ship specific features:");
        System.out.println("   - Max Speed: " + ship.getMaxSpeed() + " knots");
        System.out.println("   - Current Speed: " + ship.getCurrentSpeed() + " knots");
        System.out.println("   - Current Sea State: " + ship.getCurrentSeaState());
        System.out.println("   - Marine Nav Initialized: " + ship.getMarineNav().isInitialized());
        System.out.println("   - Weather Radar Initialized: " + ship.getWeatherRadar().isInitialized());
        System.out.println("   - Port Authority Connected: " + ship.getPortAuthority().isConnected());
        System.out.println("   - VTS Connected: " + ship.getVts().isConnected());
        System.out.println("   ✓ Ship features tested");
    }
    
    private static void testMaintenanceWorkflow(Transport transport) {
        try {
            System.out.println("\n3. Testing maintenance workflow...");
            
            // Create maintenance request
            MaintenanceRequest maintenanceRequest = new MaintenanceRequest(
                "maint_" + System.currentTimeMillis(),
                MaintenanceType.ROUTINE,
                "Routine maintenance check",
                "demo_user"
            );
            
            // Perform maintenance using template method
            System.out.println("   Starting maintenance...");
            var maintenanceFuture = transport.performMaintenance(maintenanceRequest);
            
            MaintenanceResult maintenanceResult = maintenanceFuture.get();
            
            if (maintenanceResult.isSuccess()) {
                System.out.println("   ✓ Maintenance completed successfully");
                System.out.println("   Message: " + maintenanceResult.getMessage());
                
                if (maintenanceResult.getCost() > 0) {
                    System.out.println("   Cost: $" + String.format("%.2f", maintenanceResult.getCost()));
                }
                
                if (maintenanceResult.getWorkPerformed() != null) {
                    System.out.println("   Work Performed:");
                    for (String work : maintenanceResult.getWorkPerformed()) {
                        System.out.println("     - " + work);
                    }
                }
                
                if (maintenanceResult.getNextMaintenanceDate() != null) {
                    System.out.println("   Next Maintenance: " + maintenanceResult.getNextMaintenanceDate());
                }
            } else {
                System.out.println("   ✗ Maintenance failed: " + maintenanceResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Maintenance workflow test failed: " + e.getMessage());
        }
    }
    
    private static void displayVehicleStatus(Transport transport) {
        System.out.println("\n4. Vehicle Status Information:");
        
        VehicleStatus status = transport.getStatus();
        System.out.println("   Vehicle ID: " + status.getVehicleId());
        System.out.println("   Vehicle Name: " + status.getVehicleName());
        System.out.println("   Transport Type: " + status.getTransportType());
        System.out.println("   Current State: " + status.getCurrentState());
        System.out.println("   Current Speed: " + String.format("%.1f", status.getCurrentSpeed()) + 
                         (transport.getTransportType() == TransportType.WATERCRAFT ? " knots" : " km/h"));
        System.out.println("   Fuel Level: " + String.format("%.1f", status.getFuelLevel()) + " liters");
        System.out.println("   Next Maintenance: " + status.getNextMaintenanceDate());
        System.out.println("   Status Time: " + status.getStatusTime());
        
        // Display fuel manager information
        FuelManager fuelManager = transport.getFuelManager();
        System.out.println("   Fuel Information:");
        System.out.println("     Fuel Type: " + fuelManager.getFuelType());
        System.out.println("     Fuel Percentage: " + String.format("%.1f", fuelManager.getFuelPercentage()) + "%");
        System.out.println("     Fuel Capacity: " + String.format("%.1f", fuelManager.getCapacity()) + " liters");
        System.out.println("     Consumption Rate: " + String.format("%.3f", fuelManager.getConsumptionRate()) + " L/km");
        
        // Display maintenance information
        MaintenanceManager maintenanceManager = transport.getMaintenanceManager();
        System.out.println("   Maintenance Information:");
        System.out.println("     Total Mileage: " + String.format("%.1f", maintenanceManager.getTotalMileage()) + " km");
        System.out.println("     Last Maintenance: " + maintenanceManager.getLastMaintenanceDate());
        System.out.println("     Maintenance Due: " + (maintenanceManager.isMaintenanceDue() ? "YES" : "NO"));
        
        // Display safety system status
        var safetySystem = transport.getSafetySystem();
        var activeAlerts = safetySystem.getActiveAlerts();
        System.out.println("   Safety Information:");
        System.out.println("     Emergency Stop Active: " + safetySystem.isEmergencyStopActive());
        System.out.println("     Active Safety Alerts: " + activeAlerts.size());
        
        if (!activeAlerts.isEmpty()) {
            System.out.println("     Recent Alerts:");
            for (var alert : activeAlerts.subList(0, Math.min(3, activeAlerts.size()))) {
                System.out.println("       - " + alert.getSeverity() + ": " + alert.getMessage());
            }
        }
        
        // Test refueling
        testRefueling(transport);
    }
    
    private static void testRefueling(Transport transport) {
        try {
            System.out.println("\n5. Testing refueling...");
            
            FuelManager fuelManager = transport.getFuelManager();
            double currentLevel = fuelManager.getCurrentFuelLevel();
            double capacity = fuelManager.getCapacity();
            
            if (currentLevel < capacity * 0.9) { // If less than 90% full
                RefuelRequest refuelRequest = new RefuelRequest(
                    "refuel_" + System.currentTimeMillis(),
                    fuelManager.getFuelType(),
                    capacity - currentLevel, // Fill to capacity
                    "station_001",
                    "demo_user"
                );
                
                var refuelFuture = transport.refuel(refuelRequest);
                RefuelResult refuelResult = refuelFuture.get();
                
                if (refuelResult.isSuccess()) {
                    System.out.println("   ✓ Refueling completed successfully");
                    System.out.println("   Amount: " + String.format("%.1f", refuelResult.getActualAmount()) + " liters");
                    System.out.println("   Cost: $" + String.format("%.2f", refuelResult.getCost()));
                    System.out.println("   New Fuel Level: " + String.format("%.1f", fuelManager.getCurrentFuelLevel()) + " liters");
                } else {
                    System.out.println("   ✗ Refueling failed: " + refuelResult.getMessage());
                }
            } else {
                System.out.println("   Vehicle fuel level is sufficient (" + 
                                 String.format("%.1f", fuelManager.getFuelPercentage()) + "%)");
            }
            
        } catch (Exception e) {
            System.err.println("   Refueling test failed: " + e.getMessage());
        }
    }
}
