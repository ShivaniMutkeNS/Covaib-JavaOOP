package composition.elearning;

import java.util.*;

/**
 * Supporting classes for the E-learning Course Management System
 */

/**
 * Question class for assessments
 */
class Question {
    private final String questionId;
    private final String questionText;
    private final String questionType;
    private final List<String> options;
    private final String correctAnswer;
    private final int points;
    private Assessment assessment;
    
    public Question(String questionId, String questionText, String questionType, 
                   List<String> options, String correctAnswer, int points) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.options = new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
        this.points = points;
    }
    
    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }
    
    // Getters
    public String getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public String getQuestionType() { return questionType; }
    public List<String> getOptions() { return new ArrayList<>(options); }
    public String getCorrectAnswer() { return correctAnswer; }
    public int getPoints() { return points; }
    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }
}

/**
 * Assessment attempt tracking
 */
class AssessmentAttempt {
    private final String attemptId;
    private final String assessmentId;
    private final String studentId;
    private final int attemptNumber;
    private final long startTime;
    private long endTime;
    private double score;
    private boolean passed;
    private Map<String, String> answers;
    private boolean completed;
    
    public AssessmentAttempt(String attemptId, String assessmentId, String studentId, 
                           int attemptNumber, long startTime) {
        this.attemptId = attemptId;
        this.assessmentId = assessmentId;
        this.studentId = studentId;
        this.attemptNumber = attemptNumber;
        this.startTime = startTime;
        this.answers = new HashMap<>();
        this.completed = false;
    }
    
    public void complete(double score, boolean passed, Map<String, String> answers) {
        this.endTime = System.currentTimeMillis();
        this.score = score;
        this.passed = passed;
        this.answers = new HashMap<>(answers);
        this.completed = true;
    }
    
    public long getDuration() {
        return completed ? endTime - startTime : System.currentTimeMillis() - startTime;
    }
    
    // Getters
    public String getAttemptId() { return attemptId; }
    public String getAssessmentId() { return assessmentId; }
    public String getStudentId() { return studentId; }
    public int getAttemptNumber() { return attemptNumber; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public double getScore() { return score; }
    public boolean isPassed() { return passed; }
    public Map<String, String> getAnswers() { return new HashMap<>(answers); }
    public boolean isCompleted() { return completed; }
}

/**
 * Certificate for course completion
 */
class Certificate {
    private final String certificateId;
    private final Student student;
    private final Course course;
    private final long issuedAt;
    private final double finalGrade;
    
    public Certificate(String certificateId, Student student, Course course, 
                      long issuedAt, double finalGrade) {
        this.certificateId = certificateId;
        this.student = student;
        this.course = course;
        this.issuedAt = issuedAt;
        this.finalGrade = finalGrade;
    }
    
    // Getters
    public String getCertificateId() { return certificateId; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public long getIssuedAt() { return issuedAt; }
    public double getFinalGrade() { return finalGrade; }
    
    @Override
    public String toString() {
        return String.format("Certificate[%s]: %s completed %s with grade %.1f", 
                           certificateId, student.getName(), course.getTitle(), finalGrade);
    }
}

/**
 * Content recommendation system
 */
class ContentRecommendation {
    private final Content content;
    private final String reason;
    private final double suitabilityScore;
    
    public ContentRecommendation(Content content, String reason, double suitabilityScore) {
        this.content = content;
        this.reason = reason;
        this.suitabilityScore = suitabilityScore;
    }
    
    // Getters
    public Content getContent() { return content; }
    public String getReason() { return reason; }
    public double getSuitabilityScore() { return suitabilityScore; }
}

/**
 * Progress summary for students
 */
class ProgressSummary {
    private final double completionPercentage;
    private final double contentProgress;
    private final double assessmentProgress;
    private final int completedContent;
    private final int totalContent;
    private final int completedAssessments;
    private final int totalAssessments;
    private final double averageScore;
    private final long studyTime;
    
