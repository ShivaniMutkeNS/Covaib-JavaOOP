package encapsulation.ImmutableConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MAANG-level ImmutableConfigLoader
 * - Loads configuration from a properties file.
 * - Configurations are immutable once loaded.
 * - Thread-safe, suitable for distributed systems.
 */
public final class ConfigLoader {
    private final Map<String, String> config;

    // Singleton instance
    private static volatile ConfigLoader instance = null;

    // Private constructor to prevent external instantiation
    private ConfigLoader(String configFilePath) {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Config file not found: " + configFilePath);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
        Map<String, String> tempMap = new ConcurrentHashMap<>();
        for (String name : props.stringPropertyNames()) {
            tempMap.put(name, props.getProperty(name));
        }
        // Wrap as unmodifiable map
        this.config = Collections.unmodifiableMap(tempMap);
    }

    /**
     * Initialize (singleton) with config file path.
     * Should be called once at app startup.
     */
    public static void initialize(String configFilePath) {
        if (instance == null) {
            synchronized (ConfigLoader.class) {
                if (instance == null) {
                    instance = new ConfigLoader(configFilePath);
                }
            }
        }
    }

    /**
     * Get singleton instance.
     */
    public static ConfigLoader getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConfigLoader not initialized. Call initialize() first.");
        }
        return instance;
    }

    /**
     * Get config value by key (read-only).
     */
    public String get(String key) {
        return config.get(key);
    }

    /**
     * Get all config as unmodifiable map.
     */
    public Map<String, String> getAll() {
        return config;
    }

    // Prevent modification by omitting any setters or mutators
}