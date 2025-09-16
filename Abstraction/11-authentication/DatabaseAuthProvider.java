import java.util.HashMap;
import java.util.Map;

/**
 * Database-based authentication provider
 * Authenticates users against a simulated database
 */
public class DatabaseAuthProvider extends AuthenticationProvider {
    private Map<String, User> userDatabase;
    private String connectionString;
    private boolean isConnected;
    
    public DatabaseAuthProvider() {
        super(AuthenticationType.USERNAME_PASSWORD, "Database Authentication");
        this.userDatabase = new HashMap<>();
        this.connectionString = "jdbc:mysql://localhost:3306/auth_db";
        this.isConnected = true;
        initializeTestUsers();
    }
    
    private void initializeTestUsers() {
        // Create test users
        User admin = new User("1", "admin", "admin@company.com", "admin123", UserRole.ADMIN);
        User user1 = new User("2", "john_doe", "john@company.com", "password123", UserRole.USER);
        User user2 = new User("3", "jane_smith", "jane@company.com", "secure456", UserRole.MODERATOR);
        
        userDatabase.put("admin", admin);
        userDatabase.put("john_doe", user1);
        userDatabase.put("jane_smith", user2);
    }
    
    @Override
    public AuthenticationSession authenticate(String identifier, String credential, String ipAddress, String userAgent) {
        if (!preAuthenticationCheck(identifier)) {
            return null;
        }
        
        simulateNetworkDelay(); // Simulate database query delay
        
        User user = userDatabase.get(identifier);
        if (user == null) {
            logAuthenticationAttempt(identifier, false, ipAddress);
            System.out.println("❌ User not found: " + identifier);
            return null;
        }
        
        if (!user.isActive()) {
            logAuthenticationAttempt(identifier, false, ipAddress);
            System.out.println("❌ User account is inactive: " + identifier);
            return null;
        }
        
        if (user.isLocked()) {
            logAuthenticationAttempt(identifier, false, ipAddress);
            System.out.println("🔒 User account is locked: " + identifier);
            return null;
        }
        
        boolean passwordValid = user.verifyPassword(credential);
        user.recordLoginAttempt(passwordValid, ipAddress);
        
        if (!passwordValid) {
            logAuthenticationAttempt(identifier, false, ipAddress);
            return null;
        }
        
        logAuthenticationAttempt(identifier, true, ipAddress);
        return createSession(user, ipAddress, userAgent);
    }
    
    @Override
    public boolean validateCredentials(String identifier, String credential) {
        if (!isValidCredentialFormat(credential)) {
            return false;
        }
        
        User user = userDatabase.get(identifier);
        return user != null && user.verifyPassword(credential);
    }
    
    @Override
    public void configure(String configurationData) {
        // Parse configuration data (connection string, etc.)
        this.connectionString = configurationData;
        System.out.println("🔧 Database provider configured with: " + connectionString);
        
        // Simulate connection test
        this.isConnected = Math.random() > 0.1; // 90% success rate
        System.out.println("🔌 Database connection: " + (isConnected ? "Connected" : "Failed"));
    }
    
    @Override
    public boolean isHealthy() {
        return isConnected && isEnabled;
    }
    
    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }
    
    public void addUser(User user) {
        userDatabase.put(user.getUsername(), user);
        System.out.println("➕ User added to database: " + user.getUsername());
    }
    
    public void removeUser(String username) {
        User removed = userDatabase.remove(username);
        if (removed != null) {
            System.out.println("➖ User removed from database: " + username);
        }
    }
    
    public void updateUserRole(String username, UserRole newRole) {
        User user = userDatabase.get(username);
        if (user != null) {
            user.setRole(newRole);
            System.out.println("🔄 Updated role for " + username + " to " + newRole.getDisplayName());
        }
    }
    
    public int getUserCount() {
        return userDatabase.size();
    }
    
    @Override
    public String getProviderInfo() {
        return super.getProviderInfo() + 
               "\nConnection: " + connectionString +
               "\nUsers: " + getUserCount() +
               "\nConnected: " + isConnected;
    }
}
