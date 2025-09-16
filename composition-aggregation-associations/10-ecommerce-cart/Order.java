package composition.ecommerce;

import java.util.List;

/**
 * Order data class for completed purchases
 */
public class Order {
    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final CartSummary summary;
    private final ShippingAddress shippingAddress;
    private final PaymentMethod paymentMethod;
    private final PaymentResult paymentResult;
    private final long createdAt;
    
    public Order(String orderId, String customerId, List<OrderItem> items, CartSummary summary,
                ShippingAddress shippingAddress, PaymentMethod paymentMethod, PaymentResult paymentResult) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.summary = summary;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.paymentResult = paymentResult;
        this.createdAt = System.currentTimeMillis();
    }
    
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }
    public CartSummary getSummary() { return summary; }
    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public PaymentResult getPaymentResult() { return paymentResult; }
    public long getCreatedAt() { return createdAt; }
}
