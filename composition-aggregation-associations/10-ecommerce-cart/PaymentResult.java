package composition.ecommerce;

/**
 * Payment Result data class
 */
public class PaymentResult {
    private final boolean success;
    private final String message;
    private final String transactionId;
    private final double amount;
    
    public PaymentResult(boolean success, String message, String transactionId, double amount) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.amount = amount;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
}
