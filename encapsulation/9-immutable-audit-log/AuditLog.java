
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Immutable Audit Log
 * 
 * This class demonstrates encapsulation by:
 * 1. Creating an AuditLog class that stores system events
 * 2. Once an event is logged, it cannot be modified or deleted
 * 3. Support only append operations
 * 4. Thread-safe implementation for concurrent access
 */
public class AuditLog {
    // Encapsulated audit entries
    private final List<AuditEntry> entries;
    private final String logId;
    private final LocalDateTime createdTime;
    private final AuditLogConfig config;
    
    // Thread safety
    private final Object lock = new Object();
    
    /**
     * Constructor
     */
    public AuditLog(String logId) {
        this.logId = logId;
        this.createdTime = LocalDateTime.now();
        this.entries = new CopyOnWriteArrayList<>();
        this.config = new AuditLogConfig();
    }
    
    /**
     * Constructor with custom config
     */
    public AuditLog(String logId, AuditLogConfig config) {
        this.logId = logId;
        this.createdTime = LocalDateTime.now();
        this.entries = new CopyOnWriteArrayList<>();
        this.config = config != null ? config : new AuditLogConfig();
    }
    
    /**
     * Append a new audit entry (only operation allowed)
     * @param eventType Type of event
     * @param description Event description
     * @param userId User who triggered the event
     * @return true if entry was added successfully
     */
    public boolean appendEntry(String eventType, String description, String userId) {
        return appendEntry(eventType, description, userId, null);
    }
    
