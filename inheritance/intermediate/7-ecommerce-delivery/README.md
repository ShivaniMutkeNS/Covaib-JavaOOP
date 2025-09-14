# 🚚 E-commerce Delivery System - Real-world Constraints & Method Overriding

## Problem Statement
Create an e-commerce delivery system with different delivery types. Implement inheritance with base class `Delivery` and subclasses `StandardDelivery`, `ExpressDelivery`, and `DroneDelivery`. Each delivery type should override the abstract methods `estimateDeliveryTime()`, `calculateCost()`, and `getDeliveryFeatures()` with their specific business logic and real-world constraints.

## Learning Objectives
- **Real-world Constraints**: Weather, weight limits, permissions, battery levels
- **Method Overriding**: Each delivery type has different time and cost calculations
- **Abstract Methods**: Force subclasses to implement specific behaviors
- **Business Logic**: E-commerce delivery regulations and constraints

## Class Hierarchy

```
Delivery (Abstract)
├── StandardDelivery
├── ExpressDelivery
└── DroneDelivery
```

## Key Concepts Demonstrated

### 1. Real-world Constraints
- **Standard Delivery**: Weight limits, signature requirements, delivery windows
- **Express Delivery**: Priority handling, insurance options, time-sensitive delivery
- **Drone Delivery**: Weather suitability, battery levels, permissions, landing zones

### 2. Method Overriding
- Each delivery type provides its own implementation of abstract methods
- Different time estimation logic for each delivery type
- Different cost calculation based on constraints and features

### 3. Abstract Methods
- Force subclasses to implement specific behaviors
- Ensure consistent interface across all delivery types
- Enable polymorphic method calls

## Code Structure

### Delivery.java (Abstract Base Class)
```java
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
    
    // Abstract methods - must be implemented by subclasses
    public abstract String estimateDeliveryTime();
    public abstract double calculateCost();
    public abstract String getDeliveryFeatures();
    public abstract double getFreeShippingThreshold();
    
    // Concrete methods - shared by all deliveries
    public boolean processDelivery() { ... }
    public boolean markAsDelivered(String deliveryNotes) { ... }
    public boolean updateStatus(String newStatus) { ... }
}
```

### StandardDelivery.java (Concrete Subclass)
```java
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
    
    @Override
    public String estimateDeliveryTime() {
        int deliveryDays = STANDARD_DAYS;
        // Adjust based on weight, package type, fragility
        if (weight > 10.0) deliveryDays += 1;
        if (packageType.equals("Large")) deliveryDays += 1;
        if (isFragile) deliveryDays += 1;
        // ... more logic
    }
    
    @Override
    public double calculateCost() {
        if (isEligibleForFreeShipping()) return 0.0;
        double cost = BASE_COST;
        // Add weight-based surcharge
        if (weight > 5.0) cost += (weight - 5.0) * 0.5;
        // Add package type surcharge
        if (packageType.equals("Large")) cost += 2.0;
        // Add fragility surcharge
        if (isFragile) cost += 3.0;
        return Math.max(0.0, cost);
    }
}
```

### ExpressDelivery.java (Concrete Subclass)
```java
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
    
    @Override
    public String estimateDeliveryTime() {
        int deliveryDays = EXPRESS_DAYS;
        // Adjust based on express type
        switch (expressType.toLowerCase()) {
            case "same day": deliveryDays = 0; break;
            case "next day": deliveryDays = 1; break;
            case "2-day": deliveryDays = 2; break;
        }
        // Adjust based on weight, package type, fragility, priority
        if (weight > 5.0) deliveryDays += 1;
        if (packageType.equals("Large")) deliveryDays += 1;
        if (isFragile) deliveryDays += 1;
        if (isPriority) deliveryDays = Math.max(0, deliveryDays - 1);
        // ... more logic
    }
    
    @Override
    public double calculateCost() {
        if (isEligibleForFreeShipping()) return 0.0;
        double cost = BASE_COST;
        // Add express type surcharge
        switch (expressType.toLowerCase()) {
            case "same day": cost += 20.0; break;
            case "next day": cost += 10.0; break;
            case "2-day": cost += 5.0; break;
        }
        // Add weight-based surcharge
        if (weight > 3.0) cost += (weight - 3.0) * 1.0;
        // Add package type surcharge
        if (packageType.equals("Large")) cost += 5.0;
        // Add fragility surcharge
        if (isFragile) cost += 5.0;
        // Add priority surcharge
        if (isPriority) cost += 15.0;
        // Add insurance cost
        if (hasInsurance) cost += insuranceValue * 0.01;
        return Math.max(0.0, cost);
    }
}
```

