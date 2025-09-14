# 🏨 Hotel Room Booking System - Inheritance & Method Overriding

## Problem Statement
Create a hotel room booking system with different types of rooms. Implement inheritance with base class `Room` and subclasses `StandardRoom`, `DeluxeRoom`, and `SuiteRoom`. Each room type should override the abstract methods `getPrice()` and `getAmenities()` with their specific pricing logic and amenities.

## Learning Objectives
- **Constructor Chaining**: Using `super()` to call parent constructor
- **Method Overriding**: Implementing polymorphic behavior for pricing and amenities
- **Shared vs Specialized Fields**: Common fields in base class, specific fields in subclasses
- **Abstract Methods**: Forcing subclasses to implement specific behaviors

## Class Hierarchy

```
Room (Abstract)
├── StandardRoom
├── DeluxeRoom
└── SuiteRoom
```

## Key Concepts Demonstrated

### 1. Constructor Chaining
- Subclasses call parent constructor using `super()`
- Ensures proper initialization of inherited fields
- Allows additional initialization of subclass-specific fields

### 2. Method Overriding
- Each subclass provides its own implementation of `getPrice()` and `getAmenities()`
- Demonstrates polymorphic behavior at runtime
- Different pricing logic for each room type

### 3. Shared vs Specialized Fields
- Common fields (roomNumber, floor, basePrice) in base class
- Specific fields (hasBalcony, hasJacuzzi, butlerService) in subclasses
- Demonstrates code reuse and specialization

## Code Structure

### Room.java (Abstract Base Class)
```java
public abstract class Room {
    protected int roomNumber;
    protected int floor;
    protected double basePrice;
    protected int maxOccupancy;
    protected boolean isAvailable;
    protected String roomType;
    
    // Abstract methods - must be implemented by subclasses
    public abstract double getPrice();
    public abstract String getAmenities();
    
    // Concrete methods - shared by all rooms
    public boolean bookRoom() { ... }
    public boolean checkOut() { ... }
}
```

### StandardRoom.java (Concrete Subclass)
```java
public class StandardRoom extends Room {
    private boolean hasBalcony;
    private String viewType;
    private boolean hasMiniBar;
    
    public StandardRoom(...) {
        super(roomNumber, floor, basePrice, maxOccupancy, "Standard");
        // Initialize subclass-specific fields
    }
    
    @Override
    public double getPrice() {
        double totalPrice = basePrice;
        // Add surcharges based on features
        if (hasBalcony) totalPrice += 20.0;
        // ... more pricing logic
        return totalPrice;
    }
    
    @Override
    public String getAmenities() {
        // Return standard room specific amenities
    }
}
```

### DeluxeRoom.java (Concrete Subclass)
```java
public class DeluxeRoom extends Room {
    private boolean hasJacuzzi;
    private boolean hasKitchenette;
    private String bedType;
    private boolean hasRoomService;
    
    @Override
    public double getPrice() {
        double totalPrice = basePrice;
        // Add deluxe-specific surcharges
        if (hasJacuzzi) totalPrice += 50.0;
        // ... more pricing logic
        totalPrice *= 1.15; // 15% luxury surcharge
        return totalPrice;
    }
}
```

### SuiteRoom.java (Concrete Subclass)
```java
public class SuiteRoom extends Room {
    private boolean hasLivingRoom;
    private boolean hasDiningArea;
    private boolean hasPrivateBar;
    private String butlerService;
    private boolean hasPrivateElevator;
    
    @Override
    public double getPrice() {
        double totalPrice = basePrice;
        // Add suite-specific surcharges
        if (hasLivingRoom) totalPrice += 100.0;
        // ... more pricing logic
        totalPrice *= 1.25; // 25% luxury surcharge
        return totalPrice;
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
java HotelDemo
```

## Expected Output

