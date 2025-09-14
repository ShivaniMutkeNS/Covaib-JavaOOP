package abstraction.paymentprocessor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Payment request with comprehensive details
 */
public class PaymentRequest {
    private String requestId;
    private BigDecimal amount;
    private String currency;
    private String merchantId;
    private String customerId;
    private Map<String, Object> metadata;
    private PaymentMethod paymentMethod;
    
    public PaymentRequest(String requestId, BigDecimal amount, String currency, 
                         String merchantId, String customerId, PaymentMethod paymentMethod) {
        this.requestId = requestId;
        this.amount = amount;
        this.currency = currency;
        this.merchantId = merchantId;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and setters
    public String getRequestId() { return requestId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getMerchantId() { return merchantId; }
    public String getCustomerId() { return customerId; }
    public Map<String, Object> getMetadata() { return metadata; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}

class PaymentMethod {
    private String type; // CARD, WALLET, BANK_TRANSFER, CRYPTO
    private Map<String, String> details;
    
    public PaymentMethod(String type, Map<String, String> details) {
        this.type = type;
        this.details = details;
    }
    
    public String getType() { return type; }
    public Map<String, String> getDetails() { return details; }
}
