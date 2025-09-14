package abstraction.paymentprocessor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment result with comprehensive status information
 */
public class PaymentResult {
    private String transactionId;
    private String requestId;
    private PaymentStatus status;
    private BigDecimal processedAmount;
    private String currency;
    private String errorMessage;
    private LocalDateTime timestamp;
    private String gatewayReference;
    
    public enum PaymentStatus {
        SUCCESS, FAILED, PENDING, CANCELLED, REQUIRES_ACTION
    }
    
    private PaymentResult(String transactionId, String requestId, PaymentStatus status, 
                         BigDecimal processedAmount, String currency, String errorMessage, 
                         String gatewayReference) {
        this.transactionId = transactionId;
        this.requestId = requestId;
        this.status = status;
        this.processedAmount = processedAmount;
        this.currency = currency;
        this.errorMessage = errorMessage;
        this.gatewayReference = gatewayReference;
        this.timestamp = LocalDateTime.now();
    }
    
    public static PaymentResult success(String transactionId, String requestId, 
                                      BigDecimal amount, String currency, String gatewayRef) {
        return new PaymentResult(transactionId, requestId, PaymentStatus.SUCCESS, 
                               amount, currency, null, gatewayRef);
    }
    
    public static PaymentResult failure(String requestId, String errorMessage) {
        return new PaymentResult(null, requestId, PaymentStatus.FAILED, 
                               null, null, errorMessage, null);
    }
    
    public static PaymentResult pending(String transactionId, String requestId) {
        return new PaymentResult(transactionId, requestId, PaymentStatus.PENDING, 
                               null, null, null, null);
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getRequestId() { return requestId; }
    public PaymentStatus getStatus() { return status; }
    public BigDecimal getProcessedAmount() { return processedAmount; }
    public String getCurrency() { return currency; }
    public String getErrorMessage() { return errorMessage; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getGatewayReference() { return gatewayReference; }
    
    public boolean isSuccess() { return status == PaymentStatus.SUCCESS; }
}
