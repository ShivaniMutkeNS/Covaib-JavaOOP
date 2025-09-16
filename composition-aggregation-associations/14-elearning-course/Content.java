package composition.elearning;

import java.util.*;

/**
 * Content class representing learning content within a module
 */
public class Content {
    private final String contentId;
    private final String title;
    private final String description;
    private final ContentType type;
    private final String contentUrl;
    private final int durationMinutes;
    private final int orderIndex;
    private final Map<String, Object> metadata;
    private Module module;
    private boolean isRequired;
    private final long createdAt;
    
    public Content(String contentId, String title, String description, ContentType type, 
                  String contentUrl, int durationMinutes, int orderIndex) {
        this.contentId = contentId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.contentUrl = contentUrl;
        this.durationMinutes = durationMinutes;
        this.orderIndex = orderIndex;
        this.metadata = new HashMap<>();
        this.isRequired = true;
        this.createdAt = System.currentTimeMillis();
    }
    
    public boolean isAccessible(Student student, StudentProgress progress) {
        // Check if prerequisites are met
        if (orderIndex > 0 && module != null) {
            List<Content> moduleContents = module.getContents();
            for (int i = 0; i < orderIndex && i < moduleContents.size(); i++) {
                Content prerequisite = moduleContents.get(i);
                if (prerequisite.isRequired() && 
                    !progress.isContentCompleted(prerequisite.getContentId())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public ContentAccessResult access(Student student, StudentProgress progress) {
        if (!isAccessible(student, progress)) {
            return new ContentAccessResult(false, "Prerequisites not met", null);
        }
        
        progress.recordContentAccess(contentId);
        return new ContentAccessResult(true, "Content accessed successfully", this);
    }
    
    public ContentCompletionResult markCompleted(Student student, StudentProgress progress) {
        if (!progress.isContentAccessed(contentId)) {
            return new ContentCompletionResult(false, "Content must be accessed first", false);
        }
        
        progress.recordContentCompletion(contentId);
        return new ContentCompletionResult(true, "Content marked as completed", true);
    }
    
    // Getters and setters
    public String getContentId() { return contentId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public ContentType getType() { return type; }
    public String getContentUrl() { return contentUrl; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getOrderIndex() { return orderIndex; }
    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }
    public boolean isRequired() { return isRequired; }
    public void setRequired(boolean required) { this.isRequired = required; }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public long getCreatedAt() { return createdAt; }
    
    @Override
    public String toString() {
        return String.format("Content[%s]: %s (%s, %d min)", 
                           contentId, title, type.getDisplayName(), durationMinutes);
    }
}
