/**
 * Base class for all hotel rooms
 * Demonstrates constructor chaining and shared vs specialized fields
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Room {
    protected int roomNumber;
    protected int floor;
    protected double basePrice;
    protected int maxOccupancy;
    protected boolean isAvailable;
    protected String roomType;
    protected boolean hasWifi;
    protected boolean hasAirConditioning;
    
    /**
     * Constructor for Room
     * @param roomNumber The room number
     * @param floor The floor number
     * @param basePrice The base price per night
     * @param maxOccupancy Maximum number of guests
     * @param roomType Type of room (Standard, Deluxe, Suite)
     */
    public Room(int roomNumber, int floor, double basePrice, int maxOccupancy, String roomType) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.basePrice = basePrice;
        this.maxOccupancy = maxOccupancy;
        this.roomType = roomType;
        this.isAvailable = true;
        this.hasWifi = true; // All rooms have WiFi
        this.hasAirConditioning = true; // All rooms have AC
    }
    
    /**
     * Abstract method to get the total price
     * Each room type has different pricing logic
     * @return The total price for the room
     */
    public abstract double getPrice();
    
    /**
     * Abstract method to get room amenities
     * Each room type has different amenities
     * @return String description of amenities
     */
    public abstract String getAmenities();
    
    /**
     * Concrete method to check if room is available
     * @return True if room is available
     */
    public boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Concrete method to book the room
     * @return True if booking successful
     */
    public boolean bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            return true;
        }
        return false;
    }
    
    /**
     * Concrete method to check out
     * @return True if checkout successful
     */
    public boolean checkOut() {
        if (!isAvailable) {
            isAvailable = true;
            return true;
        }
        return false;
    }
    
    /**
     * Concrete method to get room information
     * @return String with room details
     */
    public String getRoomInfo() {
        return String.format("Room %d on Floor %d - %s (Max: %d guests)", 
                           roomNumber, floor, roomType, maxOccupancy);
    }
    
    /**
     * Concrete method to get basic amenities
     * @return String with basic amenities
     */
    public String getBasicAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Basic Amenities: ");
        amenities.append("WiFi, Air Conditioning");
        return amenities.toString();
    }
    
    /**
     * Getter for room number
     * @return The room number
     */
    public int getRoomNumber() {
        return roomNumber;
    }
    
    /**
     * Getter for floor
     * @return The floor number
     */
    public int getFloor() {
        return floor;
    }
    
    /**
     * Getter for base price
     * @return The base price
     */
    public double getBasePrice() {
        return basePrice;
    }
    
    /**
     * Getter for max occupancy
     * @return Maximum number of guests
     */
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    
    /**
     * Getter for room type
     * @return The room type
     */
    public String getRoomType() {
        return roomType;
    }
    
    /**
     * Override toString method
     * @return String representation of the room
     */
    @Override
    public String toString() {
        return getRoomInfo() + " - $" + String.format("%.2f", getPrice()) + "/night";
    }
}
