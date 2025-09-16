import java.util.Map;
import java.util.HashMap;

/**
 * Electric vehicle implementation with battery management and charging capabilities
 * Demonstrates eco-friendly transportation with advanced energy management
 */
public class ElectricVehicle extends SmartVehicle {
    private double batteryCapacityKwh;
    private double chargingRateKw;
    private boolean isCharging;
    private String chargingStationType;
    private double regenerativeBrakingEfficiency;
    private Map<String, Double> energyConsumption;
    
    public ElectricVehicle(String make, String model, int year, AutonomyLevel autonomyLevel, 
                          String ownerName, double batteryCapacityKwh) {
        super(make, model, year, VehicleType.ELECTRIC_CAR, autonomyLevel, ownerName);
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.chargingRateKw = 50.0; // Default 50kW charging
        this.isCharging = false;
        this.regenerativeBrakingEfficiency = 0.8;
        this.energyConsumption = new HashMap<>();
        initializeElectricSystems();
    }
    
    private void initializeElectricSystems() {
        // Configure electric-specific settings
        configuration.put("batteryCapacity", batteryCapacityKwh);
        configuration.put("chargingRate", chargingRateKw);
        configuration.put("regenerativeBraking", true);
        configuration.put("ecoMode", true);
        
        // Install electric-specific sensors
        addSensor("battery_management_system");
        addSensor("charging_port_sensor");
        addSensor("thermal_management");
        addSensor("energy_monitor");
        
        // Initialize energy consumption tracking
        energyConsumption.put("driving", 0.0);
        energyConsumption.put("climate", 0.0);
        energyConsumption.put("electronics", 0.0);
        energyConsumption.put("charging", 0.0);
    }
    
    @Override
    public void startEngine() {
        if (status.getBatteryLevel() <= 0) {
            System.out.println("❌ Cannot start - battery depleted");
            return;
        }
        
        System.out.println("⚡ Electric motor activated silently");
        status.setOperationalState(OperationalState.IDLE);
        
        // Electric vehicles start instantly
        System.out.println("✅ Ready to drive - " + String.format("%.1f%%", status.getBatteryLevel()) + " battery");
    }
    
    @Override
    public void stopEngine() {
        System.out.println("🔌 Electric motor deactivated");
        status.setOperationalState(OperationalState.PARKED);
        
        // Enable regenerative systems when stopped
        if (status.getCurrentSpeedKmh() > 0) {
            double energyRecovered = status.getCurrentSpeedKmh() * regenerativeBrakingEfficiency * 0.01;
            status.setBatteryLevel(Math.min(100, status.getBatteryLevel() + energyRecovered));
            System.out.println("🔋 Regenerative braking recovered " + 
                             String.format("%.2f", energyRecovered) + "% battery");
        }
    }
    
    @Override
    public void accelerate(double targetSpeedKmh) {
        double maxSpeed = (Double) configuration.get("maxSpeed");
        targetSpeedKmh = Math.min(targetSpeedKmh, maxSpeed);
        
        if (status.getBatteryLevel() <= 5) {
            System.out.println("⚠️ Low battery - limited acceleration available");
            targetSpeedKmh = Math.min(targetSpeedKmh, 60);
        }
        
        System.out.println("⚡ Accelerating to " + targetSpeedKmh + " km/h (instant torque)");
        status.setCurrentSpeedKmh(targetSpeedKmh);
        
        // Calculate energy consumption
        double energyUsed = targetSpeedKmh * 0.02; // Simplified consumption model
        energyConsumption.put("driving", energyConsumption.get("driving") + energyUsed);
        
        // Electric vehicles have excellent acceleration
        System.out.println("🚀 Smooth, silent acceleration completed");
    }
    
    @Override
    public void brake(double intensity) {
        double currentSpeed = status.getCurrentSpeedKmh();
        double newSpeed = Math.max(0, currentSpeed * (1 - intensity));
        
        System.out.println("🔋 Regenerative braking engaged (intensity: " + 
                          String.format("%.1f", intensity * 100) + "%)");
        
        // Regenerative braking recovers energy
        double energyRecovered = (currentSpeed - newSpeed) * regenerativeBrakingEfficiency * 0.005;
        status.setBatteryLevel(Math.min(100, status.getBatteryLevel() + energyRecovered));
        status.setCurrentSpeedKmh(newSpeed);
        
        System.out.println("⚡ Energy recovered: " + String.format("%.3f", energyRecovered) + "% battery");
    }
    
    @Override
    public void steer(double angle) {
        System.out.println("🎯 Electric power steering: " + angle + " degrees");
        
        // Electric power steering is more efficient
        double steeringEnergy = Math.abs(angle) * 0.001;
        energyConsumption.put("electronics", energyConsumption.get("electronics") + steeringEnergy);
    }
    
    @Override
    public String getVehicleCapabilities() {
        return "Zero emissions, instant torque, regenerative braking, silent operation, smart charging";
    }
    
    public void startCharging(String stationType) {
        if (isCharging) {
            System.out.println("⚠️ Already charging");
            return;
        }
        
        if (status.getBatteryLevel() >= 100) {
            System.out.println("✅ Battery already full");
            return;
        }
        
        this.chargingStationType = stationType;
        this.isCharging = true;
        status.setOperationalState(OperationalState.CHARGING);
        
        // Set charging rate based on station type
        switch (stationType.toLowerCase()) {
            case "home":
                chargingRateKw = 7.0; // Level 2 home charging
                break;
            case "public":
                chargingRateKw = 22.0; // Public AC charging
                break;
            case "fast":
                chargingRateKw = 50.0; // DC fast charging
                break;
            case "superfast":
                chargingRateKw = 150.0; // Ultra-fast charging
                break;
            default:
                chargingRateKw = 7.0;
        }
        
        System.out.println("🔌 Charging started at " + stationType + " station (" + chargingRateKw + "kW)");
        simulateCharging();
    }
    
