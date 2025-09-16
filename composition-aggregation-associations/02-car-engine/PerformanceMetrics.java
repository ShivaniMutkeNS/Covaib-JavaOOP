package composition.car;

/**
 * Performance metrics returned by engine operations
 */
public class PerformanceMetrics {
    private final double powerOutput;
    private final double efficiency;
    private final double fuelConsumption;
    private final double batteryConsumption;
    
    public PerformanceMetrics(double powerOutput, double efficiency, 
                            double fuelConsumption, double batteryConsumption) {
        this.powerOutput = powerOutput;
        this.efficiency = efficiency;
        this.fuelConsumption = fuelConsumption;
        this.batteryConsumption = batteryConsumption;
    }
    
    public double getPowerOutput() { return powerOutput; }
    public double getEfficiency() { return efficiency; }
    public double getFuelConsumption() { return fuelConsumption; }
    public double getBatteryConsumption() { return batteryConsumption; }
}
