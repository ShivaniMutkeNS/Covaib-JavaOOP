public class Car extends Vehicle {
    public Car(String vehicleId, String driverId, String make, String model, int year, 
               String color, String licensePlate, String location, double latitude, double longitude) {
        super(vehicleId, driverId, "Car", make, model, year, color, licensePlate, 
              location, latitude, longitude, 4, 2.50, 0.80, 0.15, "Gasoline", 12.5, "Automatic");
    }
    
    @Override
    public double calculateFare(double distance, double duration, double surgeMultiplier) {
        double fare = baseFare + (distance * perKmRate) + (duration * perMinuteRate);
        return fare * surgeMultiplier;
    }
    
    @Override
    public String getVehicleFeatures() {
        return "Car Features: Seats: " + maxPassengers + ", AC: Yes, Music: Yes, Charging: Yes";
    }
    
    @Override
    public boolean canAccommodatePassengers(int passengerCount) {
        return passengerCount <= maxPassengers && passengerCount > 0;
    }
    
    @Override
    public String getSeatingRules() {
        return "Front seat: 1 passenger, Back seats: 3 passengers, No pets in front seat";
    }
}
