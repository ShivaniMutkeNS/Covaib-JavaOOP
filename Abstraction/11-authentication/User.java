import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the authentication system
 */
public class User {
    private String userId;
    private String username;
    private String email;
    private String hashedPassword;
    private UserRole role;
    private boolean isActive;
    private boolean isLocked;
    private int failedLoginAttempts;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdTime;
    private LocalDateTime lastPasswordChange;
    private List<String> loginHistory;
    private boolean mfaEnabled;
    private String mfaSecret;
    
    public User(String userId, String username, String email, String password, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashPassword(password);
        this.role = role;
        this.isActive = true;
        this.isLocked = false;
        this.failedLoginAttempts = 0;
        this.createdTime = LocalDateTime.now();
        this.lastPasswordChange = LocalDateTime.now();
        this.loginHistory = new ArrayList<>();
        this.mfaEnabled = false;
        this.mfaSecret = null;
    }
    
    private String hashPassword(String password) {
        // Simple hash simulation (in real implementation, use bcrypt or similar)
        return "HASH_" + password.hashCode() + "_" + System.currentTimeMillis();
    }
    
    public boolean verifyPassword(String password) {
        String hashedInput = hashPassword(password);
        // In real implementation, use proper password verification
        return this.hashedPassword.contains(String.valueOf(password.hashCode()));
    }
    
    public void recordLoginAttempt(boolean successful, String ipAddress) {
        String logEntry = LocalDateTime.now() + " - " + 
                         (successful ? "SUCCESS" : "FAILED") + 
                         " from " + ipAddress;
        loginHistory.add(logEntry);
        
        if (successful) {
            this.lastLoginTime = LocalDateTime.now();
            this.failedLoginAttempts = 0;
        } else {
            this.failedLoginAttempts++;
            if (failedLoginAttempts >= 5) {
                this.isLocked = true;
                System.out.println("🔒 Account locked due to multiple failed login attempts");
            }
        }
        
        // Keep only last 10 login attempts
        if (loginHistory.size() > 10) {
            loginHistory.remove(0);
        }
    }
    
    public void unlockAccount() {
        this.isLocked = false;
        this.failedLoginAttempts = 0;
        System.out.println("🔓 Account unlocked");
    }
    
    public void changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            this.hashedPassword = hashPassword(newPassword);
            this.lastPasswordChange = LocalDateTime.now();
            System.out.println("✅ Password changed successfully");
        } else {
            System.out.println("❌ Current password verification failed");
        }
    }
    
    public void enableMFA(String secret) {
        this.mfaEnabled = true;
        this.mfaSecret = secret;
        System.out.println("🔐 Multi-factor authentication enabled");
    }
    
    public void disableMFA() {
        this.mfaEnabled = false;
        this.mfaSecret = null;
        System.out.println("🔓 Multi-factor authentication disabled");
    }
    
    public boolean verifyMFA(String token) {
        if (!mfaEnabled) return true;
        
        // Simple MFA simulation (in real implementation, use TOTP)
        String expectedToken = String.valueOf((System.currentTimeMillis() / 30000) % 1000000);
        return token.equals(expectedToken) || token.equals("123456"); // Allow test token
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
    public boolean isActive() { return isActive; }
    public boolean isLocked() { return isLocked; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public LocalDateTime getLastPasswordChange() { return lastPasswordChange; }
    public List<String> getLoginHistory() { return new ArrayList<>(loginHistory); }
    public boolean isMfaEnabled() { return mfaEnabled; }
    
    // Setters
    public void setActive(boolean active) { this.isActive = active; }
    public void setRole(UserRole role) { this.role = role; }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', role=%s, active=%s, locked=%s}", 
            userId, username, role.getDisplayName(), isActive, isLocked);
    }
    
    public String getDetailedInfo() {
        return String.format(
            "👤 User Profile:\n" +
            "ID: %s\n" +
            "Username: %s\n" +
            "Email: %s\n" +
            "Role: %s\n" +
            "Status: %s\n" +
            "MFA: %s\n" +
            "Failed Attempts: %d\n" +
            "Last Login: %s\n" +
            "Created: %s",
            userId, username, email, role.getDisplayName(),
            isActive ? (isLocked ? "Locked" : "Active") : "Inactive",
            mfaEnabled ? "Enabled" : "Disabled",
            failedLoginAttempts,
            lastLoginTime != null ? lastLoginTime.toString() : "Never",
            createdTime.toString()
        );
    }
}
