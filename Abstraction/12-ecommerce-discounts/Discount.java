import java.time.LocalDateTime;
import java.util.List;

/**
 * Abstract base class for all discount implementations
 * Defines core discount operations that all discount types must implement
 */
public abstract class Discount {
    protected String discountId;
    protected String name;
    protected DiscountType discountType;
    protected double value; // percentage or fixed amount
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    protected boolean isActive;
    protected int usageLimit;
    protected int usageCount;
    protected double minimumOrderAmount;
    protected double maximumDiscountAmount;
    protected String[] applicableCategories;
    protected String[] applicableBrands;
    
    public Discount(String discountId, String name, DiscountType discountType, double value) {
        this.discountId = discountId;
        this.name = name;
        this.discountType = discountType;
        this.value = value;
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now().plusDays(30); // Default 30 days
        this.isActive = true;
        this.usageLimit = -1; // Unlimited by default
        this.usageCount = 0;
        this.minimumOrderAmount = 0.0;
        this.maximumDiscountAmount = Double.MAX_VALUE;
        this.applicableCategories = new String[0];
        this.applicableBrands = new String[0];
    }
    
    // Abstract methods that must be implemented by concrete discount classes
    public abstract double calculateDiscount(List<CartItem> cartItems);
    public abstract boolean isApplicable(List<CartItem> cartItems);
    public abstract String getDiscountDescription();
    
    // Concrete methods with default implementation
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && 
               now.isAfter(startDate) && 
               now.isBefore(endDate) &&
               (usageLimit == -1 || usageCount < usageLimit);
    }
    
    public boolean meetsMinimumOrder(double orderTotal) {
        return orderTotal >= minimumOrderAmount;
    }
    
    public boolean isApplicableToProduct(Product product) {
        // Check category restrictions
        if (applicableCategories.length > 0) {
            boolean categoryMatch = false;
            for (String category : applicableCategories) {
                if (product.getCategory().equalsIgnoreCase(category)) {
                    categoryMatch = true;
                    break;
                }
            }
            if (!categoryMatch) return false;
        }
        
        // Check brand restrictions
        if (applicableBrands.length > 0) {
            boolean brandMatch = false;
            for (String brand : applicableBrands) {
                if (product.getBrand().equalsIgnoreCase(brand)) {
                    brandMatch = true;
                    break;
                }
            }
            if (!brandMatch) return false;
        }
        
        return true;
    }
    
    protected double applyMaximumLimit(double calculatedDiscount) {
        return Math.min(calculatedDiscount, maximumDiscountAmount);
    }
    
    public void incrementUsage() {
        usageCount++;
    }
    
    public double getOrderTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                       .mapToDouble(CartItem::getOriginalTotal)
                       .sum();
    }
    
    // Getters and Setters
    public String getDiscountId() { return discountId; }
    public String getName() { return name; }
    public DiscountType getDiscountType() { return discountType; }
    public double getValue() { return value; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public int getUsageLimit() { return usageLimit; }
    public int getUsageCount() { return usageCount; }
    public double getMinimumOrderAmount() { return minimumOrderAmount; }
    public double getMaximumDiscountAmount() { return maximumDiscountAmount; }
    public String[] getApplicableCategories() { return applicableCategories.clone(); }
    public String[] getApplicableBrands() { return applicableBrands.clone(); }
    
    public void setActive(boolean active) { this.isActive = active; }
    public void setUsageLimit(int usageLimit) { this.usageLimit = usageLimit; }
    public void setMinimumOrderAmount(double minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }
    public void setMaximumDiscountAmount(double maximumDiscountAmount) { this.maximumDiscountAmount = maximumDiscountAmount; }
    public void setApplicableCategories(String[] categories) { this.applicableCategories = categories.clone(); }
    public void setApplicableBrands(String[] brands) { this.applicableBrands = brands.clone(); }
    public void setDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", 
            name, discountType.getDisplayName(), 
            isValid() ? "Active" : "Inactive");
    }
    
    public String getDetailedInfo() {
        return String.format(
            "🎫 Discount Details:\n" +
            "ID: %s\n" +
            "Name: %s\n" +
            "Type: %s\n" +
            "Value: %s\n" +
            "Valid: %s to %s\n" +
            "Status: %s\n" +
            "Usage: %s\n" +
            "Min Order: $%.2f\n" +
            "Max Discount: $%.2f\n" +
            "Categories: %s\n" +
            "Brands: %s",
            discountId, name, discountType.getDisplayName(),
            discountType.supportsPercentage() ? String.format("%.1f%%", value) : String.format("$%.2f", value),
            startDate.toLocalDate(), endDate.toLocalDate(),
            isValid() ? "Active" : "Inactive",
            usageLimit == -1 ? String.format("%d/Unlimited", usageCount) : String.format("%d/%d", usageCount, usageLimit),
            minimumOrderAmount, maximumDiscountAmount,
            applicableCategories.length > 0 ? String.join(", ", applicableCategories) : "All",
            applicableBrands.length > 0 ? String.join(", ", applicableBrands) : "All"
        );
    }
}
