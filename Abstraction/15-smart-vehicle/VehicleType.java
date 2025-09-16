/**
 * Enumeration of different vehicle types with their characteristics
 * Defines capabilities, power sources, and operational parameters
 */
public enum VehicleType {
    SEDAN("Sedan", "Passenger car", 4, 5, FuelType.GASOLINE, 180, false),
    SUV("SUV", "Sport utility vehicle", 4, 7, FuelType.GASOLINE, 160, true),
    TRUCK("Truck", "Commercial truck", 6, 3, FuelType.DIESEL, 140, true),
    ELECTRIC_CAR("Electric Car", "Battery electric vehicle", 4, 5, FuelType.ELECTRIC, 200, false),
    HYBRID("Hybrid", "Hybrid electric vehicle", 4, 5, FuelType.HYBRID, 190, false),
    MOTORCYCLE("Motorcycle", "Two-wheeled vehicle", 2, 2, FuelType.GASOLINE, 220, false),
    BUS("Bus", "Public transport bus", 6, 50, FuelType.DIESEL, 120, true),
    AUTONOMOUS_TAXI("Autonomous Taxi", "Self-driving taxi", 4, 4, FuelType.ELECTRIC, 180, false);
    
    private final String displayName;
    private final String description;
    private final int wheelCount;
    private final int passengerCapacity;
    private final FuelType primaryFuelType;
    private final int maxSpeedKmh;
    private final boolean supportsHeavyLoad;
    
    VehicleType(String displayName, String description, int wheelCount, int passengerCapacity,
                FuelType primaryFuelType, int maxSpeedKmh, boolean supportsHeavyLoad) {
        this.displayName = displayName;
        this.description = description;
        this.wheelCount = wheelCount;
        this.passengerCapacity = passengerCapacity;
        this.primaryFuelType = primaryFuelType;
        this.maxSpeedKmh = maxSpeedKmh;
        this.supportsHeavyLoad = supportsHeavyLoad;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getWheelCount() {
        return wheelCount;
    }
    
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
    
    public FuelType getPrimaryFuelType() {
        return primaryFuelType;
    }
    
    public int getMaxSpeedKmh() {
        return maxSpeedKmh;
    }
    
    public boolean supportsHeavyLoad() {
        return supportsHeavyLoad;
    }
    
    public boolean isElectric() {
        return primaryFuelType == FuelType.ELECTRIC || primaryFuelType == FuelType.HYBRID;
    }
    
    public boolean isAutonomousCapable() {
        return this == AUTONOMOUS_TAXI || isElectric();
    }
    
    public String getVehicleClass() {
        if (passengerCapacity <= 2) return "Compact";
        else if (passengerCapacity <= 5) return "Standard";
        else if (passengerCapacity <= 10) return "Large";
        else return "Commercial";
    }
    
    @Override
    public String toString() {
        return displayName + " (" + getVehicleClass() + ")";
    }
}

/**
 * Enumeration of fuel/power source types
 */
enum FuelType {
    GASOLINE("Gasoline", "Internal combustion engine", 8.5, false),
    DIESEL("Diesel", "Diesel engine", 12.0, false),
    ELECTRIC("Electric", "Battery electric motor", 25.0, true),
    HYBRID("Hybrid", "Gasoline-electric hybrid", 18.0, true),
    HYDROGEN("Hydrogen", "Fuel cell electric", 22.0, true);
    
    private final String displayName;
    private final String description;
    private final double efficiency; // km per unit (liter/kWh)
    private final boolean isEcoFriendly;
    
    FuelType(String displayName, String description, double efficiency, boolean isEcoFriendly) {
        this.displayName = displayName;
        this.description = description;
        this.efficiency = efficiency;
        this.isEcoFriendly = isEcoFriendly;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public double getEfficiency() { return efficiency; }
    public boolean isEcoFriendly() { return isEcoFriendly; }
}
