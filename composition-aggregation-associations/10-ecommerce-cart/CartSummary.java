package composition.ecommerce;

/**
 * Cart Summary data class
 */
public class CartSummary {
    private final double subtotal;
    private final double discount;
    private final double tax;
    private final double shipping;
    private final double total;
    private final int itemCount;
    private final int totalQuantity;
    
    public CartSummary(double subtotal, double discount, double tax, double shipping, 
                      double total, int itemCount, int totalQuantity) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.tax = tax;
        this.shipping = shipping;
        this.total = total;
        this.itemCount = itemCount;
        this.totalQuantity = totalQuantity;
    }
    
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getTax() { return tax; }
    public double getShipping() { return shipping; }
    public double getTotal() { return total; }
    public int getItemCount() { return itemCount; }
    public int getTotalQuantity() { return totalQuantity; }
}
