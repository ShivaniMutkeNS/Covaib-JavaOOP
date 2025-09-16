package composition.elearning;

import java.util.*;

/**
 * Module class representing a course module with content and assessments
 */
public class Module {
    private final String moduleId;
    private final String title;
    private final String description;
    private final int orderIndex;
    private final List<Content> contents;
    private final List<Assessment> assessments;
    private final Map<String, Object> metadata;
    private Course course;
    private boolean isRequired;
    private int estimatedDurationMinutes;
    private final long createdAt;
    
    public Module(String moduleId, String title, String description, int orderIndex) {
        this.moduleId = moduleId;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.contents = new ArrayList<>();
        this.assessments = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.isRequired = true;
        this.estimatedDurationMinutes = 60;
        this.createdAt = System.currentTimeMillis();
    }
    
    public void addContent(Content content) {
        contents.add(content);
        content.setModule(this);
    }
    
    public void removeContent(String contentId) {
        contents.removeIf(content -> content.getContentId().equals(contentId));
    }
    
    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
        assessment.setModule(this);
    }
    
    public void removeAssessment(String assessmentId) {
        assessments.removeIf(assessment -> assessment.getAssessmentId().equals(assessmentId));
    }
    
    public Content findContent(String contentId) {
        return contents.stream()
                .filter(content -> content.getContentId().equals(contentId))
                .findFirst()
                .orElse(null);
    }
    
    public Assessment findAssessment(String assessmentId) {
        return assessments.stream()
                .filter(assessment -> assessment.getAssessmentId().equals(assessmentId))
                .findFirst()
                .orElse(null);
    }
    
    public int getTotalContentCount() {
        return contents.size();
    }
    
    public int getTotalAssessmentCount() {
        return assessments.size();
    }
    
    public void updateEstimatedDuration() {
        this.estimatedDurationMinutes = contents.stream()
                .mapToInt(Content::getDurationMinutes)
                .sum() + assessments.stream()
                .mapToInt(Assessment::getEstimatedTimeMinutes)
                .sum();
    }
    
    // Getters and setters
    public String getModuleId() { return moduleId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getOrderIndex() { return orderIndex; }
    public List<Content> getContents() { return new ArrayList<>(contents); }
    public List<Assessment> getAssessments() { return new ArrayList<>(assessments); }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public boolean isRequired() { return isRequired; }
    public void setRequired(boolean required) { this.isRequired = required; }
    public int getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(int duration) { this.estimatedDurationMinutes = duration; }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public long getCreatedAt() { return createdAt; }
    
    @Override
    public String toString() {
        return String.format("Module[%s]: %s (%d contents, %d assessments)", 
                           moduleId, title, contents.size(), assessments.size());
    }
}
