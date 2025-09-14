
import java.time.LocalDateTime;
import java.util.*;

/**
 * Online Exam System with Anti-Cheating
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating student answers so they can only be set during exam
 * 2. Preventing students from seeing their answers once submitted
 * 3. Only ExamEvaluator can read the answers
 * 4. Implementing anti-cheating measures
 */
public class StudentAnswer {
    // Encapsulated answer data
    private final String studentId;
    private final String examId;
    private final Map<Integer, String> answers;
    private final LocalDateTime submissionTime;
    private final boolean isSubmitted;
    
    // Anti-cheating measures
    private final List<AntiCheatingEvent> antiCheatingEvents;
    private final ExamSession session;
    
    /**
     * Constructor for creating answer sheet
     */
    public StudentAnswer(String studentId, String examId, ExamSession session) {
        this.studentId = studentId;
        this.examId = examId;
        this.answers = new HashMap<>();
        this.submissionTime = null;
        this.isSubmitted = false;
        this.antiCheatingEvents = new ArrayList<>();
        this.session = session;
        
        // Validate session
        if (session == null || !session.isValid()) {
            throw new IllegalArgumentException("Invalid exam session");
        }
        
        // Log answer sheet creation
        logAntiCheatingEvent("ANSWER_SHEET_CREATED", "Answer sheet created for student: " + studentId);
    }
    
    /**
     * Set answer for a specific question (only during exam)
     * @param questionNumber Question number
     * @param answer Answer text
     * @param studentId Student ID for validation
     * @return true if answer was set successfully
     */
    public boolean setAnswer(int questionNumber, String answer, String studentId) {
        // Validate student ID
        if (!this.studentId.equals(studentId)) {
            logAntiCheatingEvent("UNAUTHORIZED_ACCESS", "Attempted to set answer for different student");
            return false;
        }
        
        // Check if exam is still active
        if (isSubmitted || !session.isActive()) {
            logAntiCheatingEvent("EXAM_ENDED", "Attempted to set answer after exam ended");
            return false;
        }
        
        // Validate answer
        if (answer == null || answer.trim().isEmpty()) {
            logAntiCheatingEvent("INVALID_ANSWER", "Attempted to set empty answer");
            return false;
        }
        
        // Set answer
        answers.put(questionNumber, answer.trim());
        logAntiCheatingEvent("ANSWER_SET", "Answer set for question " + questionNumber);
        
        return true;
    }
    
    /**
     * Submit exam (only once)
     * @param studentId Student ID for validation
     * @return true if submission was successful
     */
    public boolean submitExam(String studentId) {
        // Validate student ID
        if (!this.studentId.equals(studentId)) {
            logAntiCheatingEvent("UNAUTHORIZED_SUBMISSION", "Attempted to submit exam for different student");
            return false;
        }
        
        // Check if already submitted
        if (isSubmitted) {
            logAntiCheatingEvent("ALREADY_SUBMITTED", "Attempted to submit already submitted exam");
            return false;
        }
        
        // Check if exam is still active
        if (!session.isActive()) {
            logAntiCheatingEvent("EXAM_EXPIRED", "Attempted to submit expired exam");
            return false;
        }
        
        // Submit exam
        logAntiCheatingEvent("EXAM_SUBMITTED", "Exam submitted successfully");
        return true;
    }
    
    /**
     * Get answer for a specific question (only by ExamEvaluator)
     * @param questionNumber Question number
     * @param evaluator Evaluator instance
     * @return Answer text or null if not found
     */
    public String getAnswer(int questionNumber, ExamEvaluator evaluator) {
        if (evaluator == null) {
            logAntiCheatingEvent("UNAUTHORIZED_ACCESS", "Attempted to access answer without evaluator");
            return null;
        }
        
        if (!evaluator.isAuthorized()) {
            logAntiCheatingEvent("UNAUTHORIZED_ACCESS", "Unauthorized evaluator attempted to access answer");
            return null;
        }
        
        logAntiCheatingEvent("ANSWER_ACCESSED", "Answer accessed by evaluator for question " + questionNumber);
        return answers.get(questionNumber);
    }
    
    /**
     * Get all answers (only by ExamEvaluator)
     * @param evaluator Evaluator instance
     * @return Map of all answers or empty map if not authorized
     */
    public Map<Integer, String> getAllAnswers(ExamEvaluator evaluator) {
        if (evaluator == null || !evaluator.isAuthorized()) {
            logAntiCheatingEvent("UNAUTHORIZED_ACCESS", "Unauthorized attempt to access all answers");
            return Collections.emptyMap();
        }
        
        logAntiCheatingEvent("ALL_ANSWERS_ACCESSED", "All answers accessed by evaluator");
        return Collections.unmodifiableMap(answers);
    }
    
    /**
     * Get student ID (for validation purposes)
     * @return Student ID
     */
    public String getStudentId() {
        return studentId;
    }
    
