package composition.travel;

import java.time.LocalDateTime;

/**
 * MAANG-Level Demo: Travel Booking System with Dynamic Composition
 * Demonstrates composition flexibility, strategy pattern, and component swapping
 */
public class TravelBookingDemo {
    public static void main(String[] args) {
        System.out.println("=== MAANG-Level Travel Booking System Demo ===\n");
        
        // Create travel package
        TravelPackage package1 = new TravelPackage("PKG-2024-001", "Paris, France");
        
        // Add observers
        TravelAgentObserver agentObserver = new TravelAgentObserver("agent@travelco.com");
        CustomerNotificationObserver customerObserver = new CustomerNotificationObserver("customer@email.com");
        
        package1.addObserver(agentObserver);
        package1.addObserver(customerObserver);
        
        // Build package with components
        System.out.println("--- Building Travel Package ---");
        
        Flight flight = new Flight("AF123", "Air France", "New York", "Paris",
                                 LocalDateTime.now().plusDays(30), LocalDateTime.now().plusDays(30).plusHours(8),
                                 899.99, "Economy");
        
        Hotel hotel = new Hotel("Hotel Le Meurice", "1st Arrondissement", 5, 450.0, 3, "Deluxe Room", true);
        
        CarRental car = new CarRental("Compact Car", "Hertz", 45.0, 3, "CDG Airport", "CDG Airport", true);
        
        package1.setFlight(flight);
        package1.setHotel(hotel);
        package1.setCarRental(car);
        
        // Add insurance and activities
        TravelInsurance insurance = new TravelInsurance("Comprehensive", "TravelGuard", 89.99, 50000.0,
                                                       new String[]{"Trip Cancellation", "Medical Emergency", "Lost Luggage"});
        package1.addInsurance(insurance);
        
        Activity louvreVisit = new Activity("Louvre Museum Tour", "Guided tour of the world's largest art museum",
                                          65.0, 4, "Cultural", true);
        Activity eiffelTower = new Activity("Eiffel Tower Visit", "Skip-the-line access to Eiffel Tower",
                                          35.0, 2, "Sightseeing", true);
        
        package1.addActivity(louvreVisit);
        package1.addActivity(eiffelTower);
        
        package1.displayPackageDetails();
        
        // Test pricing strategies
        System.out.println("\n--- Testing Different Pricing Strategies ---");
        
        // Standard pricing
        PackageQuote standardQuote = package1.generateQuote();
        System.out.printf("Standard Price: $%.2f\n", standardQuote.getFinalPrice());
        
        // Early bird discount
        package1.setPricingStrategy(new EarlyBirdPricingStrategy(15.0));
        PackageQuote earlyBirdQuote = package1.generateQuote();
        System.out.printf("Early Bird Price: $%.2f (Save $%.2f)\n", 
                         earlyBirdQuote.getFinalPrice(), 
                         standardQuote.getFinalPrice() - earlyBirdQuote.getFinalPrice());
        
        // Group discount
        package1.setPricingStrategy(new GroupDiscountPricingStrategy(3, 10.0));
        PackageQuote groupQuote = package1.generateQuote();
        System.out.printf("Group Discount Price: $%.2f\n", groupQuote.getFinalPrice());
        
        // Loyalty member pricing
        package1.setPricingStrategy(new LoyaltyMemberPricingStrategy("Gold", 20.0));
        PackageQuote loyaltyQuote = package1.generateQuote();
        System.out.printf("Gold Member Price: $%.2f\n", loyaltyQuote.getFinalPrice());
        
        // Book the package
        System.out.println("\n--- Booking Package ---");
        CustomerInfo customer = new CustomerInfo("CUST-001", "John Smith", "john@email.com", "+1-555-0123", "Gold");
        BookingResult result = package1.bookPackage(customer);
        
        System.out.println("Booking Result: " + (result.isSuccess() ? 
            "Success - Confirmation: " + result.getConfirmationNumber() : 
            "Failed - " + result.getMessage()));
        
        // Test component swapping scenarios
        System.out.println("\n--- Component Swapping Scenarios ---");
        
        TravelPackage package2 = new TravelPackage("PKG-2024-002", "Tokyo, Japan");
        package2.addObserver(agentObserver);
        package2.addObserver(customerObserver);
        
        // Start with basic package
        Flight tokyoFlight = new Flight("JL001", "Japan Airlines", "Los Angeles", "Tokyo",
                                      LocalDateTime.now().plusDays(45), LocalDateTime.now().plusDays(45).plusHours(11),
                                      1299.99, "Economy");
        package2.setFlight(tokyoFlight);
        
        Hotel basicHotel = new Hotel("Tokyo Inn", "Shibuya", 3, 120.0, 5, "Standard Room", false);
        package2.setHotel(basicHotel);
        
        System.out.println("Initial Tokyo package:");
        PackageQuote initialQuote = package2.generateQuote();
        System.out.printf("Price: $%.2f\n", initialQuote.getFinalPrice());
        
        // Upgrade flight to business class
        Flight businessFlight = new Flight("JL001", "Japan Airlines", "Los Angeles", "Tokyo",
                                         LocalDateTime.now().plusDays(45), LocalDateTime.now().plusDays(45).plusHours(11),
                                         3299.99, "Business");
        package2.setFlight(businessFlight);
        
        // Upgrade hotel to luxury
        Hotel luxuryHotel = new Hotel("The Ritz-Carlton Tokyo", "Roppongi", 5, 650.0, 5, "Executive Suite", true);
        package2.setHotel(luxuryHotel);
        
        // Add car rental
        CarRental tokyoCar = new CarRental("Premium Sedan", "Toyota Rent-a-Car", 80.0, 5, "Narita Airport", "Narita Airport", true);
        package2.setCarRental(tokyoCar);
        
        System.out.println("After upgrades:");
        PackageQuote upgradedQuote = package2.generateQuote();
        System.out.printf("Price: $%.2f (Increase: $%.2f)\n", 
                         upgradedQuote.getFinalPrice(),
                         upgradedQuote.getFinalPrice() - initialQuote.getFinalPrice());
        
        // Test availability scenarios
        System.out.println("\n--- Testing Availability Scenarios ---");
        
        // Make hotel unavailable
        luxuryHotel.setAvailable(false);
        
        BookingResult unavailableResult = package2.bookPackage(customer);
        System.out.println("Booking with unavailable hotel: " + unavailableResult.getMessage());
        
        // Switch to available hotel
        Hotel alternativeHotel = new Hotel("Park Hyatt Tokyo", "Shinjuku", 5, 580.0, 5, "Park Suite", true);
        package2.setHotel(alternativeHotel);
        
        BookingResult availableResult = package2.bookPackage(customer);
        System.out.println("Booking with alternative hotel: " + (availableResult.isSuccess() ? "Success" : "Failed"));
        
        // Test package modification after booking
        System.out.println("\n--- Package Modification Test ---");
        
        TravelPackage package3 = new TravelPackage("PKG-2024-003", "London, UK");
        package3.addObserver(agentObserver);
        
        Flight londonFlight = new Flight("BA001", "British Airways", "Boston", "London",
                                       LocalDateTime.now().plusDays(60), LocalDateTime.now().plusDays(60).plusHours(7),
                                       799.99, "Premium Economy");
        package3.setFlight(londonFlight);
        
        // Book first, then try to modify
        BookingResult londonBooking = package3.bookPackage(customer);
        System.out.println("London package booked: " + londonBooking.isSuccess());
        
        // Try to add hotel after booking (should still work)
        Hotel londonHotel = new Hotel("The Savoy", "Covent Garden", 5, 520.0, 4, "River View Room", true);
        package3.setHotel(londonHotel);
        
        // Cancel and rebook
        package3.cancelPackage();
        
        // Add more components
        package3.addActivity(new Activity("Thames River Cruise", "Scenic cruise along the Thames", 45.0, 3, "Sightseeing", true));
        package3.addActivity(new Activity("West End Show", "Premium theater experience", 120.0, 3, "Entertainment", true));
        
        BookingResult rebookResult = package3.bookPackage(customer);
        System.out.println("Rebook with additional components: " + rebookResult.isSuccess());
        
        package3.displayPackageDetails();
        
        System.out.println("\n=== Demo Complete: Travel package system adapted to different requirements and component changes ===");
    }
    
    // Travel Agent Observer
    static class TravelAgentObserver implements PackageObserver {
        private final String agentEmail;
        
        public TravelAgentObserver(String agentEmail) {
            this.agentEmail = agentEmail;
        }
        
        @Override
        public void onPackageUpdate(String packageId, String message, PackageStatus status) {
            System.out.println("🏢 Agent Alert [" + agentEmail + "] - " + packageId + ": " + message);
        }
    }
    
    // Customer Notification Observer
    static class CustomerNotificationObserver implements PackageObserver {
        private final String customerEmail;
        
        public CustomerNotificationObserver(String customerEmail) {
            this.customerEmail = customerEmail;
        }
        
        @Override
        public void onPackageUpdate(String packageId, String message, PackageStatus status) {
            if (status == PackageStatus.BOOKED || status == PackageStatus.CANCELLED) {
                System.out.println("📧 Customer Alert [" + customerEmail + "] - " + packageId + ": " + message);
            }
        }
    }
}
