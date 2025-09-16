package composition.travel;

import java.time.LocalDateTime;

/**
 * Flight component for travel packages
 */
public class Flight {
    private final String flightNumber;
    private final String airline;
    private final String departureCity;
    private final String arrivalCity;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final double price;
    private final String flightClass;
    private boolean isAvailable;
    private boolean isReserved;
    
    public Flight(String flightNumber, String airline, String departureCity, String arrivalCity,
                 LocalDateTime departureTime, LocalDateTime arrivalTime, double price, String flightClass) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.flightClass = flightClass;
        this.isAvailable = true;
        this.isReserved = false;
    }
    
    public void reserve() {
        if (isAvailable && !isReserved) {
            isReserved = true;
            System.out.println("Flight " + flightNumber + " reserved");
        }
    }
    
    public void release() {
        if (isReserved) {
            isReserved = false;
            System.out.println("Flight " + flightNumber + " reservation released");
        }
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    // Getters
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getDepartureCity() { return departureCity; }
    public String getArrivalCity() { return arrivalCity; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public double getPrice() { return price; }
    public String getFlightClass() { return flightClass; }
    public boolean isAvailable() { return isAvailable && !isReserved; }
    public boolean isReserved() { return isReserved; }
}
