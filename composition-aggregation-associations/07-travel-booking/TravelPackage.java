package composition.travel;

import java.time.LocalDateTime;
import java.util.*;

/**
 * MAANG-Level Travel Package System using Composition
 * Demonstrates: Strategy Pattern, Builder Pattern, Decorator Pattern, Chain of Responsibility
 */
public class TravelPackage {
    private final String packageId;
    private final String destination;
    private Flight flight;
    private Hotel hotel;
    private CarRental carRental;
    private final List<TravelInsurance> insurances;
    private final List<Activity> activities;
    private final LocalDateTime createdAt;
    private PackageStatus status;
    private final List<PackageObserver> observers;
    private PricingStrategy pricingStrategy;
    
    public TravelPackage(String packageId, String destination) {
        this.packageId = packageId;
        this.destination = destination;
        this.insurances = new ArrayList<>();
        this.activities = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.status = PackageStatus.DRAFT;
        this.observers = new ArrayList<>();
        this.pricingStrategy = new StandardPricingStrategy();
    }
    
    // Runtime component swapping - core composition flexibility
    public void setFlight(Flight flight) {
        Flight oldFlight = this.flight;
        this.flight = flight;
        
        String message = oldFlight == null ? 
            "Flight added: " + flight.getFlightNumber() :
            "Flight changed from " + oldFlight.getFlightNumber() + " to " + flight.getFlightNumber();
        
        notifyObservers(message);
    }
    
    public void setHotel(Hotel hotel) {
        Hotel oldHotel = this.hotel;
        this.hotel = hotel;
        
        String message = oldHotel == null ? 
            "Hotel added: " + hotel.getName() :
            "Hotel changed from " + oldHotel.getName() + " to " + hotel.getName();
        
        notifyObservers(message);
    }
    
    public void setCarRental(CarRental carRental) {
        CarRental oldCar = this.carRental;
        this.carRental = carRental;
        
        String message = oldCar == null ? 
            "Car rental added: " + carRental.getVehicleType() :
            "Car rental changed from " + oldCar.getVehicleType() + " to " + carRental.getVehicleType();
        
        notifyObservers(message);
    }
    
    public void removeFlight() {
        if (flight != null) {
            String flightNumber = flight.getFlightNumber();
            flight = null;
            notifyObservers("Flight removed: " + flightNumber);
        }
    }
    
    public void removeHotel() {
        if (hotel != null) {
            String hotelName = hotel.getName();
            hotel = null;
            notifyObservers("Hotel removed: " + hotelName);
        }
    }
    
    public void removeCarRental() {
        if (carRental != null) {
            String vehicleType = carRental.getVehicleType();
            carRental = null;
            notifyObservers("Car rental removed: " + vehicleType);
        }
    }
    
    public void addInsurance(TravelInsurance insurance) {
        insurances.add(insurance);
        notifyObservers("Insurance added: " + insurance.getType());
    }
    
    public void removeInsurance(TravelInsurance insurance) {
        if (insurances.remove(insurance)) {
            notifyObservers("Insurance removed: " + insurance.getType());
        }
    }
    
    public void addActivity(Activity activity) {
        activities.add(activity);
        notifyObservers("Activity added: " + activity.getName());
    }
    
    public void removeActivity(Activity activity) {
        if (activities.remove(activity)) {
            notifyObservers("Activity removed: " + activity.getName());
        }
    }
    
