import java.util.List;

/**
 * Bulk/Volume discount implementation
 * Provides discounts based on quantity thresholds
 */
public class BulkDiscount extends Discount {
    private int minimumQuantity;
    private double discountPercentage;
    private boolean applyToAllItems; // true = discount all items, false = discount only excess
    
    public BulkDiscount(String discountId, String name, int minimumQuantity, double discountPercentage, boolean applyToAllItems) {
        super(discountId, name, DiscountType.BULK_DISCOUNT, discountPercentage);
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
        this.applyToAllItems = applyToAllItems;
    }
    
    @Override
    public double calculateDiscount(List<CartItem> cartItems) {
        if (!isValid() || !isApplicable(cartItems)) {
            return 0.0;
        }
        
        double totalDiscount = 0.0;
        
        for (CartItem item : cartItems) {
            if (isApplicableToProduct(item.getProduct()) && item.getQuantity() >= minimumQuantity) {
                double itemDiscount;
                
                if (applyToAllItems) {
                    // Apply discount to all items
                    itemDiscount = item.getOriginalTotal() * (discountPercentage / 100.0);
                } else {
                    // Apply discount only to items beyond the minimum threshold
                    int discountableQuantity = item.getQuantity() - minimumQuantity + 1;
                    itemDiscount = (discountableQuantity * item.getUnitPrice()) * (discountPercentage / 100.0);
                }
                
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
        
        // Check if any item meets the minimum quantity requirement
        for (CartItem item : cartItems) {
            if (isApplicableToProduct(item.getProduct()) && item.getQuantity() >= minimumQuantity) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String getDiscountDescription() {
        StringBuilder description = new StringBuilder();
        description.append(String.format("Buy %d+ items and get %.1f%% off", minimumQuantity, discountPercentage));
        
        if (applyToAllItems) {
            description.append(" all items");
        } else {
            description.append(" additional items");
        }
        
        if (applicableCategories.length > 0) {
            description.append(" in ").append(String.join(", ", applicableCategories));
        } else if (applicableBrands.length > 0) {
            description.append(" from ").append(String.join(", ", applicableBrands));
        }
        
        if (minimumOrderAmount > 0) {
            description.append(String.format(" (minimum order $%.2f)", minimumOrderAmount));
        }
        
        return description.toString();
    }
    
    // Getters
    public int getMinimumQuantity() { return minimumQuantity; }
    public double getDiscountPercentage() { return discountPercentage; }
    public boolean isApplyToAllItems() { return applyToAllItems; }
}
