/**
 * Deluxe Room class extending Room
 * Demonstrates constructor chaining and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class DeluxeRoom extends Room {
    private boolean hasJacuzzi;
    private boolean hasKitchenette;
    private String bedType;
    private boolean hasRoomService;
    
    /**
     * Constructor for DeluxeRoom
     * @param roomNumber The room number
     * @param floor The floor number
     * @param basePrice The base price per night
     * @param maxOccupancy Maximum number of guests
     * @param hasJacuzzi Whether the room has a jacuzzi
     * @param hasKitchenette Whether the room has a kitchenette
     * @param bedType Type of bed (King, Queen, Twin)
     * @param hasRoomService Whether the room has room service
     */
    public DeluxeRoom(int roomNumber, int floor, double basePrice, int maxOccupancy, 
                     boolean hasJacuzzi, boolean hasKitchenette, String bedType, boolean hasRoomService) {
        super(roomNumber, floor, basePrice, maxOccupancy, "Deluxe");
        this.hasJacuzzi = hasJacuzzi;
        this.hasKitchenette = hasKitchenette;
        this.bedType = bedType;
        this.hasRoomService = hasRoomService;
    }
    
    /**
     * Override getPrice method with deluxe room pricing
     * @return The total price for the deluxe room
     */
    @Override
    public double getPrice() {
        double totalPrice = basePrice;
        
        // Add jacuzzi surcharge
        if (hasJacuzzi) {
            totalPrice += 50.0;
        }
        
        // Add kitchenette surcharge
        if (hasKitchenette) {
            totalPrice += 40.0;
        }
        
        // Add bed type surcharge
        switch (bedType.toLowerCase()) {
            case "king":
                totalPrice += 30.0;
                break;
            case "queen":
                totalPrice += 20.0;
                break;
            case "twin":
                totalPrice += 10.0;
                break;
        }
        
        // Add room service surcharge
        if (hasRoomService) {
            totalPrice += 25.0;
        }
        
        // Deluxe rooms have a 15% luxury surcharge
        totalPrice *= 1.15;
        
        return totalPrice;
    }
    
    /**
     * Override getAmenities method with deluxe room amenities
     * @return String description of deluxe room amenities
     */
    @Override
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Deluxe Room Amenities: ");
        amenities.append("WiFi, Air Conditioning, 55\" Smart TV, Work Desk, ");
        amenities.append("Premium Bedding, Coffee Machine, Iron & Ironing Board");
        
        if (hasJacuzzi) {
            amenities.append(", Private Jacuzzi");
        }
        
        if (hasKitchenette) {
            amenities.append(", Kitchenette with Refrigerator");
        }
        
        if (hasRoomService) {
            amenities.append(", 24/7 Room Service");
        }
        
        amenities.append(", ").append(bedType).append(" Bed");
        
        return amenities.toString();
    }
    
    /**
     * Deluxe room specific method for concierge service
     * @return String description of concierge service
     */
    public String requestConcierge() {
        return "Personal concierge service available";
    }
    
    /**
     * Deluxe room specific method for spa services
     * @return String description of spa services
     */
    public String requestSpaServices() {
        return "In-room spa services available";
    }
    
    /**
     * Deluxe room specific method for premium dining
     * @return String description of premium dining
     */
    public String requestPremiumDining() {
        return "Premium dining options available";
    }
    
    /**
     * Getter for jacuzzi availability
     * @return True if room has jacuzzi
     */
    public boolean hasJacuzzi() {
        return hasJacuzzi;
    }
    
    /**
     * Getter for kitchenette availability
     * @return True if room has kitchenette
     */
    public boolean hasKitchenette() {
        return hasKitchenette;
    }
    
    /**
     * Getter for bed type
     * @return The type of bed
     */
    public String getBedType() {
        return bedType;
    }
    
    /**
     * Getter for room service availability
     * @return True if room has room service
     */
    public boolean hasRoomService() {
        return hasRoomService;
    }
    
    /**
     * Override toString to include deluxe room specific details
     * @return String representation of the deluxe room
     */
    @Override
    public String toString() {
        return super.toString() + " [Jacuzzi: " + hasJacuzzi + ", Kitchenette: " + hasKitchenette + 
               ", Bed: " + bedType + ", Room Service: " + hasRoomService + "]";
    }
}
