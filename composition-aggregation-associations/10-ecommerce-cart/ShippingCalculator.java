package composition.ecommerce;

/**
 * Shipping Calculator interface for dynamic shipping cost calculation
 */
public interface ShippingCalculator {
    double calculateShipping(ShoppingCart cart);
    String getCalculatorName();
}
