import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base class for all smart vehicle implementations
 * Defines core vehicle operations and autonomous capabilities
 */
public abstract class SmartVehicle {
    protected String vehicleId;
    protected String make;
    protected String model;
    protected int year;
    protected VehicleType vehicleType;
    protected AutonomyLevel autonomyLevel;
    protected VehicleStatus status;
    protected List<String> installedSensors;
    protected Map<String, Object> configuration;
    protected boolean isConnected;
    protected String ownerName;
    protected double totalDistanceTraveled;
    
    public SmartVehicle(String make, String model, int year, VehicleType vehicleType, AutonomyLevel autonomyLevel, String ownerName) {
        this.vehicleId = UUID.randomUUID().toString();
        this.make = make;
        this.model = model;
        this.year = year;
        this.vehicleType = vehicleType;
        this.autonomyLevel = autonomyLevel;
        this.ownerName = ownerName;
        this.status = new VehicleStatus(vehicleId);
        this.installedSensors = new java.util.ArrayList<>();
        this.configuration = new java.util.HashMap<>();
        this.isConnected = true;
        this.totalDistanceTraveled = 0.0;
        initializeVehicle();
    }
    
    // Abstract methods that must be implemented by concrete vehicles
    public abstract void startEngine();
    public abstract void stopEngine();
    public abstract void accelerate(double targetSpeedKmh);
    public abstract void brake(double intensity);
    public abstract void steer(double angle);
    public abstract String getVehicleCapabilities();
    
    // Concrete methods with default implementation
    protected void initializeVehicle() {
        System.out.println("🚗 Initializing " + make + " " + model + " (" + year + ")");
        loadDefaultConfiguration();
        installBasicSensors();
        performSystemCheck();
        System.out.println("✅ Vehicle initialization complete: " + vehicleId.substring(0, 8));
    }
    
    protected void loadDefaultConfiguration() {
        configuration.put("maxSpeed", vehicleType.getMaxSpeedKmh());
        configuration.put("fuelType", vehicleType.getPrimaryFuelType().getDisplayName());
        configuration.put("passengerCapacity", vehicleType.getPassengerCapacity());
        configuration.put("autonomyEnabled", autonomyLevel.getNumericLevel() >= 3);
        configuration.put("ecoMode", vehicleType.getPrimaryFuelType().isEcoFriendly());
        configuration.put("safetyLevel", autonomyLevel.getSafetyRating());
    }
    
    protected void installBasicSensors() {
        installedSensors.add("speedometer");
        installedSensors.add("fuel_gauge");
        installedSensors.add("temperature_sensor");
        installedSensors.add("tire_pressure_sensor");
        
        if (autonomyLevel.hasAdvancedSensors()) {
            installedSensors.add("lidar");
            installedSensors.add("camera_array");
            installedSensors.add("radar");
            installedSensors.add("ultrasonic_sensors");
            installedSensors.add("gps");
            installedSensors.add("imu"); // Inertial Measurement Unit
        }
        
        if (autonomyLevel.getNumericLevel() >= 4) {
            installedSensors.add("hd_mapping");
            installedSensors.add("v2x_communication"); // Vehicle-to-everything
            installedSensors.add("ai_processor");
        }
    }
    
    protected void performSystemCheck() {
        System.out.println("🔧 Performing system diagnostics...");
        
        // Check all systems
        boolean allSystemsOk = true;
        for (Map.Entry<String, Boolean> system : status.getSystemStatus().entrySet()) {
            if (!system.getValue()) {
                allSystemsOk = false;
                System.out.println("⚠️ System issue detected: " + system.getKey());
            }
        }
        
        if (allSystemsOk) {
            System.out.println("✅ All systems operational");
        } else {
            System.out.println("❌ System issues detected - maintenance required");
            status.setSystemStatus("overall_health", false);
        }
    }
    
    public void drive(String destination, double targetSpeedKmh) {
        if (status.getOperationalState() != OperationalState.PARKED && 
            status.getOperationalState() != OperationalState.IDLE) {
            System.out.println("❌ Cannot start driving - vehicle not ready");
            return;
        }
        
        System.out.println("🚗 Starting journey to: " + destination);
        status.setDestination(destination);
        
        startEngine();
        status.setOperationalState(OperationalState.DRIVING);
        accelerate(targetSpeedKmh);
        
        // Simulate driving
        simulateDriving(destination, targetSpeedKmh);
    }
    
