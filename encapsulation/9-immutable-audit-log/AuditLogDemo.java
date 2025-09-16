
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo class to demonstrate Immutable Audit Log
 */
public class AuditLogDemo {
    public static void main(String[] args) {
        System.out.println("=== Immutable Audit Log Demo ===\n");
        
        // Test basic audit log operations
        testBasicAuditLog();
        
        // Test audit log filtering
        testAuditLogFiltering();
        
        // Test audit log configuration
        testAuditLogConfiguration();
        
        // Test thread safety
        testThreadSafety();
        
        // Test immutability
        testImmutability();
        
        // Test audit log with additional data
        testAuditLogWithAdditionalData();
        
        // Test audit log summary
        testAuditLogSummary();
    }
    
    private static void testBasicAuditLog() {
        System.out.println("=== Basic Audit Log Test ===");
        
        AuditLog auditLog = new AuditLog("LOG_001");
        System.out.println("Audit log created: " + auditLog);
        
        // Add some entries
        auditLog.appendEntry("USER_LOGIN", "User logged in", "user123");
        auditLog.appendEntry("USER_LOGOUT", "User logged out", "user123");
        auditLog.appendEntry("DATA_ACCESS", "User accessed sensitive data", "user456");
        auditLog.appendEntry("SYSTEM_ERROR", "Database connection failed", "system");
        
        System.out.println("Total entries: " + auditLog.getEntryCount());
        System.out.println("Is empty: " + auditLog.isEmpty());
        System.out.println("Is full: " + auditLog.isFull());
        
        // Display all entries
        System.out.println("\nAll entries:");
        for (AuditLog.AuditEntry entry : auditLog.getAllEntries()) {
            System.out.println(entry);
        }
        
        System.out.println();
    }
    
    private static void testAuditLogFiltering() {
        System.out.println("=== Audit Log Filtering Test ===");
        
        AuditLog auditLog = new AuditLog("LOG_002");
        
        // Add entries with different types and users
        auditLog.appendEntry("USER_LOGIN", "User logged in", "user123");
        auditLog.appendEntry("USER_LOGOUT", "User logged out", "user123");
        auditLog.appendEntry("USER_LOGIN", "User logged in", "user456");
        auditLog.appendEntry("DATA_ACCESS", "User accessed data", "user123");
        auditLog.appendEntry("DATA_ACCESS", "User accessed data", "user456");
        auditLog.appendEntry("SYSTEM_ERROR", "System error occurred", "system");
        
        // Filter by event type
        List<AuditLog.AuditEntry> loginEntries = auditLog.getEntriesByType("USER_LOGIN");
        System.out.println("Login entries: " + loginEntries.size());
        
        // Filter by user
        List<AuditLog.AuditEntry> user123Entries = auditLog.getEntriesByUser("user123");
        System.out.println("User123 entries: " + user123Entries.size());
        
        // Filter by type and user
        List<AuditLog.AuditEntry> user123Logins = auditLog.getEntriesByTypeAndUser("USER_LOGIN", "user123");
        System.out.println("User123 login entries: " + user123Logins.size());
        
        // Filter by time range
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        List<AuditLog.AuditEntry> timeRangeEntries = auditLog.getEntriesByTimeRange(startTime, endTime);
        System.out.println("Entries in time range: " + timeRangeEntries.size());
        
        System.out.println();
    }
    
    private static void testAuditLogConfiguration() {
        System.out.println("=== Audit Log Configuration Test ===");
        
        // Create audit log with custom config
        AuditLog.AuditLogConfig config = new AuditLog.AuditLogConfig(5, true, true);
        AuditLog auditLog = new AuditLog("LOG_003", config);
        
        System.out.println("Max entries: " + config.getMaxEntries());
        System.out.println("Overflow allowed: " + config.isOverflowAllowed());
        System.out.println("Thread safe: " + config.isThreadSafe());
        
        // Add entries up to the limit
        for (int i = 1; i <= 7; i++) {
            boolean success = auditLog.appendEntry("TEST_EVENT", "Test event " + i, "user" + i);
            System.out.println("Added entry " + i + ": " + (success ? "SUCCESS" : "FAILED"));
            System.out.println("Current count: " + auditLog.getEntryCount());
        }
        
        System.out.println("Final entry count: " + auditLog.getEntryCount());
        System.out.println("Is full: " + auditLog.isFull());
        
        System.out.println();
    }
    