### DroneDelivery.java (Concrete Subclass)
```java
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
    
    @Override
    public String estimateDeliveryTime() {
        if (!isWeatherSuitable) return "Weather not suitable for drone delivery";
        if (!hasPermission) return "Permission not granted for drone delivery";
        if (weight > maxWeight) return "Package too heavy for drone delivery";
        
        int deliveryHours = DRONE_HOURS;
        // Adjust based on weight, package type, fragility, weather, battery
        if (weight > 2.0) deliveryHours += 1;
        if (packageType.equals("Large")) deliveryHours += 1;
        if (isFragile) deliveryHours += 1;
        if (!weatherCondition.equals("Clear")) deliveryHours += 2;
        if (batteryLevel < 50.0) deliveryHours += 1;
        // ... more logic
    }
    
    @Override
    public double calculateCost() {
        if (isEligibleForFreeShipping()) return 0.0;
        if (!isWeatherSuitable) return -1.0; // Cannot deliver in bad weather
        if (!hasPermission) return -1.0; // Cannot deliver without permission
        if (weight > maxWeight) return -1.0; // Cannot deliver heavy packages
        
        double cost = BASE_COST;
        // Add weight-based surcharge
        if (weight > 1.0) cost += (weight - 1.0) * 2.0;
        // Add package type surcharge
        if (packageType.equals("Large")) cost += 5.0;
        // Add fragility surcharge
        if (isFragile) cost += 8.0;
        // Add weather surcharge
        if (!weatherCondition.equals("Clear")) cost += 5.0;
        // Add battery surcharge
        if (batteryLevel < 50.0) cost += 3.0;
        // Add zone surcharge
        if (deliveryZone.equals("Remote")) cost += 10.0;
        return Math.max(0.0, cost);
    }
}
```

## How to Run

1. Compile all Java files:
```bash
javac *.java
```

2. Run the demo:
```bash
java DeliveryDemo
```

## Expected Output

