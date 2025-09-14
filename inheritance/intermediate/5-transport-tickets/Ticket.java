/**
 * Abstract base class for all transport tickets
 * Demonstrates inheritance + specialized fields
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Ticket {
    protected String ticketId;
    protected String passengerName;
    protected String fromLocation;
    protected String toLocation;
    protected String departureTime;
    protected String arrivalTime;
    protected double basePrice;
    protected String ticketType;
    protected boolean isBooked;
    protected String bookingDate;
    protected String seatNumber;
    
    /**
     * Constructor for Ticket
     * @param ticketId Unique ticket identifier
     * @param passengerName Name of the passenger
     * @param fromLocation Departure location
     * @param toLocation Destination location
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @param basePrice Base price of the ticket
     * @param ticketType Type of ticket
     */
    public Ticket(String ticketId, String passengerName, String fromLocation, String toLocation, 
                  String departureTime, String arrivalTime, double basePrice, String ticketType) {
        this.ticketId = ticketId;
        this.passengerName = passengerName;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        this.ticketType = ticketType;
        this.isBooked = false;
        this.bookingDate = "";
        this.seatNumber = "";
    }
    
    /**
     * Abstract method to calculate fare
     * Each transport type has different fare calculation logic
     * @return The calculated fare
     */
    public abstract double calculateFare();
    
    /**
     * Abstract method to cancel ticket
     * Each transport type has different cancellation rules
     * @return True if cancellation successful
     */
    public abstract boolean cancelTicket();
    
    /**
     * Abstract method to get transport features
     * Each transport type has different features
     * @return String description of features
     */
    public abstract String getTransportFeatures();
    
    /**
     * Concrete method to book ticket
     * @param seatNumber Seat number for the ticket
     * @return True if booking successful
     */
    public boolean bookTicket(String seatNumber) {
        if (isBooked) {
            System.out.println("Ticket is already booked.");
            return false;
        }
        
        this.seatNumber = seatNumber;
        this.isBooked = true;
        this.bookingDate = java.time.LocalDate.now().toString();
        
        System.out.println("Ticket booked successfully!");
        System.out.println("Seat: " + seatNumber);
        System.out.println("Booking date: " + bookingDate);
        return true;
    }
    
    /**
     * Concrete method to get ticket information
     * @return String with ticket details
     */
    public String getTicketInfo() {
        return String.format("Ticket ID: %s, Passenger: %s, Route: %s to %s, " +
                           "Departure: %s, Arrival: %s, Type: %s, Booked: %s", 
                           ticketId, passengerName, fromLocation, toLocation, 
                           departureTime, arrivalTime, ticketType, isBooked);
    }
    
    /**
     * Concrete method to check if ticket is booked
     * @return True if ticket is booked
     */
    public boolean isBooked() {
        return isBooked;
    }
    
    /**
     * Concrete method to get journey duration
     * @return String with journey duration
     */
    public String getJourneyDuration() {
        return "Journey duration: " + departureTime + " to " + arrivalTime;
    }
    
    /**
     * Concrete method to get route information
     * @return String with route details
     */
    public String getRouteInfo() {
        return "Route: " + fromLocation + " → " + toLocation;
    }
    
    /**
     * Getter for ticket ID
     * @return The ticket ID
     */
    public String getTicketId() {
        return ticketId;
    }
    
    /**
     * Getter for passenger name
     * @return The passenger name
     */
    public String getPassengerName() {
        return passengerName;
    }
    
    /**
     * Getter for from location
     * @return The departure location
     */
    public String getFromLocation() {
        return fromLocation;
    }
    
    /**
     * Getter for to location
     * @return The destination location
     */
    public String getToLocation() {
        return toLocation;
    }
    
    /**
     * Getter for departure time
     * @return The departure time
     */
    public String getDepartureTime() {
        return departureTime;
    }
    
    /**
     * Getter for arrival time
     * @return The arrival time
     */
    public String getArrivalTime() {
        return arrivalTime;
    }
    
    /**
     * Getter for base price
     * @return The base price
     */
    public double getBasePrice() {
        return basePrice;
    }
    
    /**
     * Getter for ticket type
     * @return The ticket type
     */
    public String getTicketType() {
        return ticketType;
    }
    
    /**
     * Getter for seat number
     * @return The seat number
     */
    public String getSeatNumber() {
        return seatNumber;
    }
    
    /**
     * Getter for booking date
     * @return The booking date
     */
    public String getBookingDate() {
        return bookingDate;
    }
    
    /**
     * Override toString method
     * @return String representation of the ticket
     */
    @Override
    public String toString() {
        return getTicketInfo();
    }
}
