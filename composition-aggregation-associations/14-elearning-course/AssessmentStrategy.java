package composition.elearning;

/**
 * Strategy pattern for assessment management
 */
public interface AssessmentStrategy {
    String getStrategyName();
    AssessmentResult conductAssessment(Student student, Assessment assessment, StudentProgress progress);
    boolean isRetakeAllowed(Assessment assessment, StudentProgress progress);
    double calculateFinalScore(Assessment assessment, StudentProgress progress);
}

/**
 * Standard assessment strategy implementation
 */
class StandardAssessmentStrategy implements AssessmentStrategy {
    
    @Override
    public String getStrategyName() {
        return "Standard Assessment";
    }
    
    @Override
    public AssessmentResult conductAssessment(Student student, Assessment assessment, StudentProgress progress) {
        if (!assessment.isAccessible(student, progress)) {
            return new AssessmentResult(false, "Assessment not accessible", 0, null);
        }
        
        // Simulate assessment taking
        double score = simulateAssessmentScore(student, assessment);
        boolean passed = score >= assessment.getPassingScore();
        
        String feedback = generateFeedback(score, assessment.getPassingScore(), passed);
        
        return new AssessmentResult(true, "Assessment completed", score, feedback);
    }
    
    @Override
    public boolean isRetakeAllowed(Assessment assessment, StudentProgress progress) {
        int attemptCount = progress.getAssessmentAttemptCount(assessment.getAssessmentId());
        return attemptCount < assessment.getMaxAttempts();
    }
    
    @Override
    public double calculateFinalScore(Assessment assessment, StudentProgress progress) {
        return progress.getAssessmentScore(assessment.getAssessmentId());
    }
    
    private double simulateAssessmentScore(Student student, Assessment assessment) {
        // Simulate scoring based on student performance and assessment difficulty
        double baseScore = 60 + Math.random() * 40; // 60-100 range
        
        // Adjust based on assessment type
        switch (assessment.getType()) {
            case QUIZ:
                baseScore += 5; // Quizzes tend to score higher
                break;
            case EXAM:
                baseScore -= 5; // Exams are more challenging
                break;
            case PROJECT:
                baseScore += Math.random() * 10 - 5; // More variable
                break;
        }
        
        return Math.min(assessment.getMaxScore(), Math.max(0, baseScore));
    }
    
    private String generateFeedback(double score, int passingScore, boolean passed) {
        if (passed) {
            if (score >= passingScore * 1.5) {
                return "Excellent work! You've demonstrated mastery of the material.";
            } else if (score >= passingScore * 1.2) {
                return "Good job! You've shown solid understanding.";
            } else {
                return "You passed! Consider reviewing the material for better understanding.";
            }
        } else {
            return "You didn't pass this time. Please review the material and try again.";
        }
    }
}

/**
 * Adaptive assessment strategy - adjusts difficulty based on performance
 */
class AdaptiveAssessmentStrategy implements AssessmentStrategy {
    
    @Override
    public String getStrategyName() {
        return "Adaptive Assessment";
    }
    
    @Override
    public AssessmentResult conductAssessment(Student student, Assessment assessment, StudentProgress progress) {
        if (!assessment.isAccessible(student, progress)) {
            return new AssessmentResult(false, "Assessment not accessible", 0, null);
        }
        
        // Adaptive scoring based on previous performance
        double averageScore = progress.getAverageAssessmentScore();
        double adaptiveScore = simulateAdaptiveScore(student, assessment, averageScore);
        boolean passed = adaptiveScore >= assessment.getPassingScore();
        
        String feedback = generateAdaptiveFeedback(adaptiveScore, averageScore, passed);
        
        return new AssessmentResult(true, "Adaptive assessment completed", adaptiveScore, feedback);
    }
    
    @Override
    public boolean isRetakeAllowed(Assessment assessment, StudentProgress progress) {
        // More lenient retake policy for adaptive assessments
        int attemptCount = progress.getAssessmentAttemptCount(assessment.getAssessmentId());
        return attemptCount < assessment.getMaxAttempts() + 1;
    }
    
    @Override
    public double calculateFinalScore(Assessment assessment, StudentProgress progress) {
        // Take the best score for adaptive assessments
        return progress.getAssessmentAttempts(assessment.getAssessmentId()).stream()
                .mapToDouble(attempt -> attempt.getScore())
                .max()
                .orElse(0.0);
    }
    
    private double simulateAdaptiveScore(Student student, Assessment assessment, double averageScore) {
        double baseScore = 50 + Math.random() * 50;
        
        // Adjust based on previous performance
        if (averageScore > 80) {
            baseScore += 10; // High performers get bonus
        } else if (averageScore < 60) {
            baseScore += 5; // Struggling students get slight boost
        }
        
        return Math.min(assessment.getMaxScore(), Math.max(0, baseScore));
    }
    
    private String generateAdaptiveFeedback(double score, double averageScore, boolean passed) {
        if (passed) {
            if (score > averageScore) {
                return "Great improvement! You're showing consistent progress.";
            } else {
                return "You passed! The system has adapted to your learning pace.";
            }
        } else {
            return "The assessment has been adapted to your level. Keep practicing and try again.";
        }
    }
}

/**
 * Peer review assessment strategy
 */
class PeerReviewAssessmentStrategy implements AssessmentStrategy {
    
    @Override
    public String getStrategyName() {
        return "Peer Review Assessment";
    }
    
    @Override
    public AssessmentResult conductAssessment(Student student, Assessment assessment, StudentProgress progress) {
        if (!assessment.isAccessible(student, progress)) {
            return new AssessmentResult(false, "Assessment not accessible", 0, null);
        }
        
        // Simulate peer review scoring
        double peerScore = simulatePeerReviewScore(assessment);
        boolean passed = peerScore >= assessment.getPassingScore();
        
        String feedback = "Your work has been reviewed by peers. " + 
                         (passed ? "Great collaboration and quality work!" : 
                          "Consider incorporating peer feedback for improvement.");
        
        return new AssessmentResult(true, "Peer review assessment completed", peerScore, feedback);
    }
    
    @Override
    public boolean isRetakeAllowed(Assessment assessment, StudentProgress progress) {
        // Limited retakes for peer review
        int attemptCount = progress.getAssessmentAttemptCount(assessment.getAssessmentId());
        return attemptCount < 2;
    }
    
    @Override
    public double calculateFinalScore(Assessment assessment, StudentProgress progress) {
        return progress.getAssessmentScore(assessment.getAssessmentId());
    }
    
    private double simulatePeerReviewScore(Assessment assessment) {
        // Simulate multiple peer ratings
        double totalScore = 0;
        int reviewCount = 3 + (int)(Math.random() * 3); // 3-5 reviews
        
        for (int i = 0; i < reviewCount; i++) {
            totalScore += 60 + Math.random() * 40; // Individual peer scores
        }
        
        double averageScore = totalScore / reviewCount;
        return Math.min(assessment.getMaxScore(), Math.max(0, averageScore));
    }
}
