/**
 * Train Ticket class extending Ticket
 * Demonstrates inheritance + specialized fields
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class TrainTicket extends Ticket {
    private String trainNumber;
    private String trainName;
    private String coachType;
    private String berthType;
    private boolean hasMeals;
    private boolean hasBedding;
    private String railwayZone;
    private double distance;
    private double reservationCharges;
    private double tatkalCharges;
    private boolean isTatkal;
    
    /**
     * Constructor for TrainTicket
     * @param ticketId Unique ticket identifier
     * @param passengerName Name of the passenger
     * @param fromLocation Departure location
     * @param toLocation Destination location
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @param basePrice Base price of the ticket
     * @param trainNumber Train number
     * @param trainName Train name
     * @param coachType Type of coach (AC, Non-AC, Sleeper)
     * @param berthType Type of berth (Lower, Middle, Upper, Side)
     * @param hasMeals Whether meals are included
     * @param hasBedding Whether bedding is provided
     * @param railwayZone Railway zone
     * @param distance Distance in kilometers
     * @param isTatkal Whether it's a Tatkal ticket
     */
    public TrainTicket(String ticketId, String passengerName, String fromLocation, String toLocation, 
                      String departureTime, String arrivalTime, double basePrice, String trainNumber, 
                      String trainName, String coachType, String berthType, boolean hasMeals, 
                      boolean hasBedding, String railwayZone, double distance, boolean isTatkal) {
        super(ticketId, passengerName, fromLocation, toLocation, departureTime, arrivalTime, basePrice, "Train");
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.coachType = coachType;
        this.berthType = berthType;
        this.hasMeals = hasMeals;
        this.hasBedding = hasBedding;
        this.railwayZone = railwayZone;
        this.distance = distance;
        this.isTatkal = isTatkal;
        this.reservationCharges = 0.0;
        this.tatkalCharges = 0.0;
    }
    
    /**
     * Override calculateFare method with train ticket pricing
     * @return The calculated fare
     */
    @Override
    public double calculateFare() {
        double totalFare = basePrice;
        
        // Add coach type surcharge
        switch (coachType.toLowerCase()) {
            case "ac":
                totalFare += 200.0;
                break;
            case "non-ac":
                totalFare += 50.0;
                break;
            case "sleeper":
                totalFare += 0.0;
                break;
        }
        
        // Add berth type surcharge
        switch (berthType.toLowerCase()) {
            case "lower":
                totalFare += 20.0;
                break;
            case "middle":
                totalFare += 0.0;
                break;
            case "upper":
                totalFare += 10.0;
                break;
            case "side":
                totalFare += 15.0;
                break;
        }
        
        // Add meals surcharge
        if (hasMeals) {
            totalFare += 150.0;
        }
        
        // Add bedding surcharge
        if (hasBedding) {
            totalFare += 50.0;
        }
        
        // Calculate reservation charges
        reservationCharges = 20.0;
        totalFare += reservationCharges;
        
        // Calculate Tatkal charges
        if (isTatkal) {
            tatkalCharges = basePrice * 0.1; // 10% Tatkal charges
            totalFare += tatkalCharges;
        }
        
        System.out.println("Train fare calculation:");
        System.out.println("Base price: $" + String.format("%.2f", basePrice));
        System.out.println("Coach type surcharge: $" + String.format("%.2f", coachType.equals("AC") ? 200.0 : coachType.equals("Non-AC") ? 50.0 : 0.0));
        System.out.println("Berth type surcharge: $" + String.format("%.2f", berthType.equals("Lower") ? 20.0 : berthType.equals("Upper") ? 10.0 : berthType.equals("Side") ? 15.0 : 0.0));
        System.out.println("Meals surcharge: $" + String.format("%.2f", hasMeals ? 150.0 : 0.0));
        System.out.println("Bedding surcharge: $" + String.format("%.2f", hasBedding ? 50.0 : 0.0));
        System.out.println("Reservation charges: $" + String.format("%.2f", reservationCharges));
        System.out.println("Tatkal charges: $" + String.format("%.2f", tatkalCharges));
        System.out.println("Total fare: $" + String.format("%.2f", totalFare));
        
        return totalFare;
    }
    
    /**
     * Override cancelTicket method with train ticket cancellation rules
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
            cancellationCharges = basePrice * 0.25; // 25% cancellation charges
        } else {
            cancellationCharges = basePrice * 0.05; // 5% cancellation charges
        }
        
        double refundAmount = basePrice - cancellationCharges;
        
        System.out.println("Train ticket cancellation:");
        System.out.println("Cancellation charges: $" + String.format("%.2f", cancellationCharges));
        System.out.println("Refund amount: $" + String.format("%.2f", refundAmount));
        
        isBooked = false;
        seatNumber = "";
        bookingDate = "";
        
        return true;
    }
    
    /**
     * Override getTransportFeatures method with train features
     * @return String description of train features
     */
    @Override
    public String getTransportFeatures() {
        return "Train Features: " +
               "Train: " + trainNumber + " (" + trainName + "), " +
               "Coach: " + coachType + ", " +
               "Berth: " + berthType + ", " +
               "Meals: " + (hasMeals ? "Included" : "Not included") + ", " +
               "Bedding: " + (hasBedding ? "Provided" : "Not provided") + ", " +
               "Zone: " + railwayZone + ", " +
               "Distance: " + String.format("%.1f", distance) + " km, " +
               "Tatkal: " + (isTatkal ? "Yes" : "No") + ", " +
               "Reservation charges: $" + String.format("%.2f", reservationCharges);
    }
    
    /**
     * Train ticket specific method to get train information
     * @return String with train details
     */
    public String getTrainInfo() {
        return String.format("Train: %s (%s), Coach: %s, Berth: %s, Zone: %s", 
                           trainNumber, trainName, coachType, berthType, railwayZone);
    }
    
    /**
     * Train ticket specific method to check amenities
     * @return String with available amenities
     */
    public String getAmenities() {
        StringBuilder amenities = new StringBuilder();
        amenities.append("Available amenities: ");
        
        if (hasMeals) {
            amenities.append("Meals, ");
        }
        if (hasBedding) {
            amenities.append("Bedding, ");
        }
        amenities.append("Reading light, Water bottle, Toilet facilities");
        
        return amenities.toString();
    }
    
    /**
     * Train ticket specific method to get journey details
     * @return String with journey details
     */
    public String getJourneyDetails() {
        return String.format("Journey: %s to %s, Distance: %.1f km, Duration: %s to %s", 
                           fromLocation, toLocation, distance, departureTime, arrivalTime);
    }
    
    /**
     * Train ticket specific method to check PNR status
     * @return String with PNR status
     */
    public String getPNRStatus() {
        return "PNR Status: Confirmed, Seat: " + seatNumber + ", Coach: " + coachType;
    }
    
    /**
     * Getter for train number
     * @return The train number
     */
    public String getTrainNumber() {
        return trainNumber;
    }
    
    /**
     * Getter for train name
     * @return The train name
     */
    public String getTrainName() {
        return trainName;
    }
    
    /**
     * Getter for coach type
     * @return The coach type
     */
    public String getCoachType() {
        return coachType;
    }
    
    /**
     * Getter for berth type
     * @return The berth type
     */
    public String getBerthType() {
        return berthType;
    }
    
    /**
     * Getter for meals availability
     * @return True if meals are included
     */
    public boolean hasMeals() {
        return hasMeals;
    }
    
    /**
     * Getter for bedding availability
     * @return True if bedding is provided
     */
    public boolean hasBedding() {
        return hasBedding;
    }
    
    /**
     * Getter for railway zone
     * @return The railway zone
     */
    public String getRailwayZone() {
        return railwayZone;
    }
    
    /**
     * Getter for distance
     * @return The distance in kilometers
     */
    public double getDistance() {
        return distance;
    }
    
    /**
     * Getter for reservation charges
     * @return The reservation charges
     */
    public double getReservationCharges() {
        return reservationCharges;
    }
    
    /**
     * Getter for Tatkal charges
     * @return The Tatkal charges
     */
    public double getTatkalCharges() {
        return tatkalCharges;
    }
    
    /**
     * Getter for Tatkal status
     * @return True if it's a Tatkal ticket
     */
    public boolean isTatkal() {
        return isTatkal;
    }
    
    /**
     * Override toString to include train ticket specific details
     * @return String representation of the train ticket
     */
    @Override
    public String toString() {
        return super.toString() + " [Train: " + trainNumber + " (" + coachType + "), Berth: " + berthType + ", Tatkal: " + isTatkal + "]";
    }
}
