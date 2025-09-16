package composition.ecommerce;

/**
 * Standard Shipping Calculator implementation
 */
public class StandardShippingCalculator implements ShippingCalculator {
    private static final double FREE_SHIPPING_THRESHOLD = 75.0;
    private static final double STANDARD_RATE = 9.99;
    private static final double WEIGHT_RATE = 0.50; // per kg
    
    @Override
    public double calculateShipping(ShoppingCart cart) {
        if (cart.isEmpty()) {
            return 0.0;
        }
        
        double subtotal = cart.getItems().values().stream()
                             .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                             .sum();
        
        // Free shipping for orders over threshold
        if (subtotal >= FREE_SHIPPING_THRESHOLD) {
            return 0.0;
        }
        
        // Calculate weight-based shipping
        double totalWeight = cart.getItems().values().stream()
                                .mapToDouble(item -> item.getProduct().getWeight() * item.getQuantity())
                                .sum();
        
        double weightCost = totalWeight * WEIGHT_RATE;
        return Math.max(STANDARD_RATE, weightCost);
    }
    
    @Override
    public String getCalculatorName() {
        return "Standard Shipping";
    }
}
