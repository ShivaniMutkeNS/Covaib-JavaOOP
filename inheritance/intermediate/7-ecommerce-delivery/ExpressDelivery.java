/**
 * Express Delivery class extending Delivery
 * Demonstrates real-world constraints and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class ExpressDelivery extends Delivery {
    private static final double BASE_COST = 15.99;
    private static final double FREE_SHIPPING_THRESHOLD = 100.0;
    private static final int EXPRESS_DAYS = 2;
    private String carrier;
    private boolean requiresSignature;
    private String deliveryWindow;
    private double weight;
    private String packageType;
    private boolean isFragile;
    private String specialInstructions;
    private boolean isPriority;
    private String expressType;
    private boolean hasInsurance;
    private double insuranceValue;
    
    /**
     * Constructor for ExpressDelivery
     * @param orderId Unique order identifier
     * @param customerName Name of the customer
     * @param customerAddress Delivery address
     * @param customerPhone Customer phone number
     * @param orderValue Value of the order
     * @param carrier Delivery carrier
     * @param weight Package weight in kg
     * @param packageType Type of package
     * @param isFragile Whether package is fragile
     * @param expressType Type of express delivery (Next Day, Same Day, 2-Day)
     */
    public ExpressDelivery(String orderId, String customerName, String customerAddress, 
                          String customerPhone, double orderValue, String carrier, 
                          double weight, String packageType, boolean isFragile, String expressType) {
        super(orderId, customerName, customerAddress, customerPhone, orderValue, "Express");
        this.carrier = carrier;
        this.requiresSignature = true; // Express deliveries typically require signature
        this.deliveryWindow = "9 AM - 5 PM";
        this.weight = weight;
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.specialInstructions = "";
        this.isPriority = false;
        this.expressType = expressType;
        this.hasInsurance = false;
        this.insuranceValue = 0.0;
    }
    
    /**
     * Override estimateDeliveryTime method with express delivery logic
     * @return String with estimated delivery time
     */
    @Override
    public String estimateDeliveryTime() {
        int deliveryDays = EXPRESS_DAYS;
        
        // Adjust delivery time based on express type
        switch (expressType.toLowerCase()) {
            case "same day":
                deliveryDays = 0;
                break;
            case "next day":
                deliveryDays = 1;
                break;
            case "2-day":
                deliveryDays = 2;
                break;
        }
        
        // Adjust delivery time based on weight
        if (weight > 5.0) {
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
        
        // Priority deliveries are faster
        if (isPriority) {
            deliveryDays = Math.max(0, deliveryDays - 1);
        }
        
        java.time.LocalDate estimatedDate = java.time.LocalDate.now().plusDays(deliveryDays);
        return estimatedDate.toString();
    }
    
    /**
     * Override calculateCost method with express delivery pricing
     * @return The calculated delivery cost
     */
    @Override
    public double calculateCost() {
        if (isEligibleForFreeShipping()) {
            return 0.0;
        }
        
        double cost = BASE_COST;
        
        // Add express type surcharge
        switch (expressType.toLowerCase()) {
            case "same day":
                cost += 20.0;
                break;
            case "next day":
                cost += 10.0;
                break;
            case "2-day":
                cost += 5.0;
                break;
        }
        
        // Add weight-based surcharge
        if (weight > 3.0) {
            cost += (weight - 3.0) * 1.0; // $1.00 per kg over 3kg
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
                cost -= 2.0;
                break;
        }
        
        // Add fragility surcharge
        if (isFragile) {
            cost += 5.0; // Extra handling fee for fragile items
        }
        
        // Add signature requirement surcharge
        if (requiresSignature) {
            cost += 2.0;
        }
        
        // Add priority surcharge
        if (isPriority) {
            cost += 15.0;
        }
        
        // Add insurance cost
        if (hasInsurance) {
            cost += insuranceValue * 0.01; // 1% of insured value
        }
        
        return Math.max(0.0, cost); // Ensure non-negative cost
    }
    
    /**
     * Override getDeliveryFeatures method with express delivery features
     * @return String description of express delivery features
     */
    @Override
    public String getDeliveryFeatures() {
        return "Express Delivery Features: " +
               "Carrier: " + carrier + ", " +
               "Express Type: " + expressType + ", " +
               "Delivery Window: " + deliveryWindow + ", " +
               "Signature Required: " + (requiresSignature ? "Yes" : "No") + ", " +
               "Weight: " + String.format("%.1f", weight) + " kg, " +
               "Package Type: " + packageType + ", " +
               "Fragile: " + (isFragile ? "Yes" : "No") + ", " +
               "Priority: " + (isPriority ? "Yes" : "No") + ", " +
               "Insurance: " + (hasInsurance ? "Yes ($" + String.format("%.2f", insuranceValue) + ")" : "No") + ", " +
               "Free Shipping Threshold: $" + String.format("%.2f", FREE_SHIPPING_THRESHOLD) + ", " +
               "Base Cost: $" + String.format("%.2f", BASE_COST);
    }
    
    /**
     * Override getFreeShippingThreshold method with express delivery threshold
     * @return The minimum order value for free shipping
     */
    @Override
    public double getFreeShippingThreshold() {
        return FREE_SHIPPING_THRESHOLD;
    }
    
    /**
     * Express delivery specific method to set priority
     * @param isPriority Whether delivery is priority
     * @return True if priority set successfully
     */
    public boolean setPriority(boolean isPriority) {
        this.isPriority = isPriority;
        System.out.println("Priority delivery set to: " + (isPriority ? "Yes" : "No"));
        return true;
    }
    
    /**
     * Express delivery specific method to add insurance
     * @param insuranceValue Value to insure
     * @return True if insurance added successfully
     */
    public boolean addInsurance(double insuranceValue) {
        if (insuranceValue <= 0) {
            System.out.println("Error: Insurance value must be positive");
            return false;
        }
        
        this.hasInsurance = true;
        this.insuranceValue = insuranceValue;
        System.out.println("Insurance added: $" + String.format("%.2f", insuranceValue));
        return true;
    }
    
    /**
     * Express delivery specific method to set delivery window
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
     * Express delivery specific method to add special instructions
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
     * Express delivery specific method to get package details
     * @return String with package details
     */
    public String getPackageDetails() {
        return String.format("Package: %s, Weight: %.1f kg, Type: %s, Fragile: %s, Carrier: %s, Express: %s", 
                           orderId, weight, packageType, isFragile ? "Yes" : "No", carrier, expressType);
    }
    
    /**
     * Express delivery specific method to check if delivery is urgent
     * @return True if delivery is urgent
     */
    public boolean isUrgent() {
        return expressType.equals("Same Day") || isPriority;
    }
    
    /**
     * Express delivery specific method to get delivery timeline
     * @return String with delivery timeline
     */
    public String getDeliveryTimeline() {
        return String.format("Order Date: %s, Estimated Delivery: %s, Express Type: %s, Delivery Window: %s", 
                           orderDate, estimatedDeliveryDate, expressType, deliveryWindow);
    }
    
    /**
     * Express delivery specific method to get express features
     * @return String with express features
     */
    public String getExpressFeatures() {
        return String.format("Express Features: Type: %s, Priority: %s, Insurance: %s, Signature: %s", 
                           expressType, isPriority ? "Yes" : "No", hasInsurance ? "Yes" : "No", requiresSignature ? "Yes" : "No");
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
     * Getter for priority status
     * @return True if delivery is priority
     */
    public boolean isPriority() {
        return isPriority;
    }
    
    /**
     * Getter for express type
     * @return The express type
     */
    public String getExpressType() {
        return expressType;
    }
    
    /**
     * Getter for insurance status
     * @return True if package is insured
     */
    public boolean hasInsurance() {
        return hasInsurance;
    }
    
    /**
     * Getter for insurance value
     * @return The insurance value
     */
    public double getInsuranceValue() {
        return insuranceValue;
    }
    
    /**
     * Override toString to include express delivery specific details
     * @return String representation of the express delivery
     */
    @Override
    public String toString() {
        return super.toString() + " [Carrier: " + carrier + ", Express: " + expressType + ", Priority: " + isPriority + "]";
    }
}
