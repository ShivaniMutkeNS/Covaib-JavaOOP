package composition.car;

/**
 * Electric Engine Implementation with battery management
 */
public class ElectricEngine implements Engine {
    private boolean isRunning;
    private final EngineSpecs specs;
    
    public ElectricEngine() {
        this.isRunning = false;
        this.specs = new EngineSpecs(400.0, 250.0, 95.0); // 400 HP, 250 km/h, 95% efficiency
    }
    
    @Override
    public boolean canStart(double fuelLevel, double batteryLevel) {
        return batteryLevel > 10.0; // Needs minimum battery charge
    }
    
    @Override
    public void start() {
        isRunning = true;
        System.out.println("Electric engine started - Silent operation, instant torque available");
    }
    
    @Override
    public void stop() {
        isRunning = false;
        System.out.println("Electric engine stopped - Regenerative braking activated");
    }
    
    @Override
    public PerformanceMetrics accelerate(double targetSpeed, double fuelLevel, double batteryLevel) {
        double speedRatio = Math.min(targetSpeed / specs.getMaxSpeed(), 1.0);
        double powerOutput = specs.getMaxPower() * speedRatio;
        
        // Electric engines maintain high efficiency across speed ranges
        double efficiency = specs.getEfficiencyRating() * (0.9 + 0.1 * (1.0 - speedRatio));
        
        // Battery consumption increases with speed but more efficiently than petrol
        double batteryConsumption = 1.5 + (speedRatio * 2.0);
        double fuelConsumption = 0.0; // No fuel consumption
        
        return new PerformanceMetrics(powerOutput, efficiency, fuelConsumption, batteryConsumption);
    }
    
    @Override
    public String getType() {
        return "High-Performance Electric Motor";
    }
    
    @Override
    public String getStartupIssue(double fuelLevel, double batteryLevel) {
        if (batteryLevel <= 10.0) {
            return "Insufficient battery charge";
        }
        return "Unknown issue";
    }
    
    @Override
    public EngineSpecs getSpecs() {
        return specs;
    }
}
