
import java.io.*;
import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Demo class to demonstrate the Immutable Config Loader
 */
public class ConfigDemo {
    public static void main(String[] args) {
        try {
            // Create a sample config file
            createSampleConfigFile();
            
            // Load configuration
            Config config = Config.getInstance("config.properties");
            
            System.out.println("=== Immutable App Config Loader Demo ===\n");
            
            // Display configuration info
            System.out.println("Configuration loaded: " + config);
            System.out.println("Total configurations: " + config.size());
            System.out.println("Is empty: " + config.isEmpty());
            System.out.println();
            
            // Test getting values
            System.out.println("=== Configuration Values ===");
            System.out.println("Database URL: " + config.getValue("db.url"));
            System.out.println("Database User: " + config.getValue("db.user"));
            System.out.println("API Key: " + config.getValue("api.key"));
            System.out.println("Cache TTL: " + config.getValue("cache.ttl", "300")); // with default
            System.out.println("Non-existent key: " + config.getValue("non.existent", "DEFAULT"));
            System.out.println();
            
            // Test key existence
            System.out.println("=== Key Existence Checks ===");
            System.out.println("Contains 'db.url': " + config.containsKey("db.url"));
            System.out.println("Contains 'missing.key': " + config.containsKey("missing.key"));
            System.out.println();
            
            // Display all keys
            System.out.println("=== All Configuration Keys ===");
            Set<String> keys = config.getAllKeys();
            for (String key : keys) {
                System.out.println("- " + key + " = " + config.getValue(key));
            }
            System.out.println();
            
            // Demonstrate immutability
            System.out.println("=== Immutability Test ===");
            System.out.println("Original config size: " + config.size());
            
            // Try to modify the returned map (this will throw UnsupportedOperationException)
            try {
                config.getAllConfigurations().put("hacker.key", "hacker.value");
            } catch (UnsupportedOperationException e) {
                System.out.println("✓ Immutability enforced: Cannot modify configuration map");
            }
            
            // Try to modify the keys set (this will throw UnsupportedOperationException)
            try {
                config.getAllKeys().add("hacker.key");
            } catch (UnsupportedOperationException e) {
                System.out.println("✓ Immutability enforced: Cannot modify keys set");
            }
            
            System.out.println("Config size after modification attempts: " + config.size());
            System.out.println();
            
            // Test thread safety
            System.out.println("=== Thread Safety Test ===");
            testThreadSafety(config);
            
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    private static void createSampleConfigFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("config.properties"))) {
            writer.println("# Application Configuration");
            writer.println("app.name=MyApp");
            writer.println("app.version=1.0.0");
            writer.println("");
            writer.println("# Database Configuration");
            writer.println("db.url=jdbc:mysql://localhost:3306/mydb");
            writer.println("db.user=admin");
            writer.println("db.password=secret123");
            writer.println("db.pool.size=10");
            writer.println("");
            writer.println("# API Configuration");
            writer.println("api.key=sk-1234567890abcdef");
            writer.println("api.base.url=https://api.example.com");
            writer.println("api.timeout=5000");
            writer.println("");
            writer.println("# Cache Configuration");
            writer.println("cache.enabled=true");
            writer.println("cache.ttl=600");
            writer.println("cache.max.size=1000");
        }
    }
    
    private static void testThreadSafety(Config config) {
        int numThreads = 5;
        Thread[] threads = new Thread[numThreads];
        
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    String value = config.getValue("db.url");
                    if (value == null) {
                        System.err.println("Thread " + threadId + ": Null value detected!");
                    }
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("✓ Thread safety test completed successfully");
    }
}
