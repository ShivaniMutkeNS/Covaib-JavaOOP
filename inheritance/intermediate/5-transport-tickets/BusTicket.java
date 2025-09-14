/**
 * Bus Ticket class extending Ticket
 * Demonstrates inheritance + specialized fields
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class BusTicket extends Ticket {
    private String busNumber;
    private String busType;
    private boolean hasAC;
    private boolean hasWifi;
    private String operator;
    private double distance;
    private double fuelSurcharge;
    private double serviceTax;
    
    /**
     * Constructor for BusTicket
     * @param ticketId Unique ticket identifier
     * @param passengerName Name of the passenger
     * @param fromLocation Departure location
     * @param toLocation Destination location
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @param basePrice Base price of the ticket
     * @param busNumber Bus number
     * @param busType Type of bus (Sleeper, Seater, Semi-Sleeper)
     * @param hasAC Whether bus has AC
     * @param hasWifi Whether bus has WiFi
     * @param operator Bus operator
     * @param distance Distance in kilometers
     */
    public BusTicket(String ticketId, String passengerName, String fromLocation, String toLocation, 
                    String departureTime, String arrivalTime, double basePrice, String busNumber, 
                    String busType, boolean hasAC, boolean hasWifi, String operator, double distance) {
        super(ticketId, passengerName, fromLocation, toLocation, departureTime, arrivalTime, basePrice, "Bus");
        this.busNumber = busNumber;
        this.busType = busType;
        this.hasAC = hasAC;
        this.hasWifi = hasWifi;
        this.operator = operator;
        this.distance = distance;
        this.fuelSurcharge = 0.0;
        this.serviceTax = 0.0;
    }
    
    /**
     * Override calculateFare method with bus ticket pricing
     * @return The calculated fare
     */
    @Override
    public double calculateFare() {
        double totalFare = basePrice;
        
        // Add bus type surcharge
        switch (busType.toLowerCase()) {
            case "sleeper":
                totalFare += 50.0;
                break;
            case "seater":
                totalFare += 0.0;
                break;
            case "semi-sleeper":
                totalFare += 25.0;
                break;
        }
        
        // Add AC surcharge
        if (hasAC) {
            totalFare += 100.0;
        }
        
        // Add WiFi surcharge
        if (hasWifi) {
            totalFare += 30.0;
        }
        
        // Calculate fuel surcharge based on distance
        fuelSurcharge = distance * 0.5;
        totalFare += fuelSurcharge;
        
        // Calculate service tax (5% of base price)
        serviceTax = basePrice * 0.05;
        totalFare += serviceTax;
        
        System.out.println("Bus fare calculation:");
        System.out.println("Base price: $" + String.format("%.2f", basePrice));
        System.out.println("Bus type surcharge: $" + String.format("%.2f", busType.equals("Sleeper") ? 50.0 : busType.equals("Semi-Sleeper") ? 25.0 : 0.0));
        System.out.println("AC surcharge: $" + String.format("%.2f", hasAC ? 100.0 : 0.0));
        System.out.println("WiFi surcharge: $" + String.format("%.2f", hasWifi ? 30.0 : 0.0));
        System.out.println("Fuel surcharge: $" + String.format("%.2f", fuelSurcharge));
        System.out.println("Service tax: $" + String.format("%.2f", serviceTax));
        System.out.println("Total fare: $" + String.format("%.2f", totalFare));
        
        return totalFare;
    }
    
    /**
     * Override cancelTicket method with bus ticket cancellation rules
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
            cancellationCharges = basePrice * 0.5; // 50% cancellation charges
        } else {
            cancellationCharges = basePrice * 0.1; // 10% cancellation charges
        }
        
        double refundAmount = basePrice - cancellationCharges;
        
        System.out.println("Bus ticket cancellation:");
        System.out.println("Cancellation charges: $" + String.format("%.2f", cancellationCharges));
        System.out.println("Refund amount: $" + String.format("%.2f", refundAmount));
        
        isBooked = false;
        seatNumber = "";
        bookingDate = "";
        
        return true;
    }
    
    /**
     * Override getTransportFeatures method with bus features
     * @return String description of bus features
     */
    @Override
    public String getTransportFeatures() {
        return "Bus Features: " +
               "Bus Number: " + busNumber + ", " +
               "Type: " + busType + ", " +
               "AC: " + (hasAC ? "Yes" : "No") + ", " +
               "WiFi: " + (hasWifi ? "Yes" : "No") + ", " +
               "Operator: " + operator + ", " +
               "Distance: " + String.format("%.1f", distance) + " km, " +
               "Fuel surcharge: $" + String.format("%.2f", fuelSurcharge) + ", " +
               "Service tax: $" + String.format("%.2f", serviceTax);
    }
    
    /**
     * Bus ticket specific method to get bus information
     * @return String with bus details
     */
    public String getBusInfo() {
        return String.format("Bus: %s (%s), Operator: %s, AC: %s, WiFi: %s", 
                           busNumber, busType, operator, hasAC ? "Yes" : "No", hasWifi ? "Yes" : "No");
    }
    
    /**
     * Bus ticket specific method to check amenities
     * @return String with available amenities
     */
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Available amenities: ");
        
        if (hasAC) {
            amenities.append("Air Conditioning, ");
        }
        if (hasWifi) {
            amenities.append("WiFi, ");
        }
        amenities.append("Comfortable seating, Reading light, Water bottle");
        
        return amenities.toString();
    }
    
    /**
     * Bus ticket specific method to get journey details
     * @return String with journey details
     */
    public String getJourneyDetails() {
        return String.format("Journey: %s to %s, Distance: %.1f km, Duration: %s to %s", 
                           fromLocation, toLocation, distance, departureTime, arrivalTime);
    }
    
    /**
     * Getter for bus number
     * @return The bus number
     */
    public String getBusNumber() {
        return busNumber;
    }
    
    /**
     * Getter for bus type
     * @return The bus type
     */
    public String getBusType() {
        return busType;
    }
    
    /**
     * Getter for AC availability
     * @return True if bus has AC
     */
    public boolean hasAC() {
        return hasAC;
    }
    
    /**
     * Getter for WiFi availability
     * @return True if bus has WiFi
     */
    public boolean hasWifi() {
        return hasWifi;
    }
    
    /**
     * Getter for operator
     * @return The bus operator
     */
    public String getOperator() {
        return operator;
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
     * Getter for service tax
     * @return The service tax
     */
    public double getServiceTax() {
        return serviceTax;
    }
    
    /**
     * Override toString to include bus ticket specific details
     * @return String representation of the bus ticket
     */
    @Override
    public String toString() {
        return super.toString() + " [Bus: " + busNumber + " (" + busType + "), AC: " + hasAC + ", WiFi: " + hasWifi + "]";
    }
}
