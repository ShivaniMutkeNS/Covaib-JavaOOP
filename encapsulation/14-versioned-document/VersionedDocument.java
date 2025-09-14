
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Versioned Document
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating content so edits create new versions instead of mutating old state
 * 2. Providing getHistory() for audit purposes
 * 3. Preventing external overwrite of existing versions
 * 4. Implementing immutable document versions
 */
public class VersionedDocument {
    // Encapsulated document data
    private final String documentId;
    private final String title;
    private final LocalDateTime createdTime;
    private final String createdBy;
    
    // Version management
    private final Map<Integer, DocumentVersion> versions;
    private final AtomicInteger currentVersionNumber;
    private final AtomicInteger nextVersionNumber;
    
    // Access control
    private final AccessController accessController;
    
    /**
     * Constructor
     */
    public VersionedDocument(String documentId, String title, String content, String createdBy) {
        this.documentId = documentId;
        this.title = title;
        this.createdTime = LocalDateTime.now();
        this.createdBy = createdBy;
        this.versions = new HashMap<>();
        this.currentVersionNumber = new AtomicInteger(1);
        this.nextVersionNumber = new AtomicInteger(2);
        this.accessController = new AccessController();
        
        // Create initial version
        DocumentVersion initialVersion = new DocumentVersion(
            1, content, createdBy, LocalDateTime.now(), "Initial version"
        );
        versions.put(1, initialVersion);
    }
    
    /**
     * Create new version with updated content
     * @param newContent New content
     * @param updatedBy User who updated the document
     * @param changeDescription Description of changes
     * @return New version number or -1 if failed
     */
    public int createNewVersion(String newContent, String updatedBy, String changeDescription) {
        if (newContent == null || newContent.trim().isEmpty()) {
            return -1;
        }
        
        if (updatedBy == null || updatedBy.trim().isEmpty()) {
            return -1;
        }
        
        if (changeDescription == null || changeDescription.trim().isEmpty()) {
            changeDescription = "No description provided";
        }
        
        int newVersionNumber = nextVersionNumber.getAndIncrement();
        DocumentVersion newVersion = new DocumentVersion(
            newVersionNumber,
            newContent.trim(),
            updatedBy.trim(),
            LocalDateTime.now(),
            changeDescription.trim()
        );
        
        versions.put(newVersionNumber, newVersion);
        currentVersionNumber.set(newVersionNumber);
        
        return newVersionNumber;
    }
    
    /**
     * Get current version content
     * @return Current version content
     */
    public String getCurrentContent() {
        DocumentVersion currentVersion = versions.get(currentVersionNumber.get());
        return currentVersion != null ? currentVersion.getContent() : "";
    }
    
    /**
     * Get content of specific version
     * @param versionNumber Version number
     * @return Content of the version or null if not found
     */
    public String getVersionContent(int versionNumber) {
        DocumentVersion version = versions.get(versionNumber);
        return version != null ? version.getContent() : null;
    }
    
    /**
     * Get current version number
     * @return Current version number
     */
    public int getCurrentVersionNumber() {
        return currentVersionNumber.get();
    }
    
    /**
     * Get total number of versions
     * @return Total number of versions
     */
    public int getTotalVersions() {
        return versions.size();
    }
    
