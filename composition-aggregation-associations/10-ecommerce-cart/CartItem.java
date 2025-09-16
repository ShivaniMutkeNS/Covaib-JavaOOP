package composition.ecommerce;

/**
 * Cart Item data class
 */
public class CartItem {
    private final Product product;
    private int quantity;
    private final long addedAt;
    private long lastModified;
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.addedAt = System.currentTimeMillis();
        this.lastModified = addedAt;
    }
    
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public long getAddedAt() { return addedAt; }
    public long getLastModified() { return lastModified; }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastModified = System.currentTimeMillis();
    }
    
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