```
🏨 HOTEL ROOM BOOKING SYSTEM 🏨
==================================================

📋 ROOM INFORMATION:
--------------------------------------------------
Room 101 on Floor 1 - Standard (Max: 2 guests)
Price: $205.00/night
Available: true

Room 102 on Floor 1 - Standard (Max: 2 guests)
Price: $160.00/night
Available: true

Room 201 on Floor 2 - Deluxe (Max: 4 guests)
Price: $402.50/night
Available: true

Room 202 on Floor 2 - Deluxe (Max: 4 guests)
Price: $322.50/night
Available: true

Room 301 on Floor 3 - Suite (Max: 6 guests)
Price: $1062.50/night
Available: true

Room 302 on Floor 3 - Suite (Max: 6 guests)
Price: $787.50/night
Available: true

🔐 ROOM BOOKING DEMONSTRATION:
--------------------------------------------------
✅ Standard Room 101 booked successfully!
✅ Standard Room 102 booked successfully!
✅ Deluxe Room 201 booked successfully!
✅ Deluxe Room 202 booked successfully!
✅ Suite Room 301 booked successfully!
✅ Suite Room 302 booked successfully!

🏠 ROOM AMENITIES:
--------------------------------------------------
Standard Room 101:
Standard Room Amenities: WiFi, Air Conditioning, TV, Work Desk, Private Balcony, Mini Bar, City View

Standard Room 102:
Standard Room Amenities: WiFi, Air Conditioning, TV, Work Desk, Garden View

Deluxe Room 201:
Deluxe Room Amenities: WiFi, Air Conditioning, 55" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Private Jacuzzi, Kitchenette with Refrigerator, 24/7 Room Service, King Bed

Deluxe Room 202:
Deluxe Room Amenities: WiFi, Air Conditioning, 55" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Queen Bed

Suite Room 301:
Suite Room Amenities: WiFi, Air Conditioning, 75" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Separate Bedroom, Marble Bathroom, Premium Toiletries, Spacious Living Room, Private Dining Area, Private Bar with Premium Spirits, Private Elevator Access, 24/7 Butler Service

Suite Room 302:
Suite Room Amenities: WiFi, Air Conditioning, 75" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Separate Bedroom, Marble Bathroom, Premium Toiletries, Spacious Living Room, Day Butler Service

🛎️ ROOM-SPECIFIC SERVICES:
--------------------------------------------------
Standard Room Services:
Standard room service available 24/7
Daily housekeeping service included

Deluxe Room Services:
Personal concierge service available
In-room spa services available
Premium dining options available

Suite Room Services:
Personal butler service: 24/7
Private dining experience with personal chef
VIP services including airport transfer and exclusive access
Suite available for private events and meetings

💰 PRICING COMPARISON:
--------------------------------------------------
Room Type          Base Price    Total Price   Surcharge
--------------------------------------------------
Standard          $150.00       $205.00       $55.00
Standard          $150.00       $160.00       $10.00
Deluxe            $250.00       $402.50       $152.50
Deluxe            $250.00       $322.50       $72.50
Suite             $500.00       $1062.50      $562.50
Suite             $500.00       $787.50       $287.50

🚪 CHECKOUT DEMONSTRATION:
--------------------------------------------------
✅ Standard Room 101 checked out successfully!
✅ Standard Room 102 checked out successfully!
✅ Deluxe Room 201 checked out successfully!
✅ Deluxe Room 202 checked out successfully!
✅ Suite Room 301 checked out successfully!
✅ Suite Room 302 checked out successfully!

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing rooms using polymorphism:
1. Standard Room 101
   Price: $205.00/night
   Amenities: Standard Room Amenities: WiFi, Air Conditioning, TV, Work Desk, Private Balcony, Mini Bar, City View

2. Standard Room 102
   Price: $160.00/night
   Amenities: Standard Room Amenities: WiFi, Air Conditioning, TV, Work Desk, Garden View

3. Deluxe Room 201
   Price: $402.50/night
   Amenities: Deluxe Room Amenities: WiFi, Air Conditioning, 55" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Private Jacuzzi, Kitchenette with Refrigerator, 24/7 Room Service, King Bed

4. Deluxe Room 202
   Price: $322.50/night
   Amenities: Deluxe Room Amenities: WiFi, Air Conditioning, 55" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Queen Bed

5. Suite Room 301
   Price: $1062.50/night
   Amenities: Suite Room Amenities: WiFi, Air Conditioning, 75" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Separate Bedroom, Marble Bathroom, Premium Toiletries, Spacious Living Room, Private Dining Area, Private Bar with Premium Spirits, Private Elevator Access, 24/7 Butler Service

6. Suite Room 302
   Price: $787.50/night
   Amenities: Suite Room Amenities: WiFi, Air Conditioning, 75" Smart TV, Work Desk, Premium Bedding, Coffee Machine, Iron & Ironing Board, Separate Bedroom, Marble Bathroom, Premium Toiletries, Spacious Living Room, Day Butler Service

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Constructor Chaining**: Using `super()` to properly initialize inherited fields
2. **Method Overriding**: Each subclass provides its own implementation of abstract methods
3. **Shared vs Specialized Fields**: Common fields in base class, specific fields in subclasses
4. **Polymorphism**: Same method call produces different results based on object type
5. **Abstract Methods**: Force subclasses to implement specific behaviors
6. **Encapsulation**: Private fields with public getters

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Business Logic**: Real-world pricing and amenity calculations

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different pricing strategies for different room types
3. **Polymorphism**: Single interface for different room types

This example demonstrates the fundamental concepts of inheritance and method overriding in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.
