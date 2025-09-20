import java.util.Map;
import java.util.HashMap;

/**
 * Hybrid vehicle implementation combining gasoline engine and electric motor
 * Demonstrates dual-power system with intelligent power management
 */
public class HybridVehicle extends SmartVehicle {
    private double fuelTankCapacityLiters;
    private double batteryCapacityKwh;
    private boolean isElectricMode;
    private boolean isEngineRunning;
    private double hybridEfficiencyRatio;
    private Map<String, Double> powerDistribution;
    private String currentDriveMode;
    
    public HybridVehicle(String make, String model, int year, VehicleType vehicleType, 
                        AutonomyLevel autonomyLevel, String ownerName, 
                        double fuelTankCapacityLiters, double batteryCapacityKwh) {
        super(make, model, year, vehicleType, autonomyLevel, ownerName);
        this.fuelTankCapacityLiters = fuelTankCapacityLiters;
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.isElectricMode = true; // Start in electric mode
        this.isEngineRunning = false;
        this.hybridEfficiencyRatio = 1.4; // 40% better efficiency than pure gasoline
        this.powerDistribution = new HashMap<>();
        this.currentDriveMode = "ECO";
        initializeHybridSystems();
    }
    
    private void initializeHybridSystems() {
        // Configure hybrid-specific settings
        configuration.put("fuelCapacity", fuelTankCapacityLiters);
        configuration.put("batteryCapacity", batteryCapacityKwh);
        configuration.put("hybridMode", true);
        configuration.put("regenerativeBraking", true);
        configuration.put("autoStartStop", true);
        
        // Install hybrid-specific sensors
        addSensor("dual_power_management");
        addSensor("engine_start_stop");
        addSensor("hybrid_battery_monitor");
        addSensor("fuel_efficiency_tracker");
        addSensor("power_split_controller");
        
        // Initialize power distribution tracking
        powerDistribution.put("electric_percentage", 60.0);
        powerDistribution.put("gasoline_percentage", 40.0);
        powerDistribution.put("regenerative_recovery", 0.0);
        
        System.out.println("⚡🔥 Hybrid powertrain initialized - dual power system ready");
    }
    
    @Override
    public void startEngine() {
        System.out.println("🔄 Hybrid system startup sequence...");
        
        // Check battery level to determine startup mode
        if (status.getBatteryLevel() > 20) {
            System.out.println("⚡ Starting in electric mode (silent operation)");
            isElectricMode = true;
            isEngineRunning = false;
        } else {
            System.out.println("🔥 Starting gasoline engine (low battery)");
            isElectricMode = false;
            isEngineRunning = true;
        }
        
        status.setOperationalState(OperationalState.IDLE);
        optimizePowerMode();
    }
    
    @Override
    public void stopEngine() {
        System.out.println("🛑 Hybrid system shutdown");
        
        if (isEngineRunning) {
            System.out.println("🔥 Gasoline engine stopped");
            isEngineRunning = false;
        }
        
        System.out.println("⚡ Electric motor deactivated");
        isElectricMode = false;
        status.setOperationalState(OperationalState.PARKED);
    }
    
    @Override
    public void accelerate(double targetSpeedKmh) {
        double maxSpeed = (Double) configuration.get("maxSpeed");
        targetSpeedKmh = Math.min(targetSpeedKmh, maxSpeed);
        
        System.out.println("🚀 Hybrid acceleration to " + targetSpeedKmh + " km/h");
        
        // Determine optimal power source based on demand
        selectOptimalPowerSource(targetSpeedKmh);
        
        status.setCurrentSpeedKmh(targetSpeedKmh);
        
        // Calculate power distribution
        calculatePowerDistribution(targetSpeedKmh);
        
        System.out.println("⚡ Electric: " + String.format("%.0f%%", powerDistribution.get("electric_percentage")) + 
                          " | 🔥 Engine: " + String.format("%.0f%%", powerDistribution.get("gasoline_percentage")));
    }
    
    @Override
    public void brake(double intensity) {
        System.out.println("🔋 Regenerative braking engaged (intensity: " + 
                          String.format("%.1f", intensity * 100) + "%)");
        
        double currentSpeed = status.getCurrentSpeedKmh();
        double newSpeed = Math.max(0, currentSpeed * (1 - intensity));
        
        // Calculate energy recovery from regenerative braking
        double energyRecovered = (currentSpeed - newSpeed) * 0.008 * intensity;
        status.setBatteryLevel(Math.min(100, status.getBatteryLevel() + energyRecovered));
        powerDistribution.put("regenerative_recovery", 
            powerDistribution.get("regenerative_recovery") + energyRecovered);
        
        status.setCurrentSpeedKmh(newSpeed);
        
        System.out.println("⚡ Energy recovered: " + String.format("%.3f", energyRecovered) + "% battery");
        
        // Auto start-stop feature
        if (newSpeed == 0 && isEngineRunning && "ECO".equals(currentDriveMode)) {
            System.out.println("🔄 Auto-stop: Engine stopped at idle");
            isEngineRunning = false;
        }
    }
    
