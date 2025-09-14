public class RideSharingDemo {
    public static void main(String[] args) {
        System.out.println("🚗 RIDE SHARING APP 🚗");
        System.out.println("=" .repeat(50));
        
        Vehicle[] vehicles = {
            new Car("CAR001", "DRV001", "Toyota", "Camry", 2022, "Blue", "ABC123", "Downtown", 40.7128, -74.0060),
            new Bike("BIKE001", "DRV002", "Honda", "CBR", 2021, "Red", "XYZ789", "Uptown", 40.7589, -73.9851)
        };
        
        System.out.println("\n📋 VEHICLE INFORMATION:");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.getVehicleInfo());
        }
        
        System.out.println("\n🚗 RIDE DEMONSTRATION:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("\n" + vehicle.getVehicleType() + " Ride:");
            vehicle.startRide(vehicle.getMaxPassengers());
            vehicle.endRide(5.5, 15.0, 1.2);
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}
