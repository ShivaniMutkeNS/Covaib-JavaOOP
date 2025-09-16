package composition.travel;

/**
 * Pricing Strategy Interface
 */
public interface PricingStrategy {
    double calculatePrice(double basePrice, TravelPackage travelPackage);
    String getStrategyName();
}

/**
 * Standard Pricing Strategy
 */
class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, TravelPackage travelPackage) {
        return basePrice; // No discount
    }
    
    @Override
    public String getStrategyName() {
        return "Standard Pricing";
    }
}

/**
 * Early Bird Discount Strategy
 */
class EarlyBirdPricingStrategy implements PricingStrategy {
    private final double discountPercentage;
    
    public EarlyBirdPricingStrategy(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public double calculatePrice(double basePrice, TravelPackage travelPackage) {
        return basePrice * (1.0 - discountPercentage / 100.0);
    }
    
    @Override
    public String getStrategyName() {
        return "Early Bird Discount (" + discountPercentage + "% off)";
    }
}

/**
 * Group Discount Strategy
 */
class GroupDiscountPricingStrategy implements PricingStrategy {
    private final int groupSize;
    private final double discountPercentage;
    
    public GroupDiscountPricingStrategy(int groupSize, double discountPercentage) {
        this.groupSize = groupSize;
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public double calculatePrice(double basePrice, TravelPackage travelPackage) {
        // Apply discount if package includes multiple services (simulating group booking)
        int serviceCount = 0;
        if (travelPackage.getFlight() != null) serviceCount++;
        if (travelPackage.getHotel() != null) serviceCount++;
        if (travelPackage.getCarRental() != null) serviceCount++;
        
        if (serviceCount >= groupSize) {
            return basePrice * (1.0 - discountPercentage / 100.0);
        }
        return basePrice;
    }
    
    @Override
    public String getStrategyName() {
        return "Group Discount (" + discountPercentage + "% off for " + groupSize + "+ services)";
    }
}

/**
 * Loyalty Member Pricing Strategy
 */
class LoyaltyMemberPricingStrategy implements PricingStrategy {
    private final String membershipTier;
    private final double discountPercentage;
    
    public LoyaltyMemberPricingStrategy(String membershipTier, double discountPercentage) {
        this.membershipTier = membershipTier;
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public double calculatePrice(double basePrice, TravelPackage travelPackage) {
        return basePrice * (1.0 - discountPercentage / 100.0);
    }
    
    @Override
    public String getStrategyName() {
        return membershipTier + " Member Discount (" + discountPercentage + "% off)";
    }
}
