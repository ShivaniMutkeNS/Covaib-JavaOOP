# 🚌 Transport Tickets System - Inheritance & Specialized Fields

## Problem Statement
Create a transport ticket management system with different types of tickets. Implement inheritance with base class `Ticket` and subclasses `BusTicket`, `TrainTicket`, and `FlightTicket`. Each ticket type should override the abstract methods `calculateFare()` and `cancelTicket()` with their specific business logic and specialized fields.

## Learning Objectives
- **Inheritance + Specialized Fields**: Common functionality with transport-specific attributes
- **Method Overriding**: Each transport type has different fare calculation and cancellation logic
- **Abstract Methods**: Force subclasses to implement specific behaviors
- **Real-world Business Logic**: Transport industry pricing and regulations

## Class Hierarchy

```
Ticket (Abstract)
├── BusTicket
├── TrainTicket
└── FlightTicket
```

## Key Concepts Demonstrated

### 1. Inheritance + Specialized Fields
- Common fields (ticketId, passengerName, route) in base class
- Specialized fields (busNumber, trainNumber, flightNumber) in subclasses
- Transport-specific attributes and business logic

### 2. Method Overriding
- Each transport type provides its own implementation of abstract methods
- Different fare calculation logic for each transport type
- Different cancellation rules and charges

### 3. Abstract Methods
- Force subclasses to implement specific behaviors
- Ensure consistent interface across all ticket types
- Enable polymorphic method calls

## Code Structure

### Ticket.java (Abstract Base Class)
```java
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
    
    // Abstract methods - must be implemented by subclasses
    public abstract double calculateFare();
    public abstract boolean cancelTicket();
    public abstract String getTransportFeatures();
    
    // Concrete methods - shared by all tickets
    public boolean bookTicket(String seatNumber) { ... }
    public String getTicketInfo() { ... }
}
```

### BusTicket.java (Concrete Subclass)
```java
public class BusTicket extends Ticket {
    private String busNumber;
    private String busType;
    private boolean hasAC;
    private boolean hasWifi;
    private String operator;
    private double distance;
    private double fuelSurcharge;
    private double serviceTax;
    
    @Override
    public double calculateFare() {
        double totalFare = basePrice;
        // Add bus type surcharge
        if (busType.equals("Sleeper")) totalFare += 50.0;
        // Add AC surcharge
        if (hasAC) totalFare += 100.0;
        // Add WiFi surcharge
        if (hasWifi) totalFare += 30.0;
        // Calculate fuel surcharge
        fuelSurcharge = distance * 0.5;
        totalFare += fuelSurcharge;
        // Calculate service tax
        serviceTax = basePrice * 0.05;
        totalFare += serviceTax;
        return totalFare;
    }
    
    @Override
    public boolean cancelTicket() {
        // Bus-specific cancellation logic
        double cancellationCharges = basePrice * 0.5; // 50% for same day
        // ... cancellation logic
    }
}
```

### TrainTicket.java (Concrete Subclass)
```java
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
    
    @Override
    public double calculateFare() {
        double totalFare = basePrice;
        // Add coach type surcharge
        if (coachType.equals("AC")) totalFare += 200.0;
        // Add berth type surcharge
        if (berthType.equals("Lower")) totalFare += 20.0;
        // Add meals surcharge
        if (hasMeals) totalFare += 150.0;
        // Add bedding surcharge
        if (hasBedding) totalFare += 50.0;
        // Calculate reservation charges
        reservationCharges = 20.0;
        totalFare += reservationCharges;
        // Calculate Tatkal charges
        if (isTatkal) {
            tatkalCharges = basePrice * 0.1;
            totalFare += tatkalCharges;
        }
        return totalFare;
    }
    
    @Override
    public boolean cancelTicket() {
        // Train-specific cancellation logic
        double cancellationCharges = basePrice * 0.25; // 25% for same day
        // ... cancellation logic
    }
}
```

### FlightTicket.java (Concrete Subclass)
```java
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
    
    @Override
    public double calculateFare() {
        double totalFare = basePrice;
        // Add class type surcharge
        if (classType.equals("Business")) totalFare += 500.0;
        if (classType.equals("First")) totalFare += 1000.0;
        // Add seat type surcharge
        if (seatType.equals("Window")) totalFare += 25.0;
        // Add amenities surcharge
        if (hasMeals) totalFare += 50.0;
        if (hasEntertainment) totalFare += 30.0;
        if (hasWifi) totalFare += 40.0;
        // Calculate fuel surcharge
        fuelSurcharge = distance * 0.8;
        totalFare += fuelSurcharge;
        // Calculate airport tax
        airportTax = isInternational ? 200.0 : 100.0;
        totalFare += airportTax;
        // Calculate service fee
        serviceFee = basePrice * 0.03;
        totalFare += serviceFee;
        return totalFare;
    }
    
    @Override
    public boolean cancelTicket() {
        // Flight-specific cancellation logic
        double cancellationCharges = basePrice * 0.75; // 75% for same day
        // ... cancellation logic
    }
}
```

