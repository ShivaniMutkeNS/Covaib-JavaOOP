import java.util.Arrays;

/**
 * Comprehensive demonstration of the smart vehicle system
 * Shows polymorphism and different vehicle types with autonomous capabilities
 */
public class SmartVehicleDemo {
    
    public static void main(String[] args) {
        System.out.println("🚗 SMART VEHICLE SYSTEM DEMONSTRATION");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        // Create different types of smart vehicles
        ElectricVehicle tesla = new ElectricVehicle("Tesla", "Model S", 2024, AutonomyLevel.LEVEL_3, "Alice Johnson", 100.0);
        AutonomousVehicle waymo = new AutonomousVehicle("Waymo", "Chrysler Pacifica", 2024, VehicleType.SUV, "Autonomous Fleet");
        HybridVehicle prius = new HybridVehicle("Toyota", "Prius", 2024, VehicleType.SEDAN, AutonomyLevel.LEVEL_2, "Bob Smith", 45.0, 1.3);
        ElectricVehicle rivian = new ElectricVehicle("Rivian", "R1T", 2024, AutonomyLevel.LEVEL_2, "Carol Davis", 135.0);
        
        // Demonstrate polymorphism with vehicle array
        SmartVehicle[] vehicles = {tesla, waymo, prius, rivian};
        
        System.out.println("\n🚗 CREATED SMART VEHICLES:");
        for (SmartVehicle vehicle : vehicles) {
            System.out.println("• " + vehicle.toString());
        }
        
        // Demo 1: Electric Vehicle Features
        demonstrateElectricVehicle(tesla);
        
        // Demo 2: Autonomous Vehicle Capabilities
        demonstrateAutonomousVehicle(waymo);
        
        // Demo 3: Hybrid Vehicle Efficiency
        demonstrateHybridVehicle(prius);
        
        // Demo 4: Vehicle Comparison and Fleet Management
        demonstrateVehicleComparison(vehicles);
        
        // Demo 5: Advanced Features Integration
        demonstrateAdvancedFeatures(vehicles);
        
        System.out.println("\n🎉 SMART VEHICLE DEMONSTRATION COMPLETED!");
    }
    
    private static void demonstrateElectricVehicle(ElectricVehicle tesla) {
        System.out.println("\n" + new String(new char[60]).replace('\0', '='));
        System.out.println("⚡ DEMO 1: ELECTRIC VEHICLE FEATURES");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        tesla.printVehicleInfo();
        
        // Test charging capabilities
        System.out.println("🔌 Testing charging capabilities...");
        tesla.getStatus().setBatteryLevel(25.0); // Simulate partially depleted battery
        tesla.startCharging("fast");
        
        // Wait for some charging
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        tesla.stopCharging();
        
        // Test eco mode
        tesla.enableEcoMode();
        System.out.println("📊 Range with eco mode: " + String.format("%.1f", tesla.calculateRange()) + " km");
        
        // Test driving
        tesla.drive("Shopping Mall", 60);
        
        // Show energy consumption report
        tesla.getEnergyConsumptionReport();
        
        // Test scheduled charging
        tesla.scheduledCharging(23, 7); // 11 PM to 7 AM
    }
    
    private static void demonstrateAutonomousVehicle(AutonomousVehicle waymo) {
        System.out.println("\n" + new String(new char[60]).replace('\0', '='));
        System.out.println("🤖 DEMO 2: AUTONOMOUS VEHICLE CAPABILITIES");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        waymo.printVehicleInfo();
        
        // Test autonomous features
        waymo.enableLearningMode();
        waymo.connectToNetwork();
        
        // Test V2V communication
        waymo.communicateWithVehicles();
        
        // Test autonomous driving
        waymo.enableAutonomousMode();
        waymo.executeAutonomousDriving("Downtown Office");
        
        // Show AI system status
        System.out.println("\n🧠 AI SYSTEM STATUS:");
        waymo.getAISystemStatus().forEach((key, value) -> 
            System.out.println("  • " + key + ": " + value));
        
        // Show detected objects
        System.out.println("\n👁️ DETECTED OBJECTS:");
        waymo.getDetectedObjects().forEach(obj -> 
            System.out.println("  • " + obj));
        
        // Show decision history
        System.out.println("\n📊 DECISION HISTORY:");
        waymo.getDecisionHistory().forEach((decision, count) -> 
            System.out.println("  • " + decision + ": " + count));
    }
    
