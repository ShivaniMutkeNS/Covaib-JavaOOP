/**
 * Base class for all retail membership tiers
 * Demonstrates base class + polymorphic methods
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Membership {
    protected String memberId;
    protected String memberName;
    protected String email;
    protected String joinDate;
    protected double totalSpent;
    protected int pointsEarned;
    protected boolean isActive;
    protected String tierName;
    
    /**
     * Constructor for Membership
     * @param memberId Unique member identifier
     * @param memberName Name of the member
     * @param email Email address
     * @param joinDate Date when member joined
     * @param tierName Name of the membership tier
     */
    public Membership(String memberId, String memberName, String email, String joinDate, String tierName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.joinDate = joinDate;
        this.tierName = tierName;
        this.totalSpent = 0.0;
        this.pointsEarned = 0;
        this.isActive = true;
    }
    
    /**
     * Abstract method to calculate discount percentage
     * Each membership tier has different discount rates
     * @return The discount percentage (0.0 to 1.0)
     */
    public abstract double calculateDiscount();
    
    /**
     * Abstract method to calculate points earned
     * Each membership tier has different point earning rates
     * @param amount The amount spent
     * @return The points earned
     */
    public abstract int calculatePoints(double amount);
    
    /**
     * Abstract method to get tier benefits
     * Each membership tier has different benefits
     * @return String description of benefits
     */
    public abstract String getBenefits();
    
    /**
     * Concrete method to make a purchase
     * @param amount The amount spent
     * @return The final amount after discount
     */
    public double makePurchase(double amount) {
        if (!isActive) {
            System.out.println("Membership is not active. Cannot make purchase.");
            return amount;
        }
        
        double discount = calculateDiscount();
        double discountedAmount = amount * (1 - discount);
        int points = calculatePoints(amount);
        
        totalSpent += amount;
        pointsEarned += points;
        
        System.out.println("Purchase made: $" + String.format("%.2f", amount));
        System.out.println("Discount applied: " + String.format("%.1f", discount * 100) + "%");
        System.out.println("Final amount: $" + String.format("%.2f", discountedAmount));
        System.out.println("Points earned: " + points);
        
        return discountedAmount;
    }
    
    /**
     * Concrete method to redeem points
     * @param pointsToRedeem Number of points to redeem
     * @return The dollar value of redeemed points
     */
    public double redeemPoints(int pointsToRedeem) {
        if (pointsToRedeem > pointsEarned) {
            System.out.println("Insufficient points. Available: " + pointsEarned);
            return 0.0;
        }
        
        double redemptionValue = pointsToRedeem * 0.01; // 1 point = $0.01
        pointsEarned -= pointsToRedeem;
        
        System.out.println("Redeemed " + pointsToRedeem + " points for $" + String.format("%.2f", redemptionValue));
        return redemptionValue;
    }
    
    /**
     * Concrete method to get member information
     * @return String with member details
     */
    public String getMemberInfo() {
        return String.format("Member ID: %s, Name: %s, Tier: %s, Total Spent: $%.2f, Points: %d", 
                           memberId, memberName, tierName, totalSpent, pointsEarned);
    }
    
    /**
     * Concrete method to check if member is eligible for upgrade
     * @return True if member is eligible for upgrade
     */
    public boolean isEligibleForUpgrade() {
        return totalSpent >= getUpgradeThreshold();
    }
    
    /**
     * Abstract method to get upgrade threshold
     * Each membership tier has different upgrade requirements
     * @return The minimum amount spent required for upgrade
     */
    public abstract double getUpgradeThreshold();
    
    /**
     * Getter for member ID
     * @return The member ID
     */
    public String getMemberId() {
        return memberId;
    }
    
    /**
     * Getter for member name
     * @return The member name
     */
    public String getMemberName() {
        return memberName;
    }
    
    /**
     * Getter for email
     * @return The email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Getter for join date
     * @return The join date
     */
    public String getJoinDate() {
        return joinDate;
    }
    
    /**
     * Getter for total spent
     * @return The total amount spent
     */
    public double getTotalSpent() {
        return totalSpent;
    }
    
    /**
     * Getter for points earned
     * @return The points earned
     */
    public int getPointsEarned() {
        return pointsEarned;
    }
    
    /**
     * Getter for active status
     * @return True if membership is active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Getter for tier name
     * @return The tier name
     */
    public String getTierName() {
        return tierName;
    }
    
    /**
     * Override toString method
     * @return String representation of the membership
     */
    @Override
    public String toString() {
        return getMemberInfo();
    }
}
