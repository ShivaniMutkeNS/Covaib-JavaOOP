package composition.restaurant;

/**
 * Fixed amount discount implementation
 */
public class FixedAmountDiscount implements DiscountStrategy {
    private final double discountAmount;
    private final String description;
    private final double minimumAmount;
    
    public FixedAmountDiscount(double discountAmount, String description, double minimumAmount) {
        this.discountAmount = discountAmount;
        this.description = description;
        this.minimumAmount = minimumAmount;
    }
    
    @Override
    public double calculateDiscount(double subtotal) {
        if (!isApplicable(subtotal)) {
            return 0.0;
        }
        return Math.min(discountAmount, subtotal); // Don't exceed subtotal
    }
    
    @Override
    public String getDescription() {
        return description + " ($" + discountAmount + " off)";
    }
    
    @Override
    public boolean isApplicable(double subtotal) {
        return subtotal >= minimumAmount;
    }
}