    public ProgressSummary(double completionPercentage, double contentProgress, 
                          double assessmentProgress, int completedContent, int totalContent,
                          int completedAssessments, int totalAssessments, 
                          double averageScore, long studyTime) {
        this.completionPercentage = completionPercentage;
        this.contentProgress = contentProgress;
        this.assessmentProgress = assessmentProgress;
        this.completedContent = completedContent;
        this.totalContent = totalContent;
        this.completedAssessments = completedAssessments;
        this.totalAssessments = totalAssessments;
        this.averageScore = averageScore;
        this.studyTime = studyTime;
    }
    
    // Getters
    public double getCompletionPercentage() { return completionPercentage; }
    public double getContentProgress() { return contentProgress; }
    public double getAssessmentProgress() { return assessmentProgress; }
    public int getCompletedContent() { return completedContent; }
    public int getTotalContent() { return totalContent; }
    public int getCompletedAssessments() { return completedAssessments; }
    public int getTotalAssessments() { return totalAssessments; }
    public double getAverageScore() { return averageScore; }
    public long getStudyTime() { return studyTime; }
    
    @Override
    public String toString() {
        return String.format("Progress: %.1f%% complete, %.1f avg score, %d/%d content, %d/%d assessments", 
                           completionPercentage, averageScore, completedContent, totalContent,
                           completedAssessments, totalAssessments);
    }
}

/**
 * Gamified progress summary with achievements
 */
class GamifiedProgressSummary extends ProgressSummary {
    private final List<String> achievements;
    private final int experiencePoints;
    private final int level;
    
    public GamifiedProgressSummary(double completionPercentage, double contentProgress, 
                                  double assessmentProgress, int completedContent, int totalContent,
                                  int completedAssessments, int totalAssessments, 
                                  double averageScore, long studyTime,
                                  List<String> achievements, int experiencePoints, int level) {
        super(completionPercentage, contentProgress, assessmentProgress, completedContent, 
              totalContent, completedAssessments, totalAssessments, averageScore, studyTime);
        this.achievements = new ArrayList<>(achievements);
        this.experiencePoints = experiencePoints;
        this.level = level;
    }
    
    // Getters
    public List<String> getAchievements() { return new ArrayList<>(achievements); }
    public int getExperiencePoints() { return experiencePoints; }
    public int getLevel() { return level; }
    
    @Override
    public String toString() {
        return String.format("%s, Level %d (%d XP), %d achievements", 
                           super.toString(), level, experiencePoints, achievements.size());
    }
}

/**
 * Course settings and configuration
 */
class CourseSettings {
    private int maxStudents;
    private boolean allowLateSubmissions;
    private boolean requireSequentialProgress;
    private int certificateThreshold;
    private boolean enableDiscussions;
    private boolean enablePeerReview;
    
    public CourseSettings() {
        this.maxStudents = 0; // 0 means unlimited
        this.allowLateSubmissions = true;
        this.requireSequentialProgress = false;
        this.certificateThreshold = 70;
        this.enableDiscussions = true;
        this.enablePeerReview = false;
    }
    
    // Getters and setters
    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
    public boolean isAllowLateSubmissions() { return allowLateSubmissions; }
    public void setAllowLateSubmissions(boolean allow) { this.allowLateSubmissions = allow; }
    public boolean isRequireSequentialProgress() { return requireSequentialProgress; }
    public void setRequireSequentialProgress(boolean require) { this.requireSequentialProgress = require; }
    public int getCertificateThreshold() { return certificateThreshold; }
    public void setCertificateThreshold(int threshold) { this.certificateThreshold = threshold; }
    public boolean isEnableDiscussions() { return enableDiscussions; }
    public void setEnableDiscussions(boolean enable) { this.enableDiscussions = enable; }
    public boolean isEnablePeerReview() { return enablePeerReview; }
    public void setEnablePeerReview(boolean enable) { this.enablePeerReview = enable; }
}

/**
 * Course analytics and metrics
 */
class CourseAnalytics {
    private int totalEnrollments;
    private int totalUnenrollments;
    private int totalCompletions;
    private int modulesAdded;
    private final List<Double> assessmentScores;
    private final Map<CourseState, Integer> stateChanges;
    private final long createdAt;
    
