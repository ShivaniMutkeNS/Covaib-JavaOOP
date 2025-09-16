import java.util.List;

/**
 * Fixed amount discount implementation
 * Applies a fixed dollar amount discount to the order
 */
public class FixedAmountDiscount extends Discount {
    
    public FixedAmountDiscount(String discountId, String name, double amount) {
        super(discountId, name, DiscountType.FIXED_AMOUNT, amount);
    }
    
    @Override
    public double calculateDiscount(List<CartItem> cartItems) {
        if (!isValid() || !isApplicable(cartItems)) {
            return 0.0;
        }
        
        double orderTotal = getOrderTotal(cartItems);
        
        if (!meetsMinimumOrder(orderTotal)) {
            return 0.0;
        }
        
        // For fixed amount discounts, apply to the entire order
        double discountAmount = Math.min(value, orderTotal);
        return applyMaximumLimit(discountAmount);
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
        
        // Fixed amount discounts typically apply to entire order
        // Check if any restrictions apply
        if (applicableCategories.length > 0 || applicableBrands.length > 0) {
            for (CartItem item : cartItems) {
                if (isApplicableToProduct(item.getProduct())) {
                    return true;
                }
            }
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getDiscountDescription() {
        StringBuilder description = new StringBuilder();
        description.append(String.format("Get $%.2f off", value));
        
        if (applicableCategories.length > 0) {
            description.append(" when you buy ").append(String.join(", ", applicableCategories));
        } else if (applicableBrands.length > 0) {
            description.append(" when you buy ").append(String.join(", ", applicableBrands)).append(" products");
        } else {
            description.append(" your order");
        }
        
        if (minimumOrderAmount > 0) {
            description.append(String.format(" (minimum order $%.2f)", minimumOrderAmount));
        }
        
        return description.toString();
    }
}
