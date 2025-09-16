/**
 * Comprehensive demonstration of the authentication system abstraction
 * Shows polymorphism, security features, and real-world authentication scenarios
 */
public class AuthenticationDemo {
    
    public static void main(String[] args) {
        System.out.println("🔐 Authentication System Demonstration");
        System.out.println("=====================================\n");
        
        // Create different authentication providers
        AuthenticationProvider[] providers = {
            new DatabaseAuthProvider(),
            new OAuthProvider("Google"),
            new BiometricAuthProvider("fingerprint")
        };
        
        // Demonstrate polymorphism with different auth methods
        demonstratePolymorphism(providers);
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate provider-specific features
        demonstrateProviderSpecificFeatures();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate session management
        demonstrateSessionManagement();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate security scenarios
        demonstrateSecurityScenarios();
        
        System.out.println("\n" + "=".repeat(70) + "\n");
        
        // Demonstrate multi-factor authentication
        demonstrateMultiFactorAuth();
    }
    
    private static void demonstratePolymorphism(AuthenticationProvider[] providers) {
        System.out.println("🎯 POLYMORPHISM DEMONSTRATION");
        System.out.println("Same interface, different authentication methods\n");
        
        String[] testCredentials = {"admin:admin123", "user@gmail.com:oauth_token", "admin:fingerprint_data"};
        
        for (int i = 0; i < providers.length; i++) {
            AuthenticationProvider provider = providers[i];
            String[] creds = testCredentials[i].split(":");
            
            System.out.println("Provider: " + provider.getClass().getSimpleName());
            System.out.println(provider.getProviderInfo());
            
            // Same method call, different implementation
            AuthenticationSession session = provider.authenticate(creds[0], creds[1], "192.168.1.100", "Mozilla/5.0");
            
            if (session != null) {
                System.out.println("✅ Authentication successful!");
                System.out.println(session.getDetailedInfo());
                session.invalidate();
            } else {
                System.out.println("❌ Authentication failed");
            }
            System.out.println();
        }
    }
    
    private static void demonstrateProviderSpecificFeatures() {
        System.out.println("🌟 PROVIDER-SPECIFIC FEATURES");
        System.out.println("Each provider has unique capabilities\n");
        
        // Database Provider Features
        System.out.println("💾 Database Provider Features:");
        DatabaseAuthProvider dbProvider = new DatabaseAuthProvider();
        User newUser = new User("4", "test_user", "test@company.com", "test123", UserRole.USER);
        dbProvider.addUser(newUser);
        dbProvider.updateUserRole("test_user", UserRole.MODERATOR);
        System.out.println("Total users: " + dbProvider.getUserCount());
        System.out.println();
        
        // OAuth Provider Features
        System.out.println("🌐 OAuth Provider Features:");
        OAuthProvider oauthProvider = new OAuthProvider("GitHub");
        String authUrl = oauthProvider.generateAuthorizationUrl("http://localhost:8080/callback", "state123");
        System.out.println("Authorization URL: " + authUrl);
        oauthProvider.revokeToken("sample_token");
        System.out.println();
        
        // Biometric Provider Features
        System.out.println("👆 Biometric Provider Features:");
        BiometricAuthProvider bioProvider = new BiometricAuthProvider("face");
        bioProvider.enrollBiometric("new_user", "face_scan_data");
        bioProvider.calibrateHardware();
        System.out.println("Enrolled users: " + bioProvider.getEnrolledCount());
        System.out.println();
    }
    