    protected void simulateDriving(String destination, double targetSpeedKmh) {
        double distance = 10 + Math.random() * 50; // Random distance 10-60 km
        double timeHours = distance / targetSpeedKmh;
        
        System.out.println("📍 Driving to " + destination + " (" + String.format("%.1f", distance) + " km)");
        
        // Simulate journey progress
        for (int i = 0; i <= 100; i += 20) {
            try {
                Thread.sleep(200); // Simulate time passing
                double currentDistance = (distance * i) / 100.0;
                totalDistanceTraveled += currentDistance / 5; // Increment total
                
                System.out.println("📊 Progress: " + i + "% - " + 
                                 String.format("%.1f", currentDistance) + " km traveled");
                
                // Update status during journey
                status.updateStatus();
                
                if (status.hasWarnings()) {
                    System.out.println("⚠️ Warning: " + String.join(", ", status.getActiveWarnings()));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // Arrive at destination
        status.setCurrentLocation(destination);
        status.setOperationalState(OperationalState.PARKED);
        stopEngine();
        
        System.out.println("🏁 Arrived at destination: " + destination);
        System.out.println("📈 Journey summary: " + String.format("%.1f", distance) + " km in " + 
                          String.format("%.1f", timeHours * 60) + " minutes");
    }
    
    public void enableAutonomousMode() {
        if (!autonomyLevel.isFullyAutonomous()) {
            System.out.println("❌ Autonomous mode not available - requires Level 4+ autonomy");
            return;
        }
        
        if (!hasSensor("ai_processor") || !hasSensor("lidar")) {
            System.out.println("❌ Missing required sensors for autonomous operation");
            return;
        }
        
        status.setOperationalState(OperationalState.AUTONOMOUS);
        configuration.put("autonomousMode", true);
        System.out.println("🤖 Autonomous mode enabled - Level " + autonomyLevel.getNumericLevel());
    }
    
    public void disableAutonomousMode() {
        if (status.getOperationalState() == OperationalState.AUTONOMOUS) {
            status.setOperationalState(OperationalState.IDLE);
            configuration.put("autonomousMode", false);
            System.out.println("👤 Manual control resumed");
        }
    }
    
    public void performMaintenance() {
        System.out.println("🔧 Starting maintenance procedures...");
        status.setOperationalState(OperationalState.MAINTENANCE);
        
        // Reset maintenance flags
        status.clearWarnings();
        
        // Simulate maintenance tasks
        System.out.println("🔍 Checking engine systems...");
        System.out.println("🛞 Inspecting tires and brakes...");
        System.out.println("🔋 Testing battery and electrical systems...");
        System.out.println("📡 Calibrating sensors...");
        
        // Reset system status
        for (String system : status.getSystemStatus().keySet()) {
            status.setSystemStatus(system, true);
        }
        
        status.setOperationalState(OperationalState.PARKED);
        System.out.println("✅ Maintenance completed - all systems operational");
    }
    
    public void connectToNetwork() {
        if (!isConnected) {
            isConnected = true;
            System.out.println("📡 Connected to vehicle network");
            
            // Enable advanced features when connected
            if (autonomyLevel.getNumericLevel() >= 3) {
                System.out.println("🌐 Over-the-air updates available");
                System.out.println("📊 Real-time traffic data enabled");
                System.out.println("🚨 Emergency services integration active");
            }
        }
    }
    
    public void disconnectFromNetwork() {
        if (isConnected) {
            isConnected = false;
            System.out.println("📡 Disconnected from vehicle network");
            System.out.println("⚠️ Some autonomous features may be limited");
        }
    }
    
    public boolean hasSensor(String sensorType) {
        return installedSensors.contains(sensorType);
    }
    
    public void addSensor(String sensorType) {
        if (!installedSensors.contains(sensorType)) {
            installedSensors.add(sensorType);
            System.out.println("📡 Sensor installed: " + sensorType);
        }
    }
    
    public void configure(String key, Object value) {
        configuration.put(key, value);
        System.out.println("⚙️ Configuration updated: " + key + " = " + value);
    }
    
    public void printVehicleInfo() {
        System.out.println("\n🚗 VEHICLE INFORMATION");
        System.out.println("=".repeat(50));
        System.out.println("ID: " + vehicleId);
        System.out.println("Make/Model: " + make + " " + model + " (" + year + ")");
        System.out.println("Type: " + vehicleType.getDisplayName());
        System.out.println("Autonomy Level: " + autonomyLevel.toString());
        System.out.println("Owner: " + ownerName);
        System.out.println("Fuel Type: " + vehicleType.getPrimaryFuelType().getDisplayName());
        System.out.println("Max Speed: " + vehicleType.getMaxSpeedKmh() + " km/h");
        System.out.println("Passenger Capacity: " + vehicleType.getPassengerCapacity());
        System.out.println("Connected: " + (isConnected ? "Yes" : "No"));
        System.out.println("Total Distance: " + String.format("%.1f", totalDistanceTraveled) + " km");
        System.out.println("Capabilities: " + getVehicleCapabilities());
        
        System.out.println("\n📊 Current Status: " + status.toString());
        System.out.println("Health: " + status.getHealthSummary());
        
        System.out.println("\n📡 Installed Sensors (" + installedSensors.size() + "):");
        for (String sensor : installedSensors) {
            System.out.println("  • " + sensor);
        }
        System.out.println();
    }
    
    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public VehicleType getVehicleType() { return vehicleType; }
    public AutonomyLevel getAutonomyLevel() { return autonomyLevel; }
    public VehicleStatus getStatus() { return status; }
    public List<String> getInstalledSensors() { return new java.util.ArrayList<>(installedSensors); }
    public boolean isConnected() { return isConnected; }
    public String getOwnerName() { return ownerName; }
    public double getTotalDistanceTraveled() { return totalDistanceTraveled; }
    public Object getConfiguration(String key) { return configuration.get(key); }
    
    @Override
    public String toString() {
        return String.format("%s %s %s (%d) - %s [%s]", 
            make, model, vehicleType.getDisplayName(), year, 
            autonomyLevel.getDisplayName(), status.getOperationalState().getDisplayName());
    }
}
