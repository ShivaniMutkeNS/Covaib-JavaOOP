package composition.computer;

/**
 * GPU Component Interface
 */
public interface GPU {
    void initialize();
    double process(double intensity);
    String getModel();
    int getVram();
    String getArchitecture();
}

/**
 * NVIDIA GPU Implementation
 */
class NvidiaGPU implements GPU {
    private final String model;
    private final int vram; // in GB
    private final String architecture;
    private final int cudaCores;
    private boolean isInitialized;
    
    public NvidiaGPU(String model, int vram, String architecture, int cudaCores) {
        this.model = model;
        this.vram = vram;
        this.architecture = architecture;
        this.cudaCores = cudaCores;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing NVIDIA GPU: " + model + " (" + cudaCores + " CUDA cores)");
        isInitialized = true;
    }
    
    @Override
    public double process(double intensity) {
        if (!isInitialized) {
            throw new IllegalStateException("GPU not initialized");
        }
        
        // NVIDIA GPUs excel at parallel processing
        double processingTime = intensity / (cudaCores * 0.001);
        
        try {
            Thread.sleep((long) processingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return processingTime;
    }
    
    @Override
    public String getModel() { return model; }
    
    @Override
    public int getVram() { return vram; }
    
    @Override
    public String getArchitecture() { return architecture; }
}

/**
 * AMD GPU Implementation
 */
class AMDGPU implements GPU {
    private final String model;
    private final int vram; // in GB
    private final String architecture;
    private final int streamProcessors;
    private boolean isInitialized;
    
    public AMDGPU(String model, int vram, String architecture, int streamProcessors) {
        this.model = model;
        this.vram = vram;
        this.architecture = architecture;
        this.streamProcessors = streamProcessors;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing AMD GPU: " + model + " (" + streamProcessors + " stream processors)");
        isInitialized = true;
    }
    
    @Override
    public double process(double intensity) {
        if (!isInitialized) {
            throw new IllegalStateException("GPU not initialized");
        }
        
        // AMD GPUs have competitive compute performance
        double processingTime = intensity / (streamProcessors * 0.0008);
        
        try {
            Thread.sleep((long) processingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return processingTime;
    }
    
    @Override
    public String getModel() { return model; }
    
    @Override
    public int getVram() { return vram; }
    
    @Override
    public String getArchitecture() { return architecture; }
}