    /**
     * Get exam ID
     * @return Exam ID
     */
    public String getExamId() {
        return examId;
    }
    
    /**
     * Check if exam is submitted
     * @return true if submitted
     */
    public boolean isSubmitted() {
        return isSubmitted;
    }
    
    /**
     * Get submission time
     * @return Submission time or null if not submitted
     */
    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }
    
    /**
     * Get number of answers
     * @return Number of answers
     */
    public int getAnswerCount() {
        return answers.size();
    }
    
    /**
     * Check if answer exists for question
     * @param questionNumber Question number
     * @return true if answer exists
     */
    public boolean hasAnswer(int questionNumber) {
        return answers.containsKey(questionNumber);
    }
    
    /**
     * Get anti-cheating events (only by ExamEvaluator)
     * @param evaluator Evaluator instance
     * @return List of anti-cheating events
     */
    public List<AntiCheatingEvent> getAntiCheatingEvents(ExamEvaluator evaluator) {
        if (evaluator == null || !evaluator.isAuthorized()) {
            return Collections.emptyList();
        }
        
        return Collections.unmodifiableList(antiCheatingEvents);
    }
    
    /**
     * Get exam session info (only by ExamEvaluator)
     * @param evaluator Evaluator instance
     * @return Exam session info
     */
    public ExamSession getExamSession(ExamEvaluator evaluator) {
        if (evaluator == null || !evaluator.isAuthorized()) {
            return null;
        }
        
        return session;
    }
    
    /**
     * Check if exam is still active
     * @return true if exam is active
     */
    public boolean isExamActive() {
        return session.isActive();
    }
    
    /**
     * Get remaining time (only during exam)
     * @return Remaining time in minutes
     */
    public long getRemainingTime() {
        if (isSubmitted || !session.isActive()) {
            return 0;
        }
        
        return session.getRemainingTime();
    }
    
    /**
     * Log anti-cheating event
     * @param eventType Type of event
     * @param description Event description
     */
    private void logAntiCheatingEvent(String eventType, String description) {
        AntiCheatingEvent event = new AntiCheatingEvent(
            eventType, 
            description, 
            LocalDateTime.now(),
            studentId
        );
        antiCheatingEvents.add(event);
    }
    
    /**
     * Anti-cheating event record
     */
    public static class AntiCheatingEvent {
        private final String eventType;
        private final String description;
        private final LocalDateTime timestamp;
        private final String studentId;
        
        public AntiCheatingEvent(String eventType, String description, LocalDateTime timestamp, String studentId) {
            this.eventType = eventType;
            this.description = description;
            this.timestamp = timestamp;
            this.studentId = studentId;
        }
        
        public String getEventType() { return eventType; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getStudentId() { return studentId; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s: %s (Student: %s)", 
                timestamp, eventType, description, studentId);
        }
    }
    
    /**
     * Exam session for time management
     */
    public static class ExamSession {
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final int durationMinutes;
        private final String sessionId;
        
        public ExamSession(String sessionId, int durationMinutes) {
            this.sessionId = sessionId;
            this.durationMinutes = durationMinutes;
            this.startTime = LocalDateTime.now();
            this.endTime = startTime.plusMinutes(durationMinutes);
        }
        
        public boolean isActive() {
            return LocalDateTime.now().isBefore(endTime);
        }
        
        public boolean isValid() {
            return sessionId != null && !sessionId.trim().isEmpty() && durationMinutes > 0;
        }
        
        public long getRemainingTime() {
            if (!isActive()) {
                return 0;
            }
            
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(endTime)) {
                return 0;
            }
            
            return java.time.Duration.between(now, endTime).toMinutes();
        }
        
        public String getSessionId() { return sessionId; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public int getDurationMinutes() { return durationMinutes; }
    }
    
    /**
     * Exam evaluator with authorization
     */
    public static class ExamEvaluator {
        private final String evaluatorId;
        private final String authorizationCode;
        private final boolean isAuthorized;
        
        public ExamEvaluator(String evaluatorId, String authorizationCode) {
            this.evaluatorId = evaluatorId;
            this.authorizationCode = authorizationCode;
            this.isAuthorized = validateAuthorization(evaluatorId, authorizationCode);
        }
        
        private boolean validateAuthorization(String evaluatorId, String authorizationCode) {
            // In real implementation, this would validate against a database
            return evaluatorId != null && 
                   !evaluatorId.trim().isEmpty() && 
                   "EVAL_AUTH_2024".equals(authorizationCode);
        }
        
        public boolean isAuthorized() {
            return isAuthorized;
        }
        
        public String getEvaluatorId() {
            return evaluatorId;
        }
        
        public String getAuthorizationCode() {
            return authorizationCode;
        }
    }
    
    @Override
    public String toString() {
        return String.format("StudentAnswer{studentId='%s', examId='%s', answerCount=%d, submitted=%s}", 
            studentId, examId, answers.size(), isSubmitted);
    }
}
