package composition.ecommerce;

/**
 * Pricing Strategy interface for dynamic pricing calculations
 */
public interface PricingStrategy {
    double calculateDiscount(ShoppingCart cart, double subtotal);
    double calculateTax(ShoppingCart cart, double taxableAmount);
    String getStrategyName();
}
