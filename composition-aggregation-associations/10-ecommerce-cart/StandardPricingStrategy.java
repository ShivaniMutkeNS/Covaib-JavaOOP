package composition.ecommerce;

/**
 * Standard Pricing Strategy implementation
 */
public class StandardPricingStrategy implements PricingStrategy {
    private static final double TAX_RATE = 0.08; // 8% tax
    
    @Override
    public double calculateDiscount(ShoppingCart cart, double subtotal) {
        // Volume discount: 5% off for orders over $100, 10% off for orders over $200
        if (subtotal >= 200) {
            return subtotal * 0.10;
        } else if (subtotal >= 100) {
            return subtotal * 0.05;
        }
        return 0.0;
    }
    
    @Override
    public double calculateTax(ShoppingCart cart, double taxableAmount) {
        return taxableAmount * TAX_RATE;
    }
    
    @Override
    public String getStrategyName() {
        return "Standard Pricing";
    }
}
