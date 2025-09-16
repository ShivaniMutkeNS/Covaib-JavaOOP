/**
 * Enumeration representing different types of discounts in e-commerce
 * Each type has specific characteristics and application rules
 */
public enum DiscountType {
    PERCENTAGE("Percentage Discount", true, false, true),
    FIXED_AMOUNT("Fixed Amount Discount", false, false, true),
    BUY_ONE_GET_ONE("Buy One Get One", false, true, false),
    BULK_DISCOUNT("Bulk/Volume Discount", true, true, true),
    SEASONAL("Seasonal Discount", true, false, true),
    LOYALTY_POINTS("Loyalty Points Discount", false, false, false),
    SHIPPING_DISCOUNT("Shipping Discount", true, false, false),
    FIRST_TIME_BUYER("First Time Buyer", true, false, true),
    REFERRAL("Referral Discount", true, false, true),
    CLEARANCE("Clearance Sale", true, false, true),
    FLASH_SALE("Flash Sale", true, false, true),
    STUDENT_DISCOUNT("Student Discount", true, false, true);
    
    private final String displayName;
    private final boolean supportsPercentage;
    private final boolean requiresQuantity;
    private final boolean canCombine;
    
    DiscountType(String displayName, boolean supportsPercentage, 
                boolean requiresQuantity, boolean canCombine) {
        this.displayName = displayName;
        this.supportsPercentage = supportsPercentage;
        this.requiresQuantity = requiresQuantity;
        this.canCombine = canCombine;
    }
    
    public String getDisplayName() { return displayName; }
    public boolean supportsPercentage() { return supportsPercentage; }
    public boolean requiresQuantity() { return requiresQuantity; }
    public boolean canCombine() { return canCombine; }
    
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", 
            displayName,
            supportsPercentage ? "%" : "$",
            canCombine ? "Combinable" : "Exclusive");
    }
}