    // Runtime pricing strategy swapping
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        notifyObservers("Pricing strategy updated to " + strategy.getStrategyName());
    }
    
    public PackageQuote generateQuote() {
        double basePrice = 0.0;
        List<String> includedServices = new ArrayList<>();
        
        if (flight != null) {
            basePrice += flight.getPrice();
            includedServices.add("Flight: " + flight.getFlightNumber());
        }
        
        if (hotel != null) {
            basePrice += hotel.getPricePerNight() * hotel.getNights();
            includedServices.add("Hotel: " + hotel.getName() + " (" + hotel.getNights() + " nights)");
        }
        
        if (carRental != null) {
            basePrice += carRental.getDailyRate() * carRental.getDays();
            includedServices.add("Car Rental: " + carRental.getVehicleType() + " (" + carRental.getDays() + " days)");
        }
        
        for (TravelInsurance insurance : insurances) {
            basePrice += insurance.getPremium();
            includedServices.add("Insurance: " + insurance.getType());
        }
        
        for (Activity activity : activities) {
            basePrice += activity.getPrice();
            includedServices.add("Activity: " + activity.getName());
        }
        
        // Apply pricing strategy
        double finalPrice = pricingStrategy.calculatePrice(basePrice, this);
        double discount = basePrice - finalPrice;
        
        return new PackageQuote(packageId, basePrice, finalPrice, discount, 
                              pricingStrategy.getStrategyName(), includedServices);
    }
    
    public BookingResult bookPackage(CustomerInfo customer) {
        if (status != PackageStatus.DRAFT) {
            return new BookingResult(false, "Package is not in draft status", null);
        }
        
        if (flight == null && hotel == null && carRental == null) {
            return new BookingResult(false, "Package must include at least one service", null);
        }
        
        // Validate availability
        List<String> unavailableServices = new ArrayList<>();
        
        if (flight != null && !flight.isAvailable()) {
            unavailableServices.add("Flight " + flight.getFlightNumber());
        }
        
        if (hotel != null && !hotel.isAvailable()) {
            unavailableServices.add("Hotel " + hotel.getName());
        }
        
        if (carRental != null && !carRental.isAvailable()) {
            unavailableServices.add("Car rental " + carRental.getVehicleType());
        }
        
        if (!unavailableServices.isEmpty()) {
            return new BookingResult(false, "Services unavailable: " + String.join(", ", unavailableServices), null);
        }
        
        // Process booking
        status = PackageStatus.BOOKED;
        String confirmationNumber = "TRV-" + System.currentTimeMillis();
        
        // Reserve services
        if (flight != null) flight.reserve();
        if (hotel != null) hotel.reserve();
        if (carRental != null) carRental.reserve();
        
        notifyObservers("Package booked successfully - Confirmation: " + confirmationNumber);
        
        return new BookingResult(true, "Booking successful", confirmationNumber);
    }
    
    public void cancelPackage() {
        if (status == PackageStatus.BOOKED) {
            // Release reservations
            if (flight != null) flight.release();
            if (hotel != null) hotel.release();
            if (carRental != null) carRental.release();
            
            status = PackageStatus.CANCELLED;
            notifyObservers("Package cancelled");
        }
    }
    
    public void addObserver(PackageObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(PackageObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String message) {
        for (PackageObserver observer : observers) {
            observer.onPackageUpdate(packageId, message, status);
        }
    }
    
    public void displayPackageDetails() {
        System.out.println("\n=== Travel Package Details ===");
        System.out.println("Package ID: " + packageId);
        System.out.println("Destination: " + destination);
        System.out.println("Status: " + status);
        System.out.println("Created: " + createdAt);
        
        if (flight != null) {
            System.out.println("\nFlight:");
            System.out.println("- " + flight.getFlightNumber() + " (" + flight.getAirline() + ")");
            System.out.println("- " + flight.getDepartureCity() + " → " + flight.getArrivalCity());
            System.out.println("- Price: $" + String.format("%.2f", flight.getPrice()));
        }
        
        if (hotel != null) {
            System.out.println("\nHotel:");
            System.out.println("- " + hotel.getName() + " (" + hotel.getStarRating() + " stars)");
            System.out.println("- " + hotel.getNights() + " nights @ $" + String.format("%.2f", hotel.getPricePerNight()) + "/night");
        }
        
        if (carRental != null) {
            System.out.println("\nCar Rental:");
            System.out.println("- " + carRental.getVehicleType() + " (" + carRental.getCompany() + ")");
            System.out.println("- " + carRental.getDays() + " days @ $" + String.format("%.2f", carRental.getDailyRate()) + "/day");
        }
        
        if (!insurances.isEmpty()) {
            System.out.println("\nInsurance:");
            for (TravelInsurance insurance : insurances) {
                System.out.println("- " + insurance.getType() + ": $" + String.format("%.2f", insurance.getPremium()));
            }
        }
        
        if (!activities.isEmpty()) {
            System.out.println("\nActivities:");
            for (Activity activity : activities) {
                System.out.println("- " + activity.getName() + ": $" + String.format("%.2f", activity.getPrice()));
            }
        }
        
        PackageQuote quote = generateQuote();
        System.out.println("\nPricing (" + pricingStrategy.getStrategyName() + "):");
        System.out.printf("Base Price: $%.2f\n", quote.getBasePrice());
        if (quote.getDiscount() > 0) {
            System.out.printf("Discount: -$%.2f\n", quote.getDiscount());
        }
        System.out.printf("Final Price: $%.2f\n", quote.getFinalPrice());
    }
    
    // Getters
    public String getPackageId() { return packageId; }
    public String getDestination() { return destination; }
    public PackageStatus getStatus() { return status; }
    public Flight getFlight() { return flight; }
    public Hotel getHotel() { return hotel; }
    public CarRental getCarRental() { return carRental; }
    public List<TravelInsurance> getInsurances() { return new ArrayList<>(insurances); }
    public List<Activity> getActivities() { return new ArrayList<>(activities); }
}
