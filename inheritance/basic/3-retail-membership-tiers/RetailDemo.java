/**
 * Demo class to showcase retail membership system
 * Demonstrates inheritance, method overriding, and polymorphic behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class RetailDemo {
    public static void main(String[] args) {
        System.out.println("🛍️ RETAIL MEMBERSHIP SYSTEM 🛍️");
        System.out.println("=" .repeat(50));
        
        // Create different types of members
        Membership[] members = {
            new SilverMember("S001", "Alice Johnson", "alice@email.com", "2023-01-15"),
            new SilverMember("S002", "Bob Smith", "bob@email.com", "2023-03-20"),
            new GoldMember("G001", "Carol Davis", "carol@email.com", "2022-11-10"),
            new GoldMember("G002", "David Wilson", "david@email.com", "2023-02-05"),
            new PlatinumMember("P001", "Eva Brown", "eva@email.com", "2022-06-01"),
            new PlatinumMember("P002", "Frank Miller", "frank@email.com", "2022-08-15")
        };
        
        // Display member information
        System.out.println("\n📋 MEMBER INFORMATION:");
        System.out.println("-".repeat(50));
        for (Membership member : members) {
            System.out.println(member.getMemberInfo());
        }
        
        // Demonstrate purchases with different membership tiers
        System.out.println("\n💳 PURCHASE DEMONSTRATION:");
        System.out.println("-".repeat(50));
        double[] purchaseAmounts = {100.0, 250.0, 500.0, 750.0, 1000.0, 1500.0};
        
        for (int i = 0; i < members.length; i++) {
            System.out.println("\n" + members[i].getMemberName() + " (" + members[i].getTierName() + " Member):");
            double finalAmount = members[i].makePurchase(purchaseAmounts[i]);
            System.out.println("Total spent so far: $" + String.format("%.2f", members[i].getTotalSpent()));
            System.out.println("Total points earned: " + members[i].getPointsEarned());
        }
        
        // Display benefits for each membership tier
        System.out.println("\n🎁 MEMBERSHIP BENEFITS:");
        System.out.println("-".repeat(50));
        for (Membership member : members) {
            System.out.println("\n" + member.getMemberName() + " (" + member.getTierName() + " Member):");
            System.out.println(member.getBenefits());
        }
        
        // Demonstrate tier-specific benefits
        System.out.println("\n⭐ TIER-SPECIFIC BENEFITS:");
        System.out.println("-".repeat(50));
        
        // Silver Member benefits
        SilverMember silverMember = new SilverMember("S003", "Grace Lee", "grace@email.com", "2023-04-01");
        System.out.println("Silver Member Benefits:");
        System.out.println(silverMember.getBirthdayDiscount());
        System.out.println(silverMember.getEarlyAccess());
        System.out.println(silverMember.getShippingStatus(75.0));
        System.out.println(silverMember.getLoyaltyRewards());
        System.out.println();
        
        // Gold Member benefits
        GoldMember goldMember = new GoldMember("G003", "Henry Chen", "henry@email.com", "2023-01-01");
        System.out.println("Gold Member Benefits:");
        System.out.println(goldMember.getPriorityService());
        System.out.println(goldMember.getExclusiveEvents());
        System.out.println(goldMember.getDoublePoints());
        System.out.println(goldMember.getFreeReturns());
        System.out.println(goldMember.getPersonalShopper());
        System.out.println(goldMember.getExclusiveProducts());
        System.out.println();
        
        // Platinum Member benefits
        PlatinumMember platinumMember = new PlatinumMember("P003", "Ivy Rodriguez", "ivy@email.com", "2022-12-01");
        System.out.println("Platinum Member Benefits:");
        System.out.println(platinumMember.getVIPService());
        System.out.println(platinumMember.getPersonalConcierge());
        System.out.println(platinumMember.getExclusiveEvents());
        System.out.println(platinumMember.getTriplePoints());
        System.out.println(platinumMember.getLimitedEditionAccess());
        System.out.println(platinumMember.getAnnualGift());
        System.out.println(platinumMember.getPriorityAccess());
        System.out.println(platinumMember.getExclusivePartnerships());
        System.out.println(platinumMember.getLifetimeBenefits());
        System.out.println();
        
        // Demonstrate point redemption
        System.out.println("\n🎯 POINT REDEMPTION DEMONSTRATION:");
        System.out.println("-".repeat(50));
        for (Membership member : members) {
            if (member.getPointsEarned() > 0) {
                int pointsToRedeem = Math.min(100, member.getPointsEarned());
                System.out.println(member.getMemberName() + " redeeming " + pointsToRedeem + " points:");
                member.redeemPoints(pointsToRedeem);
                System.out.println("Remaining points: " + member.getPointsEarned());
                System.out.println();
            }
        }
        
        // Demonstrate upgrade eligibility
        System.out.println("\n⬆️ UPGRADE ELIGIBILITY:");
        System.out.println("-".repeat(50));
        for (Membership member : members) {
            System.out.println(member.getMemberName() + " (" + member.getTierName() + "):");
            System.out.println("Total spent: $" + String.format("%.2f", member.getTotalSpent()));
            System.out.println("Upgrade threshold: $" + String.format("%.2f", member.getUpgradeThreshold()));
            if (member.isEligibleForUpgrade()) {
                System.out.println("✅ Eligible for upgrade!");
            } else {
                double needed = member.getUpgradeThreshold() - member.getTotalSpent();
                System.out.println("❌ Need to spend $" + String.format("%.2f", needed) + " more for upgrade");
            }
            System.out.println();
        }
        
        // Demonstrate polymorphism
        System.out.println("\n🔄 POLYMORPHISM DEMONSTRATION:");
        System.out.println("-".repeat(50));
        demonstratePolymorphism(members);
        
        // Demonstrate discount comparison
        System.out.println("\n💰 DISCOUNT COMPARISON:");
        System.out.println("-".repeat(50));
        double testAmount = 500.0;
        System.out.println("Purchase amount: $" + String.format("%.2f", testAmount));
        System.out.println();
        
        for (Membership member : members) {
            double discount = member.calculateDiscount();
            double discountedAmount = testAmount * (1 - discount);
            double savings = testAmount - discountedAmount;
            
            System.out.println(member.getTierName() + " Member (" + member.getMemberName() + "):");
            System.out.println("Discount: " + String.format("%.1f", discount * 100) + "%");
            System.out.println("Final amount: $" + String.format("%.2f", discountedAmount));
            System.out.println("Savings: $" + String.format("%.2f", savings));
            System.out.println();
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
    
    /**
     * Demonstrates runtime polymorphism
     * @param members Array of Membership objects
     */
    public static void demonstratePolymorphism(Membership[] members) {
        System.out.println("Processing members using polymorphism:");
        for (int i = 0; i < members.length; i++) {
            Membership member = members[i];
            System.out.println((i + 1) + ". " + member.getMemberName() + " (" + member.getTierName() + ")");
            System.out.println("   Discount: " + String.format("%.1f", member.calculateDiscount() * 100) + "%");
            System.out.println("   Points per dollar: " + member.calculatePoints(1.0));
            System.out.println("   Benefits: " + member.getBenefits());
            System.out.println();
        }
    }
}