    private static void demonstrateHybridVehicle(HybridVehicle prius) {
        System.out.println("\n" + new String(new char[60]).replace('\0', '='));
        System.out.println("⚡🔥 DEMO 3: HYBRID VEHICLE EFFICIENCY");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        prius.printVehicleInfo();
        
        // Test different drive modes
        String[] driveModes = {"ECO", "NORMAL", "SPORT", "EV"};
        
        for (String mode : driveModes) {
            System.out.println("\n🎛️ Testing " + mode + " mode:");
            prius.setDriveMode(mode);
            
            // Simulate driving in each mode
            prius.startEngine();
            prius.accelerate(80);
            prius.brake(0.3);
            
            System.out.println(prius.getEfficiencyReport());
        }
        
        // Test battery charging
        prius.getStatus().setBatteryLevel(30.0);
        prius.chargeBattery();
        
        // Wait for some charging
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Test regenerative braking
        System.out.println("\n🔋 Testing regenerative braking:");
        prius.accelerate(100);
        prius.brake(0.8); // Heavy braking to test regeneration
    }
    
    private static void demonstrateVehicleComparison(SmartVehicle[] vehicles) {
        System.out.println("\n" + new String(new char[60]).replace('\0', '='));
        System.out.println("📊 DEMO 4: VEHICLE COMPARISON & FLEET MANAGEMENT");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        System.out.println("🚗 VEHICLE FLEET OVERVIEW:");
        System.out.println(new String(new char[80]).replace('\0', '-'));
        System.out.printf("%-15s %-15s %-15s %-15s %-15s%n", 
                         "Make/Model", "Type", "Autonomy", "Fuel Type", "Status");
        System.out.println(new String(new char[80]).replace('\0', '-'));
        
        for (SmartVehicle vehicle : vehicles) {
            String makeModel = (vehicle.getMake() + " " + vehicle.getModel()).length() > 13 ?
                              (vehicle.getMake() + " " + vehicle.getModel()).substring(0, 13) + ".." :
                              vehicle.getMake() + " " + vehicle.getModel();
            
            System.out.printf("%-15s %-15s %-15s %-15s %-15s%n",
                             makeModel,
                             vehicle.getVehicleType().getDisplayName().length() > 13 ?
                             vehicle.getVehicleType().getDisplayName().substring(0, 13) + ".." :
                             vehicle.getVehicleType().getDisplayName(),
                             "Level " + vehicle.getAutonomyLevel().getNumericLevel(),
                             vehicle.getVehicleType().getPrimaryFuelType().getDisplayName().length() > 13 ?
                             vehicle.getVehicleType().getPrimaryFuelType().getDisplayName().substring(0, 13) + ".." :
                             vehicle.getVehicleType().getPrimaryFuelType().getDisplayName(),
                             vehicle.getStatus().getOperationalState().getDisplayName());
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("All vehicles implement the same interface but behave differently:");
        
        for (SmartVehicle vehicle : vehicles) {
            System.out.println("\n• " + vehicle.getMake() + " " + vehicle.getModel() + ":");
            System.out.println("  Capabilities: " + vehicle.getVehicleCapabilities());
            
            // Each vehicle type handles acceleration differently
            vehicle.startEngine();
            vehicle.accelerate(50);
            vehicle.stopEngine();
        }
        
        // Fleet statistics
        System.out.println("\n📈 FLEET STATISTICS:");
        long electricVehicles = Arrays.stream(vehicles)
            .filter(v -> v.getVehicleType().isElectric())
            .count();
        long autonomousVehicles = Arrays.stream(vehicles)
            .filter(v -> v.getAutonomyLevel().isFullyAutonomous())
            .count();
        double avgDistance = Arrays.stream(vehicles)
            .mapToDouble(SmartVehicle::getTotalDistanceTraveled)
            .average()
            .orElse(0.0);
        
        System.out.println("  • Total Vehicles: " + vehicles.length);
        System.out.println("  • Electric/Hybrid: " + electricVehicles);
        System.out.println("  • Fully Autonomous: " + autonomousVehicles);
        System.out.println("  • Average Distance: " + String.format("%.1f", avgDistance) + " km");
    }
    
    private static void demonstrateAdvancedFeatures(SmartVehicle[] vehicles) {
        System.out.println("\n" + new String(new char[60]).replace('\0', '='));
        System.out.println("🚀 DEMO 5: ADVANCED FEATURES INTEGRATION");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        // Network connectivity
        System.out.println("📡 Testing network connectivity:");
        for (SmartVehicle vehicle : vehicles) {
            vehicle.connectToNetwork();
        }
        
        // Maintenance simulation
        System.out.println("\n🔧 Fleet maintenance simulation:");
        vehicles[0].performMaintenance(); // Perform maintenance on first vehicle
        
        // Advanced sensor demonstration
        System.out.println("\n📡 SENSOR CAPABILITIES COMPARISON:");
        System.out.println(new String(new char[60]).replace('\0', '-'));
        System.out.printf("%-20s %-10s %-30s%n", "Vehicle", "Sensors", "Advanced Features");
        System.out.println(new String(new char[60]).replace('\0', '-'));
        
        for (SmartVehicle vehicle : vehicles) {
            String vehicleName = (vehicle.getMake() + " " + vehicle.getModel()).length() > 18 ?
                                (vehicle.getMake() + " " + vehicle.getModel()).substring(0, 18) + ".." :
                                vehicle.getMake() + " " + vehicle.getModel();
            
            String advancedFeatures = "";
            if (vehicle instanceof ElectricVehicle) {
                advancedFeatures = "Fast Charging, Eco Mode";
            } else if (vehicle instanceof AutonomousVehicle) {
                advancedFeatures = "AI Navigation, V2X Comm";
            } else if (vehicle instanceof HybridVehicle) {
                advancedFeatures = "Dual Power, Regen Braking";
            }
            
            System.out.printf("%-20s %-10d %-30s%n",
                             vehicleName,
                             vehicle.getInstalledSensors().size(),
                             advancedFeatures.length() > 28 ? advancedFeatures.substring(0, 28) + ".." : advancedFeatures);
        }
        
        // Future technology preview
        System.out.println("\n🔮 FUTURE TECHNOLOGY PREVIEW:");
        System.out.println("• Vehicle-to-Grid (V2G) energy sharing");
        System.out.println("• Swarm intelligence for traffic optimization");
        System.out.println("• Predictive maintenance with IoT sensors");
        System.out.println("• Blockchain-based vehicle identity and transactions");
        System.out.println("• Augmented reality windshield displays");
        System.out.println("• Quantum-encrypted vehicle communications");
        
        // Environmental impact
        System.out.println("\n🌱 ENVIRONMENTAL IMPACT ANALYSIS:");
        long ecoFriendlyVehicles = Arrays.stream(vehicles)
            .filter(v -> v.getVehicleType().getPrimaryFuelType().isEcoFriendly())
            .count();
        
        double emissionReduction = (ecoFriendlyVehicles * 100.0) / vehicles.length;
        
        System.out.println("• Eco-friendly vehicles: " + ecoFriendlyVehicles + "/" + vehicles.length);
        System.out.println("• Estimated emission reduction: " + String.format("%.1f%%", emissionReduction));
        System.out.println("• Smart routing reduces traffic congestion");
        System.out.println("• Autonomous features improve safety by 40%");
        
        // Disconnect from network
        System.out.println("\n📡 Disconnecting from network:");
        for (SmartVehicle vehicle : vehicles) {
            vehicle.disconnectFromNetwork();
        }
    }
}
