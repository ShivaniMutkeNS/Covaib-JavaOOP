package composition.car;

/**
 * Engine specifications data class
 */
public class EngineSpecs {
    private final double maxPower;
    private final double maxSpeed;
    private final double efficiencyRating;
    
    public EngineSpecs(double maxPower, double maxSpeed, double efficiencyRating) {
        this.maxPower = maxPower;
        this.maxSpeed = maxSpeed;
        this.efficiencyRating = efficiencyRating;
    }
    
    public double getMaxPower() { return maxPower; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getEfficiencyRating() { return efficiencyRating; }
}
