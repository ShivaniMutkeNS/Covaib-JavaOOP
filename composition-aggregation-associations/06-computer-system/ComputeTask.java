package composition.computer;

/**
 * Compute Task data class representing workloads
 */
public class ComputeTask {
    private final String name;
    private final double cpuIntensity; // 0-100
    private final int memoryRequirement; // in MB
    private final boolean gpuAccelerated;
    private final double gpuIntensity; // 0-100
    private final int dataSize; // in MB for storage I/O
    
    public ComputeTask(String name, double cpuIntensity, int memoryRequirement, 
                      boolean gpuAccelerated, double gpuIntensity, int dataSize) {
        this.name = name;
        this.cpuIntensity = cpuIntensity;
        this.memoryRequirement = memoryRequirement;
        this.gpuAccelerated = gpuAccelerated;
        this.gpuIntensity = gpuIntensity;
        this.dataSize = dataSize;
    }
    
    public String getName() { return name; }
    public double getCpuIntensity() { return cpuIntensity; }
    public int getMemoryRequirement() { return memoryRequirement; }
    public boolean isGpuAccelerated() { return gpuAccelerated; }
    public double getGpuIntensity() { return gpuIntensity; }
    public int getDataSize() { return dataSize; }
}
