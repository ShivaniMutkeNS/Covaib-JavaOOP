import java.util.List;

/**
 * Percentage-based discount implementation
 * Applies a percentage discount to eligible items
 */
public class PercentageDiscount extends Discount {
    
    public PercentageDiscount(String discountId, String name, double percentage) {
        super(discountId, name, DiscountType.PERCENTAGE, percentage);
    }
    
    @Override
    public double calculateDiscount(List<CartItem> cartItems) {
        if (!isValid() || !isApplicable(cartItems)) {
            return 0.0;
        }
        
        double totalDiscount = 0.0;
        double orderTotal = getOrderTotal(cartItems);
        
        if (!meetsMinimumOrder(orderTotal)) {
            return 0.0;
        }
        
        for (CartItem item : cartItems) {
            if (isApplicableToProduct(item.getProduct())) {
                double itemDiscount = item.getOriginalTotal() * (value / 100.0);
                totalDiscount += itemDiscount;
            }
        }
        
        return applyMaximumLimit(totalDiscount);
    }
    
    @Override
    public boolean isApplicable(List<CartItem> cartItems) {
        if (!isValid()) {
            return false;
        }
        
        double orderTotal = getOrderTotal(cartItems);
        if (!meetsMinimumOrder(orderTotal)) {
            return false;
        }
        
        // Check if at least one item is eligible
        for (CartItem item : cartItems) {
            if (isApplicableToProduct(item.getProduct())) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String getDiscountDescription() {
        StringBuilder description = new StringBuilder();
        description.append(String.format("Get %.1f%% off", value));
        
        if (applicableCategories.length > 0) {
            description.append(" on ").append(String.join(", ", applicableCategories));
        } else if (applicableBrands.length > 0) {
            description.append(" on ").append(String.join(", ", applicableBrands)).append(" products");
        } else {
            description.append(" your entire order");
        }
        
        if (minimumOrderAmount > 0) {
            description.append(String.format(" (minimum order $%.2f)", minimumOrderAmount));
        }
        
        if (maximumDiscountAmount < Double.MAX_VALUE) {
            description.append(String.format(" (max discount $%.2f)", maximumDiscountAmount));
        }
        
        return description.toString();
    }
}
