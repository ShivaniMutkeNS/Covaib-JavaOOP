package composition.car;

/**
 * Engine Strategy Interface for Car Composition
 */
public interface Engine {
    boolean canStart(double fuelLevel, double batteryLevel);
    void start();
    void stop();
    PerformanceMetrics accelerate(double targetSpeed, double fuelLevel, double batteryLevel);
    String getType();
    String getStartupIssue(double fuelLevel, double batteryLevel);
    EngineSpecs getSpecs();
}
