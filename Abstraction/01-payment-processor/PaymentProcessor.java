package abstraction.paymentprocessor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract Payment Processor with enterprise-level complexity
 * Handles multi-step authentication, async processing, and error recovery
 */
public abstract class PaymentProcessor {
    
    protected String processorId;
    protected boolean isActive;
    protected Map<String, Object> configuration;
    
    public PaymentProcessor(String processorId, Map<String, Object> config) {
        this.processorId = processorId;
        this.configuration = config;
        this.isActive = true;
    }
    
    /**
     * Abstract authentication method - some processors need multi-step auth
     * @param credentials Authentication credentials
     * @return AuthenticationResult with session info
     * @throws AuthenticationException if auth fails
     */
    public abstract AuthenticationResult authenticate(AuthCredentials credentials) 
            throws AuthenticationException;
    
    /**
     * Process payment with async support and retry mechanism
     * @param paymentRequest Payment details
     * @return CompletableFuture for async processing
     * @throws PaymentProcessingException if processing fails
     */
    public abstract CompletableFuture<PaymentResult> processPayment(PaymentRequest paymentRequest) 
            throws PaymentProcessingException;
    
    /**
     * Refund processing with partial refund support
     * @param refundRequest Refund details
     * @return RefundResult with status and reference
     * @throws RefundException if refund fails
     */
    public abstract RefundResult refund(RefundRequest refundRequest) 
            throws RefundException;
    
    /**
     * Template method for payment workflow - demonstrates Template Pattern
     */
    public final PaymentResult processPaymentWorkflow(PaymentRequest request, AuthCredentials credentials) {
        try {
            // Step 1: Authenticate
            AuthenticationResult authResult = authenticate(credentials);
            if (!authResult.isSuccess()) {
                throw new PaymentProcessingException("Authentication failed: " + authResult.getErrorMessage());
            }
            
            // Step 2: Validate request
            validatePaymentRequest(request);
            
            // Step 3: Pre-process hooks (can be overridden)
            preProcessHook(request);
            
            // Step 4: Process payment asynchronously
            CompletableFuture<PaymentResult> futureResult = processPayment(request);
            PaymentResult result = futureResult.get(); // Block for demo, in real world use callbacks
            
            // Step 5: Post-process hooks
            postProcessHook(result);
            
            return result;
            
        } catch (Exception e) {
            return handlePaymentError(e, request);
        }
    }
    
    // Hook methods that can be overridden by concrete implementations
    protected void preProcessHook(PaymentRequest request) {
        // Default implementation - can be overridden
        System.out.println("Pre-processing payment for processor: " + processorId);
    }
    
    protected void postProcessHook(PaymentResult result) {
        // Default implementation - can be overridden
        System.out.println("Post-processing completed for transaction: " + result.getTransactionId());
    }
    
    // Common validation logic
    private void validatePaymentRequest(PaymentRequest request) throws PaymentProcessingException {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentProcessingException("Invalid amount: " + request.getAmount());
        }
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new PaymentProcessingException("Currency is required");
        }
    }
    
    // Error handling template
    private PaymentResult handlePaymentError(Exception e, PaymentRequest request) {
        System.err.println("Payment processing failed for processor " + processorId + ": " + e.getMessage());
        return PaymentResult.failure(request.getRequestId(), e.getMessage());
    }
    
    // Getters and utility methods
    public String getProcessorId() { return processorId; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}
