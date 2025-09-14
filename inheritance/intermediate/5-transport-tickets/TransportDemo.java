/**
 * Demo class to showcase transport ticket system
 * Demonstrates inheritance, method overriding, and specialized fields
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class TransportDemo {
    public static void main(String[] args) {
        System.out.println("🚌 TRANSPORT TICKET SYSTEM 🚌");
        System.out.println("=" .repeat(50));
        
        // Create different types of tickets
        Ticket[] tickets = {
            new BusTicket("BT001", "Alice Johnson", "New York", "Boston", "2024-01-15 08:00", "2024-01-15 12:00", 50.0, "B123", "Sleeper", true, true, "Greyhound", 200.0),
            new BusTicket("BT002", "Bob Smith", "Los Angeles", "San Francisco", "2024-01-16 10:00", "2024-01-16 14:00", 40.0, "B456", "Seater", false, false, "Megabus", 350.0),
            new TrainTicket("TT001", "Carol Davis", "Chicago", "Detroit", "2024-01-17 09:00", "2024-01-17 13:00", 80.0, "T789", "Express", "AC", "Lower", true, true, "Central", 300.0, false),
            new TrainTicket("TT002", "David Wilson", "Miami", "Orlando", "2024-01-18 11:00", "2024-01-18 15:00", 60.0, "T012", "Local", "Non-AC", "Upper", false, false, "Southern", 250.0, true),
            new FlightTicket("FT001", "Eva Brown", "New York", "Los Angeles", "2024-01-19 14:00", "2024-01-19 17:00", 300.0, "F345", "American Airlines", "Boeing 737", "Economy", "Window", true, true, true, "T1", "A12", 2500.0, false),
            new FlightTicket("FT002", "Frank Miller", "New York", "London", "2024-01-20 20:00", "2024-01-21 08:00", 800.0, "F678", "British Airways", "Boeing 777", "Business", "Aisle", true, true, true, "T4", "B15", 5500.0, true)
        };
        
        // Display ticket information
        System.out.println("\n📋 TICKET INFORMATION:");
        System.out.println("-".repeat(50));
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getTicketInfo());
        }
        
        // Demonstrate ticket booking
        System.out.println("\n🎫 TICKET BOOKING DEMONSTRATION:");
        System.out.println("-".repeat(50));
        String[] seatNumbers = {"A1", "B2", "C3", "D4", "E5", "F6"};
        
        for (int i = 0; i < tickets.length; i++) {
            System.out.println("\n" + tickets[i].getPassengerName() + " (" + tickets[i].getTicketType() + "):");
            tickets[i].bookTicket(seatNumbers[i]);
        }
        
        // Demonstrate fare calculation
        System.out.println("\n💰 FARE CALCULATION:");
        System.out.println("-".repeat(50));
        for (Ticket ticket : tickets) {
            System.out.println("\n" + ticket.getPassengerName() + " (" + ticket.getTicketType() + "):");
            double fare = ticket.calculateFare();
            System.out.println("Total fare: $" + String.format("%.2f", fare));
        }
        
        // Display transport features
        System.out.println("\n🚗 TRANSPORT FEATURES:");
        System.out.println("-".repeat(50));
        for (Ticket ticket : tickets) {
            System.out.println("\n" + ticket.getPassengerName() + " (" + ticket.getTicketType() + "):");
            System.out.println(ticket.getTransportFeatures());
        }
        
        // Demonstrate ticket-specific behaviors
        System.out.println("\n🔧 TICKET-SPECIFIC BEHAVIORS:");
        System.out.println("-".repeat(50));
        
        // Bus Ticket specific behaviors
        BusTicket busTicket = new BusTicket("BT003", "Grace Lee", "Seattle", "Portland", "2024-01-21 07:00", "2024-01-21 11:00", 45.0, "B789", "Semi-Sleeper", true, false, "Amtrak", 180.0);
        System.out.println("Bus Ticket Behaviors:");
        System.out.println(busTicket.getBusInfo());
        System.out.println(busTicket.getAmenities());
        System.out.println(busTicket.getJourneyDetails());
        busTicket.calculateFare();
        System.out.println();
        
        // Train Ticket specific behaviors
        TrainTicket trainTicket = new TrainTicket("TT003", "Henry Chen", "Denver", "Salt Lake City", "2024-01-22 08:00", "2024-01-22 12:00", 70.0, "T345", "Express", "AC", "Middle", true, true, "Western", 400.0, false);
        System.out.println("Train Ticket Behaviors:");
        System.out.println(trainTicket.getTrainInfo());
        System.out.println(trainTicket.getAmenities());
        System.out.println(trainTicket.getJourneyDetails());
        System.out.println(trainTicket.getPNRStatus());
        trainTicket.calculateFare();
        System.out.println();
        
        // Flight Ticket specific behaviors
        FlightTicket flightTicket = new FlightTicket("FT003", "Ivy Rodriguez", "Chicago", "Las Vegas", "2024-01-23 15:00", "2024-01-23 17:00", 250.0, "F901", "United Airlines", "Airbus A320", "Economy", "Window", true, true, false, "T2", "C8", 1800.0, false);
        System.out.println("Flight Ticket Behaviors:");
        System.out.println(flightTicket.getFlightInfo());
        System.out.println(flightTicket.getAmenities());
        System.out.println(flightTicket.getJourneyDetails());
        System.out.println(flightTicket.getBoardingInfo());
        System.out.println(flightTicket.getBaggageAllowance());
        flightTicket.calculateFare();
        System.out.println();
        
        // Demonstrate ticket cancellation
        System.out.println("\n❌ TICKET CANCELLATION DEMONSTRATION:");
        System.out.println("-".repeat(50));
        for (Ticket ticket : tickets) {
            System.out.println("\n" + ticket.getPassengerName() + " (" + ticket.getTicketType() + "):");
            ticket.cancelTicket();
        }
        
        // Demonstrate journey information
        System.out.println("\n🗺️ JOURNEY INFORMATION:");
        System.out.println("-".repeat(50));
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getPassengerName() + " (" + ticket.getTicketType() + "):");
            System.out.println(ticket.getRouteInfo());
            System.out.println(ticket.getJourneyDuration());
            System.out.println();
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(tickets);
        
        // Demonstrate fare comparison
        System.out.println("\n💰 FARE COMPARISON:");
        System.out.println("-".repeat(50));
        System.out.println("Transport Type\t\tBase Price\tTotal Fare\tSurcharges");
        System.out.println("-".repeat(50));
        
        for (Ticket ticket : tickets) {
            double fare = ticket.calculateFare();
            double surcharges = fare - ticket.getBasePrice();
            
            System.out.printf("%-15s\t$%.2f\t\t$%.2f\t\t$%.2f%n", 
                            ticket.getTicketType(), ticket.getBasePrice(), fare, surcharges);
        }
        
        // Demonstrate specialized field usage
        System.out.println("\n🔍 SPECIALIZED FIELD USAGE:");
        System.out.println("-".repeat(50));
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getPassengerName() + " (" + ticket.getTicketType() + "):");
            
            if (ticket instanceof BusTicket) {
                BusTicket bus = (BusTicket) ticket;
                System.out.println("Bus: " + bus.getBusNumber() + " (" + bus.getBusType() + ")");
                System.out.println("Operator: " + bus.getOperator());
                System.out.println("Distance: " + String.format("%.1f", bus.getDistance()) + " km");
            } else if (ticket instanceof TrainTicket) {
                TrainTicket train = (TrainTicket) ticket;
                System.out.println("Train: " + train.getTrainNumber() + " (" + train.getTrainName() + ")");
                System.out.println("Coach: " + train.getCoachType() + ", Berth: " + train.getBerthType());
                System.out.println("Zone: " + train.getRailwayZone());
            } else if (ticket instanceof FlightTicket) {
                FlightTicket flight = (FlightTicket) ticket;
                System.out.println("Flight: " + flight.getFlightNumber() + " (" + flight.getAirline() + ")");
                System.out.println("Aircraft: " + flight.getAircraftType() + ", Class: " + flight.getClassType());
                System.out.println("Terminal: " + flight.getTerminal() + ", Gate: " + flight.getGate());
            }
            System.out.println();
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param tickets Array of Ticket objects
     */
    public static void demonstratePolymorphism(Ticket[] tickets) {
        System.out.println("Processing tickets using polymorphism:");
        for (int i = 0; i < tickets.length; i++) {
            Ticket ticket = tickets[i];
            System.out.println((i + 1) + ". " + ticket.getPassengerName() + " (" + ticket.getTicketType() + ")");
            System.out.println("   Route: " + ticket.getRouteInfo());
            System.out.println("   Features: " + ticket.getTransportFeatures());
            System.out.println();
        }
    }
}