## How to Run

1. Compile all Java files:
```bash
javac *.java
```

2. Run the demo:
```bash
java TransportDemo
```

## Expected Output

```
🚌 TRANSPORT TICKET SYSTEM 🚌
==================================================

📋 TICKET INFORMATION:
--------------------------------------------------
Ticket ID: BT001, Passenger: Alice Johnson, Route: New York to Boston, Departure: 2024-01-15 08:00, Arrival: 2024-01-15 12:00, Type: Bus, Booked: false
Ticket ID: BT002, Passenger: Bob Smith, Route: Los Angeles to San Francisco, Departure: 2024-01-16 10:00, Arrival: 2024-01-16 14:00, Type: Bus, Booked: false
Ticket ID: TT001, Passenger: Carol Davis, Route: Chicago to Detroit, Departure: 2024-01-17 09:00, Arrival: 2024-01-17 13:00, Type: Train, Booked: false
Ticket ID: TT002, Passenger: David Wilson, Route: Miami to Orlando, Departure: 2024-01-18 11:00, Arrival: 2024-01-18 15:00, Type: Train, Booked: false
Ticket ID: FT001, Passenger: Eva Brown, Route: New York to Los Angeles, Departure: 2024-01-19 14:00, Arrival: 2024-01-19 17:00, Type: Flight, Booked: false
Ticket ID: FT002, Passenger: Frank Miller, Route: New York to London, Departure: 2024-01-20 20:00, Arrival: 2024-01-21 08:00, Type: Flight, Booked: false

🎫 TICKET BOOKING DEMONSTRATION:
--------------------------------------------------

Alice Johnson (Bus):
Ticket booked successfully!
Seat: A1
Booking date: 2024-01-14

Bob Smith (Bus):
Ticket booked successfully!
Seat: B2
Booking date: 2024-01-14

Carol Davis (Train):
Ticket booked successfully!
Seat: C3
Booking date: 2024-01-14

David Wilson (Train):
Ticket booked successfully!
Seat: D4
Booking date: 2024-01-14

Eva Brown (Flight):
Ticket booked successfully!
Seat: E5
Booking date: 2024-01-14

Frank Miller (Flight):
Ticket booked successfully!
Seat: F6
Booking date: 2024-01-14

💰 FARE CALCULATION:
--------------------------------------------------

Alice Johnson (Bus):
Bus fare calculation:
Base price: $50.00
Bus type surcharge: $50.00
AC surcharge: $100.00
WiFi surcharge: $30.00
Fuel surcharge: $100.00
Service tax: $2.50
Total fare: $332.50

Bob Smith (Bus):
Bus fare calculation:
Base price: $40.00
Bus type surcharge: $0.00
AC surcharge: $0.00
WiFi surcharge: $0.00
Fuel surcharge: $175.00
Service tax: $2.00
Total fare: $217.00

Carol Davis (Train):
Train fare calculation:
Base price: $80.00
Coach type surcharge: $200.00
Berth type surcharge: $20.00
Meals surcharge: $150.00
Bedding surcharge: $50.00
Reservation charges: $20.00
Tatkal charges: $0.00
Total fare: $520.00

David Wilson (Train):
Train fare calculation:
Base price: $60.00
Coach type surcharge: $50.00
Berth type surcharge: $10.00
Meals surcharge: $0.00
Bedding surcharge: $0.00
Reservation charges: $20.00
Tatkal charges: $6.00
Total fare: $146.00

Eva Brown (Flight):
Flight fare calculation:
Base price: $300.00
Class type surcharge: $0.00
Seat type surcharge: $25.00
Meals surcharge: $50.00
Entertainment surcharge: $30.00
WiFi surcharge: $40.00
Fuel surcharge: $2000.00
Airport tax: $100.00
Service fee: $9.00
Total fare: $2554.00

Frank Miller (Flight):
Flight fare calculation:
Base price: $800.00
Class type surcharge: $500.00
Seat type surcharge: $15.00
Meals surcharge: $50.00
Entertainment surcharge: $30.00
WiFi surcharge: $40.00
Fuel surcharge: $4400.00
Airport tax: $200.00
Service fee: $24.00
Total fare: $6059.00

🚗 TRANSPORT FEATURES:
--------------------------------------------------

Alice Johnson (Bus):
Bus Features: Bus Number: B123, Type: Sleeper, AC: Yes, WiFi: Yes, Operator: Greyhound, Distance: 200.0 km, Fuel surcharge: $100.00, Service tax: $2.50

Bob Smith (Bus):
Bus Features: Bus Number: B456, Type: Seater, AC: No, WiFi: No, Operator: Megabus, Distance: 350.0 km, Fuel surcharge: $175.00, Service tax: $2.00

Carol Davis (Train):
Train Features: Train: T789 (Express), Coach: AC, Berth: Lower, Meals: Included, Bedding: Provided, Zone: Central, Distance: 300.0 km, Tatkal: No, Reservation charges: $20.00

David Wilson (Train):
Train Features: Train: T012 (Local), Coach: Non-AC, Berth: Upper, Meals: Not included, Bedding: Not provided, Zone: Southern, Distance: 250.0 km, Tatkal: Yes, Reservation charges: $20.00

Eva Brown (Flight):
Flight Features: Flight: F345 (American Airlines), Aircraft: Boeing 737, Class: Economy, Seat: Window, Meals: Included, Entertainment: Available, WiFi: Available, Terminal: T1, Gate: A12, Distance: 2500.0 km, Type: Domestic, Fuel surcharge: $2000.00, Airport tax: $100.00

Frank Miller (Flight):
Flight Features: Flight: F678 (British Airways), Aircraft: Boeing 777, Class: Business, Seat: Aisle, Meals: Included, Entertainment: Available, WiFi: Available, Terminal: T4, Gate: B15, Distance: 5500.0 km, Type: International, Fuel surcharge: $4400.00, Airport tax: $200.00

🔧 TICKET-SPECIFIC BEHAVIORS:
--------------------------------------------------
Bus Ticket Behaviors:
Bus: B789 (Semi-Sleeper), Operator: Amtrak, AC: Yes, WiFi: No
Available amenities: Air Conditioning, Comfortable seating, Reading light, Water bottle
Journey: Seattle to Portland, Distance: 180.0 km, Duration: 2024-01-21 07:00 to 2024-01-21 11:00
Bus fare calculation:
Base price: $45.00
Bus type surcharge: $25.00
AC surcharge: $100.00
WiFi surcharge: $0.00
Fuel surcharge: $90.00
Service tax: $2.25
Total fare: $262.25

Train Ticket Behaviors:
Train: T345 (Express), Coach: AC, Berth: Middle, Zone: Western
Available amenities: Meals, Bedding, Reading light, Water bottle, Toilet facilities
Journey: Denver to Salt Lake City, Distance: 400.0 km, Duration: 2024-01-22 08:00 to 2024-01-22 12:00
PNR Status: Confirmed, Seat: C3, Coach: AC
Train fare calculation:
Base price: $70.00
Coach type surcharge: $200.00
Berth type surcharge: $0.00
Meals surcharge: $150.00
Bedding surcharge: $50.00
Reservation charges: $20.00
Tatkal charges: $0.00
Total fare: $490.00

Flight Ticket Behaviors:
Flight: F901 (United Airlines), Aircraft: Airbus A320, Class: Economy, Seat: Window, Terminal: T2, Gate: C8
Available amenities: Meals, Entertainment, Comfortable seating, Reading light, Water bottle, Toilet facilities
Journey: Chicago to Las Vegas, Distance: 1800.0 km, Duration: 2024-01-23 15:00 to 2024-01-23 17:00
Boarding: Terminal T2, Gate C8, Seat E5
Baggage allowance: 1 checked bag (23kg), 1 carry-on bag (7kg)
Flight fare calculation:
Base price: $250.00
Class type surcharge: $0.00
Seat type surcharge: $25.00
Meals surcharge: $50.00
Entertainment surcharge: $30.00
WiFi surcharge: $0.00
Fuel surcharge: $1440.00
Airport tax: $100.00
Service fee: $7.50
Total fare: $1902.50

❌ TICKET CANCELLATION DEMONSTRATION:
--------------------------------------------------

Alice Johnson (Bus):
Bus ticket cancellation:
Cancellation charges: $25.00
Refund amount: $25.00

Bob Smith (Bus):
Bus ticket cancellation:
Cancellation charges: $20.00
Refund amount: $20.00

Carol Davis (Train):
Train ticket cancellation:
Cancellation charges: $20.00
Refund amount: $60.00

David Wilson (Train):
Train ticket cancellation:
Cancellation charges: $3.00
Refund amount: $57.00

Eva Brown (Flight):
Flight ticket cancellation:
Cancellation charges: $225.00
Refund amount: $75.00

Frank Miller (Flight):
Flight ticket cancellation:
Cancellation charges: $120.00
Refund amount: $680.00

🗺️ JOURNEY INFORMATION:
--------------------------------------------------
Alice Johnson (Bus):
Route: New York → Boston
Journey duration: 2024-01-15 08:00 to 2024-01-15 12:00

Bob Smith (Bus):
Route: Los Angeles → San Francisco
Journey duration: 2024-01-16 10:00 to 2024-01-16 14:00

Carol Davis (Train):
Route: Chicago → Detroit
Journey duration: 2024-01-17 09:00 to 2024-01-17 13:00

David Wilson (Train):
Route: Miami → Orlando
Journey duration: 2024-01-18 11:00 to 2024-01-18 15:00

Eva Brown (Flight):
Route: New York → Los Angeles
Journey duration: 2024-01-19 14:00 to 2024-01-19 17:00

Frank Miller (Flight):
Route: New York → London
Journey duration: 2024-01-20 20:00 to 2024-01-21 08:00

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing tickets using polymorphism:
1. Alice Johnson (Bus)
   Route: New York → Boston
   Features: Bus Features: Bus Number: B123, Type: Sleeper, AC: Yes, WiFi: Yes, Operator: Greyhound, Distance: 200.0 km, Fuel surcharge: $100.00, Service tax: $2.50

2. Bob Smith (Bus)
   Route: Los Angeles → San Francisco
   Features: Bus Features: Bus Number: B456, Type: Seater, AC: No, WiFi: No, Operator: Megabus, Distance: 350.0 km, Fuel surcharge: $175.00, Service tax: $2.00

3. Carol Davis (Train)
   Route: Chicago → Detroit
   Features: Train Features: Train: T789 (Express), Coach: AC, Berth: Lower, Meals: Included, Bedding: Provided, Zone: Central, Distance: 300.0 km, Tatkal: No, Reservation charges: $20.00

4. David Wilson (Train)
   Route: Miami → Orlando
   Features: Train Features: Train: T012 (Local), Coach: Non-AC, Berth: Upper, Meals: Not included, Bedding: Not provided, Zone: Southern, Distance: 250.0 km, Tatkal: Yes, Reservation charges: $20.00

5. Eva Brown (Flight)
   Route: New York → Los Angeles
   Features: Flight Features: Flight: F345 (American Airlines), Aircraft: Boeing 737, Class: Economy, Seat: Window, Meals: Included, Entertainment: Available, WiFi: Available, Terminal: T1, Gate: A12, Distance: 2500.0 km, Type: Domestic, Fuel surcharge: $2000.00, Airport tax: $100.00

6. Frank Miller (Flight)
   Route: New York → London
   Features: Flight Features: Flight: F678 (British Airways), Aircraft: Boeing 777, Class: Business, Seat: Aisle, Meals: Included, Entertainment: Available, WiFi: Available, Terminal: T4, Gate: B15, Distance: 5500.0 km, Type: International, Fuel surcharge: $4400.00, Airport tax: $200.00

💰 FARE COMPARISON:
--------------------------------------------------
Transport Type          Base Price    Total Fare     Surcharges
--------------------------------------------------
Bus                     $50.00       $332.50        $282.50
Bus                     $40.00       $217.00        $177.00
Train                   $80.00       $520.00        $440.00
Train                   $60.00       $146.00        $86.00
Flight                  $300.00      $2554.00       $2254.00
Flight                  $800.00      $6059.00       $5259.00

🔍 SPECIALIZED FIELD USAGE:
--------------------------------------------------
Alice Johnson (Bus):
Bus: B123 (Sleeper)
Operator: Greyhound
Distance: 200.0 km

Bob Smith (Bus):
Bus: B456 (Seater)
Operator: Megabus
Distance: 350.0 km

Carol Davis (Train):
Train: T789 (Express)
Coach: AC, Berth: Lower
Zone: Central

David Wilson (Train):
Train: T012 (Local)
Coach: Non-AC, Berth: Upper
Zone: Southern

Eva Brown (Flight):
Flight: F345 (American Airlines)
Aircraft: Boeing 737, Class: Economy
Terminal: T1, Gate: A12

Frank Miller (Flight):
Flight: F678 (British Airways)
Aircraft: Boeing 777, Class: Business
Terminal: T4, Gate: B15

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Inheritance + Specialized Fields**: Common functionality with transport-specific attributes
2. **Method Overriding**: Each transport type provides its own implementation
3. **Abstract Methods**: Force subclasses to implement specific behaviors
4. **Real-world Business Logic**: Transport industry pricing and regulations
5. **Polymorphism**: Same method call produces different results based on object type
6. **Specialized Fields**: Transport-specific attributes and business logic

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Business Logic**: Real-world transport industry pricing and regulations
- **Specialized Fields**: Transport-specific attributes and business logic

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different fare calculation and cancellation strategies for different transport types
3. **Polymorphism**: Single interface for different transport types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.
