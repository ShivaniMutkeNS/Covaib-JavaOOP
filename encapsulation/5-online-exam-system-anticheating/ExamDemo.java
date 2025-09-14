

import java.util.List;
import java.util.Map;

/**
 * Demo class to demonstrate Online Exam System with Anti-Cheating
 */
public class ExamDemo {
    public static void main(String[] args) {
        System.out.println("=== Online Exam System with Anti-Cheating Demo ===\n");
        
        // Test exam session creation and answer management
        testExamSession();
        
        // Test anti-cheating measures
        testAntiCheatingMeasures();
        
        // Test evaluator access
        testEvaluatorAccess();
        
        // Test unauthorized access attempts
        testUnauthorizedAccess();
        
        // Test exam submission
        testExamSubmission();
        
        // Test time-based exam scenarios
        testTimeBasedExam();
        
        // Test comprehensive exam scenario
        testComprehensiveExam();
    }
    
    private static void testExamSession() {
        System.out.println("=== Exam Session Test ===");
        
        // Create exam session
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_001", 60); // 60 minutes
        System.out.println("Exam session created: " + session.getSessionId());
        System.out.println("Duration: " + session.getDurationMinutes() + " minutes");
        System.out.println("Is active: " + session.isActive());
        System.out.println("Remaining time: " + session.getRemainingTime() + " minutes");
        System.out.println();
        
        // Create student answer sheet
        StudentAnswer answerSheet = new StudentAnswer("STU_001", "EXAM_001", session);
        System.out.println("Answer sheet created: " + answerSheet);
        System.out.println();
        
        // Test setting answers
        testAnswerSetting(answerSheet);
    }
    
