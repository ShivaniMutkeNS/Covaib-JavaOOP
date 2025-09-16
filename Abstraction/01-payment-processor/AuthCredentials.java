package abstraction.paymentprocessor;

import java.util.Map;

/**
 * Authentication credentials with support for different auth types
 */
public class AuthCredentials {
    private String username;
    private String password;
    private String apiKey;
    private String token;
    private Map<String, String> additionalParams;
    private AuthType authType;
    
    public enum AuthType {
        API_KEY, USERNAME_PASSWORD, OAUTH_TOKEN, MULTI_FACTOR
    }
    
    // Constructor for API Key authentication
    public AuthCredentials(String apiKey) {
        this.apiKey = apiKey;
        this.authType = AuthType.API_KEY;
    }
    
    // Constructor for username/password authentication
    public AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        this.authType = AuthType.USERNAME_PASSWORD;
    }
    
    // Constructor for OAuth token
    public AuthCredentials(String token, AuthType authType) {
        this.token = token;
        this.authType = authType;
    }
    
    // Constructor for multi-factor with additional params
    public AuthCredentials(String username, String password, Map<String, String> additionalParams) {
        this.username = username;
        this.password = password;
        this.additionalParams = additionalParams;
        this.authType = AuthType.MULTI_FACTOR;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getApiKey() { return apiKey; }
    public String getToken() { return token; }
    public Map<String, String> getAdditionalParams() { return additionalParams; }
    public AuthType getAuthType() { return authType; }
}
