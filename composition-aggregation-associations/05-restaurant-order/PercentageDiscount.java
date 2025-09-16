package composition.restaurant;

/**
 * Percentage-based discount implementation
 */
public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;
    private final String description;
    private final double minimumAmount;
    
    public PercentageDiscount(double percentage, String description, double minimumAmount) {
        this.percentage = percentage;
        this.description = description;
        this.minimumAmount = minimumAmount;
    }
    
    @Override
    public double calculateDiscount(double subtotal) {
        if (!isApplicable(subtotal)) {
            return 0.0;
        }
        return subtotal * (percentage / 100.0);
    }
    
    @Override
    public String getDescription() {
        return description + " (" + percentage + "% off)";
    }
    
    @Override
    public boolean isApplicable(double subtotal) {
        return subtotal >= minimumAmount;
    }
}
