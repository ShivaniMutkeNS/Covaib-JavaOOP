package composition.reconciliation;

import java.math.BigDecimal;
import java.util.*;

/**
 * Payment record representing internal transaction data
 */
public class PaymentRecord {
    private final String transactionId;
    private final String orderId;
    private final BigDecimal amount;
    private final String currency;
    private final PaymentMethod paymentMethod;
    private final PaymentStatus status;
    private final long transactionDate;
    private final String customerId;
    private final String merchantId;
    private final Map<String, Object> metadata;
    private final RecordSource source;
    private final long createdAt;
    
    public PaymentRecord(String transactionId, String orderId, BigDecimal amount, 
                        String currency, PaymentMethod paymentMethod, PaymentStatus status,
                        long transactionDate, String customerId, String merchantId) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionDate = transactionDate;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.metadata = new HashMap<>();
        this.source = RecordSource.INTERNAL_SYSTEM;
        this.createdAt = System.currentTimeMillis();
    }
    
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    public boolean hasMetadata(String key) {
        return metadata.containsKey(key);
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getStatus() { return status; }
    public long getTransactionDate() { return transactionDate; }
    public String getCustomerId() { return customerId; }
    public String getMerchantId() { return merchantId; }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public RecordSource getSource() { return source; }
    public long getCreatedAt() { return createdAt; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PaymentRecord that = (PaymentRecord) obj;
        return Objects.equals(transactionId, that.transactionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
    
    @Override
    public String toString() {
        return String.format("PaymentRecord[%s]: %s %s (%s) - %s", 
                           transactionId, amount, currency, paymentMethod.getDisplayName(), 
                           status.getDisplayName());
    }
}
