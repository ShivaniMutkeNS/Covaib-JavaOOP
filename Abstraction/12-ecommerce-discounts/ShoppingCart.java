import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart that manages cart items and integrates with discount engine
 */
public class ShoppingCart {
    private List<CartItem> items;
    private DiscountEngine discountEngine;
    private String customerId;
    private double shippingCost;
    private double taxRate;
    
    public ShoppingCart(String customerId) {
        this.items = new ArrayList<>();
        this.discountEngine = new DiscountEngine();
        this.customerId = customerId;
        this.shippingCost = 0.0;
        this.taxRate = 0.08; // 8% tax rate
    }
    
    public void addItem(Product product, int quantity) {
        if (!product.isInStock(quantity)) {
            System.out.println("❌ Insufficient stock for " + product.getName());
            return;
        }
        
        // Check if item already exists in cart
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                int newQuantity = item.getQuantity() + quantity;
                if (product.isInStock(newQuantity)) {
                    item.setQuantity(newQuantity);
                    System.out.println("✅ Updated quantity for " + product.getName() + " to " + newQuantity);
                } else {
                    System.out.println("❌ Cannot add more " + product.getName() + " - insufficient stock");
                }
                return;
            }
        }
        
        // Add new item
        CartItem newItem = new CartItem(product, quantity);
        items.add(newItem);
        System.out.println("🛒 Added " + quantity + "x " + product.getName() + " to cart");
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getProductId().equals(productId));
        System.out.println("🗑️ Removed item from cart: " + productId);
    }
    
    public void updateQuantity(String productId, int newQuantity) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                if (newQuantity <= 0) {
                    removeItem(productId);
                } else if (item.getProduct().isInStock(newQuantity)) {
                    item.setQuantity(newQuantity);
                    System.out.println("✅ Updated quantity for " + item.getProduct().getName() + " to " + newQuantity);
                } else {
                    System.out.println("❌ Insufficient stock for requested quantity");
                }
                return;
            }
        }
        System.out.println("❌ Item not found in cart: " + productId);
    }
    
    public void clearCart() {
        items.clear();
        System.out.println("🧹 Cart cleared");
    }
    
    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
    
    public double getOriginalSubtotal() {
        return items.stream().mapToDouble(CartItem::getOriginalTotal).sum();
    }
    
    public double getTotalSavings() {
        return items.stream().mapToDouble(CartItem::getSavings).sum();
    }
    
    public double getTax() {
        return getSubtotal() * taxRate;
    }
    
    public double getTotal() {
        return getSubtotal() + getTax() + shippingCost;
    }
    
    public void calculateShipping() {
        double totalWeight = items.stream().mapToDouble(item -> 
            item.getProduct().getWeight() * item.getQuantity()).sum();
        
        if (getOriginalSubtotal() >= 50.0) {
            shippingCost = 0.0; // Free shipping over $50
            System.out.println("🚚 Free shipping applied!");
        } else if (totalWeight <= 2.0) {
            shippingCost = 5.99;
        } else if (totalWeight <= 10.0) {
            shippingCost = 9.99;
        } else {
            shippingCost = 15.99;
        }
    }
    
    public void applyBestDiscounts() {
        if (items.isEmpty()) {
            System.out.println("❌ Cannot apply discounts to empty cart");
            return;
        }
        
        DiscountEngine.DiscountResult result = discountEngine.calculateBestDiscount(items);
        
        if (result.getTotalDiscount() > 0) {
            discountEngine.applyDiscounts(items, result);
            System.out.println("💰 " + result.toString());
            
            System.out.println("Applied discounts:");
            for (Discount discount : result.getAppliedDiscounts()) {
                System.out.println("  • " + discount.getName());
            }
        } else {
            System.out.println("ℹ️ No applicable discounts found");
        }
    }
    
    public void printCart() {
        System.out.println("\n🛒 SHOPPING CART - Customer: " + customerId);
        System.out.println("=".repeat(60));
        
        if (items.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }
        
        for (CartItem item : items) {
            System.out.println(item.toString());
        }
        
        System.out.println("-".repeat(60));
        System.out.println(String.format("Subtotal: $%.2f", getSubtotal()));
        
        if (getTotalSavings() > 0) {
            System.out.println(String.format("Original: $%.2f", getOriginalSubtotal()));
            System.out.println(String.format("You Save: $%.2f (%.1f%%)", getTotalSavings(), 
                (getTotalSavings() / getOriginalSubtotal()) * 100));
        }
        
        System.out.println(String.format("Shipping: $%.2f", shippingCost));
        System.out.println(String.format("Tax (%.1f%%): $%.2f", taxRate * 100, getTax()));
        System.out.println("=".repeat(60));
        System.out.println(String.format("TOTAL: $%.2f", getTotal()));
        System.out.println();
    }
    
    // Getters
    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public DiscountEngine getDiscountEngine() { return discountEngine; }
    public String getCustomerId() { return customerId; }
    public double getShippingCost() { return shippingCost; }
    public double getTaxRate() { return taxRate; }
    
    // Setters
    public void setShippingCost(double shippingCost) { this.shippingCost = shippingCost; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
}
