package composition.car;

/**
 * MAANG-Level Car System using Composition with Engine Strategy Pattern
 * Demonstrates: Strategy Pattern, Runtime Engine Swapping, Performance Monitoring
 */
public class Car {
    private Engine engine;
    private final String model;
    private final String brand;
    private boolean isRunning;
    private double fuelLevel;
    private double batteryLevel;
    
    public Car(String brand, String model, Engine engine) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.isRunning = false;
        this.fuelLevel = 100.0;
        this.batteryLevel = 100.0;
    }
    
    // Runtime engine swapping - core composition flexibility
    public void swapEngine(Engine newEngine) {
        if (isRunning) {
            stop();
        }
        
        Engine oldEngine = this.engine;
        this.engine = newEngine;
        
        System.out.println("Engine swapped from " + oldEngine.getType() + " to " + newEngine.getType());
        System.out.println("Performance characteristics updated:");
        displayEngineSpecs();
    }
    
    public void start() {
        if (!isRunning && engine.canStart(fuelLevel, batteryLevel)) {
            isRunning = true;
            engine.start();
            System.out.println(brand + " " + model + " started with " + engine.getType());
        } else {
            System.out.println("Cannot start: " + engine.getStartupIssue(fuelLevel, batteryLevel));
        }
    }
    
    public void stop() {
        if (isRunning) {
            isRunning = false;
            engine.stop();
            System.out.println(brand + " " + model + " stopped");
        }
    }
    
    public void accelerate(double targetSpeed) {
        if (!isRunning) {
            System.out.println("Cannot accelerate: Car is not running");
            return;
        }
        
        PerformanceMetrics metrics = engine.accelerate(targetSpeed, fuelLevel, batteryLevel);
        
        // Update resource levels based on engine type
        fuelLevel = Math.max(0, fuelLevel - metrics.getFuelConsumption());
        batteryLevel = Math.max(0, batteryLevel - metrics.getBatteryConsumption());
        
        System.out.printf("Accelerating to %.1f km/h - Power: %.1f HP, Efficiency: %.2f%%\n",
                         targetSpeed, metrics.getPowerOutput(), metrics.getEfficiency());
        
        if (fuelLevel < 10 || batteryLevel < 10) {
            System.out.println("Warning: Low energy levels!");
        }
    }
    
    public void displayCarStatus() {
        System.out.println("\n=== " + brand + " " + model + " Status ===");
        System.out.println("Engine: " + engine.getType());
        System.out.println("Running: " + isRunning);
        System.out.println("Fuel Level: " + String.format("%.1f%%", fuelLevel));
        System.out.println("Battery Level: " + String.format("%.1f%%", batteryLevel));
        displayEngineSpecs();
    }
    
    private void displayEngineSpecs() {
        EngineSpecs specs = engine.getSpecs();
        System.out.println("Max Power: " + specs.getMaxPower() + " HP");
        System.out.println("Max Speed: " + specs.getMaxSpeed() + " km/h");
        System.out.println("Efficiency Rating: " + specs.getEfficiencyRating() + "%");
    }
    
    public void refuel() {
        fuelLevel = 100.0;
        System.out.println("Fuel tank refilled");
    }
    
    public void recharge() {
        batteryLevel = 100.0;
        System.out.println("Battery recharged");
    }
}
