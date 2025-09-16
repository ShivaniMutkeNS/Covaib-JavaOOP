package composition.computer;

/**
 * RAM Component Interface
 */
public interface RAM {
    void initialize();
    boolean allocate(int sizeInMB);
    void deallocate(int sizeInMB);
    int getCapacity();
    String getType();
    int getSpeed();
    int getAvailableMemory();
}

/**
 * DDR4 RAM Implementation
 */
class DDR4RAM implements RAM {
    private final int capacity; // in GB
    private final int speed; // in MHz
    private int usedMemory; // in MB
    private boolean isInitialized;
    
    public DDR4RAM(int capacity, int speed) {
        this.capacity = capacity;
        this.speed = speed;
        this.usedMemory = 0;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing DDR4 RAM: " + capacity + "GB @ " + speed + "MHz");
        isInitialized = true;
    }
    
    @Override
    public boolean allocate(int sizeInMB) {
        if (!isInitialized) {
            throw new IllegalStateException("RAM not initialized");
        }
        
        int availableMemory = (capacity * 1024) - usedMemory;
        if (sizeInMB <= availableMemory) {
            usedMemory += sizeInMB;
            return true;
        }
        return false;
    }
    
    @Override
    public void deallocate(int sizeInMB) {
        usedMemory = Math.max(0, usedMemory - sizeInMB);
    }
    
    @Override
    public int getCapacity() { return capacity; }
    
    @Override
    public String getType() { return "DDR4"; }
    
    @Override
    public int getSpeed() { return speed; }
    
    @Override
    public int getAvailableMemory() { return (capacity * 1024) - usedMemory; }
}

/**
 * DDR5 RAM Implementation
 */
class DDR5RAM implements RAM {
    private final int capacity; // in GB
    private final int speed; // in MHz
    private int usedMemory; // in MB
    private boolean isInitialized;
    
    public DDR5RAM(int capacity, int speed) {
        this.capacity = capacity;
        this.speed = speed;
        this.usedMemory = 0;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing DDR5 RAM: " + capacity + "GB @ " + speed + "MHz");
        isInitialized = true;
    }
    
    @Override
    public boolean allocate(int sizeInMB) {
        if (!isInitialized) {
            throw new IllegalStateException("RAM not initialized");
        }
        
        int availableMemory = (capacity * 1024) - usedMemory;
        if (sizeInMB <= availableMemory) {
            usedMemory += sizeInMB;
            return true;
        }
        return false;
    }
    
    @Override
    public void deallocate(int sizeInMB) {
        usedMemory = Math.max(0, usedMemory - sizeInMB);
    }
    
    @Override
    public int getCapacity() { return capacity; }
    
    @Override
    public String getType() { return "DDR5"; }
    
    @Override
    public int getSpeed() { return speed; }
    
    @Override
    public int getAvailableMemory() { return (capacity * 1024) - usedMemory; }
}
