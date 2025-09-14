import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an active authentication session
 */
public class AuthenticationSession {
    private String sessionId;
    private User user;
    private AuthenticationType authenticationType;
    private LocalDateTime createdTime;
    private LocalDateTime lastAccessTime;
    private LocalDateTime expiryTime;
    private String ipAddress;
    private String userAgent;
    private boolean isActive;
    private int accessCount;
    
    public AuthenticationSession(User user, AuthenticationType authenticationType, 
                               String ipAddress, String userAgent) {
        this.sessionId = UUID.randomUUID().toString();
        this.user = user;
        this.authenticationType = authenticationType;
        this.createdTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
        this.expiryTime = LocalDateTime.now().plusSeconds(authenticationType.getDefaultSessionDuration());
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.isActive = true;
        this.accessCount = 1;
    }
    
    public boolean isValid() {
        return isActive && LocalDateTime.now().isBefore(expiryTime) && user.isActive() && !user.isLocked();
    }
    
    public void updateAccess() {
        if (isValid()) {
            this.lastAccessTime = LocalDateTime.now();
            this.accessCount++;
        }
    }
    
    public void extendSession(int additionalSeconds) {
        if (isValid()) {
            this.expiryTime = this.expiryTime.plusSeconds(additionalSeconds);
            System.out.println("⏰ Session extended by " + additionalSeconds + " seconds");
        }
    }
    
    public void invalidate() {
        this.isActive = false;
        System.out.println("🚪 Session invalidated: " + sessionId);
    }
    
    public long getTimeUntilExpiry() {
        return java.time.Duration.between(LocalDateTime.now(), expiryTime).getSeconds();
    }
    
    public long getSessionDuration() {
        return java.time.Duration.between(createdTime, LocalDateTime.now()).getSeconds();
    }
    
    // Getters
    public String getSessionId() { return sessionId; }
    public User getUser() { return user; }
    public AuthenticationType getAuthenticationType() { return authenticationType; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getLastAccessTime() { return lastAccessTime; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public boolean isActive() { return isActive; }
    public int getAccessCount() { return accessCount; }
    
    @Override
    public String toString() {
        return String.format("Session{id='%s', user='%s', type=%s, valid=%s, expires=%s}", 
            sessionId.substring(0, 8) + "...", 
            user.getUsername(), 
            authenticationType.getDisplayName(),
            isValid(),
            expiryTime);
    }
    
    public String getDetailedInfo() {
        return String.format(
            "🔐 Session Details:\n" +
            "Session ID: %s\n" +
            "User: %s (%s)\n" +
            "Auth Type: %s\n" +
            "IP Address: %s\n" +
            "User Agent: %s\n" +
            "Created: %s\n" +
            "Last Access: %s\n" +
            "Expires: %s\n" +
            "Duration: %d seconds\n" +
            "Access Count: %d\n" +
            "Status: %s",
            sessionId,
            user.getUsername(), user.getRole().getDisplayName(),
            authenticationType.getDisplayName(),
            ipAddress,
            userAgent,
            createdTime,
            lastAccessTime,
            expiryTime,
            getSessionDuration(),
            accessCount,
            isValid() ? "Valid" : "Invalid"
        );
    }
}
