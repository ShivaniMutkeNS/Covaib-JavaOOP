package composition.order;

/**
 * Order Item data class
 */
public class OrderItem {
    private final String productId;
    private final String name;
    private final double price;
    private final int quantity;
    
    public OrderItem(String productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
    public double getTotalPrice() {
        return price * quantity;
    }
    
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
