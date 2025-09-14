/**
 * Demo class to showcase hotel room booking system
 * Demonstrates inheritance, method overriding, and constructor chaining
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class HotelDemo {
    public static void main(String[] args) {
        System.out.println("🏨 HOTEL ROOM BOOKING SYSTEM 🏨");
        System.out.println("=" .repeat(50));
        
        // Create different types of rooms
        Room[] rooms = {
            new StandardRoom(101, 1, 150.0, 2, true, "City", true),
            new StandardRoom(102, 1, 150.0, 2, false, "Garden", false),
            new DeluxeRoom(201, 2, 250.0, 4, true, true, "King", true),
            new DeluxeRoom(202, 2, 250.0, 4, false, false, "Queen", false),
            new SuiteRoom(301, 3, 500.0, 6, true, true, true, "24/7", true),
            new SuiteRoom(302, 3, 500.0, 6, true, false, false, "Day", false)
        };
        
        // Display room information
        System.out.println("\n📋 ROOM INFORMATION:");
        System.out.println("-".repeat(50));
        for (Room room : rooms) {
            System.out.println(room.getRoomInfo());
            System.out.println("Price: $" + String.format("%.2f", room.getPrice()) + "/night");
            System.out.println("Available: " + room.isAvailable());
            System.out.println();
        }
        
        // Demonstrate room booking
        System.out.println("\n🔐 ROOM BOOKING DEMONSTRATION:");
        System.out.println("-".repeat(50));
        for (Room room : rooms) {
            if (room.bookRoom()) {
                System.out.println("✅ " + room.getRoomType() + " Room " + room.getRoomNumber() + " booked successfully!");
            } else {
                System.out.println("❌ " + room.getRoomType() + " Room " + room.getRoomNumber() + " is already booked!");
            }
        }
        
        // Display amenities for each room type
        System.out.println("\n🏠 ROOM AMENITIES:");
        System.out.println("-".repeat(50));
        for (Room room : rooms) {
            System.out.println(room.getRoomType() + " Room " + room.getRoomNumber() + ":");
            System.out.println(room.getAmenities());
            System.out.println();
        }
        
        // Demonstrate specific room behaviors
        System.out.println("\n🛎️ ROOM-SPECIFIC SERVICES:");
        System.out.println("-".repeat(50));
        
        // Standard Room services
        StandardRoom standardRoom = new StandardRoom(103, 1, 150.0, 2, true, "Pool", true);
        System.out.println("Standard Room Services:");
        System.out.println(standardRoom.requestRoomService());
        System.out.println(standardRoom.requestHousekeeping());
        System.out.println();
        
        // Deluxe Room services
        DeluxeRoom deluxeRoom = new DeluxeRoom(203, 2, 250.0, 4, true, true, "King", true);
        System.out.println("Deluxe Room Services:");
        System.out.println(deluxeRoom.requestConcierge());
        System.out.println(deluxeRoom.requestSpaServices());
        System.out.println(deluxeRoom.requestPremiumDining());
        System.out.println();
        
        // Suite Room services
        SuiteRoom suiteRoom = new SuiteRoom(303, 3, 500.0, 6, true, true, true, "24/7", true);
        System.out.println("Suite Room Services:");
        System.out.println(suiteRoom.requestButlerService());
        System.out.println(suiteRoom.requestPrivateDining());
        System.out.println(suiteRoom.requestVIPServices());
        System.out.println(suiteRoom.hostEvent());
        System.out.println();
        
        // Demonstrate pricing comparison
        System.out.println("\n💰 PRICING COMPARISON:");
        System.out.println("-".repeat(50));
        System.out.println("Room Type\t\tBase Price\tTotal Price\tSurcharge");
        System.out.println("-".repeat(50));
        
        for (Room room : rooms) {
            double surcharge = room.getPrice() - room.getBasePrice();
            System.out.printf("%-15s\t$%.2f\t\t$%.2f\t\t$%.2f%n", 
                            room.getRoomType(), room.getBasePrice(), room.getPrice(), surcharge);
        }
        
        // Demonstrate checkout
        System.out.println("\n🚪 CHECKOUT DEMONSTRATION:");
        System.out.println("-".repeat(50));
        for (Room room : rooms) {
            if (room.checkOut()) {
                System.out.println("✅ " + room.getRoomType() + " Room " + room.getRoomNumber() + " checked out successfully!");
            } else {
                System.out.println("❌ " + room.getRoomType() + " Room " + room.getRoomNumber() + " is already available!");
            }
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(rooms);
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param rooms Array of Room objects
     */
    public static void demonstratePolymorphism(Room[] rooms) {
        System.out.println("Processing rooms using polymorphism:");
        for (int i = 0; i < rooms.length; i++) {
            Room room = rooms[i];
            System.out.println((i + 1) + ". " + room.getRoomType() + " Room " + room.getRoomNumber());
            System.out.println("   Price: $" + String.format("%.2f", room.getPrice()) + "/night");
            System.out.println("   Amenities: " + room.getAmenities());
            System.out.println();
        }
    }
}
