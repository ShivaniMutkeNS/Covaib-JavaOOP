
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * E-commerce Shopping Cart
 * 
 * This class demonstrates encapsulation by:
 * 1. Items should be added/removed only through methods
 * 2. Expose unmodifiable list of cart items to external classes
 * 3. Prevent price tampering (only system-calculated discounts allowed)
 * 4. Implement proper validation and state management
 */
public class ShoppingCart {
    // Encapsulated cart items
    private final Map<String, CartItem> items;
    private final String cartId;
    private final LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    
    // Cart configuration
    private final CartConfig config;
    
    // Applied discounts
    private final List<Discount> appliedDiscounts;
    
    /**
     * Constructor
     */
    public ShoppingCart(String cartId) {
        this.cartId = cartId;
        this.items = new HashMap<>();
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        this.config = new CartConfig();
        this.appliedDiscounts = new ArrayList<>();
    }
    
    /**
     * Add item to cart
     * @param productId Product ID
     * @param productName Product name
     * @param price Product price
     * @param quantity Quantity to add
     * @return true if item was added successfully
     */
    public boolean addItem(String productId, String productName, BigDecimal price, int quantity) {
        if (productId == null || productId.trim().isEmpty() || 
            productName == null || productName.trim().isEmpty() || 
            price == null || price.compareTo(BigDecimal.ZERO) <= 0 || 
            quantity <= 0) {
            return false;
        }
        
        if (items.size() >= config.getMaxItems()) {
            return false; // Cart is full
        }
        
        CartItem existingItem = items.get(productId);
        if (existingItem != null) {
            // Update existing item
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > config.getMaxQuantityPerItem()) {
                return false; // Exceeds max quantity per item
            }
            
            CartItem updatedItem = new CartItem(
                existingItem.getProductId(),
                existingItem.getProductName(),
                existingItem.getPrice(), // Price cannot be changed
                newQuantity
            );
            items.put(productId, updatedItem);
        } else {
            // Add new item
            if (quantity > config.getMaxQuantityPerItem()) {
                return false; // Exceeds max quantity per item
            }
            
            CartItem newItem = new CartItem(productId, productName, price, quantity);
            items.put(productId, newItem);
        }
        
