package composition.ecommerce;

/**
 * Express Shipping Calculator implementation
 */
public class ExpressShippingCalculator implements ShippingCalculator {
    private static final double EXPRESS_BASE_RATE = 19.99;
    private static final double EXPRESS_WEIGHT_RATE = 1.00; // per kg
    
    @Override
    public double calculateShipping(ShoppingCart cart) {
        if (cart.isEmpty()) {
            return 0.0;
        }
        
        // Express shipping always has a cost
        double totalWeight = cart.getItems().values().stream()
                                .mapToDouble(item -> item.getProduct().getWeight() * item.getQuantity())
                                .sum();
        
        double weightCost = totalWeight * EXPRESS_WEIGHT_RATE;
        return EXPRESS_BASE_RATE + weightCost;
    }
    
    @Override
    public String getCalculatorName() {
        return "Express Shipping";
    }
}