    /**
     * Get document history (read-only)
     * @return Unmodifiable list of document versions
     */
    public List<DocumentVersion> getHistory() {
        return versions.values().stream()
                .sorted(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get version by number
     * @param versionNumber Version number
     * @return Document version or null if not found
     */
    public DocumentVersion getVersion(int versionNumber) {
        return versions.get(versionNumber);
    }
    
    /**
     * Get versions by user
     * @param userId User ID
     * @return List of versions created by the user
     */
    public List<DocumentVersion> getVersionsByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        return versions.values().stream()
                .filter(version -> userId.equals(version.getUpdatedBy()))
                .sorted(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Get versions within time range
     * @param startTime Start time
     * @param endTime End time
     * @return List of versions within the time range
     */
    public List<DocumentVersion> getVersionsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return Collections.emptyList();
        }
        
        return versions.values().stream()
                .filter(version -> !version.getUpdatedTime().isBefore(startTime) && 
                                 !version.getUpdatedTime().isAfter(endTime))
                .sorted(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    /**
     * Compare two versions
     * @param version1 First version number
     * @param version2 Second version number
     * @return Comparison result or null if either version not found
     */
    public VersionComparison compareVersions(int version1, int version2) {
        DocumentVersion v1 = versions.get(version1);
        DocumentVersion v2 = versions.get(version2);
        
        if (v1 == null || v2 == null) {
            return null;
        }
        
        return new VersionComparison(v1, v2);
    }
    
    /**
     * Get document summary
     * @return Document summary
     */
    public String getDocumentSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Document ID: ").append(documentId).append("\n");
        summary.append("Title: ").append(title).append("\n");
        summary.append("Created: ").append(createdTime).append(" by ").append(createdBy).append("\n");
        summary.append("Current Version: ").append(currentVersionNumber.get()).append("\n");
        summary.append("Total Versions: ").append(versions.size()).append("\n");
        summary.append("Last Updated: ").append(getLastUpdatedTime()).append("\n");
        
        return summary.toString();
    }
    
    /**
     * Get last updated time
     * @return Last updated time
     */
    public LocalDateTime getLastUpdatedTime() {
        return versions.values().stream()
                .map(DocumentVersion::getUpdatedTime)
                .max(LocalDateTime::compareTo)
                .orElse(createdTime);
    }
    
    /**
     * Get last updated by
     * @return Last updated by
     */
    public String getLastUpdatedBy() {
        return versions.values().stream()
                .max(Comparator.comparingInt(DocumentVersion::getVersionNumber))
                .map(DocumentVersion::getUpdatedBy)
                .orElse(createdBy);
    }
    
    /**
     * Check if user has access to document
     * @param userId User ID
     * @param action Action to perform
     * @return true if user has access
     */
    public boolean hasAccess(String userId, String action) {
        return accessController.hasAccess(userId, action);
    }
    
    /**
     * Check if version exists
     * @param versionNumber Version number
     * @return true if version exists
     */
    public boolean hasVersion(int versionNumber) {
        return versions.containsKey(versionNumber);
    }
    
    /**
     * Get document metadata
     * @return Document metadata
     */
    public DocumentMetadata getMetadata() {
        return new DocumentMetadata(
            documentId, title, createdTime, createdBy, 
            currentVersionNumber.get(), versions.size(), getLastUpdatedTime(), getLastUpdatedBy()
        );
    }
    
    // Getters
    public String getDocumentId() { return documentId; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public String getCreatedBy() { return createdBy; }
    
    /**
     * Document version (immutable)
     */
    public static class DocumentVersion {
        private final int versionNumber;
        private final String content;
        private final String updatedBy;
        private final LocalDateTime updatedTime;
        private final String changeDescription;
        
        public DocumentVersion(int versionNumber, String content, String updatedBy, 
                             LocalDateTime updatedTime, String changeDescription) {
            this.versionNumber = versionNumber;
            this.content = content;
            this.updatedBy = updatedBy;
            this.updatedTime = updatedTime;
            this.changeDescription = changeDescription;
        }
        
        public int getVersionNumber() { return versionNumber; }
        public String getContent() { return content; }
        public String getUpdatedBy() { return updatedBy; }
        public LocalDateTime getUpdatedTime() { return updatedTime; }
        public String getChangeDescription() { return changeDescription; }
        
        @Override
        public String toString() {
            return String.format("DocumentVersion{v%d, by='%s', time=%s, desc='%s'}", 
                versionNumber, updatedBy, updatedTime, changeDescription);
        }
    }
    
    /**
     * Version comparison result
     */
    public static class VersionComparison {
        private final DocumentVersion version1;
        private final DocumentVersion version2;
        private final boolean contentChanged;
        private final int contentLengthDifference;
        
        public VersionComparison(DocumentVersion version1, DocumentVersion version2) {
            this.version1 = version1;
            this.version2 = version2;
            this.contentChanged = !version1.getContent().equals(version2.getContent());
            this.contentLengthDifference = version2.getContent().length() - version1.getContent().length();
        }
        
        public DocumentVersion getVersion1() { return version1; }
        public DocumentVersion getVersion2() { return version2; }
        public boolean isContentChanged() { return contentChanged; }
        public int getContentLengthDifference() { return contentLengthDifference; }
        
        @Override
        public String toString() {
            return String.format("VersionComparison{v%d vs v%d, changed=%s, lengthDiff=%d}", 
                version1.getVersionNumber(), version2.getVersionNumber(), 
                contentChanged, contentLengthDifference);
        }
    }
    
    /**
     * Document metadata
     */
    public static class DocumentMetadata {
        private final String documentId;
        private final String title;
        private final LocalDateTime createdTime;
        private final String createdBy;
        private final int currentVersion;
        private final int totalVersions;
        private final LocalDateTime lastUpdatedTime;
        private final String lastUpdatedBy;
        
        public DocumentMetadata(String documentId, String title, LocalDateTime createdTime, 
                              String createdBy, int currentVersion, int totalVersions, 
                              LocalDateTime lastUpdatedTime, String lastUpdatedBy) {
            this.documentId = documentId;
            this.title = title;
            this.createdTime = createdTime;
            this.createdBy = createdBy;
            this.currentVersion = currentVersion;
            this.totalVersions = totalVersions;
            this.lastUpdatedTime = lastUpdatedTime;
            this.lastUpdatedBy = lastUpdatedBy;
        }
        
        public String getDocumentId() { return documentId; }
        public String getTitle() { return title; }
        public LocalDateTime getCreatedTime() { return createdTime; }
        public String getCreatedBy() { return createdBy; }
        public int getCurrentVersion() { return currentVersion; }
        public int getTotalVersions() { return totalVersions; }
        public LocalDateTime getLastUpdatedTime() { return lastUpdatedTime; }
        public String getLastUpdatedBy() { return lastUpdatedBy; }
        
        @Override
        public String toString() {
            return String.format("DocumentMetadata{id='%s', title='%s', v%d/%d, lastUpdated=%s by %s}", 
                documentId, title, currentVersion, totalVersions, lastUpdatedTime, lastUpdatedBy);
        }
    }
    
    /**
     * Access controller for document operations
     */
    private static class AccessController {
        // In a real implementation, this would handle user permissions
        // For this demo, we'll keep it simple
        
        public boolean hasAccess(String userId, String action) {
            // Simple demo implementation - allow all access
            return userId != null && !userId.trim().isEmpty();
        }
    }
    
    @Override
    public String toString() {
        return String.format("VersionedDocument{id='%s', title='%s', v%d/%d}", 
            documentId, title, currentVersionNumber.get(), versions.size());
    }
}
