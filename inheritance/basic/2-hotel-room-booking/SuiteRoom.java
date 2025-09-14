/**
 * Suite Room class extending Room
 * Demonstrates constructor chaining and method overriding
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class SuiteRoom extends Room {
    private boolean hasLivingRoom;
    private boolean hasDiningArea;
    private boolean hasPrivateBar;
    private String butlerService;
    private boolean hasPrivateElevator;
    
    /**
     * Constructor for SuiteRoom
     * @param roomNumber The room number
     * @param floor The floor number
     * @param basePrice The base price per night
     * @param maxOccupancy Maximum number of guests
     * @param hasLivingRoom Whether the suite has a living room
     * @param hasDiningArea Whether the suite has a dining area
     * @param hasPrivateBar Whether the suite has a private bar
     * @param butlerService Type of butler service (24/7, Day, Evening)
     * @param hasPrivateElevator Whether the suite has private elevator access
     */
    public SuiteRoom(int roomNumber, int floor, double basePrice, int maxOccupancy, 
                    boolean hasLivingRoom, boolean hasDiningArea, boolean hasPrivateBar, 
                    String butlerService, boolean hasPrivateElevator) {
        super(roomNumber, floor, basePrice, maxOccupancy, "Suite");
        this.hasLivingRoom = hasLivingRoom;
        this.hasDiningArea = hasDiningArea;
        this.hasPrivateBar = hasPrivateBar;
        this.butlerService = butlerService;
        this.hasPrivateElevator = hasPrivateElevator;
    }
    
    /**
     * Override getPrice method with suite room pricing
     * @return The total price for the suite room
     */
    @Override
    public double getPrice() {
        double totalPrice = basePrice;
        
        // Add living room surcharge
        if (hasLivingRoom) {
            totalPrice += 100.0;
        }
        
        // Add dining area surcharge
        if (hasDiningArea) {
            totalPrice += 80.0;
        }
        
        // Add private bar surcharge
        if (hasPrivateBar) {
            totalPrice += 120.0;
        }
        
        // Add butler service surcharge
        switch (butlerService.toLowerCase()) {
            case "24/7":
                totalPrice += 200.0;
                break;
            case "day":
                totalPrice += 100.0;
                break;
            case "evening":
                totalPrice += 80.0;
                break;
        }
        
        // Add private elevator surcharge
        if (hasPrivateElevator) {
            totalPrice += 150.0;
        }
        
        // Suite rooms have a 25% luxury surcharge
        totalPrice *= 1.25;
        
        return totalPrice;
    }
    
    /**
     * Override getAmenities method with suite room amenities
     * @return String description of suite room amenities
     */
    @Override
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Suite Room Amenities: ");
        amenities.append("WiFi, Air Conditioning, 75\" Smart TV, Work Desk, ");
        amenities.append("Premium Bedding, Coffee Machine, Iron & Ironing Board, ");
        amenities.append("Separate Bedroom, Marble Bathroom, Premium Toiletries");
        
        if (hasLivingRoom) {
            amenities.append(", Spacious Living Room");
        }
        
        if (hasDiningArea) {
            amenities.append(", Private Dining Area");
        }
        
        if (hasPrivateBar) {
            amenities.append(", Private Bar with Premium Spirits");
        }
        
        if (hasPrivateElevator) {
            amenities.append(", Private Elevator Access");
        }
        
        amenities.append(", ").append(butlerService).append(" Butler Service");
        
        return amenities.toString();
    }
    
    /**
     * Suite room specific method for butler service
     * @return String description of butler service
     */
    public String requestButlerService() {
        return "Personal butler service: " + butlerService;
    }
    
    /**
     * Suite room specific method for private dining
     * @return String description of private dining
     */
    public String requestPrivateDining() {
        return "Private dining experience with personal chef";
    }
    
    /**
     * Suite room specific method for VIP services
     * @return String description of VIP services
     */
    public String requestVIPServices() {
        return "VIP services including airport transfer and exclusive access";
    }
    
    /**
     * Suite room specific method for event hosting
     * @return String description of event hosting
     */
    public String hostEvent() {
        return "Suite available for private events and meetings";
    }
    
    /**
     * Getter for living room availability
     * @return True if suite has living room
     */
    public boolean hasLivingRoom() {
        return hasLivingRoom;
    }
    
    /**
     * Getter for dining area availability
     * @return True if suite has dining area
     */
    public boolean hasDiningArea() {
        return hasDiningArea;
    }
    
    /**
     * Getter for private bar availability
     * @return True if suite has private bar
     */
    public boolean hasPrivateBar() {
        return hasPrivateBar;
    }
    
    /**
     * Getter for butler service type
     * @return The type of butler service
     */
    public String getButlerService() {
        return butlerService;
    }
    
    /**
     * Getter for private elevator availability
     * @return True if suite has private elevator
     */
    public boolean hasPrivateElevator() {
        return hasPrivateElevator;
    }
    
    /**
     * Override toString to include suite room specific details
     * @return String representation of the suite room
     */
    @Override
    public String toString() {
        return super.toString() + " [Living Room: " + hasLivingRoom + ", Dining Area: " + hasDiningArea + 
               ", Private Bar: " + hasPrivateBar + ", Butler: " + butlerService + 
               ", Private Elevator: " + hasPrivateElevator + "]";
    }
}
