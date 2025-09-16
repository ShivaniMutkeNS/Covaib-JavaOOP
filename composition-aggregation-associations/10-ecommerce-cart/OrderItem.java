package composition.ecommerce;

/**
 * Order Item data class for order line items
 */
public class OrderItem {
    private final Product product;
    private final int quantity;
    private final double priceAtTime;
    
    public OrderItem(Product product, int quantity, double priceAtTime) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
    }
    
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPriceAtTime() { return priceAtTime; }
    public double getTotalPrice() { return priceAtTime * quantity; }
}
