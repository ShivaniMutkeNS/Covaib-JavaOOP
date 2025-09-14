package abstraction.loggerframework;

import java.util.HashMap;
import java.util.Map;

/**
 * Demo class showcasing polymorphic usage of different logger implementations
 * Demonstrates how client code remains unchanged regardless of logging destination
 */
public class LoggerDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Logger Framework Abstraction Demo ===\n");
        
        // Create different logger implementations
        Logger[] loggers = createLoggers();
        
        // Test each logger polymorphically
        for (Logger logger : loggers) {
            System.out.println("Testing logger: " + logger.getClass().getSimpleName());
            System.out.println("Logger ID: " + logger.getLoggerId());
            System.out.println("Threshold: " + logger.getThreshold());
            System.out.println("Async Mode: " + logger.isAsyncMode());
            
            try {
                // Test different log levels
                testLogLevels(logger);
                
                // Test structured logging with context
                testStructuredLogging(logger);
                
                // Test exception logging
                testExceptionLogging(logger);
                
                // Test filtering
                testFiltering(logger);
                
                // Flush and close
                logger.flush();
                logger.close();
                
            } catch (Exception e) {
                System.err.println("Error testing logger: " + e.getMessage());
            }
            
            System.out.println("-".repeat(60));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static Logger[] createLoggers() {
        // File Logger configuration
        Map<String, Object> fileConfig = new HashMap<>();
        fileConfig.put("log_file_path", "logs/demo.log");
        fileConfig.put("enable_rotation", true);
        fileConfig.put("max_file_size", 1024 * 1024); // 1MB
        fileConfig.put("max_backup_files", 3);
        fileConfig.put("enable_buffering", true);
        fileConfig.put("buffer_size", 4096);
        fileConfig.put("async_mode", false);
        
        // Database Logger configuration (simulated)
        Map<String, Object> dbConfig = new HashMap<>();
        dbConfig.put("connection_url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dbConfig.put("username", "sa");
        dbConfig.put("password", "");
        dbConfig.put("table_name", "demo_logs");
        dbConfig.put("enable_batching", true);
        dbConfig.put("batch_size", 50);
        dbConfig.put("max_retries", 2);
        dbConfig.put("async_mode", true);
        
        // Cloud Logger configuration (simulated)
        Map<String, Object> cloudConfig = new HashMap<>();
        cloudConfig.put("endpoint", "https://api.example.com/logs");
        cloudConfig.put("api_key", "demo_api_key_12345");
        cloudConfig.put("enable_compression", true);
        cloudConfig.put("enable_circuit_breaker", true);
        cloudConfig.put("max_concurrent_requests", 5);
        cloudConfig.put("timeout_ms", 3000);
        cloudConfig.put("retry_delay_ms", 500);
        cloudConfig.put("async_mode", true);
        
        return new Logger[] {
            new FileLogger("file_logger_001", Logger.LogLevel.DEBUG, fileConfig),
            new DatabaseLogger("db_logger_001", Logger.LogLevel.INFO, dbConfig),
            new CloudLogger("cloud_logger_001", Logger.LogLevel.WARN, cloudConfig)
        };
    }
    
    private static void testLogLevels(Logger logger) {
        System.out.println("\n1. Testing different log levels...");
        
        logger.trace("This is a TRACE message - very detailed debugging info");
        logger.debug("This is a DEBUG message - debugging information");
        logger.info("This is an INFO message - general information");
        logger.warn("This is a WARN message - warning about potential issues");
        logger.error("This is an ERROR message - error occurred but application continues");
        logger.fatal("This is a FATAL message - critical error, application may terminate");
        
        // Test formatted messages
        logger.info("User %s logged in from IP %s at %s", "john_doe", "192.168.1.100", "2024-01-15 10:30:00");
        logger.debug("Processing request with ID: %d, took %d ms", 12345, 250);
        
        System.out.println("   ✓ Log levels tested");
    }
    
    private static void testStructuredLogging(Logger logger) {
        System.out.println("\n2. Testing structured logging with context...");
        
        // Create context map
        Map<String, Object> context = new HashMap<>();
        context.put("user_id", "user_12345");
        context.put("session_id", "sess_abcdef");
        context.put("request_id", "req_98765");
        context.put("ip_address", "192.168.1.100");
        context.put("user_agent", "Mozilla/5.0 (Demo Browser)");
        
        logger.logWithContext(Logger.LogLevel.INFO, "User performed search operation", context);
        
        // Update context for next operation
        context.put("search_query", "java abstraction patterns");
        context.put("results_count", 42);
        context.put("response_time_ms", 150);
        
        logger.logWithContext(Logger.LogLevel.INFO, "Search completed successfully", context);
        
        System.out.println("   ✓ Structured logging tested");
    }
    
    private static void testExceptionLogging(Logger logger) {
        System.out.println("\n3. Testing exception logging...");
        
        try {
            // Simulate an exception
            simulateBusinessLogic();
        } catch (Exception e) {
            logger.error("Business logic failed", e);
            
            // Log with context
            Map<String, Object> errorContext = new HashMap<>();
            errorContext.put("operation", "simulate_business_logic");
            errorContext.put("error_code", "BL_001");
            errorContext.put("retry_count", 0);
            
            logger.logWithContext(Logger.LogLevel.ERROR, "Exception in business logic", e, errorContext);
        }
        
        System.out.println("   ✓ Exception logging tested");
    }
    
    private static void testFiltering(Logger logger) {
        System.out.println("\n4. Testing log filtering...");
        
        // Set a context filter (only log entries with specific user_id)
        ContextFilter contextFilter = new ContextFilter("user_id", "admin_user");
        logger.setFilter(contextFilter);
        
        Map<String, Object> adminContext = new HashMap<>();
        adminContext.put("user_id", "admin_user");
        adminContext.put("action", "delete_user");
        
        Map<String, Object> regularContext = new HashMap<>();
        regularContext.put("user_id", "regular_user");
        regularContext.put("action", "view_profile");
        
        // This should be logged (matches filter)
        logger.logWithContext(Logger.LogLevel.INFO, "Admin action performed", adminContext);
        
        // This should be filtered out
        logger.logWithContext(Logger.LogLevel.INFO, "Regular user action", regularContext);
        
        // Set composite filter (level AND context)
        LevelFilter levelFilter = new LevelFilter(Logger.LogLevel.WARN);
        CompositeFilter compositeFilter = new CompositeFilter(true, levelFilter, contextFilter);
        logger.setFilter(compositeFilter);
        
        // This should be logged (admin user AND warn level)
        logger.logWithContext(Logger.LogLevel.ERROR, "Admin error occurred", adminContext);
        
        // This should be filtered out (admin user but info level)
        logger.logWithContext(Logger.LogLevel.INFO, "Admin info message", adminContext);
        
        // Reset filter
        logger.setFilter(null);
        
        System.out.println("   ✓ Log filtering tested");
    }
    
    private static void simulateBusinessLogic() throws Exception {
        // Simulate nested exception
        try {
            throw new IllegalArgumentException("Invalid input parameter");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Business logic validation failed", e);
        }
    }
}