    public CourseAnalytics() {
        this.totalEnrollments = 0;
        this.totalUnenrollments = 0;
        this.totalCompletions = 0;
        this.modulesAdded = 0;
        this.assessmentScores = new ArrayList<>();
        this.stateChanges = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
    }
    
    public void incrementEnrollments() { totalEnrollments++; }
    public void incrementUnenrollments() { totalUnenrollments++; }
    public void incrementCompletions() { totalCompletions++; }
    public void incrementModulesAdded() { modulesAdded++; }
    
    public void recordAssessmentCompletion(double score) {
        assessmentScores.add(score);
    }
    
    public void recordStateChange(CourseState state) {
        stateChanges.put(state, stateChanges.getOrDefault(state, 0) + 1);
    }
    
    public void updateCurrentMetrics(int currentEnrollments, int totalModules, 
                                   double averageProgress, double completionRate) {
        // Update current metrics for reporting
    }
    
    public EngagementMetrics calculateEngagementMetrics(Collection<StudentProgress> progressList) {
        if (progressList.isEmpty()) {
            return new EngagementMetrics(0, 0, 0, 0);
        }
        
        double averageProgress = progressList.stream()
                .mapToDouble(p -> 50.0) // Simplified calculation
                .average()
                .orElse(0.0);
        
        long averageStudyTime = progressList.stream()
                .mapToLong(StudentProgress::getTotalStudyTime)
                .sum() / progressList.size();
        
        double engagementScore = Math.min(100, averageProgress + (averageStudyTime / 3600000.0)); // Convert to hours
        
        return new EngagementMetrics(averageProgress, averageStudyTime, engagementScore, progressList.size());
    }
    
    // Getters
    public int getTotalEnrollments() { return totalEnrollments; }
    public int getTotalUnenrollments() { return totalUnenrollments; }
    public int getTotalCompletions() { return totalCompletions; }
    public int getModulesAdded() { return modulesAdded; }
    public List<Double> getAssessmentScores() { return new ArrayList<>(assessmentScores); }
    public Map<CourseState, Integer> getStateChanges() { return new HashMap<>(stateChanges); }
    public long getCreatedAt() { return createdAt; }
}

/**
 * Engagement metrics for course analysis
 */
class EngagementMetrics {
    private final double averageProgress;
    private final long averageStudyTime;
    private final double engagementScore;
    private final int activeStudents;
    
    public EngagementMetrics(double averageProgress, long averageStudyTime, 
                           double engagementScore, int activeStudents) {
        this.averageProgress = averageProgress;
        this.averageStudyTime = averageStudyTime;
        this.engagementScore = engagementScore;
        this.activeStudents = activeStudents;
    }
    
    // Getters
    public double getAverageProgress() { return averageProgress; }
    public long getAverageStudyTime() { return averageStudyTime; }
    public double getEngagementScore() { return engagementScore; }
    public int getActiveStudents() { return activeStudents; }
    
    @Override
    public String toString() {
        return String.format("Engagement: %.1f%% progress, %d min avg study, %.1f score, %d students", 
                           averageProgress, averageStudyTime / 60000, engagementScore, activeStudents);
    }
}

/**
 * Event listener interface for course events
 */
interface CourseEventListener {
    void onCourseEvent(String courseId, String eventMessage);
    void onStudentEnrolled(String courseId, String studentId);
    void onStudentCompleted(String courseId, String studentId);
    void onModuleAdded(String courseId, String moduleId);
    void onAssessmentCompleted(String courseId, String studentId, String assessmentId, double score);
}
