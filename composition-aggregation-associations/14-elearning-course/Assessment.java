package composition.elearning;

import java.util.*;

/**
 * Assessment class representing evaluations within a module
 */
public class Assessment {
    private final String assessmentId;
    private final String title;
    private final String description;
    private final AssessmentType type;
    private final int maxScore;
    private final int passingScore;
    private final int estimatedTimeMinutes;
    private final List<Question> questions;
    private final Map<String, Object> metadata;
    private Module module;
    private boolean isRequired;
    private int maxAttempts;
    private final long createdAt;
    
    public Assessment(String assessmentId, String title, String description, 
                     AssessmentType type, int maxScore, int passingScore, int estimatedTimeMinutes) {
        this.assessmentId = assessmentId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.maxScore = maxScore;
        this.passingScore = passingScore;
        this.estimatedTimeMinutes = estimatedTimeMinutes;
        this.questions = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.isRequired = true;
        this.maxAttempts = 3;
        this.createdAt = System.currentTimeMillis();
    }
    
    public void addQuestion(Question question) {
        questions.add(question);
        question.setAssessment(this);
    }
    
    public void removeQuestion(String questionId) {
        questions.removeIf(question -> question.getQuestionId().equals(questionId));
    }
    
    public Question findQuestion(String questionId) {
        return questions.stream()
                .filter(question -> question.getQuestionId().equals(questionId))
                .findFirst()
                .orElse(null);
    }
    
    public boolean isAccessible(Student student, StudentProgress progress) {
        // Check if module prerequisites are met
        if (module != null) {
            List<Content> moduleContents = module.getContents();
            for (Content content : moduleContents) {
                if (content.isRequired() && !progress.isContentCompleted(content.getContentId())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public AssessmentAttemptResult startAttempt(Student student, StudentProgress progress) {
        if (!isAccessible(student, progress)) {
            return new AssessmentAttemptResult(false, "Prerequisites not met", null);
        }
        
        int attemptCount = progress.getAssessmentAttemptCount(assessmentId);
        if (attemptCount >= maxAttempts) {
            return new AssessmentAttemptResult(false, "Maximum attempts exceeded", null);
        }
        
        AssessmentAttempt attempt = new AssessmentAttempt(
            UUID.randomUUID().toString(), assessmentId, student.getStudentId(), 
            attemptCount + 1, System.currentTimeMillis()
        );
        
        progress.recordAssessmentAttempt(assessmentId, attempt);
        return new AssessmentAttemptResult(true, "Assessment attempt started", attempt);
    }
    
    public AssessmentSubmissionResult submitAttempt(AssessmentAttempt attempt, 
                                                   Map<String, String> answers) {
        int score = calculateScore(answers);
        boolean passed = score >= passingScore;
        
        attempt.complete(score, passed, answers);
        
        return new AssessmentSubmissionResult(true, "Assessment submitted successfully", 
                                            score, passed, attempt);
    }
    
    private int calculateScore(Map<String, String> answers) {
        int totalScore = 0;
        int maxPossibleScore = 0;
        
        for (Question question : questions) {
            maxPossibleScore += question.getPoints();
            String answer = answers.get(question.getQuestionId());
            if (answer != null && question.isCorrectAnswer(answer)) {
                totalScore += question.getPoints();
            }
        }
        
        // Convert to percentage of max score
        if (maxPossibleScore > 0) {
            return (int) ((double) totalScore / maxPossibleScore * maxScore);
        }
        
        return 0;
    }
    
    // Getters and setters
    public String getAssessmentId() { return assessmentId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public AssessmentType getType() { return type; }
    public int getMaxScore() { return maxScore; }
    public int getPassingScore() { return passingScore; }
    public int getEstimatedTimeMinutes() { return estimatedTimeMinutes; }
    public List<Question> getQuestions() { return new ArrayList<>(questions); }
    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }
    public boolean isRequired() { return isRequired; }
    public void setRequired(boolean required) { this.isRequired = required; }
    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public long getCreatedAt() { return createdAt; }
    
    @Override
    public String toString() {
        return String.format("Assessment[%s]: %s (%s, %d questions, %d/%d points)", 
                           assessmentId, title, type.getDisplayName(), 
                           questions.size(), passingScore, maxScore);
    }
}
