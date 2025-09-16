package composition.elearning;

import java.util.*;

/**
 * Student progress tracking for course completion
 */
public class StudentProgress {
    private final String studentId;
    private final String courseId;
    private final Map<String, Long> contentAccessTimes;
    private final Map<String, Long> contentCompletionTimes;
    private final Map<String, Double> assessmentResults;
    private final Map<String, List<AssessmentAttempt>> assessmentAttempts;
    private final Map<String, ProgressStatus> moduleProgress;
    private boolean completed;
    private long completionTime;
    private double finalGrade;
    private final long startedAt;
    
    public StudentProgress(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.contentAccessTimes = new HashMap<>();
        this.contentCompletionTimes = new HashMap<>();
        this.assessmentResults = new HashMap<>();
        this.assessmentAttempts = new HashMap<>();
        this.moduleProgress = new HashMap<>();
        this.completed = false;
        this.completionTime = 0;
        this.finalGrade = 0.0;
        this.startedAt = System.currentTimeMillis();
    }
    
    // Content progress tracking
    public void recordContentAccess(String contentId) {
        contentAccessTimes.put(contentId, System.currentTimeMillis());
    }
    
    public void recordContentCompletion(String contentId) {
        contentCompletionTimes.put(contentId, System.currentTimeMillis());
    }
    
    public boolean isContentAccessed(String contentId) {
        return contentAccessTimes.containsKey(contentId);
    }
    
    public boolean isContentCompleted(String contentId) {
        return contentCompletionTimes.containsKey(contentId);
    }
    
    // Assessment progress tracking
    public void recordAssessmentAttempt(String assessmentId, AssessmentAttempt attempt) {
        assessmentAttempts.computeIfAbsent(assessmentId, k -> new ArrayList<>()).add(attempt);
    }
    
    public void recordAssessmentResult(String assessmentId, double score) {
        assessmentResults.put(assessmentId, score);
    }
    
    public int getAssessmentAttemptCount(String assessmentId) {
        return assessmentAttempts.getOrDefault(assessmentId, new ArrayList<>()).size();
    }
    
    public double getAssessmentScore(String assessmentId) {
        return assessmentResults.getOrDefault(assessmentId, 0.0);
    }
    
    public List<AssessmentAttempt> getAssessmentAttempts(String assessmentId) {
        return new ArrayList<>(assessmentAttempts.getOrDefault(assessmentId, new ArrayList<>()));
    }
    
    // Module progress tracking
    public void updateModuleProgress(String moduleId, ProgressStatus status) {
        moduleProgress.put(moduleId, status);
    }
    
    public ProgressStatus getModuleProgress(String moduleId) {
        return moduleProgress.getOrDefault(moduleId, ProgressStatus.NOT_STARTED);
    }
    
    // Course completion
    public void markCompleted() {
        this.completed = true;
        this.completionTime = System.currentTimeMillis();
    }
    
    public void setFinalGrade(double grade) {
        this.finalGrade = grade;
    }
    
    // Analytics methods
    public long getTotalStudyTime() {
        if (contentAccessTimes.isEmpty()) return 0;
        
        long earliestAccess = contentAccessTimes.values().stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(startedAt);
        
        long latestActivity = Math.max(
            contentCompletionTimes.values().stream().mapToLong(Long::longValue).max().orElse(0),
            assessmentAttempts.values().stream()
                .flatMap(List::stream)
                .mapToLong(AssessmentAttempt::getStartTime)
                .max()
                .orElse(0)
        );
        
        return latestActivity > earliestAccess ? latestActivity - earliestAccess : 0;
    }
    
    public double getAverageAssessmentScore() {
        if (assessmentResults.isEmpty()) return 0.0;
        
        return assessmentResults.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    
    public int getCompletedContentCount() {
        return contentCompletionTimes.size();
    }
    
    public int getCompletedAssessmentCount() {
        return assessmentResults.size();
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public Map<String, Long> getContentAccessTimes() { return new HashMap<>(contentAccessTimes); }
    public Map<String, Long> getContentCompletionTimes() { return new HashMap<>(contentCompletionTimes); }
    public Map<String, Double> getAssessmentResults() { return new HashMap<>(assessmentResults); }
    public Map<String, List<AssessmentAttempt>> getAssessmentAttempts() { return new HashMap<>(assessmentAttempts); }
    public Map<String, ProgressStatus> getModuleProgress() { return new HashMap<>(moduleProgress); }
    public boolean isCompleted() { return completed; }
    public long getCompletionTime() { return completionTime; }
    public double getFinalGrade() { return finalGrade; }
    public long getStartedAt() { return startedAt; }
    
    @Override
    public String toString() {
        return String.format("Progress[%s-%s]: %d content, %d assessments, %.1f%% avg score", 
                           studentId, courseId, getCompletedContentCount(), 
                           getCompletedAssessmentCount(), getAverageAssessmentScore());
    }
}
