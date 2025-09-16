package composition.computer;

/**
 * Storage Component Interface
 */
public interface Storage {
    void initialize();
    void flush();
    double readWrite(int dataSize);
    int getCapacity();
    String getType();
    int getSpeed();
}

/**
 * SSD Storage Implementation
 */
class SSDStorage implements Storage {
    private final int capacity; // in GB
    private final int speed; // in MB/s
    private final String interface_type;
    private boolean isInitialized;
    
    public SSDStorage(int capacity, int speed, String interface_type) {
        this.capacity = capacity;
        this.speed = speed;
        this.interface_type = interface_type;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing SSD: " + capacity + "GB " + interface_type + " @ " + speed + "MB/s");
        isInitialized = true;
    }
    
    @Override
    public void flush() {
        if (isInitialized) {
            System.out.println("Flushing SSD cache...");
        }
    }
    
    @Override
    public double readWrite(int dataSize) {
        if (!isInitialized) {
            throw new IllegalStateException("Storage not initialized");
        }
        
        // SSD has fast, consistent read/write speeds
        double transferTime = (double) dataSize / speed;
        
        try {
            Thread.sleep((long) transferTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return transferTime;
    }
    
    @Override
    public int getCapacity() { return capacity; }
    
    @Override
    public String getType() { return "SSD (" + interface_type + ")"; }
    
    @Override
    public int getSpeed() { return speed; }
}

/**
 * HDD Storage Implementation
 */
class HDDStorage implements Storage {
    private final int capacity; // in GB
    private final int rpm;
    private final int speed; // in MB/s
    private boolean isInitialized;
    
    public HDDStorage(int capacity, int rpm, int speed) {
        this.capacity = capacity;
        this.rpm = rpm;
        this.speed = speed;
        this.isInitialized = false;
    }
    
    @Override
    public void initialize() {
        System.out.println("Initializing HDD: " + capacity + "GB @ " + rpm + "RPM (" + speed + "MB/s)");
        isInitialized = true;
    }
    
    @Override
    public void flush() {
        if (isInitialized) {
            System.out.println("Parking HDD heads...");
        }
    }
    
    @Override
    public double readWrite(int dataSize) {
        if (!isInitialized) {
            throw new IllegalStateException("Storage not initialized");
        }
        
        // HDD has variable speeds due to mechanical nature
        double seekTime = 8.5; // Average seek time in ms
        double transferTime = (double) dataSize / speed;
        double totalTime = seekTime + transferTime;
        
        try {
            Thread.sleep((long) totalTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return totalTime;
    }
    
    @Override
    public int getCapacity() { return capacity; }
    
    @Override
    public String getType() { return "HDD (" + rpm + "RPM)"; }
    
    @Override
    public int getSpeed() { return speed; }
}
