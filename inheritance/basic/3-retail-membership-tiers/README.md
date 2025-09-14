# 🛍️ Retail Membership Tiers - Inheritance & Polymorphic Methods

## Problem Statement
Create a retail membership system with different tiers. Implement inheritance with base class `Membership` and subclasses `SilverMember`, `GoldMember`, and `PlatinumMember`. Each membership tier should override the abstract methods `calculateDiscount()`, `calculatePoints()`, and `getBenefits()` with their specific business logic.

## Learning Objectives
- **Base Class + Polymorphic Methods**: Common functionality with specialized behavior
- **Method Overriding**: Each subclass provides its own implementation
- **Abstract Methods**: Force subclasses to implement specific behaviors
- **Business Logic**: Real-world pricing and benefit calculations

## Class Hierarchy

```
Membership (Abstract)
├── SilverMember
├── GoldMember
└── PlatinumMember
```

## Key Concepts Demonstrated

### 1. Base Class + Polymorphic Methods
- Common functionality (makePurchase, redeemPoints) in base class
- Specialized behavior (discount calculation, point earning) in subclasses
- Runtime polymorphism for different membership tiers

### 2. Method Overriding
- Each subclass provides its own implementation of abstract methods
- Demonstrates polymorphic behavior at runtime
- Different business logic for each membership tier

### 3. Abstract Methods
- Force subclasses to implement specific behaviors
- Ensure consistent interface across all membership types
- Enable polymorphic method calls

## Code Structure

### Membership.java (Abstract Base Class)
```java
public abstract class Membership {
    protected String memberId;
    protected String memberName;
    protected String email;
    protected String joinDate;
    protected double totalSpent;
    protected int pointsEarned;
    protected boolean isActive;
    protected String tierName;
    
    // Abstract methods - must be implemented by subclasses
    public abstract double calculateDiscount();
    public abstract int calculatePoints(double amount);
    public abstract String getBenefits();
    public abstract double getUpgradeThreshold();
    
    // Concrete methods - shared by all memberships
    public double makePurchase(double amount) { ... }
    public double redeemPoints(int pointsToRedeem) { ... }
}
```

### SilverMember.java (Concrete Subclass)
```java
public class SilverMember extends Membership {
    private static final double SILVER_DISCOUNT = 0.05; // 5% discount
    private static final int POINTS_PER_DOLLAR = 1; // 1 point per dollar
    private static final double UPGRADE_THRESHOLD = 1000.0; // $1000 for upgrade
    
    @Override
    public double calculateDiscount() {
        return SILVER_DISCOUNT;
    }
    
    @Override
    public int calculatePoints(double amount) {
        return (int) (amount * POINTS_PER_DOLLAR);
    }
    
    @Override
    public String getBenefits() {
        return "Silver Member Benefits: 5% discount, 1 point per dollar, ...";
    }
}
```

### GoldMember.java (Concrete Subclass)
```java
public class GoldMember extends Membership {
    private static final double GOLD_DISCOUNT = 0.10; // 10% discount
    private static final int POINTS_PER_DOLLAR = 2; // 2 points per dollar
    private static final double UPGRADE_THRESHOLD = 2500.0; // $2500 for upgrade
    
    @Override
    public double calculateDiscount() {
        return GOLD_DISCOUNT;
    }
    
    @Override
    public int calculatePoints(double amount) {
        return (int) (amount * POINTS_PER_DOLLAR);
    }
    
    @Override
    public String getBenefits() {
        return "Gold Member Benefits: 10% discount, 2 points per dollar, ...";
    }
}
```

### PlatinumMember.java (Concrete Subclass)
```java
public class PlatinumMember extends Membership {
    private static final double PLATINUM_DISCOUNT = 0.15; // 15% discount
    private static final int POINTS_PER_DOLLAR = 3; // 3 points per dollar
    private static final double UPGRADE_THRESHOLD = 5000.0; // $5000 for upgrade
    
    @Override
    public double calculateDiscount() {
        return PLATINUM_DISCOUNT;
    }
    
    @Override
    public int calculatePoints(double amount) {
        return (int) (amount * POINTS_PER_DOLLAR);
    }
    
    @Override
    public String getBenefits() {
        return "Platinum Member Benefits: 15% discount, 3 points per dollar, ...";
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
java RetailDemo
```

## Expected Output

