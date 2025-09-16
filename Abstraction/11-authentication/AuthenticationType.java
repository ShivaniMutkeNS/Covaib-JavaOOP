/**
 * Enumeration representing different authentication methods
 * Each type has specific security levels and characteristics
 */
public enum AuthenticationType {
    USERNAME_PASSWORD("Username/Password", 1, false, 300),
    TWO_FACTOR("Two-Factor Authentication", 3, true, 900),
    OAUTH("OAuth 2.0", 2, true, 3600),
    LDAP("LDAP Directory", 2, false, 1800),
    BIOMETRIC("Biometric Authentication", 4, true, 7200),
    API_KEY("API Key Authentication", 1, false, 86400),
    JWT_TOKEN("JWT Token", 3, true, 3600),
    CERTIFICATE("Certificate-based", 4, true, 43200);
    
    private final String displayName;
    private final int securityLevel; // 1-4 (1=Low, 4=High)
    private final boolean supportsMultiFactor;
    private final int defaultSessionDuration; // seconds
    
    AuthenticationType(String displayName, int securityLevel, 
                      boolean supportsMultiFactor, int defaultSessionDuration) {
        this.displayName = displayName;
        this.securityLevel = securityLevel;
        this.supportsMultiFactor = supportsMultiFactor;
        this.defaultSessionDuration = defaultSessionDuration;
    }
    
    public String getDisplayName() { return displayName; }
    public int getSecurityLevel() { return securityLevel; }
    public boolean supportsMultiFactor() { return supportsMultiFactor; }
    public int getDefaultSessionDuration() { return defaultSessionDuration; }
    
    public String getSecurityLevelDescription() {
        switch (securityLevel) {
            case 1: return "Low Security";
            case 2: return "Medium Security";
            case 3: return "High Security";
            case 4: return "Maximum Security";
            default: return "Unknown";
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", 
            displayName, 
            getSecurityLevelDescription(),
            supportsMultiFactor ? "MFA Supported" : "Single Factor");
    }
}
