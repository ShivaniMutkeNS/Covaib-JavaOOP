/**
 * Abstract base class for all authentication providers
 * Defines core authentication operations that all providers must implement
 */
public abstract class AuthenticationProvider {
    protected AuthenticationType authenticationType;
    protected String providerName;
    protected boolean isEnabled;
    protected int maxRetryAttempts;
    protected int timeoutSeconds;
    
    public AuthenticationProvider(AuthenticationType authenticationType, String providerName) {
        this.authenticationType = authenticationType;
        this.providerName = providerName;
        this.isEnabled = true;
        this.maxRetryAttempts = 3;
        this.timeoutSeconds = 30;
    }
    
    // Abstract methods that must be implemented by concrete providers
    public abstract AuthenticationSession authenticate(String identifier, String credential, String ipAddress, String userAgent);
    public abstract boolean validateCredentials(String identifier, String credential);
    public abstract void configure(String configurationData);
    public abstract boolean isHealthy();
    
    // Concrete methods with default implementation
    public boolean preAuthenticationCheck(String identifier) {
        if (!isEnabled) {
            System.out.println("❌ Authentication provider is disabled: " + providerName);
            return false;
        }
        
        if (identifier == null || identifier.trim().isEmpty()) {
            System.out.println("❌ Invalid identifier provided");
            return false;
        }
        
        return true;
    }
    
    public void logAuthenticationAttempt(String identifier, boolean success, String ipAddress) {
        String status = success ? "SUCCESS" : "FAILED";
        String logMessage = String.format("[%s] %s authentication %s for %s from %s", 
            java.time.LocalDateTime.now(), providerName, status, identifier, ipAddress);
        System.out.println("📝 " + logMessage);
    }
    
    public AuthenticationSession createSession(User user, String ipAddress, String userAgent) {
        if (user == null) {
            System.out.println("❌ Cannot create session for null user");
            return null;
        }
        
        AuthenticationSession session = new AuthenticationSession(user, authenticationType, ipAddress, userAgent);
        System.out.println("✅ Session created: " + session.getSessionId());
        return session;
    }
    
    protected boolean isValidCredentialFormat(String credential) {
        return credential != null && credential.length() >= 4;
    }
    
    protected void simulateNetworkDelay() {
        try {
            Thread.sleep(100 + (int)(Math.random() * 200)); // 100-300ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Getters and Setters
    public AuthenticationType getAuthenticationType() { return authenticationType; }
    public String getProviderName() { return providerName; }
    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { this.isEnabled = enabled; }
    public int getMaxRetryAttempts() { return maxRetryAttempts; }
    public void setMaxRetryAttempts(int maxRetryAttempts) { this.maxRetryAttempts = maxRetryAttempts; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", 
            providerName, 
            authenticationType.getDisplayName(),
            isEnabled ? "Enabled" : "Disabled");
    }
    
    public String getProviderInfo() {
        return String.format(
            "🔐 Authentication Provider:\n" +
            "Name: %s\n" +
            "Type: %s\n" +
            "Security Level: %s\n" +
            "Status: %s\n" +
            "Max Retries: %d\n" +
            "Timeout: %d seconds\n" +
            "Health: %s",
            providerName,
            authenticationType.getDisplayName(),
            authenticationType.getSecurityLevelDescription(),
            isEnabled ? "Enabled" : "Disabled",
            maxRetryAttempts,
            timeoutSeconds,
            isHealthy() ? "Healthy" : "Unhealthy"
        );
    }
}
