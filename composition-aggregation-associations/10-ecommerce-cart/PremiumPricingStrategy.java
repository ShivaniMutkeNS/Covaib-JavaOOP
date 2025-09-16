package composition.ecommerce;

/**
 * Premium Pricing Strategy implementation with enhanced discounts
 */
public class PremiumPricingStrategy implements PricingStrategy {
    private static final double TAX_RATE = 0.06; // 6% tax for premium customers
    
    @Override
    public double calculateDiscount(ShoppingCart cart, double subtotal) {
        // Premium discounts: 10% off for orders over $50, 15% off for orders over $150, 20% off for orders over $300
        if (subtotal >= 300) {
            return subtotal * 0.20;
        } else if (subtotal >= 150) {
            return subtotal * 0.15;
        } else if (subtotal >= 50) {
            return subtotal * 0.10;
        }
        return 0.0;
    }
    
    @Override
    public double calculateTax(ShoppingCart cart, double taxableAmount) {
        return taxableAmount * TAX_RATE;
    }
    
    @Override
    public String getStrategyName() {
        return "Premium Pricing";
    }
}
