package abstraction.paymentprocessor;

/**
 * Authentication result with session management
 */
public class AuthenticationResult {
    private boolean success;
    private String sessionId;
    private String errorMessage;
    private long expiryTime;
    private String refreshToken;
    
    private AuthenticationResult(boolean success, String sessionId, String errorMessage, 
                               long expiryTime, String refreshToken) {
        this.success = success;
        this.sessionId = sessionId;
        this.errorMessage = errorMessage;
        this.expiryTime = expiryTime;
        this.refreshToken = refreshToken;
    }
    
    public static AuthenticationResult success(String sessionId, long expiryTime, String refreshToken) {
        return new AuthenticationResult(true, sessionId, null, expiryTime, refreshToken);
    }
    
    public static AuthenticationResult failure(String errorMessage) {
        return new AuthenticationResult(false, null, errorMessage, 0, null);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getSessionId() { return sessionId; }
    public String getErrorMessage() { return errorMessage; }
    public long getExpiryTime() { return expiryTime; }
    public String getRefreshToken() { return refreshToken; }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
