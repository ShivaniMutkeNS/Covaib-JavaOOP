package composition.computer;

/**
 * CPU Component Interface
 */
public interface CPU {
    void initialize();
    double process(double intensity);
    String getModel();
    int getCores();
    double getClockSpeed();
    String getArchitecture();
}

/**
 * Intel CPU Implementation
 */
class IntelCPU implements CPU {
    private final String model;
    private final int cores;
    private final double clockSpeed;
    private final String architecture;
    private boolean isInitialized;
    
    public IntelCPU(String model, int cores, double clockSpeed, String architecture) {
        this.model = model;
        this.cores = cores;
        this.clockSpeed = clockSpeed;
        this.architecture = architecture;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing Intel CPU: " + model);
        isInitialized = true;
    }
    
    @Override
    public double process(double intensity) {
        if (!isInitialized) {
            throw new IllegalStateException("CPU not initialized");
        }
        
        // Intel CPUs have good single-thread performance
        double processingTime = intensity / (clockSpeed * cores * 0.9);
        
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
    public int getCores() { return cores; }
    
    @Override
    public double getClockSpeed() { return clockSpeed; }
    
    @Override
    public String getArchitecture() { return architecture; }
}

/**
 * AMD CPU Implementation
 */
class AMDCPU implements CPU {
    private final String model;
    private final int cores;
    private final double clockSpeed;
    private final String architecture;
    private boolean isInitialized;
    
    public AMDCPU(String model, int cores, double clockSpeed, String architecture) {
        this.model = model;
        this.cores = cores;
        this.clockSpeed = clockSpeed;
        this.architecture = architecture;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing AMD CPU: " + model);
        isInitialized = true;
    }
    
    @Override
    public double process(double intensity) {
        if (!isInitialized) {
            throw new IllegalStateException("CPU not initialized");
        }
        
        // AMD CPUs have better multi-thread performance
        double processingTime = intensity / (clockSpeed * cores * 1.1);
        
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
    public int getCores() { return cores; }
    
    @Override
    public double getClockSpeed() { return clockSpeed; }
    
    @Override
    public String getArchitecture() { return architecture; }
}
