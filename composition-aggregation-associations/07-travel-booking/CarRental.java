package composition.travel;

/**
 * Car Rental component for travel packages
 */
public class CarRental {
    private final String vehicleType;
    private final String company;
    private final double dailyRate;
    private final int days;
    private final String pickupLocation;
    private final String dropoffLocation;
    private final boolean includesInsurance;
    private boolean isAvailable;
    private boolean isReserved;
    
    public CarRental(String vehicleType, String company, double dailyRate, int days,
                    String pickupLocation, String dropoffLocation, boolean includesInsurance) {
        this.vehicleType = vehicleType;
        this.company = company;
        this.dailyRate = dailyRate;
        this.days = days;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.includesInsurance = includesInsurance;
        this.isAvailable = true;
        this.isReserved = false;
    }
    
    public void reserve() {
        if (isAvailable && !isReserved) {
            isReserved = true;
            System.out.println("Car rental " + vehicleType + " reserved for " + days + " days");
        }
    }
    
    public void release() {
        if (isReserved) {
            isReserved = false;
            System.out.println("Car rental " + vehicleType + " reservation released");
        }
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    public double getTotalPrice() {
        return dailyRate * days;
    }
    
    // Getters
    public String getVehicleType() { return vehicleType; }
    public String getCompany() { return company; }
    public double getDailyRate() { return dailyRate; }
    public int getDays() { return days; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropoffLocation() { return dropoffLocation; }
    public boolean includesInsurance() { return includesInsurance; }
    public boolean isAvailable() { return isAvailable && !isReserved; }
    public boolean isReserved() { return isReserved; }
}
