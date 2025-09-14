/**
 * Demo class to showcase e-commerce delivery system
 * Demonstrates real-world constraints and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class DeliveryDemo {
    public static void main(String[] args) {
        System.out.println("🚚 E-COMMERCE DELIVERY SYSTEM 🚚");
        System.out.println("=" .repeat(50));
        
        // Create different types of deliveries
        Delivery[] deliveries = {
            new StandardDelivery("ORD001", "Alice Johnson", "123 Main St, New York, NY 10001", "+1-555-0123", 75.0, "UPS", 2.5, "Medium", false),
            new StandardDelivery("ORD002", "Bob Smith", "456 Oak Ave, Los Angeles, CA 90210", "+1-555-0456", 25.0, "FedEx", 1.2, "Small", true),
            new ExpressDelivery("ORD003", "Carol Davis", "789 Pine St, Chicago, IL 60601", "+1-555-0789", 150.0, "DHL", 0.8, "Small", false, "Next Day"),
            new ExpressDelivery("ORD004", "David Wilson", "321 Elm St, Miami, FL 33101", "+1-555-0321", 200.0, "UPS", 3.2, "Large", true, "Same Day"),
            new DroneDelivery("ORD005", "Eva Brown", "654 Maple Dr, Austin, TX 78701", "+1-555-0654", 300.0, "DRONE001", "PILOT001", 0.5, "Small", false, "Urban", "Quadcopter"),
            new DroneDelivery("ORD006", "Frank Miller", "987 Cedar Ln, Seattle, WA 98101", "+1-555-0987", 250.0, "DRONE002", "PILOT002", 1.8, "Medium", true, "Suburban", "Hexacopter")
        };
        
        // Display delivery information
        System.out.println("\n📋 DELIVERY INFORMATION:");
        System.out.println("-".repeat(50));
        for (Delivery delivery : deliveries) {
            System.out.println(delivery.getDeliveryInfo());
        }
        
        // Demonstrate delivery processing
        System.out.println("\n📦 DELIVERY PROCESSING DEMONSTRATION:");
        System.out.println("-".repeat(50));
        for (Delivery delivery : deliveries) {
            System.out.println("\n" + delivery.getDeliveryType() + " Delivery - Order " + delivery.getOrderId() + ":");
            delivery.processDelivery();
        }
        
        // Display delivery features
        System.out.println("\n🔧 DELIVERY FEATURES:");
        System.out.println("-".repeat(50));
        for (Delivery delivery : deliveries) {
            System.out.println("\n" + delivery.getDeliveryType() + " Delivery - Order " + delivery.getOrderId() + ":");
            System.out.println(delivery.getDeliveryFeatures());
        }
        
        // Demonstrate delivery-specific behaviors
        System.out.println("\n🚚 DELIVERY-SPECIFIC BEHAVIORS:");
        System.out.println("-".repeat(50));
        
        // Standard Delivery specific behaviors
        StandardDelivery standardDelivery = new StandardDelivery("ORD007", "Grace Lee", "147 Birch St, Denver, CO 80201", "+1-555-0147", 60.0, "USPS", 4.1, "Large", false);
        System.out.println("Standard Delivery Behaviors:");
        System.out.println(standardDelivery.getPackageDetails());
        System.out.println("Oversized: " + standardDelivery.isOversized());
        System.out.println("Free shipping eligible: " + standardDelivery.isEligibleForFreeShipping());
        standardDelivery.setSignatureRequirement(true);
        standardDelivery.setDeliveryWindow("10 AM - 2 PM");
        standardDelivery.addSpecialInstructions("Leave at front door if no answer");
        standardDelivery.processDelivery();
        System.out.println(standardDelivery.getDeliveryTimeline());
        System.out.println();
        
        // Express Delivery specific behaviors
        ExpressDelivery expressDelivery = new ExpressDelivery("ORD008", "Henry Chen", "258 Spruce Ave, Phoenix, AZ 85001", "+1-555-0258", 180.0, "FedEx", 2.3, "Medium", true, "2-Day");
        System.out.println("Express Delivery Behaviors:");
        System.out.println(expressDelivery.getPackageDetails());
        System.out.println("Urgent: " + expressDelivery.isUrgent());
        System.out.println("Free shipping eligible: " + expressDelivery.isEligibleForFreeShipping());
        expressDelivery.setPriority(true);
        expressDelivery.addInsurance(500.0);
        expressDelivery.setDeliveryWindow("8 AM - 12 PM");
        expressDelivery.addSpecialInstructions("Handle with care - fragile items");
        expressDelivery.processDelivery();
        System.out.println(expressDelivery.getDeliveryTimeline());
        System.out.println(expressDelivery.getExpressFeatures());
        System.out.println();
        
        // Drone Delivery specific behaviors
        DroneDelivery droneDelivery = new DroneDelivery("ORD009", "Ivy Rodriguez", "369 Willow Way, Portland, OR 97201", "+1-555-0369", 400.0, "DRONE003", "PILOT003", 0.3, "Small", false, "Urban", "Octocopter");
        System.out.println("Drone Delivery Behaviors:");
        System.out.println(droneDelivery.getPackageDetails());
        System.out.println("Delivery possible: " + droneDelivery.isDeliveryPossible());
        System.out.println("Free shipping eligible: " + droneDelivery.isEligibleForFreeShipping());
        droneDelivery.checkWeatherSuitability("Clear");
        droneDelivery.requestPermission();
        droneDelivery.setFlightPath("GPS coordinates: 45.5152, -122.6784");
        droneDelivery.setLandingZone("Front yard - safe landing area");
        droneDelivery.checkBatteryLevel();
        System.out.println(droneDelivery.getDroneStatus());
        System.out.println(droneDelivery.getFlightDetails());
        droneDelivery.processDelivery();
        System.out.println();
        
        // Demonstrate delivery status updates
        System.out.println("\n📊 DELIVERY STATUS UPDATES:");
        System.out.println("-".repeat(50));
        for (Delivery delivery : deliveries) {
            System.out.println(delivery.getDeliveryType() + " Delivery - Order " + delivery.getOrderId() + ":");
            delivery.updateStatus("Out for Delivery");
            delivery.updateStatus("Delivered");
            delivery.markAsDelivered("Delivered successfully to customer");
            System.out.println();
        }
        
        // Demonstrate free shipping eligibility
        System.out.println("\n💰 FREE SHIPPING ELIGIBILITY:");
        System.out.println("-".repeat(50));
        for (Delivery delivery : deliveries) {
            System.out.println(delivery.getDeliveryType() + " Delivery - Order " + delivery.getOrderId() + ":");
            System.out.println("Order value: $" + String.format("%.2f", delivery.getOrderValue()));
            System.out.println("Free shipping threshold: $" + String.format("%.2f", delivery.getFreeShippingThreshold()));
            System.out.println("Eligible for free shipping: " + delivery.isEligibleForFreeShipping());
            System.out.println();
        }
        
        // Demonstrate cost comparison
        System.out.println("\n💵 COST COMPARISON:");
        System.out.println("-".repeat(50));
        System.out.println("Delivery Type\t\tOrder Value\tDelivery Cost\tFree Shipping");
        System.out.println("-".repeat(50));
        
        for (Delivery delivery : deliveries) {
            double cost = delivery.calculateCost();
            String freeShipping = delivery.isEligibleForFreeShipping() ? "Yes" : "No";
            
            System.out.printf("%-15s\t$%.2f\t\t$%.2f\t\t%s%n", 
                            delivery.getDeliveryType(), delivery.getOrderValue(), cost, freeShipping);
        }
        
        // Demonstrate delivery time estimation
        System.out.println("\n⏰ DELIVERY TIME ESTIMATION:");
        System.out.println("-".repeat(50));
        for (Delivery delivery : deliveries) {
            System.out.println(delivery.getDeliveryType() + " Delivery - Order " + delivery.getOrderId() + ":");
            System.out.println("Estimated delivery: " + delivery.estimateDeliveryTime());
            System.out.println();
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(deliveries);
        
        // Demonstrate real-world constraints
        System.out.println("\n🌍 REAL-WORLD CONSTRAINTS:");
        System.out.println("-".repeat(50));
        System.out.println("Standard Delivery Constraints:");
        System.out.println("- Weight limits based on package type");
        System.out.println("- Signature requirements for high-value items");
        System.out.println("- Delivery windows and time constraints");
        System.out.println("- Weather and traffic considerations");
        System.out.println();
        System.out.println("Express Delivery Constraints:");
        System.out.println("- Higher cost for faster delivery");
        System.out.println("- Limited weight and size restrictions");
        System.out.println("- Priority handling and insurance options");
        System.out.println("- Time-sensitive delivery windows");
        System.out.println();
        System.out.println("Drone Delivery Constraints:");
        System.out.println("- Weather suitability requirements");
        System.out.println("- Weight and size limitations");
        System.out.println("- Battery level and flight range");
        System.out.println("- Permission and regulatory compliance");
        System.out.println("- Landing zone and safety requirements");
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param deliveries Array of Delivery objects
     */
    public static void demonstratePolymorphism(Delivery[] deliveries) {
        System.out.println("Processing deliveries using polymorphism:");
        for (int i = 0; i < deliveries.length; i++) {
            Delivery delivery = deliveries[i];
            System.out.println((i + 1) + ". " + delivery.getDeliveryType() + " Delivery - Order " + delivery.getOrderId());
            System.out.println("   Customer: " + delivery.getCustomerName());
            System.out.println("   Address: " + delivery.getCustomerAddress());
            System.out.println("   Features: " + delivery.getDeliveryFeatures());
            System.out.println();
        }
    }
}
