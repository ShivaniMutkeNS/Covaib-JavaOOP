package abstraction.paymentprocessor;

/**
 * Custom exceptions for payment processing
 */
public class PaymentProcessingException extends Exception {
    private String errorCode;
    private boolean retryable;
    
    public PaymentProcessingException(String message) {
        super(message);
        this.retryable = false;
    }
    
    public PaymentProcessingException(String message, String errorCode, boolean retryable) {
        super(message);
        this.errorCode = errorCode;
        this.retryable = retryable;
    }
    
    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getErrorCode() { return errorCode; }
    public boolean isRetryable() { return retryable; }
}

class AuthenticationException extends Exception {
    public AuthenticationException(String message) { super(message); }
    public AuthenticationException(String message, Throwable cause) { super(message, cause); }
}

class RefundException extends Exception {
    public RefundException(String message) { super(message); }
    public RefundException(String message, Throwable cause) { super(message, cause); }
}