    /**
     * Append a new audit entry with additional data
     * @param eventType Type of event
     * @param description Event description
     * @param userId User who triggered the event
     * @param additionalData Additional data for the event
     * @return true if entry was added successfully
     */
    public boolean appendEntry(String eventType, String description, String userId, Map<String, Object> additionalData) {
        if (eventType == null || eventType.trim().isEmpty()) {
            return false;
        }
        
        if (description == null || description.trim().isEmpty()) {
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        
        // Check if log is full
        if (config.getMaxEntries() > 0 && entries.size() >= config.getMaxEntries()) {
            if (config.isOverflowAllowed()) {
                // Remove oldest entries to make room
                removeOldestEntries(1);
            } else {
                return false;
            }
        }
        
        // Create new audit entry
        AuditEntry entry = new AuditEntry(
            generateEntryId(),
            eventType.trim(),
            description.trim(),
            userId.trim(),
            LocalDateTime.now(),
            additionalData != null ? new HashMap<>(additionalData) : new HashMap<>()
        );
        
        // Add entry (thread-safe)
        synchronized (lock) {
            entries.add(entry);
        }
        
        return true;
    }
    
    /**
     * Get all audit entries (read-only)
     * @return Unmodifiable list of audit entries
     */
    public List<AuditEntry> getAllEntries() {
        return Collections.unmodifiableList(entries);
    }
    
    /**
     * Get entries by event type
     * @param eventType Event type to filter by
     * @return List of entries matching the event type
     */
    public List<AuditEntry> getEntriesByType(String eventType) {
        if (eventType == null || eventType.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        return entries.stream()
                .filter(entry -> eventType.equals(entry.getEventType()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get entries by user
     * @param userId User ID to filter by
     * @return List of entries for the user
     */
    public List<AuditEntry> getEntriesByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        return entries.stream()
                .filter(entry -> userId.equals(entry.getUserId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get entries within time range
     * @param startTime Start time (inclusive)
     * @param endTime End time (inclusive)
     * @return List of entries within the time range
     */
    public List<AuditEntry> getEntriesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return Collections.emptyList();
        }
        
        return entries.stream()
                .filter(entry -> !entry.getTimestamp().isBefore(startTime) && 
                               !entry.getTimestamp().isAfter(endTime))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get entries by event type and user
     * @param eventType Event type to filter by
     * @param userId User ID to filter by
     * @return List of entries matching both criteria
     */
    public List<AuditEntry> getEntriesByTypeAndUser(String eventType, String userId) {
        if (eventType == null || eventType.trim().isEmpty() || 
            userId == null || userId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        return entries.stream()
                .filter(entry -> eventType.equals(entry.getEventType()) && 
                               userId.equals(entry.getUserId()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get total number of entries
     * @return Number of entries
     */
    public int getEntryCount() {
        return entries.size();
    }
    
    /**
     * Get log ID
     * @return Log ID
     */
    public String getLogId() {
        return logId;
    }
    
    /**
     * Get creation time
     * @return Creation time
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    /**
     * Get log summary
     * @return Log summary
     */
    public String getLogSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Audit Log ID: ").append(logId).append("\n");
        summary.append("Created: ").append(createdTime).append("\n");
        summary.append("Total Entries: ").append(entries.size()).append("\n");
        
        // Count entries by type
        Map<String, Long> typeCounts = entries.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    AuditEntry::getEventType, 
                    java.util.stream.Collectors.counting()
                ));
        
        summary.append("Entries by Type:\n");
        typeCounts.forEach((type, count) -> 
            summary.append("  ").append(type).append(": ").append(count).append("\n")
        );
        
        return summary.toString();
    }
    
    /**
     * Check if log is empty
     * @return true if log is empty
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }
    
    /**
     * Check if log is full
     * @return true if log is full
     */
    public boolean isFull() {
        return config.getMaxEntries() > 0 && entries.size() >= config.getMaxEntries();
    }
    
    /**
     * Get oldest entry
     * @return Oldest entry or null if empty
     */
    public AuditEntry getOldestEntry() {
        return entries.isEmpty() ? null : entries.get(0);
    }
    
    /**
     * Get newest entry
     * @return Newest entry or null if empty
     */
    public AuditEntry getNewestEntry() {
        return entries.isEmpty() ? null : entries.get(entries.size() - 1);
    }
    
    /**
     * Generate unique entry ID
     * @return Unique entry ID
     */
    private String generateEntryId() {
        return logId + "_" + System.currentTimeMillis() + "_" + entries.size();
    }
    
    /**
     * Remove oldest entries to make room
     * @param count Number of entries to remove
     */
    private void removeOldestEntries(int count) {
        synchronized (lock) {
            for (int i = 0; i < count && !entries.isEmpty(); i++) {
                entries.remove(0);
            }
        }
    }
    
    /**
     * Audit entry (immutable)
     */
    public static class AuditEntry {
        private final String entryId;
        private final String eventType;
        private final String description;
        private final String userId;
        private final LocalDateTime timestamp;
        private final Map<String, Object> additionalData;
        
        public AuditEntry(String entryId, String eventType, String description, 
                         String userId, LocalDateTime timestamp, Map<String, Object> additionalData) {
            this.entryId = entryId;
            this.eventType = eventType;
            this.description = description;
            this.userId = userId;
            this.timestamp = timestamp;
            this.additionalData = Collections.unmodifiableMap(additionalData);
        }
        
        public String getEntryId() { return entryId; }
        public String getEventType() { return eventType; }
        public String getDescription() { return description; }
        public String getUserId() { return userId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, Object> getAdditionalData() { return additionalData; }
        
        @Override
        public String toString() {
            return String.format("AuditEntry{id='%s', type='%s', user='%s', time=%s, desc='%s'}", 
                entryId, eventType, userId, timestamp, description);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            AuditEntry that = (AuditEntry) obj;
            return Objects.equals(entryId, that.entryId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(entryId);
        }
    }
    
    /**
     * Audit log configuration
     */
    public static class AuditLogConfig {
        private final int maxEntries;
        private final boolean overflowAllowed;
        private final boolean threadSafe;
        
        public AuditLogConfig() {
            this.maxEntries = 10000; // Default max entries
            this.overflowAllowed = true; // Allow overflow by default
            this.threadSafe = true; // Thread-safe by default
        }
        
        public AuditLogConfig(int maxEntries, boolean overflowAllowed, boolean threadSafe) {
            this.maxEntries = maxEntries;
            this.overflowAllowed = overflowAllowed;
            this.threadSafe = threadSafe;
        }
        
        public int getMaxEntries() { return maxEntries; }
        public boolean isOverflowAllowed() { return overflowAllowed; }
        public boolean isThreadSafe() { return threadSafe; }
    }
    
    @Override
    public String toString() {
        return String.format("AuditLog{id='%s', entries=%d, created=%s}", 
            logId, entries.size(), createdTime);
    }
}
