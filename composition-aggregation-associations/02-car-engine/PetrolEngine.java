package composition.car;

/**
 * Petrol Engine Implementation with realistic fuel consumption
 */
public class PetrolEngine implements Engine {
    private boolean isRunning;
    private final EngineSpecs specs;
    
    public PetrolEngine() {
        this.isRunning = false;
        this.specs = new EngineSpecs(300.0, 220.0, 75.0); // 300 HP, 220 km/h, 75% efficiency
    }
    
    @Override
    public boolean canStart(double fuelLevel, double batteryLevel) {
        return fuelLevel > 5.0; // Needs minimum fuel
    }
    
    @Override
    public void start() {
        isRunning = true;
        System.out.println("Petrol engine started - Vroom! Engine warming up...");
    }
    
    @Override
    public void stop() {
        isRunning = false;
        System.out.println("Petrol engine stopped - Engine cooling down");
    }
    
    @Override
    public PerformanceMetrics accelerate(double targetSpeed, double fuelLevel, double batteryLevel) {
        double speedRatio = Math.min(targetSpeed / specs.getMaxSpeed(), 1.0);
        double powerOutput = specs.getMaxPower() * speedRatio;
        
        // Petrol engines are less efficient at low speeds
        double efficiency = specs.getEfficiencyRating() * (0.6 + 0.4 * speedRatio);
        
        // Higher fuel consumption at higher speeds
        double fuelConsumption = 2.0 + (speedRatio * 3.0);
        double batteryConsumption = 0.1; // Minimal battery use for electronics
        
        return new PerformanceMetrics(powerOutput, efficiency, fuelConsumption, batteryConsumption);
    }
    
    @Override
    public String getType() {
        return "V6 Petrol Engine";
    }
    
    @Override
    public String getStartupIssue(double fuelLevel, double batteryLevel) {
        if (fuelLevel <= 5.0) {
            return "Insufficient fuel level";
        }
        return "Unknown issue";
    }
    
    @Override
    public EngineSpecs getSpecs() {
        return specs;
    }
}
