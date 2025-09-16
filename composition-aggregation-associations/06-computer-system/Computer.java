package composition.computer;

import java.util.*;

/**
 * MAANG-Level Computer System using Composition
 * Demonstrates: Strategy Pattern, Component Swapping, Performance Monitoring, Builder Pattern
 */
public class Computer {
    private CPU cpu;
    private RAM ram;
    private GPU gpu;
    private Storage storage;
    private final String model;
    private final String serialNumber;
    private boolean isPoweredOn;
    private final SystemMonitor monitor;
    private final List<ComponentUpgradeListener> upgradeListeners;
    
    public Computer(String model, String serialNumber, CPU cpu, RAM ram, GPU gpu, Storage storage) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.cpu = cpu;
        this.ram = ram;
        this.gpu = gpu;
        this.storage = storage;
        this.isPoweredOn = false;
        this.monitor = new SystemMonitor();
        this.upgradeListeners = new ArrayList<>();
    }
    
    public void powerOn() {
        if (isPoweredOn) {
            System.out.println("Computer is already powered on");
            return;
        }
        
        System.out.println("Powering on computer " + model + "...");
        
        // Initialize components
        cpu.initialize();
        ram.initialize();
        gpu.initialize();
        storage.initialize();
        
        isPoweredOn = true;
        monitor.startMonitoring();
        
        System.out.println("Computer powered on successfully");
        displaySystemInfo();
    }
    
    public void powerOff() {
        if (!isPoweredOn) {
            System.out.println("Computer is already powered off");
            return;
        }
        
        System.out.println("Shutting down computer...");
        
        monitor.stopMonitoring();
        storage.flush();
        isPoweredOn = false;
        
        System.out.println("Computer powered off");
    }
    
    // Runtime component upgrades - core composition flexibility
    public void upgradeCPU(CPU newCPU) {
        if (isPoweredOn) {
            powerOff();
        }
        
        CPU oldCPU = this.cpu;
        this.cpu = newCPU;
        
        notifyUpgrade("CPU", oldCPU.getModel(), newCPU.getModel());
        System.out.println("CPU upgraded from " + oldCPU.getModel() + " to " + newCPU.getModel());
        
        if (isPoweredOn) {
            cpu.initialize();
        }
    }
    
    public void upgradeRAM(RAM newRAM) {
        if (isPoweredOn) {
            System.out.println("Hot-swapping RAM (if supported)...");
        }
        
        RAM oldRAM = this.ram;
        this.ram = newRAM;
        
        notifyUpgrade("RAM", oldRAM.getCapacity() + "GB", newRAM.getCapacity() + "GB");
        System.out.println("RAM upgraded from " + oldRAM.getCapacity() + "GB to " + newRAM.getCapacity() + "GB");
        
        if (isPoweredOn) {
            ram.initialize();
        }
    }
    
    public void upgradeGPU(GPU newGPU) {
        if (isPoweredOn) {
            powerOff();
        }
        
        GPU oldGPU = this.gpu;
        this.gpu = newGPU;
        
        notifyUpgrade("GPU", oldGPU.getModel(), newGPU.getModel());
        System.out.println("GPU upgraded from " + oldGPU.getModel() + " to " + newGPU.getModel());
        
        if (isPoweredOn) {
            gpu.initialize();
        }
    }
    
    public void upgradeStorage(Storage newStorage) {
        Storage oldStorage = this.storage;
        
        if (isPoweredOn) {
            System.out.println("Migrating data from old storage...");
            oldStorage.flush();
        }
        
        this.storage = newStorage;
        
        notifyUpgrade("Storage", oldStorage.getType() + " " + oldStorage.getCapacity() + "GB", 
                     newStorage.getType() + " " + newStorage.getCapacity() + "GB");
        System.out.println("Storage upgraded from " + oldStorage.getType() + " to " + newStorage.getType());
        
        if (isPoweredOn) {
            storage.initialize();
        }
    }
    
    public ComputeResult executeTask(ComputeTask task) {
        if (!isPoweredOn) {
            return new ComputeResult(false, "Computer is not powered on", 0);
        }
        
        System.out.println("Executing task: " + task.getName());
        
        // Task execution involves all components working together
        long startTime = System.currentTimeMillis();
        
        // CPU processing
        double cpuTime = cpu.process(task.getCpuIntensity());
        
        // RAM usage
        boolean ramAvailable = ram.allocate(task.getMemoryRequirement());
        if (!ramAvailable) {
            return new ComputeResult(false, "Insufficient RAM", 0);
        }
        
        // GPU processing (if required)
        double gpuTime = 0;
        if (task.isGpuAccelerated()) {
            gpuTime = gpu.process(task.getGpuIntensity());
        }
        
        // Storage I/O
        double storageTime = storage.readWrite(task.getDataSize());
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        // Update system metrics
        monitor.recordTaskExecution(task, totalTime);
        
        // Release RAM
        ram.deallocate(task.getMemoryRequirement());
        
        double performanceScore = calculatePerformanceScore(cpuTime, gpuTime, storageTime);
        
        System.out.printf("Task completed in %dms (Performance Score: %.2f)\n", totalTime, performanceScore);
        
        return new ComputeResult(true, "Task completed successfully", performanceScore);
    }
    
    private double calculatePerformanceScore(double cpuTime, double gpuTime, double storageTime) {
        // Higher scores for faster execution
        double cpuScore = Math.max(0, 100 - cpuTime);
        double gpuScore = Math.max(0, 100 - gpuTime);
        double storageScore = Math.max(0, 100 - storageTime);
        
        return (cpuScore + gpuScore + storageScore) / 3.0;
    }
    
    public void addUpgradeListener(ComponentUpgradeListener listener) {
        upgradeListeners.add(listener);
    }
    
    private void notifyUpgrade(String component, String oldSpec, String newSpec) {
        for (ComponentUpgradeListener listener : upgradeListeners) {
            listener.onComponentUpgrade(serialNumber, component, oldSpec, newSpec);
        }
    }
    
    public void displaySystemInfo() {
        System.out.println("\n=== System Information ===");
        System.out.println("Model: " + model);
        System.out.println("Serial: " + serialNumber);
        System.out.println("Status: " + (isPoweredOn ? "ON" : "OFF"));
        System.out.println("CPU: " + cpu.getModel() + " (" + cpu.getCores() + " cores, " + cpu.getClockSpeed() + " GHz)");
        System.out.println("RAM: " + ram.getCapacity() + "GB " + ram.getType() + " (" + ram.getSpeed() + " MHz)");
        System.out.println("GPU: " + gpu.getModel() + " (" + gpu.getVram() + "GB VRAM)");
        System.out.println("Storage: " + storage.getCapacity() + "GB " + storage.getType() + " (" + storage.getSpeed() + " MB/s)");
        
        if (isPoweredOn) {
            monitor.displayMetrics();
        }
    }
    
    public SystemBenchmark runBenchmark() {
        if (!isPoweredOn) {
            throw new IllegalStateException("Computer must be powered on to run benchmark");
        }
        
        System.out.println("\n--- Running System Benchmark ---");
        
        // CPU benchmark
        ComputeTask cpuTask = new ComputeTask("CPU Benchmark", 90, 1024, false, 0, 100);
        ComputeResult cpuResult = executeTask(cpuTask);
        
        // GPU benchmark
        ComputeTask gpuTask = new ComputeTask("GPU Benchmark", 30, 2048, true, 95, 500);
        ComputeResult gpuResult = executeTask(gpuTask);
        
        // Storage benchmark
        ComputeTask storageTask = new ComputeTask("Storage Benchmark", 10, 512, false, 0, 5000);
        ComputeResult storageResult = executeTask(storageTask);
        
        // Memory benchmark
        ComputeTask memoryTask = new ComputeTask("Memory Benchmark", 50, 8192, false, 0, 1000);
        ComputeResult memoryResult = executeTask(memoryTask);
        
        return new SystemBenchmark(cpuResult.getPerformanceScore(), 
                                 gpuResult.getPerformanceScore(),
                                 storageResult.getPerformanceScore(),
                                 memoryResult.getPerformanceScore());
    }
    
    // Getters
    public CPU getCpu() { return cpu; }
    public RAM getRam() { return ram; }
    public GPU getGpu() { return gpu; }
    public Storage getStorage() { return storage; }
    public boolean isPoweredOn() { return isPoweredOn; }
    public SystemMonitor getMonitor() { return monitor; }
}
