package composition.computer;

/**
 * MAANG-Level Demo: Computer System with Component Upgrades
 * Demonstrates composition flexibility, component swapping, and performance monitoring
 */
public class ComputerSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Computer System Demo ===\n");
        
        // Create initial computer configuration
        CPU initialCPU = new IntelCPU("Intel i5-12400", 6, 2.5, "Alder Lake");
        RAM initialRAM = new DDR4RAM(16, 3200);
        GPU initialGPU = new NvidiaGPU("RTX 3060", 12, "Ampere", 3584);
        Storage initialStorage = new HDDStorage(1000, 7200, 150);
        
        Computer computer = new Computer("Gaming PC", "SN-2024-001", 
                                       initialCPU, initialRAM, initialGPU, initialStorage);
        
        // Add upgrade listener
        ITDepartmentListener itListener = new ITDepartmentListener();
        computer.addUpgradeListener(itListener);
        
        // Initial system info
        System.out.println("--- Initial Configuration ---");
        computer.displaySystemInfo();
        
        // Power on and run initial benchmark
        System.out.println("\n--- Initial Performance Test ---");
        computer.powerOn();
        
        SystemBenchmark initialBenchmark = computer.runBenchmark();
        initialBenchmark.displayResults();
        
        // Test various workloads
        System.out.println("\n--- Testing Different Workloads ---");
        
        // Gaming workload
        ComputeTask gamingTask = new ComputeTask("Gaming (AAA Title)", 70, 8192, true, 85, 2000);
        computer.executeTask(gamingTask);
        
        // Video editing workload
        ComputeTask videoTask = new ComputeTask("Video Editing", 90, 16384, true, 95, 10000);
        ComputeResult videoResult = computer.executeTask(videoTask);
        
        if (!videoResult.isSuccess()) {
            System.out.println("Video editing failed: " + videoResult.getMessage());
        }
        
        // Upgrade CPU for better performance
        System.out.println("\n--- CPU Upgrade ---");
        CPU upgradedCPU = new AMDCPU("AMD Ryzen 7 7700X", 8, 4.5, "Zen 4");
        computer.upgradeCPU(upgradedCPU);
        computer.powerOn();
        
        // Upgrade RAM for video editing
        System.out.println("\n--- RAM Upgrade ---");
        RAM upgradedRAM = new DDR5RAM(32, 5600);
        computer.upgradeRAM(upgradedRAM);
        
        // Retry video editing task
        System.out.println("\n--- Retrying Video Editing After Upgrades ---");
        ComputeResult videoResult2 = computer.executeTask(videoTask);
        System.out.println("Video editing performance improved: " + 
                         (videoResult2.getPerformanceScore() > videoResult.getPerformanceScore()));
        
        // Upgrade storage for faster I/O
        System.out.println("\n--- Storage Upgrade ---");
        Storage upgradedStorage = new SSDStorage(2000, 7000, "NVMe PCIe 4.0");
        computer.upgradeStorage(upgradedStorage);
        
        // Upgrade GPU for AI workloads
        System.out.println("\n--- GPU Upgrade ---");
        GPU upgradedGPU = new NvidiaGPU("RTX 4080", 16, "Ada Lovelace", 9728);
        computer.upgradeGPU(upgradedGPU);
        computer.powerOn();
        
        // Test AI/ML workload
        System.out.println("\n--- AI/ML Workload Test ---");
        ComputeTask aiTask = new ComputeTask("Machine Learning Training", 60, 24576, true, 98, 5000);
        computer.executeTask(aiTask);
        
        // Final benchmark after all upgrades
        System.out.println("\n--- Final Performance Test ---");
        SystemBenchmark finalBenchmark = computer.runBenchmark();
        finalBenchmark.displayResults();
        
        // Compare performance improvement
        System.out.println("\n--- Performance Comparison ---");
        double improvement = finalBenchmark.getOverallScore() - initialBenchmark.getOverallScore();
        System.out.printf("Performance Improvement: +%.2f points (%.1f%% increase)\n", 
                         improvement, (improvement / initialBenchmark.getOverallScore()) * 100);
        
        // Test component compatibility scenarios
        System.out.println("\n--- Component Compatibility Test ---");
        
        // Create budget configuration
        Computer budgetPC = new Computer("Budget PC", "SN-2024-002",
                                       new IntelCPU("Intel i3-12100", 4, 3.3, "Alder Lake"),
                                       new DDR4RAM(8, 2666),
                                       new AMDGPU("RX 6500 XT", 4, "RDNA 2", 1024),
                                       new SSDStorage(500, 2500, "SATA"));
        
        budgetPC.addUpgradeListener(itListener);
        budgetPC.powerOn();
        
        // Test budget PC with demanding task
        ComputeTask demandingTask = new ComputeTask("4K Video Rendering", 95, 20480, true, 90, 15000);
        ComputeResult budgetResult = budgetPC.executeTask(demandingTask);
        
        if (!budgetResult.isSuccess()) {
            System.out.println("Budget PC cannot handle demanding task - upgrading components...");
            
            // Upgrade budget PC step by step
            budgetPC.upgradeRAM(new DDR4RAM(32, 3200));
            budgetPC.upgradeGPU(new NvidiaGPU("RTX 4070", 12, "Ada Lovelace", 5888));
            
            ComputeResult upgradedResult = budgetPC.executeTask(demandingTask);
            System.out.println("After upgrades: " + (upgradedResult.isSuccess() ? "Success!" : "Still insufficient"));
        }
        
        computer.displaySystemInfo();
        budgetPC.displaySystemInfo();
        
        // Cleanup
        computer.powerOff();
        budgetPC.powerOff();
        
        System.out.println("\n=== Demo Complete: Computer system adapted to different performance requirements through component upgrades ===");
    }
    
    // IT Department upgrade listener
    static class ITDepartmentListener implements ComponentUpgradeListener {
        @Override
        public void onComponentUpgrade(String computerSerial, String componentType, String oldSpec, String newSpec) {
            System.out.println("🔧 IT Alert [" + computerSerial + "]: " + componentType + 
                             " upgraded from " + oldSpec + " to " + newSpec);
            
            // Log upgrade for inventory management
            System.out.println("📋 Inventory updated: " + componentType + " change recorded");
        }
    }
}
