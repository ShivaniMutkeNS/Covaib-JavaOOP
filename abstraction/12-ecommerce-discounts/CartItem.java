/**
 * Represents an item in the shopping cart with quantity and pricing details
 */
public class CartItem {
    private Product product;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private double discountApplied;
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getBasePrice();
        this.totalPrice = unitPrice * quantity;
        this.discountApplied = 0.0;
    }
    
    public void applyDiscount(double discountAmount) {
        this.discountApplied += discountAmount;
        this.totalPrice = Math.max(0, (unitPrice * quantity) - discountApplied);
    }
    
    public void resetDiscount() {
        this.discountApplied = 0.0;
        this.totalPrice = unitPrice * quantity;
    }
    
    public double getOriginalTotal() {
        return unitPrice * quantity;
    }
    
    public double getSavings() {
        return discountApplied;
    }
    
    public double getDiscountPercentage() {
        double original = getOriginalTotal();
        return original > 0 ? (discountApplied / original) * 100.0 : 0.0;
    }
    
    // Getters
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalPrice() { return totalPrice; }
    public double getDiscountApplied() { return discountApplied; }
    
    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = Math.max(0, (unitPrice * quantity) - discountApplied);
    }
    
    @Override
    public String toString() {
        return String.format("%s x%d - $%.2f%s", 
            product.getName(), quantity, totalPrice,
            discountApplied > 0 ? String.format(" (Save $%.2f)", discountApplied) : "");
    }
    
    public String getDetailedInfo() {
        return String.format(
            "📦 Cart Item:\n" +
            "Product: %s\n" +
            "Quantity: %d\n" +
            "Unit Price: $%.2f\n" +
            "Original Total: $%.2f\n" +
            "Discount Applied: $%.2f\n" +
            "Final Total: $%.2f\n" +
            "Savings: %.1f%%",
            product.getName(), quantity, unitPrice, getOriginalTotal(),
            discountApplied, totalPrice, getDiscountPercentage()
        );
    }
}
