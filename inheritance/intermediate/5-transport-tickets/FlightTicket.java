/**
 * Flight Ticket class extending Ticket
 * Demonstrates inheritance + specialized fields
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class FlightTicket extends Ticket {
    private String flightNumber;
    private String airline;
    private String aircraftType;
    private String classType;
    private String seatType;
    private boolean hasMeals;
    private boolean hasEntertainment;
    private boolean hasWifi;
    private String terminal;
    private String gate;
    private double distance;
    private double fuelSurcharge;
    private double airportTax;
    private double serviceFee;
    private boolean isInternational;
    
    /**
     * Constructor for FlightTicket
     * @param ticketId Unique ticket identifier
     * @param passengerName Name of the passenger
     * @param fromLocation Departure location
     * @param toLocation Destination location
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @param basePrice Base price of the ticket
     * @param flightNumber Flight number
     * @param airline Airline name
     * @param aircraftType Type of aircraft
     * @param classType Class type (Economy, Business, First)
     * @param seatType Type of seat (Window, Aisle, Middle)
     * @param hasMeals Whether meals are included
     * @param hasEntertainment Whether entertainment is available
     * @param hasWifi Whether WiFi is available
     * @param terminal Terminal number
     * @param gate Gate number
     * @param distance Distance in kilometers
     * @param isInternational Whether it's an international flight
     */
    public FlightTicket(String ticketId, String passengerName, String fromLocation, String toLocation, 
                       String departureTime, String arrivalTime, double basePrice, String flightNumber, 
                       String airline, String aircraftType, String classType, String seatType, 
                       boolean hasMeals, boolean hasEntertainment, boolean hasWifi, String terminal, 
                       String gate, double distance, boolean isInternational) {
        super(ticketId, passengerName, fromLocation, toLocation, departureTime, arrivalTime, basePrice, "Flight");
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.aircraftType = aircraftType;
        this.classType = classType;
        this.seatType = seatType;
        this.hasMeals = hasMeals;
        this.hasEntertainment = hasEntertainment;
        this.hasWifi = hasWifi;
        this.terminal = terminal;
        this.gate = gate;
        this.distance = distance;
        this.isInternational = isInternational;
        this.fuelSurcharge = 0.0;
        this.airportTax = 0.0;
        this.serviceFee = 0.0;
    }
    
    /**
     * Override calculateFare method with flight ticket pricing
     * @return The calculated fare
     */
    @Override
    public double calculateFare() {
        double totalFare = basePrice;
        
        // Add class type surcharge
        switch (classType.toLowerCase()) {
            case "economy":
                totalFare += 0.0;
                break;
            case "business":
                totalFare += 500.0;
                break;
            case "first":
                totalFare += 1000.0;
                break;
        }
        
        // Add seat type surcharge
        switch (seatType.toLowerCase()) {
            case "window":
                totalFare += 25.0;
                break;
            case "aisle":
                totalFare += 15.0;
                break;
            case "middle":
                totalFare += 0.0;
                break;
        }
        
        // Add meals surcharge
        if (hasMeals) {
            totalFare += 50.0;
        }
        
        // Add entertainment surcharge
        if (hasEntertainment) {
            totalFare += 30.0;
        }
        
        // Add WiFi surcharge
        if (hasWifi) {
            totalFare += 40.0;
        }
        
        // Calculate fuel surcharge based on distance
        fuelSurcharge = distance * 0.8;
        totalFare += fuelSurcharge;
        
        // Calculate airport tax
        if (isInternational) {
            airportTax = 200.0;
        } else {
            airportTax = 100.0;
        }
        totalFare += airportTax;
        
        // Calculate service fee
        serviceFee = basePrice * 0.03; // 3% service fee
        totalFare += serviceFee;
        
        System.out.println("Flight fare calculation:");
        System.out.println("Base price: $" + String.format("%.2f", basePrice));
        System.out.println("Class type surcharge: $" + String.format("%.2f", classType.equals("Business") ? 500.0 : classType.equals("First") ? 1000.0 : 0.0));
        System.out.println("Seat type surcharge: $" + String.format("%.2f", seatType.equals("Window") ? 25.0 : seatType.equals("Aisle") ? 15.0 : 0.0));
        System.out.println("Meals surcharge: $" + String.format("%.2f", hasMeals ? 50.0 : 0.0));
        System.out.println("Entertainment surcharge: $" + String.format("%.2f", hasEntertainment ? 30.0 : 0.0));
        System.out.println("WiFi surcharge: $" + String.format("%.2f", hasWifi ? 40.0 : 0.0));
        System.out.println("Fuel surcharge: $" + String.format("%.2f", fuelSurcharge));
        System.out.println("Airport tax: $" + String.format("%.2f", airportTax));
        System.out.println("Service fee: $" + String.format("%.2f", serviceFee));
        System.out.println("Total fare: $" + String.format("%.2f", totalFare));
        
        return totalFare;
    }
    
    /**
     * Override cancelTicket method with flight ticket cancellation rules
     * @return True if cancellation successful
     */
    @Override
    public boolean cancelTicket() {
        if (!isBooked) {
            System.out.println("Ticket is not booked. Cannot cancel.");
            return false;
        }
        
        // Calculate cancellation charges based on time before departure
        double cancellationCharges = 0.0;
        String today = java.time.LocalDate.now().toString();
        String departureDate = departureTime.split(" ")[0]; // Assuming format "YYYY-MM-DD HH:MM"
        
        if (today.equals(departureDate)) {
            cancellationCharges = basePrice * 0.75; // 75% cancellation charges
        } else {
            cancellationCharges = basePrice * 0.15; // 15% cancellation charges
        }
        
        double refundAmount = basePrice - cancellationCharges;
        
        System.out.println("Flight ticket cancellation:");
        System.out.println("Cancellation charges: $" + String.format("%.2f", cancellationCharges));
        System.out.println("Refund amount: $" + String.format("%.2f", refundAmount));
        
        isBooked = false;
        seatNumber = "";
        bookingDate = "";
        
        return true;
    }
    
    /**
     * Override getTransportFeatures method with flight features
     * @return String description of flight features
     */
    @Override
    public String getTransportFeatures() {
        return "Flight Features: " +
               "Flight: " + flightNumber + " (" + airline + "), " +
               "Aircraft: " + aircraftType + ", " +
               "Class: " + classType + ", " +
               "Seat: " + seatType + ", " +
               "Meals: " + (hasMeals ? "Included" : "Not included") + ", " +
               "Entertainment: " + (hasEntertainment ? "Available" : "Not available") + ", " +
               "WiFi: " + (hasWifi ? "Available" : "Not available") + ", " +
               "Terminal: " + terminal + ", " +
               "Gate: " + gate + ", " +
               "Distance: " + String.format("%.1f", distance) + " km, " +
               "Type: " + (isInternational ? "International" : "Domestic") + ", " +
               "Fuel surcharge: $" + String.format("%.2f", fuelSurcharge) + ", " +
               "Airport tax: $" + String.format("%.2f", airportTax);
    }
    
    /**
     * Flight ticket specific method to get flight information
     * @return String with flight details
     */
    public String getFlightInfo() {
        return String.format("Flight: %s (%s), Aircraft: %s, Class: %s, Seat: %s, Terminal: %s, Gate: %s", 
                           flightNumber, airline, aircraftType, classType, seatType, terminal, gate);
    }
    
    /**
     * Flight ticket specific method to check amenities
     * @return String with available amenities
     */
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Available amenities: ");
        
        if (hasMeals) {
            amenities.append("Meals, ");
        }
        if (hasEntertainment) {
            amenities.append("Entertainment, ");
        }
        if (hasWifi) {
            amenities.append("WiFi, ");
        }
        amenities.append("Comfortable seating, Reading light, Water bottle, Toilet facilities");
        
        return amenities.toString();
    }
    
    /**
     * Flight ticket specific method to get journey details
     * @return String with journey details
     */
    public String getJourneyDetails() {
        return String.format("Journey: %s to %s, Distance: %.1f km, Duration: %s to %s", 
                           fromLocation, toLocation, distance, departureTime, arrivalTime);
    }
    
    /**
     * Flight ticket specific method to check boarding information
     * @return String with boarding information
     */
    public String getBoardingInfo() {
        return "Boarding: Terminal " + terminal + ", Gate " + gate + ", Seat " + seatNumber;
    }
    
    /**
     * Flight ticket specific method to check baggage allowance
     * @return String with baggage allowance
     */
    public String getBaggageAllowance() {
        switch (classType.toLowerCase()) {
            case "economy":
                return "Baggage allowance: 1 checked bag (23kg), 1 carry-on bag (7kg)";
            case "business":
                return "Baggage allowance: 2 checked bags (32kg each), 1 carry-on bag (7kg)";
            case "first":
                return "Baggage allowance: 3 checked bags (32kg each), 1 carry-on bag (7kg)";
            default:
                return "Baggage allowance: Standard";
        }
    }
    
    /**
     * Getter for flight number
     * @return The flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }
    
    /**
     * Getter for airline
     * @return The airline name
     */
    public String getAirline() {
        return airline;
    }
    
    /**
     * Getter for aircraft type
     * @return The aircraft type
     */
    public String getAircraftType() {
        return aircraftType;
    }
    
    /**
     * Getter for class type
     * @return The class type
     */
    public String getClassType() {
        return classType;
    }
    
    /**
     * Getter for seat type
     * @return The seat type
     */
    public String getSeatType() {
        return seatType;
    }
    
    /**
     * Getter for meals availability
     * @return True if meals are included
     */
    public boolean hasMeals() {
        return hasMeals;
    }
    
    /**
     * Getter for entertainment availability
     * @return True if entertainment is available
     */
    public boolean hasEntertainment() {
        return hasEntertainment;
    }
    
    /**
     * Getter for WiFi availability
     * @return True if WiFi is available
     */
    public boolean hasWifi() {
        return hasWifi;
    }
    
    /**
     * Getter for terminal
     * @return The terminal number
     */
    public String getTerminal() {
        return terminal;
    }
    
    /**
     * Getter for gate
     * @return The gate number
     */
    public String getGate() {
        return gate;
    }
    
    /**
     * Getter for distance
     * @return The distance in kilometers
     */
    public double getDistance() {
        return distance;
    }
    
    /**
     * Getter for fuel surcharge
     * @return The fuel surcharge
     */
    public double getFuelSurcharge() {
        return fuelSurcharge;
    }
    
    /**
     * Getter for airport tax
     * @return The airport tax
     */
    public double getAirportTax() {
        return airportTax;
    }
    
    /**
     * Getter for service fee
     * @return The service fee
     */
    public double getServiceFee() {
        return serviceFee;
    }
    
    /**
     * Getter for international status
     * @return True if it's an international flight
     */
    public boolean isInternational() {
        return isInternational;
    }
    
    /**
     * Override toString to include flight ticket specific details
     * @return String representation of the flight ticket
     */
    @Override
    public String toString() {
        return super.toString() + " [Flight: " + flightNumber + " (" + classType + "), Seat: " + seatType + ", International: " + isInternational + "]";
    }
}
