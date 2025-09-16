package composition.order;

/**
 * Payment Result data class
 */
public class PaymentResult {
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