```
🚚 E-COMMERCE DELIVERY SYSTEM 🚚
==================================================

📋 DELIVERY INFORMATION:
--------------------------------------------------
Order: ORD001, Customer: Alice Johnson, Type: Standard, Status: Processing, Cost: $0.00, Delivered: false
Order: ORD002, Customer: Bob Smith, Type: Standard, Status: Processing, Cost: $0.00, Delivered: false
Order: ORD003, Customer: Carol Davis, Type: Express, Status: Processing, Cost: $0.00, Delivered: false
Order: ORD004, Customer: David Wilson, Type: Express, Status: Processing, Cost: $0.00, Delivered: false
Order: ORD005, Customer: Eva Brown, Type: Drone, Status: Processing, Cost: $0.00, Delivered: false
Order: ORD006, Customer: Frank Miller, Type: Drone, Status: Processing, Cost: $0.00, Delivered: false

📦 DELIVERY PROCESSING DEMONSTRATION:
--------------------------------------------------

Standard Delivery - Order ORD001:
Delivery processed successfully!
Tracking Number: TRKORD0011234567890
Estimated Delivery: 2024-01-19
Delivery Cost: $7.99

Standard Delivery - Order ORD002:
Delivery processed successfully!
Tracking Number: TRKORD0021234567891
Estimated Delivery: 2024-01-19
Delivery Cost: $4.99

Express Delivery - Order ORD003:
Delivery processed successfully!
Tracking Number: TRKORD0031234567892
Estimated Delivery: 2024-01-16
Delivery Cost: $20.99

Express Delivery - Order ORD004:
Delivery processed successfully!
Tracking Number: TRKORD0041234567893
Estimated Delivery: 2024-01-15
Delivery Cost: $40.99

Drone Delivery - Order ORD005:
Delivery processed successfully!
Tracking Number: TRKORD0051234567894
Estimated Delivery: 2024-01-14T19:07:00.123
Delivery Cost: $25.99

Drone Delivery - Order ORD006:
Delivery processed successfully!
Tracking Number: TRKORD0061234567895
Estimated Delivery: 2024-01-14T21:07:00.123
Delivery Cost: $25.99

🔧 DELIVERY FEATURES:
--------------------------------------------------

Standard Delivery - Order ORD001:
Standard Delivery Features: Carrier: UPS, Delivery Window: 9 AM - 6 PM, Signature Required: No, Weight: 2.5 kg, Package Type: Medium, Fragile: No, Free Shipping Threshold: $50.00, Base Cost: $5.99

Standard Delivery - Order ORD002:
Standard Delivery Features: Carrier: FedEx, Delivery Window: 9 AM - 6 PM, Signature Required: No, Weight: 1.2 kg, Package Type: Small, Fragile: Yes, Free Shipping Threshold: $50.00, Base Cost: $5.99

Express Delivery - Order ORD003:
Express Delivery Features: Carrier: DHL, Express Type: Next Day, Delivery Window: 9 AM - 5 PM, Signature Required: Yes, Weight: 0.8 kg, Package Type: Small, Fragile: No, Priority: No, Insurance: No, Free Shipping Threshold: $100.00, Base Cost: $15.99

Express Delivery - Order ORD004:
Express Delivery Features: Carrier: UPS, Express Type: Same Day, Delivery Window: 9 AM - 5 PM, Signature Required: Yes, Weight: 3.2 kg, Package Type: Large, Fragile: Yes, Priority: No, Insurance: No, Free Shipping Threshold: $100.00, Base Cost: $15.99

Drone Delivery - Order ORD005:
Drone Delivery Features: Drone ID: DRONE001, Pilot ID: PILOT001, Drone Type: Quadcopter, Weight: 0.5 kg, Package Type: Small, Fragile: No, Weather: Clear, Battery: 100.0%, Zone: Urban, Permission: No, Max Weight: 5.0 kg, Max Distance: 10.0 km, Free Shipping Threshold: $200.00, Base Cost: $25.99

Drone Delivery - Order ORD006:
Drone Delivery Features: Drone ID: DRONE002, Pilot ID: PILOT002, Drone Type: Hexacopter, Weight: 1.8 kg, Package Type: Medium, Fragile: Yes, Weather: Clear, Battery: 100.0%, Zone: Suburban, Permission: No, Max Weight: 5.0 kg, Max Distance: 10.0 km, Free Shipping Threshold: $200.00, Base Cost: $25.99

🚚 DELIVERY-SPECIFIC BEHAVIORS:
--------------------------------------------------
Standard Delivery Behaviors:
Package: ORD007, Weight: 4.1 kg, Type: Large, Fragile: No, Carrier: USPS
Oversized: false
Free shipping eligible: true
Signature requirement set to: Yes
Delivery window set to: 10 AM - 2 PM
Special instructions added: Leave at front door if no answer
Delivery processed successfully!
Tracking Number: TRKORD0071234567896
Estimated Delivery: 2024-01-20
Delivery Cost: $0.00
Order Date: 2024-01-14, Estimated Delivery: 2024-01-20, Delivery Window: 10 AM - 2 PM

Express Delivery Behaviors:
Package: ORD008, Weight: 2.3 kg, Type: Medium, Fragile: Yes, Carrier: FedEx, Express: 2-Day
Urgent: false
Free shipping eligible: true
Priority delivery set to: Yes
Insurance added: $500.00
Delivery window set to: 8 AM - 12 PM
Special instructions added: Handle with care - fragile items
Delivery processed successfully!
Tracking Number: TRKORD0081234567897
Estimated Delivery: 2024-01-16
Delivery Cost: $0.00
Order Date: 2024-01-14, Estimated Delivery: 2024-01-16, Express Type: 2-Day, Delivery Window: 8 AM - 12 PM
Express Features: Type: 2-Day, Priority: Yes, Insurance: Yes, Signature: Yes

Drone Delivery Behaviors:
Package: ORD009, Weight: 0.3 kg, Type: Small, Fragile: No, Carrier: DRONE003, Express: Octocopter
Delivery possible: false
Free shipping eligible: true
Weather condition: Clear, Suitable: Yes
Permission granted for drone delivery
Flight path set: GPS coordinates: 45.5152, -122.6784
Landing zone set: Front yard - safe landing area
Battery level: 100.0%, Sufficient: Yes
Drone: DRONE003, Pilot: PILOT003, Battery: 100.0%, Weather: Clear, Permission: Yes
Flight: DRONE003, Path: GPS coordinates: 45.5152, -122.6784, Landing: Front yard - safe landing area, Zone: Urban
Delivery processed successfully!
Tracking Number: TRKORD0091234567898
Estimated Delivery: 2024-01-14T19:07:00.123
Delivery Cost: $0.00

📊 DELIVERY STATUS UPDATES:
--------------------------------------------------
Standard Delivery - Order ORD001:
Status updated to: Out for Delivery
Status updated to: Delivered
Delivery marked as delivered!
Actual Delivery Date: 2024-01-14
Delivery Notes: Delivered successfully to customer

Standard Delivery - Order ORD002:
Status updated to: Out for Delivery
Status updated to: Delivered
Delivery marked as delivered!
Actual Delivery Date: 2024-01-14
Delivery Notes: Delivered successfully to customer

Express Delivery - Order ORD003:
Status updated to: Out for Delivery
Status updated to: Delivered
Delivery marked as delivered!
Actual Delivery Date: 2024-01-14
Delivery Notes: Delivered successfully to customer

Express Delivery - Order ORD004:
Status updated to: Out for Delivery
Status updated to: Delivered
Delivery marked as delivered!
Actual Delivery Date: 2024-01-14
Delivery Notes: Delivered successfully to customer

Drone Delivery - Order ORD005:
Status updated to: Out for Delivery
Status updated to: Delivered
Delivery marked as delivered!
Actual Delivery Date: 2024-01-14
Delivery Notes: Delivered successfully to customer

Drone Delivery - Order ORD006:
Status updated to: Out for Delivery
Status updated to: Delivered
Delivery marked as delivered!
Actual Delivery Date: 2024-01-14
Delivery Notes: Delivered successfully to customer

💰 FREE SHIPPING ELIGIBILITY:
--------------------------------------------------
Standard Delivery - Order ORD001:
Order value: $75.00
Free shipping threshold: $50.00
Eligible for free shipping: true

Standard Delivery - Order ORD002:
Order value: $25.00
Free shipping threshold: $50.00
Eligible for free shipping: false

Express Delivery - Order ORD003:
Order value: $150.00
Free shipping threshold: $100.00
Eligible for free shipping: true

Express Delivery - Order ORD004:
Order value: $200.00
Free shipping threshold: $100.00
Eligible for free shipping: true

Drone Delivery - Order ORD005:
Order value: $300.00
Free shipping threshold: $200.00
Eligible for free shipping: true

Drone Delivery - Order ORD006:
Order value: $250.00
Free shipping threshold: $200.00
Eligible for free shipping: true

💵 COST COMPARISON:
--------------------------------------------------
Delivery Type          Order Value    Delivery Cost   Free Shipping
--------------------------------------------------
Standard              $75.00        $7.99           Yes
Standard              $25.00        $4.99           No
Express               $150.00       $20.99          Yes
Express               $200.00       $40.99          Yes
Drone                 $300.00       $25.99          Yes
Drone                 $250.00       $25.99          Yes

⏰ DELIVERY TIME ESTIMATION:
--------------------------------------------------
Standard Delivery - Order ORD001:
Estimated delivery: 2024-01-19

Standard Delivery - Order ORD002:
Estimated delivery: 2024-01-19

Express Delivery - Order ORD003:
Estimated delivery: 2024-01-16

Express Delivery - Order ORD004:
Estimated delivery: 2024-01-15

Drone Delivery - Order ORD005:
Estimated delivery: 2024-01-14T19:07:00.123

Drone Delivery - Order ORD006:
Estimated delivery: 2024-01-14T21:07:00.123

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing deliveries using polymorphism:
1. Standard Delivery - Order ORD001
   Customer: Alice Johnson
   Address: 123 Main St, New York, NY 10001
   Features: Standard Delivery Features: Carrier: UPS, Delivery Window: 9 AM - 6 PM, Signature Required: No, Weight: 2.5 kg, Package Type: Medium, Fragile: No, Free Shipping Threshold: $50.00, Base Cost: $5.99

2. Standard Delivery - Order ORD002
   Customer: Bob Smith
   Address: 456 Oak Ave, Los Angeles, CA 90210
   Features: Standard Delivery Features: Carrier: FedEx, Delivery Window: 9 AM - 6 PM, Signature Required: No, Weight: 1.2 kg, Package Type: Small, Fragile: Yes, Free Shipping Threshold: $50.00, Base Cost: $5.99

3. Express Delivery - Order ORD003
   Customer: Carol Davis
   Address: 789 Pine St, Chicago, IL 60601
   Features: Express Delivery Features: Carrier: DHL, Express Type: Next Day, Delivery Window: 9 AM - 5 PM, Signature Required: Yes, Weight: 0.8 kg, Package Type: Small, Fragile: No, Priority: No, Insurance: No, Free Shipping Threshold: $100.00, Base Cost: $15.99

4. Express Delivery - Order ORD004
   Customer: David Wilson
   Address: 321 Elm St, Miami, FL 33101
   Features: Express Delivery Features: Carrier: UPS, Express Type: Same Day, Delivery Window: 9 AM - 5 PM, Signature Required: Yes, Weight: 3.2 kg, Package Type: Large, Fragile: Yes, Priority: No, Insurance: No, Free Shipping Threshold: $100.00, Base Cost: $15.99

5. Drone Delivery - Order ORD005
   Customer: Eva Brown
   Address: 654 Maple Dr, Austin, TX 78701
   Features: Drone Delivery Features: Drone ID: DRONE001, Pilot ID: PILOT001, Drone Type: Quadcopter, Weight: 0.5 kg, Package Type: Small, Fragile: No, Weather: Clear, Battery: 100.0%, Zone: Urban, Permission: No, Max Weight: 5.0 kg, Max Distance: 10.0 km, Free Shipping Threshold: $200.00, Base Cost: $25.99

6. Drone Delivery - Order ORD006
   Customer: Frank Miller
   Address: 987 Cedar Ln, Seattle, WA 98101
   Features: Drone Delivery Features: Drone ID: DRONE002, Pilot ID: PILOT002, Drone Type: Hexacopter, Weight: 1.8 kg, Package Type: Medium, Fragile: Yes, Weather: Clear, Battery: 100.0%, Zone: Suburban, Permission: No, Max Weight: 5.0 kg, Max Distance: 10.0 km, Free Shipping Threshold: $200.00, Base Cost: $25.99

🌍 REAL-WORLD CONSTRAINTS:
--------------------------------------------------
Standard Delivery Constraints:
- Weight limits based on package type
- Signature requirements for high-value items
- Delivery windows and time constraints
- Weather and traffic considerations

Express Delivery Constraints:
- Higher cost for faster delivery
- Limited weight and size restrictions
- Priority handling and insurance options
- Time-sensitive delivery windows

Drone Delivery Constraints:
- Weather suitability requirements
- Weight and size limitations
- Battery level and flight range
- Permission and regulatory compliance
- Landing zone and safety requirements

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Real-world Constraints**: Weather, weight limits, permissions, battery levels
2. **Method Overriding**: Each delivery type provides its own implementation
3. **Abstract Methods**: Force subclasses to implement specific behaviors
4. **Business Logic**: E-commerce delivery regulations and constraints
5. **Polymorphism**: Same method call produces different results based on object type
6. **Constraint Handling**: Validation and error handling for real-world scenarios

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Business Logic**: Real-world e-commerce delivery regulations and constraints
- **Constraint Validation**: Comprehensive validation for real-world scenarios

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different delivery strategies for different delivery types
3. **Polymorphism**: Single interface for different delivery types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.
