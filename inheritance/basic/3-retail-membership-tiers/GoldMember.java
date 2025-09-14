/**
 * Gold Member class extending Membership
 * Demonstrates method overriding and polymorphic behavior
 * 
 * @author Expert Developer
 * @version 1.0
 */
public class GoldMember extends Membership {
    private static final double GOLD_DISCOUNT = 0.10; // 10% discount
    private static final int POINTS_PER_DOLLAR = 2; // 2 points per dollar
    private static final double UPGRADE_THRESHOLD = 2500.0; // $2500 for upgrade
    
    /**
     * Constructor for GoldMember
     * @param memberId Unique member identifier
     * @param memberName Name of the member
     * @param email Email address
     * @param joinDate Date when member joined
     */
    public GoldMember(String memberId, String memberName, String email, String joinDate) {
        super(memberId, memberName, email, joinDate, "Gold");
    }
    
    /**
     * Override calculateDiscount method with gold member discount
     * @return The discount percentage for gold members
     */
    @Override
    public double calculateDiscount() {
        return GOLD_DISCOUNT;
    }
    
    /**
     * Override calculatePoints method with gold member point rate
     * @param amount The amount spent
     * @return The points earned
     */
    @Override
    public int calculatePoints(double amount) {
        return (int) (amount * POINTS_PER_DOLLAR);
    }
    
    /**
     * Override getBenefits method with gold member benefits
     * @return String description of gold member benefits
     */
    @Override
    public String getBenefits() {
        return "Gold Member Benefits: 10% discount on all purchases, " +
               "2 points per dollar spent, Free shipping on all orders, " +
               "Priority customer service, Exclusive gold member events, " +
               "Double points on birthdays, Free returns";
    }
    
    /**
     * Override getUpgradeThreshold method with gold member upgrade requirement
     * @return The minimum amount spent required for upgrade
     */
    @Override
    public double getUpgradeThreshold() {
        return UPGRADE_THRESHOLD;
    }
    
    /**
     * Gold member specific method for priority service
     * @return String description of priority service
     */
    public String getPriorityService() {
        return "You have access to our priority customer service line!";
    }
    
    /**
     * Gold member specific method for exclusive events
     * @return String description of exclusive events
     */
    public String getExclusiveEvents() {
        return "You're invited to our exclusive gold member events and previews!";
    }
    
    /**
     * Gold member specific method for double points
     * @return String description of double points
     */
    public String getDoublePoints() {
        return "Earn double points on your birthday and special occasions!";
    }
    
    /**
     * Gold member specific method for free returns
     * @return String description of free returns
     */
    public String getFreeReturns() {
        return "Enjoy free returns on all your purchases!";
    }
    
    /**
     * Gold member specific method for personal shopper
     * @return String description of personal shopper
     */
    public String getPersonalShopper() {
        return "Access to our personal shopping service for style advice!";
    }
    
    /**
     * Gold member specific method for exclusive products
     * @return String description of exclusive products
     */
    public String getExclusiveProducts() {
        return "Access to exclusive gold member only products and collections!";
    }
    
    /**
     * Override toString to include gold member specific details
     * @return String representation of the gold membership
     */
    @Override
    public String toString() {
        return super.toString() + " [Discount: " + String.format("%.1f", GOLD_DISCOUNT * 100) + "%, Points: " + POINTS_PER_DOLLAR + "x]";
    }
}
