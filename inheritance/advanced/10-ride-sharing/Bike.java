public class Bike extends Vehicle {
    public Bike(String vehicleId, String driverId, String make, String model, int year, 
                String color, String licensePlate, String location, double latitude, double longitude) {
        super(vehicleId, driverId, "Bike", make, model, year, color, licensePlate, 
              location, latitude, longitude, 1, 1.50, 0.50, 0.10, "Gasoline", 25.0, "Manual");
    }
    
    @Override
    public double calculateFare(double distance, double duration, double surgeMultiplier) {
        double fare = baseFare + (distance * perKmRate) + (duration * perMinuteRate);
        return fare * surgeMultiplier;
    }
    
    @Override
    public String getVehicleFeatures() {
        return "Bike Features: Helmet: Yes, Windshield: Yes, Storage: Yes, GPS: Yes";
    }
    
    @Override
    public boolean canAccommodatePassengers(int passengerCount) {
        return passengerCount == 1;
    }
    
    @Override
    public String getSeatingRules() {
        return "Single rider only, Helmet required, No pillion for safety";
    }
}
