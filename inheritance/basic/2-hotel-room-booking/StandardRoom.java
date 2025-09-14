/**
 * Standard Room class extending Room
 * Demonstrates constructor chaining and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class StandardRoom extends Room {
    private boolean hasBalcony;
    private String viewType;
    private boolean hasMiniBar;
    
    /**
     * Constructor for StandardRoom
     * @param roomNumber The room number
     * @param floor The floor number
     * @param basePrice The base price per night
     * @param maxOccupancy Maximum number of guests
     * @param hasBalcony Whether the room has a balcony
     * @param viewType Type of view (City, Garden, Pool)
     * @param hasMiniBar Whether the room has a mini bar
     */
    public StandardRoom(int roomNumber, int floor, double basePrice, int maxOccupancy, 
                       boolean hasBalcony, String viewType, boolean hasMiniBar) {
        super(roomNumber, floor, basePrice, maxOccupancy, "Standard");
        this.hasBalcony = hasBalcony;
        this.viewType = viewType;
        this.hasMiniBar = hasMiniBar;
    }
    
    /**
     * Override getPrice method with standard room pricing
     * @return The total price for the standard room
     */
    @Override
    public double getPrice() {
        double totalPrice = basePrice;
        
        // Add balcony surcharge
        if (hasBalcony) {
            totalPrice += 20.0;
        }
        
        // Add view surcharge
        switch (viewType.toLowerCase()) {
            case "city":
                totalPrice += 15.0;
                break;
            case "garden":
                totalPrice += 10.0;
                break;
            case "pool":
                totalPrice += 25.0;
                break;
        }
        
        // Add mini bar surcharge
        if (hasMiniBar) {
            totalPrice += 30.0;
        }
        
        return totalPrice;
    }
    
    /**
     * Override getAmenities method with standard room amenities
     * @return String description of standard room amenities
     */
    @Override
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Standard Room Amenities: ");
        amenities.append("WiFi, Air Conditioning, TV, Work Desk");
        
        if (hasBalcony) {
            amenities.append(", Private Balcony");
        }
        
        if (hasMiniBar) {
            amenities.append(", Mini Bar");
        }
        
        amenities.append(", ").append(viewType).append(" View");
        
        return amenities.toString();
    }
    
    /**
     * Standard room specific method for room service
     * @return String description of room service
     */
    public String requestRoomService() {
        return "Standard room service available 24/7";
    }
    
    /**
     * Standard room specific method for housekeeping
     * @return String description of housekeeping
     */
    public String requestHousekeeping() {
        return "Daily housekeeping service included";
    }
    
    /**
     * Getter for balcony availability
     * @return True if room has balcony
     */
    public boolean hasBalcony() {
        return hasBalcony;
    }
    
    /**
     * Getter for view type
     * @return The type of view
     */
    public String getViewType() {
        return viewType;
    }
    
    /**
     * Getter for mini bar availability
     * @return True if room has mini bar
     */
    public boolean hasMiniBar() {
        return hasMiniBar;
    }
    
    /**
     * Override toString to include standard room specific details
     * @return String representation of the standard room
     */
    @Override
    public String toString() {
        return super.toString() + " [Balcony: " + hasBalcony + ", View: " + viewType + ", Mini Bar: " + hasMiniBar + "]";
    }
}
