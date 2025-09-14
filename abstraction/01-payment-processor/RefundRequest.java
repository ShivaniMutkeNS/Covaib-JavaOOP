package abstraction.paymentprocessor;

import java.math.BigDecimal;

/**
 * Refund request with partial refund support
 */
public class RefundRequest {
    private String refundId;
    private String originalTransactionId;
    private BigDecimal refundAmount;
    private String reason;
    private boolean isPartialRefund;
    
    public RefundRequest(String refundId, String originalTransactionId, 
                        BigDecimal refundAmount, String reason) {
        this.refundId = refundId;
        this.originalTransactionId = originalTransactionId;
        this.refundAmount = refundAmount;
        this.reason = reason;
        this.isPartialRefund = true; // Will be determined by processor
    }
    
    // Getters
    public String getRefundId() { return refundId; }
    public String getOriginalTransactionId() { return originalTransactionId; }
    public BigDecimal getRefundAmount() { return refundAmount; }
    public String getReason() { return reason; }
    public boolean isPartialRefund() { return isPartialRefund; }
    
    public void setPartialRefund(boolean partialRefund) { this.isPartialRefund = partialRefund; }
}
