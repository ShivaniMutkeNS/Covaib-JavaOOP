package abstraction.paymentprocessor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Refund result with comprehensive status tracking
 */
public class RefundResult {
    private String refundTransactionId;
    private String originalTransactionId;
    private RefundStatus status;
    private BigDecimal refundedAmount;
    private String currency;
    private String errorMessage;
    private LocalDateTime timestamp;
    private String gatewayReference;
    
    public enum RefundStatus {
        SUCCESS, FAILED, PENDING, PARTIAL_SUCCESS
    }
    
    private RefundResult(String refundTransactionId, String originalTransactionId, 
                        RefundStatus status, BigDecimal refundedAmount, String currency, 
                        String errorMessage, String gatewayReference) {
        this.refundTransactionId = refundTransactionId;
        this.originalTransactionId = originalTransactionId;
        this.status = status;
        this.refundedAmount = refundedAmount;
        this.currency = currency;
        this.errorMessage = errorMessage;
        this.gatewayReference = gatewayReference;
        this.timestamp = LocalDateTime.now();
    }
    
    public static RefundResult success(String refundTxnId, String originalTxnId, 
                                     BigDecimal amount, String currency, String gatewayRef) {
        return new RefundResult(refundTxnId, originalTxnId, RefundStatus.SUCCESS, 
                              amount, currency, null, gatewayRef);
    }
    
    public static RefundResult failure(String originalTxnId, String errorMessage) {
        return new RefundResult(null, originalTxnId, RefundStatus.FAILED, 
                              null, null, errorMessage, null);
    }
    
    // Getters
    public String getRefundTransactionId() { return refundTransactionId; }
    public String getOriginalTransactionId() { return originalTransactionId; }
    public RefundStatus getStatus() { return status; }
    public BigDecimal getRefundedAmount() { return refundedAmount; }
    public String getCurrency() { return currency; }
    public String getErrorMessage() { return errorMessage; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getGatewayReference() { return gatewayReference; }
    
    public boolean isSuccess() { return status == RefundStatus.SUCCESS; }
}