    private static void testAnswerSetting(StudentAnswer answerSheet) {
        System.out.println("=== Answer Setting Test ===");
        
        // Set answers for different questions
        boolean success1 = answerSheet.setAnswer(1, "Option A", "STU_001");
        System.out.println("Set answer for question 1: " + (success1 ? "SUCCESS" : "FAILED"));
        
        boolean success2 = answerSheet.setAnswer(2, "Option B", "STU_001");
        System.out.println("Set answer for question 2: " + (success2 ? "SUCCESS" : "FAILED"));
        
        boolean success3 = answerSheet.setAnswer(3, "Option C", "STU_001");
        System.out.println("Set answer for question 3: " + (success3 ? "SUCCESS" : "FAILED"));
        
        // Test invalid answer setting
        boolean invalid1 = answerSheet.setAnswer(4, "", "STU_001"); // Empty answer
        System.out.println("Set empty answer for question 4: " + (invalid1 ? "SUCCESS" : "FAILED"));
        
        boolean invalid2 = answerSheet.setAnswer(5, "Option D", "STU_002"); // Wrong student ID
        System.out.println("Set answer with wrong student ID: " + (invalid2 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Total answers set: " + answerSheet.getAnswerCount());
        System.out.println("Has answer for question 1: " + answerSheet.hasAnswer(1));
        System.out.println("Has answer for question 4: " + answerSheet.hasAnswer(4));
        System.out.println();
    }
    
    private static void testAntiCheatingMeasures() {
        System.out.println("=== Anti-Cheating Measures Test ===");
        
        // Create exam session
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_002", 30);
        StudentAnswer answerSheet = new StudentAnswer("STU_002", "EXAM_002", session);
        
        // Test various anti-cheating scenarios
        System.out.println("Testing anti-cheating measures:");
        
        // 1. Attempt to set answer with wrong student ID
        boolean result1 = answerSheet.setAnswer(1, "Answer 1", "STU_003");
        System.out.println("Wrong student ID attempt: " + (result1 ? "SUCCESS" : "FAILED"));
        
        // 2. Attempt to set empty answer
        boolean result2 = answerSheet.setAnswer(2, "", "STU_002");
        System.out.println("Empty answer attempt: " + (result2 ? "SUCCESS" : "FAILED"));
        
        // 3. Attempt to set answer with null
        boolean result3 = answerSheet.setAnswer(3, null, "STU_002");
        System.out.println("Null answer attempt: " + (result3 ? "SUCCESS" : "FAILED"));
        
        // 4. Valid answer setting
        boolean result4 = answerSheet.setAnswer(4, "Valid Answer", "STU_002");
        System.out.println("Valid answer setting: " + (result4 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Answer count: " + answerSheet.getAnswerCount());
        System.out.println();
    }
    
    private static void testEvaluatorAccess() {
        System.out.println("=== Evaluator Access Test ===");
        
        // Create exam session and answer sheet
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_003", 45);
        StudentAnswer answerSheet = new StudentAnswer("STU_003", "EXAM_003", session);
        
        // Set some answers
        answerSheet.setAnswer(1, "Answer 1", "STU_003");
        answerSheet.setAnswer(2, "Answer 2", "STU_003");
        answerSheet.setAnswer(3, "Answer 3", "STU_003");
        
        // Create authorized evaluator
        StudentAnswer.ExamEvaluator evaluator = new StudentAnswer.ExamEvaluator("EVAL_001", "EVAL_AUTH_2024");
        System.out.println("Evaluator created: " + evaluator.getEvaluatorId());
        System.out.println("Is authorized: " + evaluator.isAuthorized());
        
        // Test accessing answers with authorized evaluator
        String answer1 = answerSheet.getAnswer(1, evaluator);
        System.out.println("Answer 1 (authorized): " + answer1);
        
        String answer2 = answerSheet.getAnswer(2, evaluator);
        System.out.println("Answer 2 (authorized): " + answer2);
        
        // Test accessing all answers
        Map<Integer, String> allAnswers = answerSheet.getAllAnswers(evaluator);
        System.out.println("All answers (authorized): " + allAnswers.size() + " answers");
        
        // Test accessing anti-cheating events
        List<StudentAnswer.AntiCheatingEvent> events = answerSheet.getAntiCheatingEvents(evaluator);
        System.out.println("Anti-cheating events: " + events.size() + " events");
        
        System.out.println();
    }
    
    private static void testUnauthorizedAccess() {
        System.out.println("=== Unauthorized Access Test ===");
        
        // Create exam session and answer sheet
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_004", 30);
        StudentAnswer answerSheet = new StudentAnswer("STU_004", "EXAM_004", session);
        
        // Set some answers
        answerSheet.setAnswer(1, "Answer 1", "STU_004");
        answerSheet.setAnswer(2, "Answer 2", "STU_004");
        
        // Test unauthorized evaluator
        StudentAnswer.ExamEvaluator unauthorizedEvaluator = new StudentAnswer.ExamEvaluator("EVAL_002", "WRONG_AUTH");
        System.out.println("Unauthorized evaluator created: " + unauthorizedEvaluator.getEvaluatorId());
        System.out.println("Is authorized: " + unauthorizedEvaluator.isAuthorized());
        
        // Test accessing answers with unauthorized evaluator
        String answer1 = answerSheet.getAnswer(1, unauthorizedEvaluator);
        System.out.println("Answer 1 (unauthorized): " + (answer1 != null ? answer1 : "ACCESS DENIED"));
        
        Map<Integer, String> allAnswers = answerSheet.getAllAnswers(unauthorizedEvaluator);
        System.out.println("All answers (unauthorized): " + allAnswers.size() + " answers");
        
        List<StudentAnswer.AntiCheatingEvent> events = answerSheet.getAntiCheatingEvents(unauthorizedEvaluator);
        System.out.println("Anti-cheating events (unauthorized): " + events.size() + " events");
        
        // Test accessing with null evaluator
        String answer2 = answerSheet.getAnswer(2, null);
        System.out.println("Answer 2 (null evaluator): " + (answer2 != null ? answer2 : "ACCESS DENIED"));
        
        System.out.println();
    }
    
    private static void testExamSubmission() {
        System.out.println("=== Exam Submission Test ===");
        
        // Create exam session and answer sheet
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_005", 30);
        StudentAnswer answerSheet = new StudentAnswer("STU_005", "EXAM_005", session);
        
        // Set some answers
        answerSheet.setAnswer(1, "Answer 1", "STU_005");
        answerSheet.setAnswer(2, "Answer 2", "STU_005");
        answerSheet.setAnswer(3, "Answer 3", "STU_005");
        
        System.out.println("Answers set: " + answerSheet.getAnswerCount());
        System.out.println("Is submitted: " + answerSheet.isSubmitted());
        System.out.println("Is exam active: " + answerSheet.isExamActive());
        
        // Test submission
        boolean submitResult = answerSheet.submitExam("STU_005");
        System.out.println("Exam submission: " + (submitResult ? "SUCCESS" : "FAILED"));
        
        // Test double submission
        boolean doubleSubmit = answerSheet.submitExam("STU_005");
        System.out.println("Double submission: " + (doubleSubmit ? "SUCCESS" : "FAILED"));
        
        // Test submission with wrong student ID
        boolean wrongStudent = answerSheet.submitExam("STU_006");
        System.out.println("Wrong student submission: " + (wrongStudent ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    /**
     * Test time-based exam scenarios
     */
    private static void testTimeBasedExam() {
        System.out.println("=== Time-Based Exam Test ===");
        
        // Create short exam session (1 minute)
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_006", 1);
        StudentAnswer answerSheet = new StudentAnswer("STU_006", "EXAM_006", session);
        
        System.out.println("Exam started: " + session.getStartTime());
        System.out.println("Exam ends: " + session.getEndTime());
        System.out.println("Is active: " + session.isActive());
        System.out.println("Remaining time: " + session.getRemainingTime() + " minutes");
        
        // Wait for exam to expire (in real scenario)
        System.out.println("Waiting for exam to expire...");
        
        // Test setting answer after expiration
        boolean result = answerSheet.setAnswer(1, "Answer 1", "STU_006");
        System.out.println("Set answer after expiration: " + (result ? "SUCCESS" : "FAILED"));
        
        // Test submission after expiration
        boolean submitResult = answerSheet.submitExam("STU_006");
        System.out.println("Submit after expiration: " + (submitResult ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    /**
     * Test comprehensive exam scenario
     */
    private static void testComprehensiveExam() {
        System.out.println("=== Comprehensive Exam Test ===");
        
        // Create exam session
        StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_007", 60);
        StudentAnswer answerSheet = new StudentAnswer("STU_007", "EXAM_007", session);
        
        // Create authorized evaluator
        StudentAnswer.ExamEvaluator evaluator = new StudentAnswer.ExamEvaluator("EVAL_003", "EVAL_AUTH_2024");
        
        // Set answers for all questions
        for (int i = 1; i <= 10; i++) {
            boolean success = answerSheet.setAnswer(i, "Answer " + i, "STU_007");
            System.out.println("Question " + i + ": " + (success ? "SUCCESS" : "FAILED"));
        }
        
        System.out.println("Total answers: " + answerSheet.getAnswerCount());
        
        // Submit exam
        boolean submitResult = answerSheet.submitExam("STU_007");
        System.out.println("Exam submission: " + (submitResult ? "SUCCESS" : "FAILED"));
        
        // Evaluate answers
        Map<Integer, String> allAnswers = answerSheet.getAllAnswers(evaluator);
        System.out.println("Evaluator accessed " + allAnswers.size() + " answers");
        
        // Check anti-cheating events
        List<StudentAnswer.AntiCheatingEvent> events = answerSheet.getAntiCheatingEvents(evaluator);
        System.out.println("Anti-cheating events recorded: " + events.size());
        
        // Display some events
        for (int i = 0; i < Math.min(5, events.size()); i++) {
            System.out.println("Event " + (i + 1) + ": " + events.get(i));
        }
        
        System.out.println();
    }
}
