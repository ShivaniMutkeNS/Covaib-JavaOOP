package composition.car;

/**
 * Hybrid Engine Implementation combining petrol and electric power
 */
public class HybridEngine implements Engine {
    private boolean isRunning;
    private final EngineSpecs specs;
    private boolean electricMode;
    
    public HybridEngine() {
        this.isRunning = false;
        this.specs = new EngineSpecs(350.0, 200.0, 85.0); // 350 HP combined, 200 km/h, 85% efficiency
        this.electricMode = true; // Start in electric mode for efficiency
    }
    
    @Override
    public boolean canStart(double fuelLevel, double batteryLevel) {
        return (fuelLevel > 5.0 || batteryLevel > 15.0); // Can start on either fuel or battery
    }
    
    @Override
    public void start() {
        isRunning = true;
        electricMode = true;
        System.out.println("Hybrid engine started - Starting in electric mode for optimal efficiency");
    }
    
    @Override
    public void stop() {
        isRunning = false;
        System.out.println("Hybrid engine stopped - Energy recovery systems activated");
    }
    
    @Override
    public PerformanceMetrics accelerate(double targetSpeed, double fuelLevel, double batteryLevel) {
        double speedRatio = Math.min(targetSpeed / specs.getMaxSpeed(), 1.0);
        
        // Switch modes based on conditions
        switchMode(targetSpeed, batteryLevel);
        
        double powerOutput = specs.getMaxPower() * speedRatio;
        double efficiency;
        double fuelConsumption;
        double batteryConsumption;
        
        if (electricMode && batteryLevel > 15.0) {
            // Electric mode - high efficiency, low speed
            efficiency = specs.getEfficiencyRating() * 1.1; // Bonus efficiency in electric mode
            fuelConsumption = 0.0;
            batteryConsumption = 1.0 + (speedRatio * 1.5);
        } else {
            // Petrol mode or combined mode
            efficiency = specs.getEfficiencyRating() * (0.8 + 0.2 * speedRatio);
            fuelConsumption = 1.5 + (speedRatio * 2.5);
            batteryConsumption = 0.2; // Charging battery while driving
        }
        
        return new PerformanceMetrics(powerOutput, efficiency, fuelConsumption, batteryConsumption);
    }
    
    private void switchMode(double targetSpeed, double batteryLevel) {
        boolean shouldUseElectric = (targetSpeed < 60.0 && batteryLevel > 15.0);
        
        if (shouldUseElectric != electricMode) {
            electricMode = shouldUseElectric;
            String mode = electricMode ? "electric" : "petrol";
            System.out.println("Hybrid system switched to " + mode + " mode");
        }
    }
    
    @Override
    public String getType() {
        return "Intelligent Hybrid Engine (Petrol + Electric)";
    }
    
    @Override
    public String getStartupIssue(double fuelLevel, double batteryLevel) {
        if (fuelLevel <= 5.0 && batteryLevel <= 15.0) {
            return "Both fuel and battery levels too low";
        }
        return "Unknown issue";
    }
    
    @Override
    public EngineSpecs getSpecs() {
        return specs;
    }
}