    private void simulateCharging() {
        new Thread(() -> {
            double currentBattery = status.getBatteryLevel();
            double targetBattery = 100.0;
            
            while (isCharging && currentBattery < targetBattery) {
                try {
                    Thread.sleep(1000); // 1 second intervals
                    
                    // Calculate charging increment (simplified)
                    double chargingIncrement = (chargingRateKw / batteryCapacityKwh) * (100.0 / 3600.0); // Per second
                    currentBattery = Math.min(targetBattery, currentBattery + chargingIncrement);
                    
                    status.setBatteryLevel(currentBattery);
                    energyConsumption.put("charging", energyConsumption.get("charging") + chargingIncrement);
                    
                    if (currentBattery % 10 < 1) { // Report every ~10%
                        System.out.println("🔋 Charging progress: " + String.format("%.1f%%", currentBattery));
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            if (currentBattery >= targetBattery) {
                System.out.println("✅ Charging complete - battery full");
                stopCharging();
            }
        }).start();
    }
    
    public void stopCharging() {
        if (!isCharging) {
            System.out.println("⚠️ Not currently charging");
            return;
        }
        
        isCharging = false;
        status.setOperationalState(OperationalState.PARKED);
        System.out.println("🔌 Charging stopped - " + String.format("%.1f%%", status.getBatteryLevel()) + " battery");
    }
    
    public void enableEcoMode() {
        configuration.put("ecoMode", true);
        System.out.println("🌱 Eco mode enabled - optimizing for maximum range");
        
        // Reduce power consumption in eco mode
        configuration.put("maxSpeed", (Double) configuration.get("maxSpeed") * 0.85);
        regenerativeBrakingEfficiency = Math.min(0.95, regenerativeBrakingEfficiency + 0.1);
    }
    
    public void disableEcoMode() {
        configuration.put("ecoMode", false);
        System.out.println("⚡ Performance mode enabled - full power available");
        
        // Restore full performance
        configuration.put("maxSpeed", vehicleType.getMaxSpeedKmh());
        regenerativeBrakingEfficiency = 0.8;
    }
    
    public double calculateRange() {
        double currentBattery = status.getBatteryLevel();
        double efficiency = vehicleType.getPrimaryFuelType().getEfficiency(); // km per kWh
        double availableEnergy = (currentBattery / 100.0) * batteryCapacityKwh;
        
        // Adjust for driving conditions and eco mode
        double efficiencyMultiplier = 1.0;
        if ((Boolean) configuration.get("ecoMode")) {
            efficiencyMultiplier = 1.2;
        }
        
        return availableEnergy * efficiency * efficiencyMultiplier;
    }
    
    public void scheduledCharging(int startHour, int endHour) {
        System.out.println("⏰ Scheduled charging set: " + startHour + ":00 to " + endHour + ":00");
        System.out.println("💰 Optimizing for off-peak electricity rates");
        
        // In a real implementation, this would integrate with smart grid systems
        configuration.put("scheduledChargingStart", startHour);
        configuration.put("scheduledChargingEnd", endHour);
    }
    
    public Map<String, Double> getEnergyConsumptionReport() {
        Map<String, Double> report = new HashMap<>(energyConsumption);
        double total = energyConsumption.values().stream().mapToDouble(Double::doubleValue).sum();
        
        System.out.println("\n⚡ ENERGY CONSUMPTION REPORT");
        System.out.println(new String(new char[40]).replace('\0', '='));
        for (Map.Entry<String, Double> entry : report.entrySet()) {
            double percentage = total > 0 ? (entry.getValue() / total) * 100 : 0;
            System.out.println(String.format("%-12s: %6.2f kWh (%4.1f%%)", 
                entry.getKey(), entry.getValue(), percentage));
        }
        System.out.println(String.format("%-12s: %6.2f kWh", "TOTAL", total));
        System.out.println();
        
        return report;
    }
    
    // Getters
    public double getBatteryCapacityKwh() { return batteryCapacityKwh; }
    public double getChargingRateKw() { return chargingRateKw; }
    public boolean isCharging() { return isCharging; }
    public String getChargingStationType() { return chargingStationType; }
    public double getRegenerativeBrakingEfficiency() { return regenerativeBrakingEfficiency; }
    public double getCurrentRange() { return calculateRange(); }
    
    @Override
    public void printVehicleInfo() {
        super.printVehicleInfo();
        
        System.out.println("🔋 ELECTRIC VEHICLE DETAILS");
        System.out.println(new String(new char[40]).replace('\0', '='));
        System.out.println("Battery Capacity: " + batteryCapacityKwh + " kWh");
        System.out.println("Current Battery: " + String.format("%.1f%%", status.getBatteryLevel()));
        System.out.println("Estimated Range: " + String.format("%.1f", calculateRange()) + " km");
        System.out.println("Charging Rate: " + chargingRateKw + " kW");
        System.out.println("Charging Status: " + (isCharging ? "Charging at " + chargingStationType : "Not charging"));
        System.out.println("Regenerative Braking: " + String.format("%.0f%%", regenerativeBrakingEfficiency * 100) + " efficiency");
        System.out.println("Eco Mode: " + (Boolean) configuration.get("ecoMode"));
        System.out.println();
    }
}
