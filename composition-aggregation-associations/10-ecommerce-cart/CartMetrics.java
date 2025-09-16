package composition.ecommerce;

/**
 * Cart Metrics data class for tracking cart operations
 */
public class CartMetrics {
    private int itemsAdded;
    private int itemsRemoved;
    private int cartClears;
    private int successfulCheckouts;
    private int failedCheckouts;
    
    public CartMetrics() {
        this.itemsAdded = 0;
        this.itemsRemoved = 0;
        this.cartClears = 0;
        this.successfulCheckouts = 0;
        this.failedCheckouts = 0;
    }
    
    public void incrementItemsAdded() { itemsAdded++; }
    public void incrementItemsRemoved() { itemsRemoved++; }
    public void incrementCartClears() { cartClears++; }
    public void incrementSuccessfulCheckouts() { successfulCheckouts++; }
    public void incrementFailedCheckouts() { failedCheckouts++; }
    
    public int getItemsAdded() { return itemsAdded; }
    public int getItemsRemoved() { return itemsRemoved; }
    public int getCartClears() { return cartClears; }
    public int getSuccessfulCheckouts() { return successfulCheckouts; }
    public int getFailedCheckouts() { return failedCheckouts; }
}
