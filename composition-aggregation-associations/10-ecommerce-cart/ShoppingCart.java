package composition.ecommerce;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MAANG-Level E-commerce Shopping Cart System using Composition
 * Demonstrates: Strategy Pattern, Observer Pattern, State Pattern, Command Pattern
 */
public class ShoppingCart {
    private final String cartId;
    private final String customerId;
    private final Map<String, CartItem> items;
    private final List<CartEventListener> listeners;
    private PricingStrategy pricingStrategy;
    private InventoryManager inventoryManager;
    private PaymentProcessor paymentProcessor;
    private ShippingCalculator shippingCalculator;
    private final CartMetrics metrics;
    private CartState state;
    private final long createdAt;
    private long lastModified;
    
    public ShoppingCart(String cartId, String customerId) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.items = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.pricingStrategy = new StandardPricingStrategy();
        this.inventoryManager = new DefaultInventoryManager();
        this.paymentProcessor = new DefaultPaymentProcessor();
        this.shippingCalculator = new StandardShippingCalculator();
        this.metrics = new CartMetrics();
        this.state = CartState.ACTIVE;
        this.createdAt = System.currentTimeMillis();
        this.lastModified = createdAt;
    }
    
    // Runtime strategy swapping for pricing
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        notifyListeners("Pricing strategy updated");
        recalculateCart();
    }
    
    public void setInventoryManager(InventoryManager manager) {
        this.inventoryManager = manager;
        notifyListeners("Inventory manager updated");
    }
    
    public void setPaymentProcessor(PaymentProcessor processor) {
        this.paymentProcessor = processor;
        notifyListeners("Payment processor updated");
    }
    
    public void setShippingCalculator(ShippingCalculator calculator) {
        this.shippingCalculator = calculator;
        notifyListeners("Shipping calculator updated");
        recalculateCart();
    }
    
    // Core cart operations
    public CartOperationResult addItem(Product product, int quantity) {
        if (state != CartState.ACTIVE) {
            return new CartOperationResult(false, "Cart is not active", null);
        }
        
        // Check inventory availability
        if (!inventoryManager.isAvailable(product.getProductId(), quantity)) {
            return new CartOperationResult(false, "Insufficient inventory", null);
        }
        
        String productId = product.getProductId();
        CartItem existingItem = items.get(productId);
        
        if (existingItem != null) {
            // Update existing item
            int newQuantity = existingItem.getQuantity() + quantity;
            if (!inventoryManager.isAvailable(productId, newQuantity)) {
                return new CartOperationResult(false, "Cannot add more items - insufficient inventory", null);
            }
            
            existingItem.setQuantity(newQuantity);
            existingItem.setLastModified(System.currentTimeMillis());
        } else {
            // Add new item
            CartItem newItem = new CartItem(product, quantity);
            items.put(productId, newItem);
        }
        
        updateTimestamp();
        metrics.incrementItemsAdded();
        notifyListeners("Item added: " + product.getName() + " (qty: " + quantity + ")");
        recalculateCart();
        
        return new CartOperationResult(true, "Item added successfully", items.get(productId));
    }
    
    public CartOperationResult removeItem(String productId) {
        if (state != CartState.ACTIVE) {
            return new CartOperationResult(false, "Cart is not active", null);
        }
        
        CartItem removedItem = items.remove(productId);
        if (removedItem == null) {
            return new CartOperationResult(false, "Item not found in cart", null);
        }
        
        updateTimestamp();
        metrics.incrementItemsRemoved();
        notifyListeners("Item removed: " + removedItem.getProduct().getName());
        recalculateCart();
        
        return new CartOperationResult(true, "Item removed successfully", removedItem);
    }
    
    public CartOperationResult updateQuantity(String productId, int newQuantity) {
        if (state != CartState.ACTIVE) {
            return new CartOperationResult(false, "Cart is not active", null);
        }
        
        if (newQuantity <= 0) {
            return removeItem(productId);
        }
        
        CartItem item = items.get(productId);
        if (item == null) {
            return new CartOperationResult(false, "Item not found in cart", null);
        }
        
        if (!inventoryManager.isAvailable(productId, newQuantity)) {
            return new CartOperationResult(false, "Insufficient inventory for requested quantity", null);
        }
        
        int oldQuantity = item.getQuantity();
        item.setQuantity(newQuantity);
        item.setLastModified(System.currentTimeMillis());
        
        updateTimestamp();
        notifyListeners("Quantity updated: " + item.getProduct().getName() + 
                       " (" + oldQuantity + " → " + newQuantity + ")");
        recalculateCart();
        
        return new CartOperationResult(true, "Quantity updated successfully", item);
    }
    
    public void clearCart() {
        if (state != CartState.ACTIVE) {
            return;
        }
        
        int itemCount = items.size();
        items.clear();
        updateTimestamp();
        metrics.incrementCartClears();
        notifyListeners("Cart cleared (" + itemCount + " items removed)");
        recalculateCart();
    }
    
    // Cart calculations
    public CartSummary getCartSummary() {
        double subtotal = calculateSubtotal();
        double discount = pricingStrategy.calculateDiscount(this, subtotal);
        double tax = pricingStrategy.calculateTax(this, subtotal - discount);
        double shipping = shippingCalculator.calculateShipping(this);
        double total = subtotal - discount + tax + shipping;
        
        return new CartSummary(subtotal, discount, tax, shipping, total, items.size(), getTotalQuantity());
    }
    
    private double calculateSubtotal() {
        return items.values().stream()
                   .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                   .sum();
    }
    
    private int getTotalQuantity() {
        return items.values().stream()
                   .mapToInt(CartItem::getQuantity)
                   .sum();
    }
    
    private void recalculateCart() {
        // Trigger recalculation and notify listeners
        CartSummary summary = getCartSummary();
        notifyListeners("Cart recalculated - Total: $" + String.format("%.2f", summary.getTotal()));
    }
    
    // Checkout process
    public CheckoutResult initiateCheckout(ShippingAddress shippingAddress, PaymentMethod paymentMethod) {
        if (state != CartState.ACTIVE) {
            return new CheckoutResult(false, "Cart is not in active state", null, null);
        }
        
        if (items.isEmpty()) {
            return new CheckoutResult(false, "Cart is empty", null, null);
        }
        
        // Validate inventory before checkout
        for (CartItem item : items.values()) {
            if (!inventoryManager.isAvailable(item.getProduct().getProductId(), item.getQuantity())) {
                return new CheckoutResult(false, 
                    "Item no longer available: " + item.getProduct().getName(), null, null);
            }
        }
        
        state = CartState.CHECKOUT_IN_PROGRESS;
        notifyListeners("Checkout initiated");
        
        try {
            // Reserve inventory
            for (CartItem item : items.values()) {
                inventoryManager.reserveInventory(item.getProduct().getProductId(), item.getQuantity());
            }
            
            CartSummary summary = getCartSummary();
            
            // Process payment
            PaymentResult paymentResult = paymentProcessor.processPayment(
                paymentMethod, summary.getTotal(), "Cart: " + cartId);
            
            if (!paymentResult.isSuccess()) {
                // Release reserved inventory
                releaseReservedInventory();
                state = CartState.ACTIVE;
                return new CheckoutResult(false, "Payment failed: " + paymentResult.getMessage(), 
                                        summary, paymentResult);
            }
            
            // Create order
            Order order = createOrder(shippingAddress, paymentMethod, summary, paymentResult);
            
            // Update inventory
            for (CartItem item : items.values()) {
                inventoryManager.updateInventory(item.getProduct().getProductId(), -item.getQuantity());
            }
            
            state = CartState.CHECKED_OUT;
            metrics.incrementSuccessfulCheckouts();
            notifyListeners("Checkout completed successfully - Order: " + order.getOrderId());
            
            return new CheckoutResult(true, "Checkout successful", summary, paymentResult, order);
            
        } catch (Exception e) {
            releaseReservedInventory();
            state = CartState.ACTIVE;
            return new CheckoutResult(false, "Checkout failed: " + e.getMessage(), null, null);
        }
    }
    
    private void releaseReservedInventory() {
        for (CartItem item : items.values()) {
            inventoryManager.releaseReservation(item.getProduct().getProductId(), item.getQuantity());
        }
    }
    
    private Order createOrder(ShippingAddress shippingAddress, PaymentMethod paymentMethod, 
                            CartSummary summary, PaymentResult paymentResult) {
        String orderId = "ORDER_" + System.currentTimeMillis() + "_" + cartId;
        
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : items.values()) {
            orderItems.add(new OrderItem(
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice()
            ));
        }
        
        return new Order(orderId, customerId, orderItems, summary, 
                        shippingAddress, paymentMethod, paymentResult);
    }
    
    // Cart persistence and recovery
    public CartSnapshot createSnapshot() {
        return new CartSnapshot(cartId, customerId, new ArrayList<>(items.values()), 
                              state, createdAt, lastModified);
    }
    
    public void restoreFromSnapshot(CartSnapshot snapshot) {
        if (!cartId.equals(snapshot.getCartId()) || !customerId.equals(snapshot.getCustomerId())) {
            throw new IllegalArgumentException("Snapshot does not match this cart");
        }
        
        items.clear();
        for (CartItem item : snapshot.getItems()) {
            items.put(item.getProduct().getProductId(), item);
        }
        
        state = snapshot.getState();
        lastModified = snapshot.getLastModified();
        
        notifyListeners("Cart restored from snapshot");
        recalculateCart();
    }
    
    // Cart merging (for user login scenarios)
    public void mergeWith(ShoppingCart otherCart) {
        if (state != CartState.ACTIVE || otherCart.state != CartState.ACTIVE) {
            throw new IllegalStateException("Both carts must be active for merging");
        }
        
        for (CartItem otherItem : otherCart.items.values()) {
            String productId = otherItem.getProduct().getProductId();
            CartItem existingItem = items.get(productId);
            
            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + otherItem.getQuantity();
                if (inventoryManager.isAvailable(productId, newQuantity)) {
                    existingItem.setQuantity(newQuantity);
                }
            } else {
                if (inventoryManager.isAvailable(productId, otherItem.getQuantity())) {
                    items.put(productId, new CartItem(otherItem.getProduct(), otherItem.getQuantity()));
                }
            }
        }
        
        updateTimestamp();
        notifyListeners("Cart merged with " + otherCart.cartId);
        recalculateCart();
    }
    
    // Wishlist integration
    public void moveToWishlist(String productId, WishlistManager wishlistManager) {
        CartItem item = items.get(productId);
        if (item != null) {
            wishlistManager.addToWishlist(customerId, item.getProduct());
            removeItem(productId);
            notifyListeners("Item moved to wishlist: " + item.getProduct().getName());
        }
    }
    
    public void addFromWishlist(Product product, int quantity, WishlistManager wishlistManager) {
        CartOperationResult result = addItem(product, quantity);
        if (result.isSuccess()) {
            wishlistManager.removeFromWishlist(customerId, product.getProductId());
            notifyListeners("Item added from wishlist: " + product.getName());
        }
    }
    
    // Event handling
    public void addEventListener(CartEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(CartEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (CartEventListener listener : listeners) {
            listener.onCartEvent(cartId, message, getCartSummary());
        }
    }
    
    private void updateTimestamp() {
        lastModified = System.currentTimeMillis();
    }
    
    // Cart analytics and recommendations
    public List<Product> getRecommendations(RecommendationEngine engine) {
        return engine.getRecommendations(this);
    }
    
    public CartAnalytics getAnalytics() {
        return new CartAnalytics(
            metrics,
            getCartSummary(),
            System.currentTimeMillis() - createdAt,
            getMostExpensiveItem(),
            getCheapestItem(),
            getAverageItemPrice()
        );
    }
    
    private CartItem getMostExpensiveItem() {
        return items.values().stream()
                   .max(Comparator.comparing(item -> item.getProduct().getPrice()))
                   .orElse(null);
    }
    
    private CartItem getCheapestItem() {
        return items.values().stream()
                   .min(Comparator.comparing(item -> item.getProduct().getPrice()))
                   .orElse(null);
    }
    
    private double getAverageItemPrice() {
        return items.values().stream()
                   .mapToDouble(item -> item.getProduct().getPrice())
                   .average()
                   .orElse(0.0);
    }
    
    public void displayCartContents() {
        System.out.println("\n🛒 Shopping Cart: " + cartId);
        System.out.println("Customer: " + customerId);
        System.out.println("State: " + state);
        System.out.println("Items: " + items.size());
        
        if (items.isEmpty()) {
            System.out.println("   (Cart is empty)");
        } else {
            for (CartItem item : items.values()) {
                System.out.printf("   📦 %s - $%.2f x %d = $%.2f\n",
                    item.getProduct().getName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    item.getProduct().getPrice() * item.getQuantity());
            }
        }
        
        CartSummary summary = getCartSummary();
        System.out.printf("\n💰 Summary:\n");
        System.out.printf("   Subtotal: $%.2f\n", summary.getSubtotal());
        System.out.printf("   Discount: -$%.2f\n", summary.getDiscount());
        System.out.printf("   Tax: $%.2f\n", summary.getTax());
        System.out.printf("   Shipping: $%.2f\n", summary.getShipping());
        System.out.printf("   TOTAL: $%.2f\n", summary.getTotal());
    }
    
    // Getters
    public String getCartId() { return cartId; }
    public String getCustomerId() { return customerId; }
    public Map<String, CartItem> getItems() { return new HashMap<>(items); }
    public CartState getState() { return state; }
    public long getCreatedAt() { return createdAt; }
    public long getLastModified() { return lastModified; }
    public CartMetrics getMetrics() { return metrics; }
    public boolean isEmpty() { return items.isEmpty(); }
    public int getItemCount() { return items.size(); }
}
