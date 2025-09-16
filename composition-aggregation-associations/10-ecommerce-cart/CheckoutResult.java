package composition.ecommerce;

/**
 * Checkout Result data class
 */
public class CheckoutResult {
    private final boolean success;
    private final String message;
    private final CartSummary cartSummary;
    private final PaymentResult paymentResult;
    private final Order order;
    
    public CheckoutResult(boolean success, String message, CartSummary cartSummary, PaymentResult paymentResult) {
        this(success, message, cartSummary, paymentResult, null);
    }
    
    public CheckoutResult(boolean success, String message, CartSummary cartSummary, 
                         PaymentResult paymentResult, Order order) {
        this.success = success;
        this.message = message;
        this.cartSummary = cartSummary;
        this.paymentResult = paymentResult;
        this.order = order;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public CartSummary getCartSummary() { return cartSummary; }
    public PaymentResult getPaymentResult() { return paymentResult; }
    public Order getOrder() { return order; }
}
