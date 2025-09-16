package composition.elearning;

import java.util.*;

/**
 * Result classes for various e-learning operations
 */

/**
 * Result of course state changes
 */
class CourseStateResult {
    private final boolean success;
    private final String message;
    private final CourseState newState;
    
    public CourseStateResult(boolean success, String message, CourseState newState) {
        this.success = success;
        this.message = message;
        this.newState = newState;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public CourseState getNewState() { return newState; }
}

/**
 * Result of module operations
 */
class ModuleResult {
    private final boolean success;
    private final String message;
    private final Module module;
    
    public ModuleResult(boolean success, String message, Module module) {
        this.success = success;
        this.message = message;
        this.module = module;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Module getModule() { return module; }
}

/**
 * Result of enrollment operations
 */
class EnrollmentResult {
    private final boolean success;
    private final String message;
    private final StudentProgress progress;
    
    public EnrollmentResult(boolean success, String message, StudentProgress progress) {
        this.success = success;
        this.message = message;
        this.progress = progress;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public StudentProgress getProgress() { return progress; }
}

/**
 * Result of content delivery operations
 */
class ContentDeliveryResult {
    private final boolean success;
    private final String message;
    private final Content content;
    private final String deliveryNote;
    
    public ContentDeliveryResult(boolean success, String message, Content content, String deliveryNote) {
        this.success = success;
        this.message = message;
        this.content = content;
        this.deliveryNote = deliveryNote;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Content getContent() { return content; }
    public String getDeliveryNote() { return deliveryNote; }
}

/**
 * Result of assessment operations
 */
class AssessmentResult {
    private final boolean success;
    private final String message;
    private final double score;
    private final String feedback;
    
    public AssessmentResult(boolean success, String message, double score, String feedback) {
        this.success = success;
        this.message = message;
        this.score = score;
        this.feedback = feedback;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getScore() { return score; }
    public String getFeedback() { return feedback; }
}

/**
 * Result of progress queries
 */
class ProgressResult {
    private final boolean success;
    private final String message;
    private final ProgressSummary summary;
    
    public ProgressResult(boolean success, String message, ProgressSummary summary) {
        this.success = success;
        this.message = message;
        this.summary = summary;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ProgressSummary getSummary() { return summary; }
}

/**
 * Result of course completion checks
 */
class CourseCompletionResult {
    private final boolean success;
    private final String message;
    private final boolean completed;
    private final Certificate certificate;
    
    public CourseCompletionResult(boolean success, String message, boolean completed, Certificate certificate) {
        this.success = success;
        this.message = message;
        this.completed = completed;
        this.certificate = certificate;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public boolean isCompleted() { return completed; }
    public Certificate getCertificate() { return certificate; }
}

/**
 * Result of content access operations
 */
class ContentAccessResult {
    private final boolean success;
    private final String message;
    private final Content content;
    
    public ContentAccessResult(boolean success, String message, Content content) {
        this.success = success;
        this.message = message;
        this.content = content;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Content getContent() { return content; }
}

/**
 * Result of content completion operations
 */
class ContentCompletionResult {
    private final boolean success;
    private final String message;
    private final boolean completed;
    
    public ContentCompletionResult(boolean success, String message, boolean completed) {
        this.success = success;
        this.message = message;
        this.completed = completed;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public boolean isCompleted() { return completed; }
}

/**
 * Result of assessment attempt operations
 */
class AssessmentAttemptResult {
    private final boolean success;
    private final String message;
    private final AssessmentAttempt attempt;
    
    public AssessmentAttemptResult(boolean success, String message, AssessmentAttempt attempt) {
        this.success = success;
        this.message = message;
        this.attempt = attempt;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public AssessmentAttempt getAttempt() { return attempt; }
}

/**
 * Result of assessment submission operations
 */
class AssessmentSubmissionResult {
    private final boolean success;
    private final String message;
    private final double score;
    private final boolean passed;
    private final AssessmentAttempt attempt;
    
    public AssessmentSubmissionResult(boolean success, String message, double score, 
                                    boolean passed, AssessmentAttempt attempt) {
        this.success = success;
        this.message = message;
        this.score = score;
        this.passed = passed;
        this.attempt = attempt;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getScore() { return score; }
    public boolean isPassed() { return passed; }
    public AssessmentAttempt getAttempt() { return attempt; }
}
