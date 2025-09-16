package composition.ecommerce;

import java.util.List;

/**
 * Cart Snapshot data class for cart persistence
 */
public class CartSnapshot {
    private final String cartId;
    private final String customerId;
    private final List<CartItem> items;
    private final CartState state;
    private final long createdAt;
    private final long lastModified;
    
    public CartSnapshot(String cartId, String customerId, List<CartItem> items, 
                       CartState state, long createdAt, long lastModified) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.items = items;
        this.state = state;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
    
    public String getCartId() { return cartId; }
    public String getCustomerId() { return customerId; }
    public List<CartItem> getItems() { return items; }
    public CartState getState() { return state; }
    public long getCreatedAt() { return createdAt; }
    public long getLastModified() { return lastModified; }
}