    private static void testThreadSafety() {
        System.out.println("=== Thread Safety Test ===");
        
        AuditLog auditLog = new AuditLog("LOG_004");
        
        // Create multiple threads adding entries
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    auditLog.appendEntry("THREAD_EVENT", "Thread " + threadId + " event " + j, "thread" + threadId);
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
        
        System.out.println("Final entry count: " + auditLog.getEntryCount());
        System.out.println("Expected: 50 entries");
        
        System.out.println();
    }
    
    private static void testImmutability() {
        System.out.println("=== Immutability Test ===");
        
        AuditLog auditLog = new AuditLog("LOG_005");
        
        // Add an entry
        auditLog.appendEntry("TEST_EVENT", "Test event", "user123");
        
        // Get the entry
        List<AuditLog.AuditEntry> entries = auditLog.getAllEntries();
        AuditLog.AuditEntry entry = entries.get(0);
        
        System.out.println("Original entry: " + entry);
        
        // Try to modify the entry (this should not be possible)
        try {
            // The entry is immutable, so we can't modify it
            System.out.println("Entry ID: " + entry.getEntryId());
            System.out.println("Event Type: " + entry.getEventType());
            System.out.println("Description: " + entry.getDescription());
            System.out.println("User ID: " + entry.getUserId());
            System.out.println("Timestamp: " + entry.getTimestamp());
            
            // Additional data is also immutable
            Map<String, Object> additionalData = entry.getAdditionalData();
            System.out.println("Additional data: " + additionalData);
            
            // Try to modify additional data (should not work)
            try {
                additionalData.put("newKey", "newValue");
                System.out.println("ERROR: Should not be able to modify additional data");
            } catch (UnsupportedOperationException e) {
                System.out.println("✓ Correctly prevented modification of additional data");
            }
            
        } catch (Exception e) {
            System.out.println("Exception during immutability test: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Test audit log with additional data
     */
    private static void testAuditLogWithAdditionalData() {
        System.out.println("=== Audit Log with Additional Data Test ===");
        
        AuditLog auditLog = new AuditLog("LOG_006");
        
        // Add entry with additional data
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("ipAddress", "192.168.1.100");
        additionalData.put("userAgent", "Mozilla/5.0");
        additionalData.put("sessionId", "sess_123456");
        
        boolean success = auditLog.appendEntry("USER_LOGIN", "User logged in", "user123", additionalData);
        System.out.println("Added entry with additional data: " + (success ? "SUCCESS" : "FAILED"));
        
        // Get the entry and display additional data
        List<AuditLog.AuditEntry> entries = auditLog.getAllEntries();
        if (!entries.isEmpty()) {
            AuditLog.AuditEntry entry = entries.get(0);
            System.out.println("Entry: " + entry);
            System.out.println("Additional data: " + entry.getAdditionalData());
        }
        
        System.out.println();
    }
    
    /**
     * Test audit log summary
     */
    private static void testAuditLogSummary() {
        System.out.println("=== Audit Log Summary Test ===");
        
        AuditLog auditLog = new AuditLog("LOG_007");
        
        // Add various types of entries
        auditLog.appendEntry("USER_LOGIN", "User logged in", "user123");
        auditLog.appendEntry("USER_LOGOUT", "User logged out", "user123");
        auditLog.appendEntry("USER_LOGIN", "User logged in", "user456");
        auditLog.appendEntry("DATA_ACCESS", "User accessed data", "user123");
        auditLog.appendEntry("SYSTEM_ERROR", "System error occurred", "system");
        auditLog.appendEntry("SYSTEM_ERROR", "Another system error", "system");
        
        // Display log summary
        System.out.println(auditLog.getLogSummary());
        
        System.out.println();
    }
}
