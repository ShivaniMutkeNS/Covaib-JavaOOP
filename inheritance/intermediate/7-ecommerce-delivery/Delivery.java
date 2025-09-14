/**
 * Abstract base class for all delivery types
 * Demonstrates real-world constraints and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Delivery {
    protected String orderId;
    protected String customerName;
    protected String customerAddress;
    protected String customerPhone;
    protected String deliveryType;
    protected double orderValue;
    protected double deliveryCost;
    protected String status;
    protected String orderDate;
    protected String estimatedDeliveryDate;
    protected String actualDeliveryDate;
    protected String trackingNumber;
    protected boolean isDelivered;
    protected String deliveryNotes;
    
    /**
     * Constructor for Delivery
     * @param orderId Unique order identifier
     * @param customerName Name of the customer
     * @param customerAddress Delivery address
     * @param customerPhone Customer phone number
     * @param orderValue Value of the order
     * @param deliveryType Type of delivery
     */
    public Delivery(String orderId, String customerName, String customerAddress, 
                   String customerPhone, double orderValue, String deliveryType) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.orderValue = orderValue;
        this.deliveryType = deliveryType;
        this.deliveryCost = 0.0;
        this.status = "Processing";
        this.orderDate = java.time.LocalDate.now().toString();
        this.estimatedDeliveryDate = "";
        this.actualDeliveryDate = "";
        this.trackingNumber = "";
        this.isDelivered = false;
        this.deliveryNotes = "";
    }
    
    /**
     * Abstract method to estimate delivery time
     * Each delivery type has different time estimation logic
     * @return String with estimated delivery time
     */
    public abstract String estimateDeliveryTime();
    
    /**
     * Abstract method to calculate delivery cost
     * Each delivery type has different cost calculation logic
     * @return The calculated delivery cost
     */
    public abstract double calculateCost();
    
    /**
     * Abstract method to get delivery features
     * Each delivery type has different features
     * @return String description of delivery features
     */
    public abstract String getDeliveryFeatures();
    
    /**
     * Concrete method to process delivery
     * @return True if delivery processed successfully
     */
    public boolean processDelivery() {
        if (status.equals("Processing")) {
            status = "Shipped";
            trackingNumber = generateTrackingNumber();
            estimatedDeliveryDate = estimateDeliveryTime();
            deliveryCost = calculateCost();
            
            System.out.println("Delivery processed successfully!");
            System.out.println("Tracking Number: " + trackingNumber);
            System.out.println("Estimated Delivery: " + estimatedDeliveryDate);
            System.out.println("Delivery Cost: $" + String.format("%.2f", deliveryCost));
            
            return true;
        }
        
        System.out.println("Delivery cannot be processed. Current status: " + status);
        return false;
    }
    
    /**
     * Concrete method to mark delivery as delivered
     * @param deliveryNotes Notes about the delivery
     * @return True if delivery marked as delivered
     */
    public boolean markAsDelivered(String deliveryNotes) {
        if (status.equals("Shipped") || status.equals("Out for Delivery")) {
            status = "Delivered";
            isDelivered = true;
            actualDeliveryDate = java.time.LocalDate.now().toString();
            this.deliveryNotes = deliveryNotes;
            
            System.out.println("Delivery marked as delivered!");
            System.out.println("Actual Delivery Date: " + actualDeliveryDate);
            System.out.println("Delivery Notes: " + deliveryNotes);
            
            return true;
        }
        
        System.out.println("Delivery cannot be marked as delivered. Current status: " + status);
        return false;
    }
    
    /**
     * Concrete method to update delivery status
     * @param newStatus New status
     * @return True if status updated successfully
     */
    public boolean updateStatus(String newStatus) {
        if (isValidStatusTransition(status, newStatus)) {
            status = newStatus;
            System.out.println("Status updated to: " + newStatus);
            return true;
        }
        
        System.out.println("Invalid status transition from " + status + " to " + newStatus);
        return false;
    }
    
    /**
     * Concrete method to get delivery information
     * @return String with delivery details
     */
    public String getDeliveryInfo() {
        return String.format("Order: %s, Customer: %s, Type: %s, Status: %s, Cost: $%.2f, Delivered: %s", 
                           orderId, customerName, deliveryType, status, deliveryCost, isDelivered);
    }
    
    /**
     * Concrete method to check if delivery is eligible for free shipping
     * @return True if delivery is eligible for free shipping
     */
    public boolean isEligibleForFreeShipping() {
        return orderValue >= getFreeShippingThreshold();
    }
    
    /**
     * Abstract method to get free shipping threshold
     * Each delivery type has different free shipping requirements
     * @return The minimum order value for free shipping
     */
    public abstract double getFreeShippingThreshold();
    
    /**
     * Concrete method to generate tracking number
     * @return Generated tracking number
     */
    private String generateTrackingNumber() {
        return "TRK" + orderId + System.currentTimeMillis() % 10000;
    }
    
    /**
     * Concrete method to validate status transition
     * @param currentStatus Current status
     * @param newStatus New status
     * @return True if transition is valid
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "Processing":
                return newStatus.equals("Shipped") || newStatus.equals("Cancelled");
            case "Shipped":
                return newStatus.equals("Out for Delivery") || newStatus.equals("Delivered") || newStatus.equals("Returned");
            case "Out for Delivery":
                return newStatus.equals("Delivered") || newStatus.equals("Failed Delivery");
            case "Delivered":
                return newStatus.equals("Returned");
            case "Failed Delivery":
                return newStatus.equals("Out for Delivery") || newStatus.equals("Returned");
            case "Returned":
                return newStatus.equals("Processing");
            case "Cancelled":
                return false; // Cannot change from cancelled
            default:
                return false;
        }
    }
    
    /**
     * Getter for order ID
     * @return The order ID
     */
    public String getOrderId() {
        return orderId;
    }
    
    /**
     * Getter for customer name
     * @return The customer name
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * Getter for customer address
     * @return The customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }
    
    /**
     * Getter for customer phone
     * @return The customer phone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    /**
     * Getter for delivery type
     * @return The delivery type
     */
    public String getDeliveryType() {
        return deliveryType;
    }
    
    /**
     * Getter for order value
     * @return The order value
     */
    public double getOrderValue() {
        return orderValue;
    }
    
    /**
     * Getter for delivery cost
     * @return The delivery cost
     */
    public double getDeliveryCost() {
        return deliveryCost;
    }
    
    /**
     * Getter for status
     * @return The status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Getter for order date
     * @return The order date
     */
    public String getOrderDate() {
        return orderDate;
    }
    
    /**
     * Getter for estimated delivery date
     * @return The estimated delivery date
     */
    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }
    
    /**
     * Getter for actual delivery date
     * @return The actual delivery date
     */
    public String getActualDeliveryDate() {
        return actualDeliveryDate;
    }
    
    /**
     * Getter for tracking number
     * @return The tracking number
     */
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    /**
     * Getter for delivered status
     * @return True if delivery is delivered
     */
    public boolean isDelivered() {
        return isDelivered;
    }
    
    /**
     * Getter for delivery notes
     * @return The delivery notes
     */
    public String getDeliveryNotes() {
        return deliveryNotes;
    }
    
    /**
     * Override toString method
     * @return String representation of the delivery
     */
    @Override
    public String toString() {
        return getDeliveryInfo();
    }
}
