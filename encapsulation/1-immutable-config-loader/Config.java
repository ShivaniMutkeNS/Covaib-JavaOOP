
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Immutable App Config Loader
 * 
 * This class demonstrates encapsulation by:
 * 1. Loading configuration values from a file once
 * 2. Ensuring immutability - values cannot be altered at runtime
 * 3. Exposing only controlled getters
 * 4. Using thread-safe initialization
 */
public final class Config {
    private static volatile Config instance;
    private static final Object lock = new Object();
    
    // Immutable map to store configuration values
    private final Map<String, String> configMap;
    
    // Private constructor to prevent instantiation
    private Config(String configFilePath) throws IOException {
        this.configMap = loadConfigFromFile(configFilePath);
    }
    
    /**
     * Singleton pattern with double-checked locking for thread safety
     */
    public static Config getInstance(String configFilePath) throws IOException {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Config(configFilePath);
                }
            }
        }
        return instance;
    }
    
    /**
     * Load configuration from file
     * Format: key=value (one per line)
     */
    private Map<String, String> loadConfigFromFile(String configFilePath) throws IOException {
        Map<String, String> config = new ConcurrentHashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        config.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        }
        
        // Return immutable map
        return Collections.unmodifiableMap(config);
    }
    
    /**
     * Get configuration value by key
     * @param key Configuration key
     * @return Configuration value or null if not found
     */
    public String getValue(String key) {
        return configMap.get(key);
    }
    
    /**
     * Get configuration value with default
     * @param key Configuration key
     * @param defaultValue Default value if key not found
     * @return Configuration value or default
     */
    public String getValue(String key, String defaultValue) {
        return configMap.getOrDefault(key, defaultValue);
    }
    
    /**
     * Check if configuration key exists
     * @param key Configuration key
     * @return true if key exists
     */
    public boolean containsKey(String key) {
        return configMap.containsKey(key);
    }
    
    /**
     * Get all configuration keys
     * @return Unmodifiable set of keys
     */
    public Set<String> getAllKeys() {
        return Collections.unmodifiableSet(configMap.keySet());
    }
    
    /**
     * Get all configuration entries
     * @return Unmodifiable map of all configurations
     */
    public Map<String, String> getAllConfigurations() {
        return Collections.unmodifiableMap(configMap);
    }
    
    /**
     * Get configuration count
     * @return Number of configuration entries
     */
    public int size() {
        return configMap.size();
    }
    
    /**
     * Check if configuration is empty
     * @return true if no configurations loaded
     */
    public boolean isEmpty() {
        return configMap.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Config{size=" + configMap.size() + ", keys=" + configMap.keySet() + "}";
    }
}