    @Override
    public void steer(double angle) {
        System.out.println("🎯 Hybrid power steering: " + angle + " degrees");
        
        // Power steering uses minimal energy from either source
        if (isElectricMode) {
            System.out.println("⚡ Electric power assist");
        } else {
            System.out.println("🔥 Engine-driven power assist");
        }
    }
    
    @Override
    public String getVehicleCapabilities() {
        return "Dual power sources, regenerative braking, auto start-stop, optimal efficiency, reduced emissions";
    }
    
    private void selectOptimalPowerSource(double targetSpeedKmh) {
        // Intelligent power source selection based on efficiency
        
        if (targetSpeedKmh <= 40 && status.getBatteryLevel() > 15) {
            // Low speed: electric is most efficient
            switchToElectricMode();
        } else if (targetSpeedKmh > 80 || status.getBatteryLevel() < 10) {
            // High speed or low battery: engine is more efficient
            switchToEngineMode();
        } else {
            // Medium speed: use both for optimal performance
            switchToHybridMode();
        }
    }
    
    private void switchToElectricMode() {
        if (!isElectricMode || isEngineRunning) {
            System.out.println("⚡ Switching to electric-only mode");
            isElectricMode = true;
            if (isEngineRunning && status.getCurrentSpeedKmh() < 30) {
                isEngineRunning = false;
                System.out.println("🔥 Engine stopped for efficiency");
            }
        }
    }
    
    private void switchToEngineMode() {
        if (isElectricMode || !isEngineRunning) {
            System.out.println("🔥 Switching to engine mode");
            isElectricMode = false;
            if (!isEngineRunning) {
                isEngineRunning = true;
                System.out.println("🔥 Engine started automatically");
            }
        }
    }
    
    private void switchToHybridMode() {
        System.out.println("⚡🔥 Switching to hybrid mode (both power sources)");
        isElectricMode = true;
        if (!isEngineRunning) {
            isEngineRunning = true;
            System.out.println("🔥 Engine started for hybrid operation");
        }
    }
    
    private void calculatePowerDistribution(double targetSpeedKmh) {
        double electricPercentage = 0;
        double gasolinePercentage = 0;
        
        if (isElectricMode && !isEngineRunning) {
            // Electric only
            electricPercentage = 100;
            gasolinePercentage = 0;
        } else if (!isElectricMode && isEngineRunning) {
            // Engine only
            electricPercentage = 0;
            gasolinePercentage = 100;
        } else if (isElectricMode && isEngineRunning) {
            // Hybrid mode - distribute based on efficiency
            double speedFactor = targetSpeedKmh / (Double) configuration.get("maxSpeed");
            electricPercentage = Math.max(20, 80 - (speedFactor * 60));
            gasolinePercentage = 100 - electricPercentage;
        }
        
        powerDistribution.put("electric_percentage", electricPercentage);
        powerDistribution.put("gasoline_percentage", gasolinePercentage);
        
        // Update fuel and battery consumption
        updateEnergyConsumption(electricPercentage, gasolinePercentage, targetSpeedKmh);
    }
    
    private void updateEnergyConsumption(double electricPct, double gasolinePct, double speed) {
        // Simulate energy consumption based on power distribution
        if (electricPct > 0) {
            double batteryConsumption = (speed * electricPct / 100.0) * 0.001;
            status.setBatteryLevel(Math.max(0, status.getBatteryLevel() - batteryConsumption));
        }
        
        if (gasolinePct > 0) {
            double fuelConsumption = (speed * gasolinePct / 100.0) * 0.0008;
            status.setFuelLevel(Math.max(0, status.getFuelLevel() - fuelConsumption));
        }
    }
    
    private void optimizePowerMode() {
        // Automatically optimize based on current conditions
        double batteryLevel = status.getBatteryLevel();
        double fuelLevel = status.getFuelLevel();
        
        if (batteryLevel < 15 && fuelLevel > 20) {
            System.out.println("🔄 Auto-optimization: Prioritizing engine to preserve battery");
            switchToEngineMode();
        } else if (fuelLevel < 15 && batteryLevel > 30) {
            System.out.println("🔄 Auto-optimization: Prioritizing electric to preserve fuel");
            switchToElectricMode();
        } else {
            System.out.println("🔄 Auto-optimization: Balanced hybrid operation");
        }
    }
    
