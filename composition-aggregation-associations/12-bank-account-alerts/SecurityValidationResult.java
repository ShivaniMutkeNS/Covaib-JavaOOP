package composition.banking;

/**
 * Security Validation Result data class
 */
public class SecurityValidationResult {
    private final boolean valid;
    private final String reason;
    
    public SecurityValidationResult(boolean valid, String reason) {
        this.valid = valid;
        this.reason = reason;
    }
    
    public boolean isValid() { return valid; }
    public String getReason() { return reason; }
}
