/**
 * Abstract base class for all payment methods
 * Demonstrates abstract base + real polymorphism
 * 
 * @author Expert Developer
 * @version 1.0
 */
public abstract class Payment {
    protected String paymentId;
    protected String customerId;
    protected double amount;
    protected String currency;
    protected String status;
    protected String timestamp;
    protected String description;
    protected String paymentMethod;
    protected double processingFee;
    protected String transactionId;
    protected boolean isProcessed;
    protected String failureReason;
    protected int retryCount;
    protected int maxRetries;
    protected String merchantId;
    protected String orderId;
    
    /**
     * Constructor for Payment
     * @param paymentId Unique payment identifier
     * @param customerId Customer identifier
     * @param amount Payment amount
     * @param currency Currency code
     * @param description Payment description
     * @param paymentMethod Payment method type
     * @param merchantId Merchant identifier
     * @param orderId Order identifier
     */
    public Payment(String paymentId, String customerId, double amount, String currency, 
                   String description, String paymentMethod, String merchantId, String orderId) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.merchantId = merchantId;
        this.orderId = orderId;
        this.status = "Pending";
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.processingFee = 0.0;
        this.transactionId = "";
        this.isProcessed = false;
        this.failureReason = "";
        this.retryCount = 0;
        this.maxRetries = 3;
    }
    
    /**
     * Abstract method to process payment
     * Each payment method has different processing logic
     * @return True if payment processed successfully
     */
    public abstract boolean processPayment();
    
    /**
     * Abstract method to calculate processing fee
     * Each payment method has different fee structure
     * @return The calculated processing fee
     */
    public abstract double calculateProcessingFee();
    
    /**
     * Abstract method to get payment features
     * Each payment method has different features
     * @return String description of payment features
     */
    public abstract String getPaymentFeatures();
    
    /**
     * Abstract method to validate payment
     * Each payment method has different validation rules
     * @return True if payment is valid
     */
    public abstract boolean validatePayment();
    
    /**
     * Concrete method to retry payment
     * @return True if retry is successful
     */
    public boolean retryPayment() {
        if (retryCount >= maxRetries) {
            System.out.println("Maximum retry attempts reached for payment " + paymentId);
            return false;
        }
        
        retryCount++;
        System.out.println("Retrying payment " + paymentId + " (Attempt " + retryCount + "/" + maxRetries + ")");
        
        return processPayment();
    }
    
    /**
     * Concrete method to get payment information
     * @return String with payment details
     */
    public String getPaymentInfo() {
        return String.format("Payment: %s, Method: %s, Amount: %s%.2f, Status: %s, Fee: %s%.2f", 
                           paymentId, paymentMethod, currency, amount, status, currency, processingFee);
    }
    
    /**
     * Concrete method to update payment status
     * @param newStatus New payment status
     * @param transactionId Transaction ID if successful
     * @param failureReason Failure reason if failed
     * @return True if status updated successfully
     */
    public boolean updateStatus(String newStatus, String transactionId, String failureReason) {
        this.status = newStatus;
        this.transactionId = transactionId;
        this.failureReason = failureReason;
        this.isProcessed = !newStatus.equals("Failed");
        
        System.out.println("Payment " + paymentId + " status updated to: " + newStatus);
        if (!transactionId.isEmpty()) {
            System.out.println("Transaction ID: " + transactionId);
        }
        if (!failureReason.isEmpty()) {
            System.out.println("Failure reason: " + failureReason);
        }
        
        return true;
    }
    
    /**
     * Concrete method to check if payment is successful
     * @return True if payment is successful
     */
    public boolean isSuccessful() {
        return status.equals("Completed") && isProcessed;
    }
    
    /**
     * Concrete method to check if payment is failed
     * @return True if payment is failed
     */
    public boolean isFailed() {
        return status.equals("Failed") && !isProcessed;
    }
    
    /**
     * Concrete method to check if payment can be retried
     * @return True if payment can be retried
     */
    public boolean canRetry() {
        return isFailed() && retryCount < maxRetries;
    }
    
    /**
     * Concrete method to get payment summary
     * @return String with payment summary
     */
    public String getPaymentSummary() {
        return String.format("Payment Summary: %s %s%.2f (%s) - %s", 
                           paymentMethod, currency, amount, status, 
                           isProcessed ? "Processed" : "Not Processed");
    }
    
    /**
     * Getter for payment ID
     * @return The payment ID
     */
    public String getPaymentId() {
        return paymentId;
    }
    
    /**
     * Getter for customer ID
     * @return The customer ID
     */
    public String getCustomerId() {
        return customerId;
    }
    
    /**
     * Getter for amount
     * @return The payment amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Getter for currency
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }
    
    /**
     * Getter for status
     * @return The payment status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Getter for timestamp
     * @return The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
    
    /**
     * Getter for description
     * @return The description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Getter for payment method
     * @return The payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    /**
     * Getter for processing fee
     * @return The processing fee
     */
    public double getProcessingFee() {
        return processingFee;
    }
    
    /**
     * Getter for transaction ID
     * @return The transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }
    
    /**
     * Getter for processed status
     * @return True if payment is processed
     */
    public boolean isProcessed() {
        return isProcessed;
    }
    
    /**
     * Getter for failure reason
     * @return The failure reason
     */
    public String getFailureReason() {
        return failureReason;
    }
    
    /**
     * Getter for retry count
     * @return The retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Getter for max retries
     * @return The maximum retries
     */
    public int getMaxRetries() {
        return maxRetries;
    }
    
    /**
     * Getter for merchant ID
     * @return The merchant ID
     */
    public String getMerchantId() {
        return merchantId;
    }
    
    /**
     * Getter for order ID
     * @return The order ID
     */
    public String getOrderId() {
        return orderId;
    }
    
    /**
     * Override toString method
     * @return String representation of the payment
     */
    @Override
    public String toString() {
        return getPaymentInfo();
    }
}
