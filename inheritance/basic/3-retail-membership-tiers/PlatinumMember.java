/**
 * Platinum Member class extending Membership
 * Demonstrates method overriding and polymorphic behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class PlatinumMember extends Membership {
    private static final double PLATINUM_DISCOUNT = 0.15; // 15% discount
    private static final int POINTS_PER_DOLLAR = 3; // 3 points per dollar
    private static final double UPGRADE_THRESHOLD = 5000.0; // $5000 for upgrade (highest tier)
    
    /**
     * Constructor for PlatinumMember
     * @param memberId Unique member identifier
     * @param memberName Name of the member
     * @param email Email address
     * @param joinDate Date when member joined
     */
    public PlatinumMember(String memberId, String memberName, String email, String joinDate) {
        super(memberId, memberName, email, joinDate, "Platinum");
    }
    
    /**
     * Override calculateDiscount method with platinum member discount
     * @return The discount percentage for platinum members
     */
    @Override
    public double calculateDiscount() {
        return PLATINUM_DISCOUNT;
    }
    
    /**
     * Override calculatePoints method with platinum member point rate
     * @param amount The amount spent
     * @return The points earned
     */
    @Override
    public int calculatePoints(double amount) {
        return (int) (amount * POINTS_PER_DOLLAR);
    }
    
    /**
     * Override getBenefits method with platinum member benefits
     * @return String description of platinum member benefits
     */
    @Override
    public String getBenefits() {
        return "Platinum Member Benefits: 15% discount on all purchases, " +
               "3 points per dollar spent, Free shipping on all orders, " +
               "VIP customer service, Exclusive platinum member events, " +
               "Triple points on birthdays, Free returns, Personal concierge, " +
               "Access to limited edition products, Annual gift";
    }
    
    /**
     * Override getUpgradeThreshold method with platinum member upgrade requirement
     * @return The minimum amount spent required for upgrade (highest tier)
     */
    @Override
    public double getUpgradeThreshold() {
        return UPGRADE_THRESHOLD;
    }
    
    /**
     * Platinum member specific method for VIP service
     * @return String description of VIP service
     */
    public String getVIPService() {
        return "You have access to our VIP customer service with dedicated support!";
    }
    
    /**
     * Platinum member specific method for personal concierge
     * @return String description of personal concierge
     */
    public String getPersonalConcierge() {
        return "Your personal concierge is available 24/7 for any assistance!";
    }
    
    /**
     * Platinum member specific method for exclusive events
     * @return String description of exclusive events
     */
    public String getExclusiveEvents() {
        return "You're invited to our most exclusive platinum member events and private previews!";
    }
    
    /**
     * Platinum member specific method for triple points
     * @return String description of triple points
     */
    public String getTriplePoints() {
        return "Earn triple points on your birthday and special occasions!";
    }
    
    /**
     * Platinum member specific method for limited edition access
     * @return String description of limited edition access
     */
    public String getLimitedEditionAccess() {
        return "Exclusive access to limited edition products and collaborations!";
    }
    
    /**
     * Platinum member specific method for annual gift
     * @return String description of annual gift
     */
    public String getAnnualGift() {
        return "Receive an exclusive annual gift as a thank you for your loyalty!";
    }
    
    /**
     * Platinum member specific method for priority access
     * @return String description of priority access
     */
    public String getPriorityAccess() {
        return "Priority access to new collections and restocks!";
    }
    
    /**
     * Platinum member specific method for exclusive partnerships
     * @return String description of exclusive partnerships
     */
    public String getExclusivePartnerships() {
        return "Access to exclusive partnerships and collaborations with luxury brands!";
    }
    
    /**
     * Platinum member specific method for lifetime benefits
     * @return String description of lifetime benefits
     */
    public String getLifetimeBenefits() {
        return "Your platinum status includes lifetime benefits and recognition!";
    }
    
    /**
     * Override toString to include platinum member specific details
     * @return String representation of the platinum membership
     */
    @Override
    public String toString() {
        return super.toString() + " [Discount: " + String.format("%.1f", PLATINUM_DISCOUNT * 100) + "%, Points: " + POINTS_PER_DOLLAR + "x]";
    }
}
