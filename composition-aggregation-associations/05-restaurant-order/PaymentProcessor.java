package composition.restaurant;

/**
 * Payment Processor Strategy Interface
 */
public interface PaymentProcessor {
    PaymentResult processPayment(double amount, String orderId);
    String getType();
    boolean isAvailable();
}

/**
 * Payment Result data class
 */
class PaymentResult {
    private final boolean success;
    private final String errorMessage;
    private final String transactionId;
    
    public PaymentResult(boolean success, String errorMessage, String transactionId) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.transactionId = transactionId;
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public String getTransactionId() { return transactionId; }
}
