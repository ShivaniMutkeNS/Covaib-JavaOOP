package composition.travel;

/**
 * Hotel component for travel packages
 */
public class Hotel {
    private final String name;
    private final String location;
    private final int starRating;
    private final double pricePerNight;
    private final int nights;
    private final String roomType;
    private final boolean includesBreakfast;
    private boolean isAvailable;
    private boolean isReserved;
    
    public Hotel(String name, String location, int starRating, double pricePerNight, 
                int nights, String roomType, boolean includesBreakfast) {
        this.name = name;
        this.location = location;
        this.starRating = starRating;
        this.pricePerNight = pricePerNight;
        this.nights = nights;
        this.roomType = roomType;
        this.includesBreakfast = includesBreakfast;
        this.isAvailable = true;
        this.isReserved = false;
    }
    
    public void reserve() {
        if (isAvailable && !isReserved) {
            isReserved = true;
            System.out.println("Hotel " + name + " reserved for " + nights + " nights");
        }
    }
    
    public void release() {
        if (isReserved) {
            isReserved = false;
            System.out.println("Hotel " + name + " reservation released");
        }
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    public double getTotalPrice() {
        return pricePerNight * nights;
    }
    
    // Getters
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getStarRating() { return starRating; }
    public double getPricePerNight() { return pricePerNight; }
    public int getNights() { return nights; }
    public String getRoomType() { return roomType; }
    public boolean includesBreakfast() { return includesBreakfast; }
    public boolean isAvailable() { return isAvailable && !isReserved; }
    public boolean isReserved() { return isReserved; }
}
