/**
 * Silver Member class extending Membership
 * Demonstrates method overriding and polymorphic behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class SilverMember extends Membership {
    private static final double SILVER_DISCOUNT = 0.05; // 5% discount
    private static final int POINTS_PER_DOLLAR = 1; // 1 point per dollar
    private static final double UPGRADE_THRESHOLD = 1000.0; // $1000 for upgrade
    
    /**
     * Constructor for SilverMember
     * @param memberId Unique member identifier
     * @param memberName Name of the member
     * @param email Email address
     * @param joinDate Date when member joined
     */
    public SilverMember(String memberId, String memberName, String email, String joinDate) {
        super(memberId, memberName, email, joinDate, "Silver");
    }
    
    /**
     * Override calculateDiscount method with silver member discount
     * @return The discount percentage for silver members
     */
    @Override
    public double calculateDiscount() {
        return SILVER_DISCOUNT;
    }
    
    /**
     * Override calculatePoints method with silver member point rate
     * @param amount The amount spent
     * @return The points earned
     */
    @Override
    public int calculatePoints(double amount) {
        return (int) (amount * POINTS_PER_DOLLAR);
    }
    
    /**
     * Override getBenefits method with silver member benefits
     * @return String description of silver member benefits
     */
    @Override
    public String getBenefits() {
        return "Silver Member Benefits: 5% discount on all purchases, " +
               "1 point per dollar spent, Free shipping on orders over $50, " +
               "Early access to sales, Birthday discount";
    }
    
    /**
     * Override getUpgradeThreshold method with silver member upgrade requirement
     * @return The minimum amount spent required for upgrade
     */
    @Override
    public double getUpgradeThreshold() {
        return UPGRADE_THRESHOLD;
    }
    
    /**
     * Silver member specific method for birthday discount
     * @return String description of birthday discount
     */
    public String getBirthdayDiscount() {
        return "Happy Birthday! You get an additional 10% off your next purchase!";
    }
    
    /**
     * Silver member specific method for early access
     * @return String description of early access
     */
    public String getEarlyAccess() {
        return "You have early access to our exclusive sales and new arrivals!";
    }
    
    /**
     * Silver member specific method for free shipping
     * @param orderAmount The order amount
     * @return String description of shipping status
     */
    public String getShippingStatus(double orderAmount) {
        if (orderAmount >= 50.0) {
            return "Congratulations! You qualify for free shipping!";
        } else {
            double remaining = 50.0 - orderAmount;
            return "Add $" + String.format("%.2f", remaining) + " more for free shipping!";
        }
    }
    
    /**
     * Silver member specific method for loyalty rewards
     * @return String description of loyalty rewards
     */
    public String getLoyaltyRewards() {
        return "Thank you for being a loyal Silver member! Your continued support is appreciated.";
    }
    
    /**
     * Override toString to include silver member specific details
     * @return String representation of the silver membership
     */
    @Override
    public String toString() {
        return super.toString() + " [Discount: " + String.format("%.1f", SILVER_DISCOUNT * 100) + "%, Points: " + POINTS_PER_DOLLAR + "x]";
    }
}
