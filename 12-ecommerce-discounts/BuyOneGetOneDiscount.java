import java.util.List;

/**
 * Buy One Get One (BOGO) discount implementation
 * Provides free or discounted items based on quantity purchased
 */
public class BuyOneGetOneDiscount extends Discount {
    private int buyQuantity;
    private int getQuantity;
    private double getDiscountPercentage; // 100% = free, 50% = half price
    
    public BuyOneGetOneDiscount(String discountId, String name, int buyQuantity, int getQuantity, double getDiscountPercentage) {
        super(discountId, name, DiscountType.BUY_ONE_GET_ONE, getDiscountPercentage);
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.getDiscountPercentage = getDiscountPercentage;
    }
    
    // Standard BOGO (Buy 1 Get 1 Free)
    public BuyOneGetOneDiscount(String discountId, String name) {
        this(discountId, name, 1, 1, 100.0);
    }
    
    @Override
    public double calculateDiscount(List<CartItem> cartItems) {
        if (!isValid() || !isApplicable(cartItems)) {
            return 0.0;
        }
        
        double totalDiscount = 0.0;
        
        for (CartItem item : cartItems) {
            if (isApplicableToProduct(item.getProduct())) {
                int quantity = item.getQuantity();
                int eligibleSets = quantity / (buyQuantity + getQuantity);
                int remainingItems = quantity % (buyQuantity + getQuantity);
                
                // Calculate discount for complete sets
                double setDiscount = eligibleSets * getQuantity * item.getUnitPrice() * (getDiscountPercentage / 100.0);
                
                // Check if remaining items qualify for partial discount
                if (remainingItems >= buyQuantity) {
                    int partialGetItems = Math.min(getQuantity, remainingItems - buyQuantity);
                    double partialDiscount = partialGetItems * item.getUnitPrice() * (getDiscountPercentage / 100.0);
                    setDiscount += partialDiscount;
                }
                
                totalDiscount += setDiscount;
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
        
        // Check if any item has enough quantity to qualify
        for (CartItem item : cartItems) {
            if (isApplicableToProduct(item.getProduct()) && item.getQuantity() >= buyQuantity) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String getDiscountDescription() {
        StringBuilder description = new StringBuilder();
        
        if (buyQuantity == 1 && getQuantity == 1 && getDiscountPercentage == 100.0) {
            description.append("Buy 1 Get 1 FREE");
        } else if (getDiscountPercentage == 100.0) {
            description.append(String.format("Buy %d Get %d FREE", buyQuantity, getQuantity));
        } else {
            description.append(String.format("Buy %d Get %d at %.0f%% off", buyQuantity, getQuantity, getDiscountPercentage));
        }
        
        if (applicableCategories.length > 0) {
            description.append(" on ").append(String.join(", ", applicableCategories));
        } else if (applicableBrands.length > 0) {
            description.append(" on ").append(String.join(", ", applicableBrands)).append(" products");
        }
        
        if (minimumOrderAmount > 0) {
            description.append(String.format(" (minimum order $%.2f)", minimumOrderAmount));
        }
        
        return description.toString();
    }
    
    // Getters
    public int getBuyQuantity() { return buyQuantity; }
    public int getGetQuantity() { return getQuantity; }
    public double getGetDiscountPercentage() { return getDiscountPercentage; }
}