    public void setDriveMode(String mode) {
        this.currentDriveMode = mode.toUpperCase();
        System.out.println("🎛️ Drive mode set to: " + currentDriveMode);
        
        switch (currentDriveMode) {
            case "ECO":
                configuration.put("maxSpeed", vehicleType.getMaxSpeedKmh() * 0.9);
                hybridEfficiencyRatio = 1.5;
                System.out.println("🌱 ECO mode: Maximum efficiency prioritized");
                break;
            case "NORMAL":
                configuration.put("maxSpeed", vehicleType.getMaxSpeedKmh());
                hybridEfficiencyRatio = 1.4;
                System.out.println("⚖️ NORMAL mode: Balanced performance and efficiency");
                break;
            case "SPORT":
                configuration.put("maxSpeed", vehicleType.getMaxSpeedKmh() * 1.05);
                hybridEfficiencyRatio = 1.2;
                System.out.println("🏎️ SPORT mode: Performance prioritized");
                break;
            case "EV":
                System.out.println("⚡ EV mode: Electric-only operation (when possible)");
                break;
        }
    }
    
    public void chargeBattery() {
        if (status.getBatteryLevel() >= 100) {
            System.out.println("✅ Battery already full");
            return;
        }
        
        System.out.println("🔌 Charging hybrid battery...");
        status.setOperationalState(OperationalState.CHARGING);
        
        // Simulate charging (slower than pure electric vehicles)
        new Thread(() -> {
            double currentBattery = status.getBatteryLevel();
            double targetBattery = 100.0;
            
            while (currentBattery < targetBattery) {
                try {
                    Thread.sleep(2000); // 2 second intervals
                    currentBattery = Math.min(targetBattery, currentBattery + 2.0); // 2% per interval
                    status.setBatteryLevel(currentBattery);
                    
                    if (currentBattery % 20 < 2) { // Report every ~20%
                        System.out.println("🔋 Charging progress: " + String.format("%.0f%%", currentBattery));
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            System.out.println("✅ Hybrid battery charging complete");
            status.setOperationalState(OperationalState.PARKED);
        }).start();
    }
    
    public double calculateFuelEfficiency() {
        // Calculate combined fuel efficiency
        double baseEfficiency = vehicleType.getPrimaryFuelType().getEfficiency();
        double hybridBonus = baseEfficiency * (hybridEfficiencyRatio - 1.0);
        
        // Adjust based on power distribution
        double electricRatio = powerDistribution.get("electric_percentage") / 100.0;
        double totalEfficiency = baseEfficiency + (hybridBonus * electricRatio);
        
        return totalEfficiency;
    }
    
    public String getEfficiencyReport() {
        double fuelEff = calculateFuelEfficiency();
        double electricUsage = powerDistribution.get("electric_percentage");
        double gasolineUsage = powerDistribution.get("gasoline_percentage");
        double recovered = powerDistribution.get("regenerative_recovery");
        
        StringBuilder report = new StringBuilder();
        report.append("\n⚡🔥 HYBRID EFFICIENCY REPORT\n");
        report.append(new String(new char[40]).replace('\0', '=')).append("\n");
        report.append(String.format("Combined Efficiency: %.1f km/L\n", fuelEff));
        report.append(String.format("Electric Usage: %.1f%%\n", electricUsage));
        report.append(String.format("Gasoline Usage: %.1f%%\n", gasolineUsage));
        report.append(String.format("Energy Recovered: %.2f%%\n", recovered));
        report.append("Drive Mode: ").append(currentDriveMode).append("\n");
        report.append("Efficiency Ratio: ").append(String.format("%.1fx", hybridEfficiencyRatio)).append("\n");
        
        return report.toString();
    }
    
    // Getters
    public double getFuelTankCapacityLiters() { return fuelTankCapacityLiters; }
    public double getBatteryCapacityKwh() { return batteryCapacityKwh; }
    public boolean isElectricMode() { return isElectricMode; }
    public boolean isEngineRunning() { return isEngineRunning; }
    public String getCurrentDriveMode() { return currentDriveMode; }
    public double getHybridEfficiencyRatio() { return hybridEfficiencyRatio; }
    public Map<String, Double> getPowerDistribution() { return new HashMap<>(powerDistribution); }
    
    @Override
    public void printVehicleInfo() {
        super.printVehicleInfo();
        
        System.out.println("⚡🔥 HYBRID VEHICLE DETAILS");
        System.out.println(new String(new char[40]).replace('\0', '='));
        System.out.println("Fuel Tank Capacity: " + fuelTankCapacityLiters + " L");
        System.out.println("Battery Capacity: " + batteryCapacityKwh + " kWh");
        System.out.println("Current Fuel: " + String.format("%.1f%%", status.getFuelLevel()));
        System.out.println("Current Battery: " + String.format("%.1f%%", status.getBatteryLevel()));
        System.out.println("Drive Mode: " + currentDriveMode);
        System.out.println("Electric Mode: " + (isElectricMode ? "Active" : "Inactive"));
        System.out.println("Engine Running: " + (isEngineRunning ? "Yes" : "No"));
        System.out.println("Efficiency Ratio: " + String.format("%.1fx", hybridEfficiencyRatio));
        System.out.println("Combined Efficiency: " + String.format("%.1f", calculateFuelEfficiency()) + " km/L");
        System.out.println();
    }
}
