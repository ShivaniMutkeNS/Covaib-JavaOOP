/**
 * Standard Delivery class extending Delivery
 * Demonstrates real-world constraints and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class StandardDelivery extends Delivery {
    private static final double BASE_COST = 5.99;
    private static final double FREE_SHIPPING_THRESHOLD = 50.0;
    private static final int STANDARD_DAYS = 5;
    private String carrier;
    private boolean requiresSignature;
    private String deliveryWindow;
    private double weight;
    private String packageType;
    private boolean isFragile;
    private String specialInstructions;
    
    /**
     * Constructor for StandardDelivery
     * @param orderId Unique order identifier
     * @param customerName Name of the customer
     * @param customerAddress Delivery address
     * @param customerPhone Customer phone number
     * @param orderValue Value of the order
     * @param carrier Delivery carrier
     * @param weight Package weight in kg
     * @param packageType Type of package
     * @param isFragile Whether package is fragile
     */
    public StandardDelivery(String orderId, String customerName, String customerAddress, 
                           String customerPhone, double orderValue, String carrier, 
                           double weight, String packageType, boolean isFragile) {
        super(orderId, customerName, customerAddress, customerPhone, orderValue, "Standard");
        this.carrier = carrier;
        this.requiresSignature = false;
        this.deliveryWindow = "9 AM - 6 PM";
        this.weight = weight;
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.specialInstructions = "";
    }
    
    /**
     * Override estimateDeliveryTime method with standard delivery logic
     * @return String with estimated delivery time
     */
    @Override
    public String estimateDeliveryTime() {
        int deliveryDays = STANDARD_DAYS;
        
        // Adjust delivery time based on weight
        if (weight > 10.0) {
            deliveryDays += 1; // Heavy packages take longer
        }
        
        // Adjust delivery time based on package type
        if (packageType.equals("Large") || packageType.equals("Oversized")) {
            deliveryDays += 1;
        }
        
        // Adjust delivery time based on fragility
        if (isFragile) {
            deliveryDays += 1; // Fragile packages need extra care
        }
        
        java.time.LocalDate estimatedDate = java.time.LocalDate.now().plusDays(deliveryDays);
        return estimatedDate.toString();
    }
    
    /**
     * Override calculateCost method with standard delivery pricing
     * @return The calculated delivery cost
     */
    @Override
    public double calculateCost() {
        if (isEligibleForFreeShipping()) {
            return 0.0;
        }
        
        double cost = BASE_COST;
        
        // Add weight-based surcharge
        if (weight > 5.0) {
            cost += (weight - 5.0) * 0.5; // $0.50 per kg over 5kg
        }
        
        // Add package type surcharge
        switch (packageType.toLowerCase()) {
            case "large":
                cost += 2.0;
                break;
            case "oversized":
                cost += 5.0;
                break;
            case "small":
                cost -= 1.0;
                break;
        }
        
        // Add fragility surcharge
        if (isFragile) {
            cost += 3.0; // Extra handling fee for fragile items
        }
        
        // Add signature requirement surcharge
        if (requiresSignature) {
            cost += 1.0;
        }
        
        return Math.max(0.0, cost); // Ensure non-negative cost
    }
    
    /**
     * Override getDeliveryFeatures method with standard delivery features
     * @return String description of standard delivery features
     */
    @Override
    public String getDeliveryFeatures() {
        return "Standard Delivery Features: " +
               "Carrier: " + carrier + ", " +
               "Delivery Window: " + deliveryWindow + ", " +
               "Signature Required: " + (requiresSignature ? "Yes" : "No") + ", " +
               "Weight: " + String.format("%.1f", weight) + " kg, " +
               "Package Type: " + packageType + ", " +
               "Fragile: " + (isFragile ? "Yes" : "No") + ", " +
               "Free Shipping Threshold: $" + String.format("%.2f", FREE_SHIPPING_THRESHOLD) + ", " +
               "Base Cost: $" + String.format("%.2f", BASE_COST);
    }
    
    /**
     * Override getFreeShippingThreshold method with standard delivery threshold
     * @return The minimum order value for free shipping
     */
    @Override
    public double getFreeShippingThreshold() {
        return FREE_SHIPPING_THRESHOLD;
    }
    
    /**
     * Standard delivery specific method to set signature requirement
     * @param requiresSignature Whether signature is required
     * @return True if signature requirement set successfully
     */
    public boolean setSignatureRequirement(boolean requiresSignature) {
        this.requiresSignature = requiresSignature;
        System.out.println("Signature requirement set to: " + (requiresSignature ? "Yes" : "No"));
        return true;
    }
    
    /**
     * Standard delivery specific method to set delivery window
     * @param deliveryWindow Delivery time window
     * @return True if delivery window set successfully
     */
    public boolean setDeliveryWindow(String deliveryWindow) {
        if (deliveryWindow == null || deliveryWindow.trim().isEmpty()) {
            System.out.println("Error: Delivery window cannot be empty");
            return false;
        }
        
        this.deliveryWindow = deliveryWindow;
        System.out.println("Delivery window set to: " + deliveryWindow);
        return true;
    }
    
    /**
     * Standard delivery specific method to add special instructions
     * @param instructions Special delivery instructions
     * @return True if instructions added successfully
     */
    public boolean addSpecialInstructions(String instructions) {
        if (instructions == null || instructions.trim().isEmpty()) {
            System.out.println("Error: Instructions cannot be empty");
            return false;
        }
        
        this.specialInstructions = instructions;
        System.out.println("Special instructions added: " + instructions);
        return true;
    }
    
    /**
     * Standard delivery specific method to get package details
     * @return String with package details
     */
    public String getPackageDetails() {
        return String.format("Package: %s, Weight: %.1f kg, Type: %s, Fragile: %s, Carrier: %s", 
                           orderId, weight, packageType, isFragile ? "Yes" : "No", carrier);
    }
    
    /**
     * Standard delivery specific method to check if package is oversized
     * @return True if package is oversized
     */
    public boolean isOversized() {
        return packageType.equals("Oversized") || weight > 15.0;
    }
    
    /**
     * Standard delivery specific method to get delivery timeline
     * @return String with delivery timeline
     */
    public String getDeliveryTimeline() {
        return String.format("Order Date: %s, Estimated Delivery: %s, Delivery Window: %s", 
                           orderDate, estimatedDeliveryDate, deliveryWindow);
    }
    
    /**
     * Getter for carrier
     * @return The delivery carrier
     */
    public String getCarrier() {
        return carrier;
    }
    
    /**
     * Getter for signature requirement
     * @return True if signature is required
     */
    public boolean requiresSignature() {
        return requiresSignature;
    }
    
    /**
     * Getter for delivery window
     * @return The delivery window
     */
    public String getDeliveryWindow() {
        return deliveryWindow;
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
     * Override toString to include standard delivery specific details
     * @return String representation of the standard delivery
     */
    @Override
    public String toString() {
        return super.toString() + " [Carrier: " + carrier + ", Weight: " + String.format("%.1f", weight) + " kg, Type: " + packageType + "]";
    }
}
