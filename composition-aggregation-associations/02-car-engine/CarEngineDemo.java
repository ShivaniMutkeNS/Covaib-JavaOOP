package composition.car;

/**
 * MAANG-Level Demo: Car Engine System with Runtime Swapping
 * Demonstrates composition flexibility and strategy pattern implementation
 */
public class CarEngineDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Car Engine System Demo ===\n");
        
        // Create car with initial petrol engine
        Car tesla = new Car("Tesla", "Model S", new PetrolEngine());
        
        System.out.println("--- Initial Setup: Petrol Engine ---");
        tesla.displayCarStatus();
        
        // Test petrol engine behavior
        System.out.println("\n--- Testing Petrol Engine Performance ---");
        tesla.start();
        tesla.accelerate(50.0);
        tesla.accelerate(100.0);
        tesla.accelerate(150.0);
        tesla.displayCarStatus();
        
        // Runtime engine swap to electric
        System.out.println("\n--- Runtime Engine Swap: Petrol → Electric ---");
        tesla.swapEngine(new ElectricEngine());
        tesla.recharge(); // Recharge for electric engine
        
        tesla.start();
        tesla.accelerate(50.0);
        tesla.accelerate(100.0);
        tesla.accelerate(200.0); // Higher speed possible with electric
        tesla.displayCarStatus();
        
        // Runtime engine swap to hybrid
        System.out.println("\n--- Runtime Engine Swap: Electric → Hybrid ---");
        tesla.swapEngine(new HybridEngine());
        tesla.refuel(); // Add fuel for hybrid operation
        tesla.recharge();
        
        tesla.start();
        System.out.println("\n--- Testing Hybrid Mode Switching ---");
        tesla.accelerate(30.0);  // Should use electric mode
        tesla.accelerate(80.0);  // Should switch to petrol mode
        tesla.accelerate(40.0);  // Should switch back to electric
        tesla.displayCarStatus();
        
        // Test low energy scenarios
        System.out.println("\n--- Testing Low Energy Scenarios ---");
        Car lowEnergyTesla = new Car("Tesla", "Model 3", new ElectricEngine());
        
        // Simulate low battery
        for (int i = 0; i < 12; i++) {
            lowEnergyTesla.accelerate(100.0);
        }
        
        System.out.println("\n--- Attempting to start with low battery ---");
        lowEnergyTesla.start(); // Should fail
        
        // Swap to hybrid engine when electric fails
        System.out.println("\n--- Emergency Engine Swap: Electric → Hybrid ---");
        lowEnergyTesla.swapEngine(new HybridEngine());
        lowEnergyTesla.refuel();
        lowEnergyTesla.start(); // Should work with fuel
        
        lowEnergyTesla.displayCarStatus();
        
        System.out.println("\n=== Demo Complete: Car adapted to different engine types without modification ===");
    }
}