        updateLastModifiedTime();
        return true;
    }
    
    /**
     * Remove item from cart
     * @param productId Product ID
     * @param quantity Quantity to remove
     * @return true if item was removed successfully
     */
    public boolean removeItem(String productId, int quantity) {
        if (productId == null || productId.trim().isEmpty() || quantity <= 0) {
            return false;
        }
        
        CartItem existingItem = items.get(productId);
        if (existingItem == null) {
            return false; // Item not in cart
        }
        
        int newQuantity = existingItem.getQuantity() - quantity;
        if (newQuantity <= 0) {
            items.remove(productId);
        } else {
            CartItem updatedItem = new CartItem(
                existingItem.getProductId(),
                existingItem.getProductName(),
                existingItem.getPrice(),
                newQuantity
            );
            items.put(productId, updatedItem);
        }
        
        updateLastModifiedTime();
        return true;
    }
    
    /**
     * Remove all of an item from cart
     * @param productId Product ID
     * @return true if item was removed successfully
     */
    public boolean removeItemCompletely(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return false;
        }
        
        CartItem removed = items.remove(productId);
        if (removed != null) {
            updateLastModifiedTime();
            return true;
        }
        
        return false;
    }
    
    /**
     * Update item quantity
     * @param productId Product ID
     * @param newQuantity New quantity
     * @return true if quantity was updated successfully
     */
    public boolean updateQuantity(String productId, int newQuantity) {
        if (productId == null || productId.trim().isEmpty() || newQuantity < 0) {
            return false;
        }
        
        CartItem existingItem = items.get(productId);
        if (existingItem == null) {
            return false; // Item not in cart
        }
        
        if (newQuantity == 0) {
            items.remove(productId);
        } else {
            if (newQuantity > config.getMaxQuantityPerItem()) {
                return false; // Exceeds max quantity per item
            }
            
            CartItem updatedItem = new CartItem(
                existingItem.getProductId(),
                existingItem.getProductName(),
                existingItem.getPrice(),
                newQuantity
            );
            items.put(productId, updatedItem);
        }
        
        updateLastModifiedTime();
        return true;
    }
    
    /**
     * Clear all items from cart
     * @return true if cart was cleared successfully
     */
    public boolean clearCart() {
        items.clear();
        appliedDiscounts.clear();
        updateLastModifiedTime();
        return true;
    }
    
    /**
     * Apply discount
     * @param discount Discount to apply
     * @return true if discount was applied successfully
     */
    public boolean applyDiscount(Discount discount) {
        if (discount == null || !discount.isValid()) {
            return false;
        }
        
        if (items.isEmpty()) {
            return false; // Cannot apply discount to empty cart
        }
        
        // Check if discount is already applied
        for (Discount existingDiscount : appliedDiscounts) {
            if (existingDiscount.getDiscountCode().equals(discount.getDiscountCode())) {
                return false; // Discount already applied
            }
        }
        
        appliedDiscounts.add(discount);
        updateLastModifiedTime();
        return true;
    }
    
    /**
     * Remove discount
     * @param discountCode Discount code to remove
     * @return true if discount was removed successfully
     */
    public boolean removeDiscount(String discountCode) {
        if (discountCode == null || discountCode.trim().isEmpty()) {
            return false;
        }
        
        boolean removed = appliedDiscounts.removeIf(discount -> 
            discount.getDiscountCode().equals(discountCode));
        
        if (removed) {
            updateLastModifiedTime();
        }
        
        return removed;
    }
    
    /**
     * Get cart items (read-only)
     * @return Unmodifiable list of cart items
     */
    public List<CartItem> getCartItems() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }
    
    /**
     * Get cart item by product ID
     * @param productId Product ID
     * @return Cart item or null if not found
     */
    public CartItem getCartItem(String productId) {
        return items.get(productId);
    }
    
    /**
     * Get total number of items in cart
     * @return Total item count
     */
    public int getTotalItemCount() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    /**
     * Get number of different products in cart
     * @return Number of different products
     */
    public int getProductCount() {
        return items.size();
    }
    
    /**
     * Get subtotal (before discounts)
     * @return Subtotal amount
     */
    public BigDecimal getSubtotal() {
        return items.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get total discount amount
     * @return Total discount amount
     */
    public BigDecimal getTotalDiscount() {
        BigDecimal subtotal = getSubtotal();
        BigDecimal totalDiscount = BigDecimal.ZERO;
        
        for (Discount discount : appliedDiscounts) {
            BigDecimal discountAmount = discount.calculateDiscount(subtotal);
            totalDiscount = totalDiscount.add(discountAmount);
        }
        
        return totalDiscount;
    }
    
    /**
     * Get total amount (after discounts)
     * @return Total amount
     */
    public BigDecimal getTotal() {
        BigDecimal subtotal = getSubtotal();
        BigDecimal totalDiscount = getTotalDiscount();
        return subtotal.subtract(totalDiscount);
    }
    
    /**
     * Get applied discounts (read-only)
     * @return Unmodifiable list of applied discounts
     */
    public List<Discount> getAppliedDiscounts() {
        return Collections.unmodifiableList(appliedDiscounts);
    }
    
    /**
     * Check if cart is empty
     * @return true if cart is empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    /**
     * Check if cart is full
     * @return true if cart is full
     */
    public boolean isFull() {
        return items.size() >= config.getMaxItems();
    }
    
    /**
     * Get cart summary
     * @return Cart summary string
     */
    public String getCartSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Cart ID: ").append(cartId).append("\n");
        summary.append("Created: ").append(createdTime).append("\n");
        summary.append("Last Modified: ").append(lastModifiedTime).append("\n");
        summary.append("Items: ").append(getProductCount()).append(" products, ").append(getTotalItemCount()).append(" total items\n");
        summary.append("Subtotal: $").append(getSubtotal()).append("\n");
        summary.append("Discounts: $").append(getTotalDiscount()).append("\n");
        summary.append("Total: $").append(getTotal()).append("\n");
        
        if (!appliedDiscounts.isEmpty()) {
            summary.append("Applied Discounts:\n");
            for (Discount discount : appliedDiscounts) {
                summary.append("  ").append(discount.getDiscountCode()).append(": ").append(discount.getDescription()).append("\n");
            }
        }
        
        return summary.toString();
    }
    
    /**
     * Update last modified time
     */
    private void updateLastModifiedTime() {
        this.lastModifiedTime = LocalDateTime.now();
    }
    
    // Getters
    public String getCartId() { return cartId; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getLastModifiedTime() { return lastModifiedTime; }
    
    /**
     * Cart item (immutable)
     */
    public static class CartItem {
        private final String productId;
        private final String productName;
        private final BigDecimal price;
        private final int quantity;
        
        public CartItem(String productId, String productName, BigDecimal price, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }
        
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public BigDecimal getPrice() { return price; }
        public int getQuantity() { return quantity; }
        
        public BigDecimal getTotalPrice() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        
        @Override
        public String toString() {
            return String.format("CartItem{id='%s', name='%s', price=%s, quantity=%d, total=%s}", 
                productId, productName, price, quantity, getTotalPrice());
        }
    }
    
    /**
     * Discount class
     */
    public static class Discount {
        private final String discountCode;
        private final String description;
        private final DiscountType type;
        private final BigDecimal value;
        private final BigDecimal minimumAmount;
        private final LocalDateTime expiryDate;
        
        public Discount(String discountCode, String description, DiscountType type, 
                       BigDecimal value, BigDecimal minimumAmount, LocalDateTime expiryDate) {
            this.discountCode = discountCode;
            this.description = description;
            this.type = type;
            this.value = value;
            this.minimumAmount = minimumAmount;
            this.expiryDate = expiryDate;
        }
        
        public String getDiscountCode() { return discountCode; }
        public String getDescription() { return description; }
        public DiscountType getType() { return type; }
        public BigDecimal getValue() { return value; }
        public BigDecimal getMinimumAmount() { return minimumAmount; }
        public LocalDateTime getExpiryDate() { return expiryDate; }
        
        public boolean isValid() {
            return discountCode != null && !discountCode.trim().isEmpty() &&
                   description != null && !description.trim().isEmpty() &&
                   type != null && value != null && value.compareTo(BigDecimal.ZERO) > 0 &&
                   minimumAmount != null && minimumAmount.compareTo(BigDecimal.ZERO) >= 0 &&
                   (expiryDate == null || expiryDate.isAfter(LocalDateTime.now()));
        }
        
        public BigDecimal calculateDiscount(BigDecimal subtotal) {
            if (!isValid() || subtotal.compareTo(minimumAmount) < 0) {
                return BigDecimal.ZERO;
            }
            
            switch (type) {
                case PERCENTAGE:
                    return subtotal.multiply(value.divide(BigDecimal.valueOf(100)));
                case FIXED_AMOUNT:
                    return value;
                default:
                    return BigDecimal.ZERO;
            }
        }
    }
    
    /**
     * Discount types
     */
    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }
    
    /**
     * Cart configuration
     */
    private static class CartConfig {
        private final int maxItems;
        private final int maxQuantityPerItem;
        
        public CartConfig() {
            this.maxItems = 50;
            this.maxQuantityPerItem = 10;
        }
        
        public int getMaxItems() { return maxItems; }
        public int getMaxQuantityPerItem() { return maxQuantityPerItem; }
    }
    
    @Override
    public String toString() {
        return String.format("ShoppingCart{id='%s', items=%d, total=$%s}", 
            cartId, items.size(), getTotal());
    }
}