```
🛍️ RETAIL MEMBERSHIP SYSTEM 🛍️
==================================================

📋 MEMBER INFORMATION:
--------------------------------------------------
Member ID: S001, Name: Alice Johnson, Tier: Silver, Total Spent: $0.00, Points: 0
Member ID: S002, Name: Bob Smith, Tier: Silver, Total Spent: $0.00, Points: 0
Member ID: G001, Name: Carol Davis, Tier: Gold, Total Spent: $0.00, Points: 0
Member ID: G002, Name: David Wilson, Tier: Gold, Total Spent: $0.00, Points: 0
Member ID: P001, Name: Eva Brown, Tier: Platinum, Total Spent: $0.00, Points: 0
Member ID: P002, Name: Frank Miller, Tier: Platinum, Total Spent: $0.00, Points: 0

💳 PURCHASE DEMONSTRATION:
--------------------------------------------------

Alice Johnson (Silver Member):
Purchase made: $100.00
Discount applied: 5.0%
Final amount: $95.00
Points earned: 100
Total spent so far: $100.00
Total points earned: 100

Bob Smith (Silver Member):
Purchase made: $250.00
Discount applied: 5.0%
Final amount: $237.50
Points earned: 250
Total spent so far: $250.00
Total points earned: 250

Carol Davis (Gold Member):
Purchase made: $500.00
Discount applied: 10.0%
Final amount: $450.00
Points earned: 1000
Total spent so far: $500.00
Total points earned: 1000

David Wilson (Gold Member):
Purchase made: $750.00
Discount applied: 10.0%
Final amount: $675.00
Points earned: 1500
Total spent so far: $750.00
Total points earned: 1500

Eva Brown (Platinum Member):
Purchase made: $1000.00
Discount applied: 15.0%
Final amount: $850.00
Points earned: 3000
Total spent so far: $1000.00
Total points earned: 3000

Frank Miller (Platinum Member):
Purchase made: $1500.00
Discount applied: 15.0%
Final amount: $1275.00
Points earned: 4500
Total spent so far: $1500.00
Total points earned: 4500

🎁 MEMBERSHIP BENEFITS:
--------------------------------------------------

Alice Johnson (Silver Member):
Silver Member Benefits: 5% discount on all purchases, 1 point per dollar spent, Free shipping on orders over $50, Early access to sales, Birthday discount

Bob Smith (Silver Member):
Silver Member Benefits: 5% discount on all purchases, 1 point per dollar spent, Free shipping on orders over $50, Early access to sales, Birthday discount

Carol Davis (Gold Member):
Gold Member Benefits: 10% discount on all purchases, 2 points per dollar spent, Free shipping on all orders, Priority customer service, Exclusive gold member events, Double points on birthdays, Free returns

David Wilson (Gold Member):
Gold Member Benefits: 10% discount on all purchases, 2 points per dollar spent, Free shipping on all orders, Priority customer service, Exclusive gold member events, Double points on birthdays, Free returns

Eva Brown (Platinum Member):
Platinum Member Benefits: 15% discount on all purchases, 3 points per dollar spent, Free shipping on all orders, VIP customer service, Exclusive platinum member events, Triple points on birthdays, Free returns, Personal concierge, Access to limited edition products, Annual gift

Frank Miller (Platinum Member):
Platinum Member Benefits: 15% discount on all purchases, 3 points per dollar spent, Free shipping on all orders, VIP customer service, Exclusive platinum member events, Triple points on birthdays, Free returns, Personal concierge, Access to limited edition products, Annual gift

⭐ TIER-SPECIFIC BENEFITS:
--------------------------------------------------
Silver Member Benefits:
Happy Birthday! You get an additional 10% off your next purchase!
You have early access to our exclusive sales and new arrivals!
Congratulations! You qualify for free shipping!
Thank you for being a loyal Silver member! Your continued support is appreciated.

Gold Member Benefits:
You have access to our priority customer service line!
You're invited to our exclusive gold member events and previews!
Earn double points on your birthday and special occasions!
Enjoy free returns on all your purchases!
Access to our personal shopping service for style advice!
Access to exclusive gold member only products and collections!

Platinum Member Benefits:
You have access to our VIP customer service with dedicated support!
Your personal concierge is available 24/7 for any assistance!
You're invited to our most exclusive platinum member events and private previews!
Earn triple points on your birthday and special occasions!
Exclusive access to limited edition products and collaborations!
Receive an exclusive annual gift as a thank you for your loyalty!
Priority access to new collections and restocks!
Access to exclusive partnerships and collaborations with luxury brands!
Your platinum status includes lifetime benefits and recognition!

🎯 POINT REDEMPTION DEMONSTRATION:
--------------------------------------------------
Alice Johnson redeeming 100 points:
Redeemed 100 points for $1.00
Remaining points: 0

Bob Smith redeeming 100 points:
Redeemed 100 points for $1.00
Remaining points: 150

Carol Davis redeeming 100 points:
Redeemed 100 points for $1.00
Remaining points: 900

David Wilson redeeming 100 points:
Redeemed 100 points for $1.00
Remaining points: 1400

Eva Brown redeeming 100 points:
Redeemed 100 points for $1.00
Remaining points: 2900

Frank Miller redeeming 100 points:
Redeemed 100 points for $1.00
Remaining points: 4400

⬆️ UPGRADE ELIGIBILITY:
--------------------------------------------------
Alice Johnson (Silver):
Total spent: $100.00
Upgrade threshold: $1000.00
❌ Need to spend $900.00 more for upgrade

Bob Smith (Silver):
Total spent: $250.00
Upgrade threshold: $1000.00
❌ Need to spend $750.00 more for upgrade

Carol Davis (Gold):
Total spent: $500.00
Upgrade threshold: $2500.00
❌ Need to spend $2000.00 more for upgrade

David Wilson (Gold):
Total spent: $750.00
Upgrade threshold: $2500.00
❌ Need to spend $1750.00 more for upgrade

Eva Brown (Platinum):
Total spent: $1000.00
Upgrade threshold: $5000.00
❌ Need to spend $4000.00 more for upgrade

Frank Miller (Platinum):
Total spent: $1500.00
Upgrade threshold: $5000.00
❌ Need to spend $3500.00 more for upgrade

🔄 POLYMORPHISM DEMONSTRATION:
--------------------------------------------------
Processing members using polymorphism:
1. Alice Johnson (Silver)
   Discount: 5.0%
   Points per dollar: 1
   Benefits: Silver Member Benefits: 5% discount on all purchases, 1 point per dollar spent, Free shipping on orders over $50, Early access to sales, Birthday discount

2. Bob Smith (Silver)
   Discount: 5.0%
   Points per dollar: 1
   Benefits: Silver Member Benefits: 5% discount on all purchases, 1 point per dollar spent, Free shipping on orders over $50, Early access to sales, Birthday discount

3. Carol Davis (Gold)
   Discount: 10.0%
   Points per dollar: 2
   Benefits: Gold Member Benefits: 10% discount on all purchases, 2 points per dollar spent, Free shipping on all orders, Priority customer service, Exclusive gold member events, Double points on birthdays, Free returns

4. David Wilson (Gold)
   Discount: 10.0%
   Points per dollar: 2
   Benefits: Gold Member Benefits: 10% discount on all purchases, 2 points per dollar spent, Free shipping on all orders, Priority customer service, Exclusive gold member events, Double points on birthdays, Free returns

5. Eva Brown (Platinum)
   Discount: 15.0%
   Points per dollar: 3
   Benefits: Platinum Member Benefits: 15% discount on all purchases, 3 points per dollar spent, Free shipping on all orders, VIP customer service, Exclusive platinum member events, Triple points on birthdays, Free returns, Personal concierge, Access to limited edition products, Annual gift

6. Frank Miller (Platinum)
   Discount: 15.0%
   Points per dollar: 3
   Benefits: Platinum Member Benefits: 15% discount on all purchases, 3 points per dollar spent, Free shipping on all orders, VIP customer service, Exclusive platinum member events, Triple points on birthdays, Free returns, Personal concierge, Access to limited edition products, Annual gift

💰 DISCOUNT COMPARISON:
--------------------------------------------------
Purchase amount: $500.00

Silver Member (Alice Johnson):
Discount: 5.0%
Final amount: $475.00
Savings: $25.00

Silver Member (Bob Smith):
Discount: 5.0%
Final amount: $475.00
Savings: $25.00

Gold Member (Carol Davis):
Discount: 10.0%
Final amount: $450.00
Savings: $50.00

Gold Member (David Wilson):
Discount: 10.0%
Final amount: $450.00
Savings: $50.00

Platinum Member (Eva Brown):
Discount: 15.0%
Final amount: $425.00
Savings: $75.00

Platinum Member (Frank Miller):
Discount: 15.0%
Final amount: $425.00
Savings: $75.00

✨ DEMONSTRATION COMPLETE! ✨
```

## Key Learning Points

1. **Base Class + Polymorphic Methods**: Common functionality with specialized behavior
2. **Method Overriding**: Each subclass provides its own implementation
3. **Abstract Methods**: Force subclasses to implement specific behaviors
4. **Business Logic**: Real-world pricing and benefit calculations
5. **Polymorphism**: Same method call produces different results based on object type
6. **Encapsulation**: Private fields with public getters

## Advanced Concepts Demonstrated

- **Runtime Polymorphism**: Method calls resolved at runtime based on actual object type
- **Abstract Class Design**: Cannot instantiate abstract classes directly
- **Method Overriding vs Overloading**: Different concepts with different purposes
- **Access Modifiers**: Protected fields accessible to subclasses
- **String Formatting**: Professional output formatting
- **Business Logic**: Real-world pricing and benefit calculations
- **Constants**: Using static final for configuration values

## Design Patterns Used

1. **Template Method Pattern**: Abstract base class defines the structure, subclasses implement specific details
2. **Strategy Pattern**: Different pricing strategies for different membership tiers
3. **Polymorphism**: Single interface for different membership types

This example demonstrates the fundamental concepts of inheritance and polymorphism in Java, providing a solid foundation for understanding more complex object-oriented programming patterns in real-world applications.