    private static void demonstrateSessionManagement() {
        System.out.println("🔄 SESSION MANAGEMENT");
        System.out.println("Session lifecycle and security\n");
        
        DatabaseAuthProvider provider = new DatabaseAuthProvider();
        
        // Create session
        AuthenticationSession session = provider.authenticate("admin", "admin123", "10.0.0.1", "Chrome/91.0");
        
        if (session != null) {
            System.out.println("Session created: " + session.getSessionId());
            
            // Simulate session usage
            for (int i = 0; i < 3; i++) {
                session.updateAccess();
                System.out.println("Session accessed. Access count: " + session.getAccessCount());
                
                try {
                    Thread.sleep(1000); // Wait 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Extend session
            session.extendSession(300);
            System.out.println("Time until expiry: " + session.getTimeUntilExpiry() + " seconds");
            
            // Session info
            System.out.println(session.getDetailedInfo());
            
            // Invalidate session
            session.invalidate();
            System.out.println("Session valid: " + session.isValid());
        }
        System.out.println();
    }
    
    private static void demonstrateSecurityScenarios() {
        System.out.println("🛡️ SECURITY SCENARIOS");
        System.out.println("Account lockout, failed attempts, and security measures\n");
        
        DatabaseAuthProvider provider = new DatabaseAuthProvider();
        User testUser = new User("5", "security_test", "security@test.com", "correct123", UserRole.USER);
        provider.addUser(testUser);
        
        // Scenario 1: Multiple failed login attempts
        System.out.println("Scenario 1: Brute Force Protection");
        for (int i = 1; i <= 6; i++) {
            System.out.println("Attempt " + i + ":");
            AuthenticationSession session = provider.authenticate("security_test", "wrong_password", "192.168.1.200", "Attacker");
            
            if (session == null) {
                User user = provider.getUserByUsername("security_test");
                System.out.println("Failed attempts: " + user.getFailedLoginAttempts());
                if (user.isLocked()) {
                    System.out.println("🔒 Account locked after " + i + " failed attempts");
                    break;
                }
            }
        }
        
        // Unlock and successful login
        User lockedUser = provider.getUserByUsername("security_test");
        lockedUser.unlockAccount();
        
        AuthenticationSession successSession = provider.authenticate("security_test", "correct123", "192.168.1.100", "Legitimate User");
        if (successSession != null) {
            System.out.println("✅ Successful login after unlock");
            successSession.invalidate();
        }
        System.out.println();
        
        // Scenario 2: Password change
        System.out.println("Scenario 2: Password Security");
        User user = provider.getUserByUsername("security_test");
        user.changePassword("correct123", "new_secure_password456");
        
        // Test old password (should fail)
        AuthenticationSession oldPassSession = provider.authenticate("security_test", "correct123", "192.168.1.100", "User");
        System.out.println("Old password works: " + (oldPassSession != null));
        
        // Test new password (should work)
        AuthenticationSession newPassSession = provider.authenticate("security_test", "new_secure_password456", "192.168.1.100", "User");
        System.out.println("New password works: " + (newPassSession != null));
        if (newPassSession != null) {
            newPassSession.invalidate();
        }
        System.out.println();
    }
    
    private static void demonstrateMultiFactorAuth() {
        System.out.println("🔐 MULTI-FACTOR AUTHENTICATION");
        System.out.println("Enhanced security with multiple authentication factors\n");
        
        // Create user with MFA enabled
        User mfaUser = new User("6", "mfa_user", "mfa@secure.com", "password123", UserRole.ADMIN);
        mfaUser.enableMFA("SECRET_KEY_123");
        
        DatabaseAuthProvider dbProvider = new DatabaseAuthProvider();
        dbProvider.addUser(mfaUser);
        
        System.out.println("User MFA Status: " + (mfaUser.isMfaEnabled() ? "Enabled" : "Disabled"));
        
        // Step 1: Primary authentication (username/password)
        System.out.println("\nStep 1: Primary Authentication");
        AuthenticationSession primarySession = dbProvider.authenticate("mfa_user", "password123", "192.168.1.50", "SecureApp");
        
        if (primarySession != null) {
            System.out.println("✅ Primary authentication successful");
            
            // Step 2: Secondary authentication (MFA token)
            System.out.println("\nStep 2: Multi-Factor Authentication");
            boolean mfaValid = mfaUser.verifyMFA("123456"); // Test token
            
            if (mfaValid) {
                System.out.println("✅ MFA verification successful");
                System.out.println("🎉 Full authentication completed!");
                System.out.println(primarySession.getDetailedInfo());
            } else {
                System.out.println("❌ MFA verification failed");
                primarySession.invalidate();
            }
            
            if (primarySession.isValid()) {
                primarySession.invalidate();
            }
        }
        
        // Demonstrate MFA disable
        System.out.println("\nMFA Management:");
        mfaUser.disableMFA();
        System.out.println("User MFA Status: " + (mfaUser.isMfaEnabled() ? "Enabled" : "Disabled"));
        
        System.out.println("\n🎉 Authentication System Demonstration Complete!");
        System.out.println("All abstraction concepts successfully demonstrated:");
        System.out.println("✅ Abstract classes and polymorphism");
        System.out.println("✅ Multiple authentication providers");
        System.out.println("✅ Session management and security");
        System.out.println("✅ User roles and permissions");
        System.out.println("✅ Multi-factor authentication");
        System.out.println("✅ Security scenarios and protection");
    }
}
