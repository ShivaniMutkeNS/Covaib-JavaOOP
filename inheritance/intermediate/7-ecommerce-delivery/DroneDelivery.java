/**
 * Drone Delivery class extending Delivery
 * Demonstrates real-world constraints and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class DroneDelivery extends Delivery {
    private static final double BASE_COST = 25.99;
    private static final double FREE_SHIPPING_THRESHOLD = 200.0;
    private static final int DRONE_HOURS = 2;
    private String droneId;
    private String pilotId;
    private double weight;
    private String packageType;
    private boolean isFragile;
    private String specialInstructions;
    private String weatherCondition;
    private boolean isWeatherSuitable;
    private String flightPath;
    private double batteryLevel;
    private String deliveryZone;
    private boolean hasPermission;
    private String droneType;
    private double maxWeight;
    private double maxDistance;
    private String landingZone;
    
    /**
     * Constructor for DroneDelivery
     * @param orderId Unique order identifier
     * @param customerName Name of the customer
     * @param customerAddress Delivery address
     * @param customerPhone Customer phone number
     * @param orderValue Value of the order
     * @param droneId Drone identifier
     * @param pilotId Pilot identifier
     * @param weight Package weight in kg
     * @param packageType Type of package
     * @param isFragile Whether package is fragile
     * @param deliveryZone Delivery zone
     * @param droneType Type of drone
     */
    public DroneDelivery(String orderId, String customerName, String customerAddress, 
                        String customerPhone, double orderValue, String droneId, 
                        String pilotId, double weight, String packageType, boolean isFragile, 
                        String deliveryZone, String droneType) {
        super(orderId, customerName, customerAddress, customerPhone, orderValue, "Drone");
        this.droneId = droneId;
        this.pilotId = pilotId;
        this.weight = weight;
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.specialInstructions = "";
        this.weatherCondition = "Clear";
        this.isWeatherSuitable = true;
        this.flightPath = "";
        this.batteryLevel = 100.0;
        this.deliveryZone = deliveryZone;
        this.hasPermission = false;
        this.droneType = droneType;
        this.maxWeight = 5.0; // Default max weight
        this.maxDistance = 10.0; // Default max distance in km
        this.landingZone = "";
    }
    
    /**
     * Override estimateDeliveryTime method with drone delivery logic
     * @return String with estimated delivery time
     */
    @Override
    public String estimateDeliveryTime() {
        if (!isWeatherSuitable) {
            return "Weather not suitable for drone delivery";
        }
        
        if (!hasPermission) {
            return "Permission not granted for drone delivery";
        }
        
        if (weight > maxWeight) {
            return "Package too heavy for drone delivery";
        }
        
        int deliveryHours = DRONE_HOURS;
        
        // Adjust delivery time based on weight
        if (weight > 2.0) {
            deliveryHours += 1; // Heavy packages take longer
        }
        
        // Adjust delivery time based on package type
        if (packageType.equals("Large") || packageType.equals("Oversized")) {
            deliveryHours += 1;
        }
        
        // Adjust delivery time based on fragility
        if (isFragile) {
            deliveryHours += 1; // Fragile packages need extra care
        }
        
        // Adjust delivery time based on weather
        if (!weatherCondition.equals("Clear")) {
            deliveryHours += 2; // Bad weather delays delivery
        }
        
        // Adjust delivery time based on battery level
        if (batteryLevel < 50.0) {
            deliveryHours += 1; // Low battery requires charging
        }
        
        java.time.LocalDateTime estimatedTime = java.time.LocalDateTime.now().plusHours(deliveryHours);
        return estimatedTime.toString();
    }
    
    /**
     * Override calculateCost method with drone delivery pricing
     * @return The calculated delivery cost
     */
    @Override
    public double calculateCost() {
        if (isEligibleForFreeShipping()) {
            return 0.0;
        }
        
        if (!isWeatherSuitable) {
            return -1.0; // Cannot deliver in bad weather
        }
        
        if (!hasPermission) {
            return -1.0; // Cannot deliver without permission
        }
        
        if (weight > maxWeight) {
            return -1.0; // Cannot deliver heavy packages
        }
        
        double cost = BASE_COST;
        
        // Add weight-based surcharge
        if (weight > 1.0) {
            cost += (weight - 1.0) * 2.0; // $2.00 per kg over 1kg
        }
        
        // Add package type surcharge
        switch (packageType.toLowerCase()) {
            case "large":
                cost += 5.0;
                break;
            case "oversized":
                cost += 10.0;
                break;
            case "small":
                cost -= 3.0;
                break;
        }
        
        // Add fragility surcharge
        if (isFragile) {
            cost += 8.0; // Extra handling fee for fragile items
        }
        
        // Add weather surcharge
        if (!weatherCondition.equals("Clear")) {
            cost += 5.0; // Weather risk surcharge
        }
        
        // Add battery surcharge
        if (batteryLevel < 50.0) {
            cost += 3.0; // Battery charging surcharge
        }
        
        // Add zone surcharge
        if (deliveryZone.equals("Remote") || deliveryZone.equals("Rural")) {
            cost += 10.0; // Remote area surcharge
        }
        
        return Math.max(0.0, cost); // Ensure non-negative cost
    }
    
    /**
     * Override getDeliveryFeatures method with drone delivery features
     * @return String description of drone delivery features
     */
    @Override
    public String getDeliveryFeatures() {
        return "Drone Delivery Features: " +
               "Drone ID: " + droneId + ", " +
               "Pilot ID: " + pilotId + ", " +
               "Drone Type: " + droneType + ", " +
               "Weight: " + String.format("%.1f", weight) + " kg, " +
               "Package Type: " + packageType + ", " +
               "Fragile: " + (isFragile ? "Yes" : "No") + ", " +
               "Weather: " + weatherCondition + ", " +
               "Battery: " + String.format("%.1f", batteryLevel) + "%, " +
               "Zone: " + deliveryZone + ", " +
               "Permission: " + (hasPermission ? "Yes" : "No") + ", " +
               "Max Weight: " + String.format("%.1f", maxWeight) + " kg, " +
               "Max Distance: " + String.format("%.1f", maxDistance) + " km, " +
               "Free Shipping Threshold: $" + String.format("%.2f", FREE_SHIPPING_THRESHOLD) + ", " +
               "Base Cost: $" + String.format("%.2f", BASE_COST);
    }
    
    /**
     * Override getFreeShippingThreshold method with drone delivery threshold
     * @return The minimum order value for free shipping
     */
    @Override
    public double getFreeShippingThreshold() {
        return FREE_SHIPPING_THRESHOLD;
    }
    
    /**
     * Drone delivery specific method to check weather suitability
     * @param weatherCondition Current weather condition
     * @return True if weather is suitable for drone delivery
     */
    public boolean checkWeatherSuitability(String weatherCondition) {
        this.weatherCondition = weatherCondition;
        
        switch (weatherCondition.toLowerCase()) {
            case "clear":
            case "sunny":
            case "partly cloudy":
                isWeatherSuitable = true;
                break;
            case "rainy":
            case "stormy":
            case "windy":
            case "foggy":
                isWeatherSuitable = false;
                break;
            default:
                isWeatherSuitable = false;
        }
        
        System.out.println("Weather condition: " + weatherCondition + ", Suitable: " + (isWeatherSuitable ? "Yes" : "No"));
        return isWeatherSuitable;
    }
    
    /**
     * Drone delivery specific method to request permission
     * @return True if permission granted
     */
    public boolean requestPermission() {
        // Simulate permission request
        hasPermission = weight <= maxWeight && isWeatherSuitable && batteryLevel >= 30.0;
        
        if (hasPermission) {
            System.out.println("Permission granted for drone delivery");
        } else {
            System.out.println("Permission denied for drone delivery");
        }
        
        return hasPermission;
    }
    
    /**
     * Drone delivery specific method to set flight path
     * @param flightPath Flight path coordinates
     * @return True if flight path set successfully
     */
    public boolean setFlightPath(String flightPath) {
        if (flightPath == null || flightPath.trim().isEmpty()) {
            System.out.println("Error: Flight path cannot be empty");
            return false;
        }
        
        this.flightPath = flightPath;
        System.out.println("Flight path set: " + flightPath);
        return true;
    }
    
    /**
     * Drone delivery specific method to set landing zone
     * @param landingZone Landing zone coordinates
     * @return True if landing zone set successfully
     */
    public boolean setLandingZone(String landingZone) {
        if (landingZone == null || landingZone.trim().isEmpty()) {
            System.out.println("Error: Landing zone cannot be empty");
            return false;
        }
        
        this.landingZone = landingZone;
        System.out.println("Landing zone set: " + landingZone);
        return true;
    }
    
    /**
     * Drone delivery specific method to check battery level
     * @return True if battery level is sufficient
     */
    public boolean checkBatteryLevel() {
        boolean sufficient = batteryLevel >= 30.0;
        System.out.println("Battery level: " + String.format("%.1f", batteryLevel) + "%, Sufficient: " + (sufficient ? "Yes" : "No"));
        return sufficient;
    }
    
    /**
     * Drone delivery specific method to get drone status
     * @return String with drone status
     */
    public String getDroneStatus() {
        return String.format("Drone: %s, Pilot: %s, Battery: %.1f%%, Weather: %s, Permission: %s", 
                           droneId, pilotId, batteryLevel, weatherCondition, hasPermission ? "Yes" : "No");
    }
    
    /**
     * Drone delivery specific method to get flight details
     * @return String with flight details
     */
    public String getFlightDetails() {
        return String.format("Flight: %s, Path: %s, Landing: %s, Zone: %s", 
                           droneId, flightPath, landingZone, deliveryZone);
    }
    
    /**
     * Drone delivery specific method to check if delivery is possible
     * @return True if delivery is possible
     */
    public boolean isDeliveryPossible() {
        return isWeatherSuitable && hasPermission && weight <= maxWeight && batteryLevel >= 30.0;
    }
    
    /**
     * Getter for drone ID
     * @return The drone ID
     */
    public String getDroneId() {
        return droneId;
    }
    
    /**
     * Getter for pilot ID
     * @return The pilot ID
     */
    public String getPilotId() {
        return pilotId;
    }
    
    /**
     * Getter for weight
     * @return The package weight
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Getter for package type
     * @return The package type
     */
    public String getPackageType() {
        return packageType;
    }
    
    /**
     * Getter for fragile status
     * @return True if package is fragile
     */
    public boolean isFragile() {
        return isFragile;
    }
    
    /**
     * Getter for special instructions
     * @return The special instructions
     */
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    /**
     * Getter for weather condition
     * @return The weather condition
     */
    public String getWeatherCondition() {
        return weatherCondition;
    }
    
    /**
     * Getter for weather suitability
     * @return True if weather is suitable
     */
    public boolean isWeatherSuitable() {
        return isWeatherSuitable;
    }
    
    /**
     * Getter for flight path
     * @return The flight path
     */
    public String getFlightPath() {
        return flightPath;
    }
    
    /**
     * Getter for battery level
     * @return The battery level
     */
    public double getBatteryLevel() {
        return batteryLevel;
    }
    
    /**
     * Getter for delivery zone
     * @return The delivery zone
     */
    public String getDeliveryZone() {
        return deliveryZone;
    }
    
    /**
     * Getter for permission status
     * @return True if permission is granted
     */
    public boolean hasPermission() {
        return hasPermission;
    }
    
    /**
     * Getter for drone type
     * @return The drone type
     */
    public String getDroneType() {
        return droneType;
    }
    
    /**
     * Getter for max weight
     * @return The maximum weight
     */
    public double getMaxWeight() {
        return maxWeight;
    }
    
    /**
     * Getter for max distance
     * @return The maximum distance
     */
    public double getMaxDistance() {
        return maxDistance;
    }
    
    /**
     * Getter for landing zone
     * @return The landing zone
     */
    public String getLandingZone() {
        return landingZone;
    }
    
    /**
     * Override toString to include drone delivery specific details
     * @return String representation of the drone delivery
     */
    @Override
    public String toString() {
        return super.toString() + " [Drone: " + droneId + ", Pilot: " + pilotId + ", Weather: " + weatherCondition + "]";
    }
}
