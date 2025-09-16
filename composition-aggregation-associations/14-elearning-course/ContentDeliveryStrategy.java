package composition.elearning;

/**
 * Strategy pattern for content delivery management
 */
public interface ContentDeliveryStrategy {
    String getStrategyName();
    ContentDeliveryResult deliverContent(Student student, Module module, StudentProgress progress);
    boolean isContentAvailable(Student student, Content content, StudentProgress progress);
    ContentRecommendation getNextRecommendation(Student student, Module module, StudentProgress progress);
}

/**
 * Sequential content delivery strategy - content must be completed in order
 */
class SequentialContentDeliveryStrategy implements ContentDeliveryStrategy {
    
    @Override
    public String getStrategyName() {
        return "Sequential Content Delivery";
    }
    
    @Override
    public ContentDeliveryResult deliverContent(Student student, Module module, StudentProgress progress) {
        // Find the next content to deliver
        for (Content content : module.getContents()) {
            if (!progress.isContentCompleted(content.getContentId())) {
                if (isContentAvailable(student, content, progress)) {
                    return new ContentDeliveryResult(true, "Content delivered", content, 
                                                   "Next content in sequence");
                } else {
                    return new ContentDeliveryResult(false, "Prerequisites not met", null, 
                                                   "Complete previous content first");
                }
            }
        }
        
        return new ContentDeliveryResult(false, "All content completed", null, 
                                       "Module content completed");
    }
    
    @Override
    public boolean isContentAvailable(Student student, Content content, StudentProgress progress) {
        return content.isAccessible(student, progress);
    }
    
    @Override
    public ContentRecommendation getNextRecommendation(Student student, Module module, StudentProgress progress) {
        for (Content content : module.getContents()) {
            if (!progress.isContentCompleted(content.getContentId()) && 
                isContentAvailable(student, content, progress)) {
                return new ContentRecommendation(content, "Next in sequence", 1.0);
            }
        }
        return null;
    }
}

/**
 * Adaptive content delivery strategy - personalizes based on learning style and performance
 */
class AdaptiveContentDeliveryStrategy implements ContentDeliveryStrategy {
    
    @Override
    public String getStrategyName() {
        return "Adaptive Content Delivery";
    }
    
    @Override
    public ContentDeliveryResult deliverContent(Student student, Module module, StudentProgress progress) {
        ContentRecommendation recommendation = getNextRecommendation(student, module, progress);
        
        if (recommendation != null) {
            Content content = recommendation.getContent();
            return new ContentDeliveryResult(true, "Adaptive content delivered", content, 
                                           recommendation.getReason());
        }
        
        return new ContentDeliveryResult(false, "No suitable content found", null, 
                                       "All available content completed");
    }
    
    @Override
    public boolean isContentAvailable(Student student, Content content, StudentProgress progress) {
        // More flexible availability based on student performance
        if (content.isAccessible(student, progress)) {
            return true;
        }
        
        // Allow skipping if student is performing well
        double averageScore = progress.getAverageAssessmentScore();
        if (averageScore > 85 && !content.isRequired()) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public ContentRecommendation getNextRecommendation(Student student, Module module, StudentProgress progress) {
        String learningStyle = student.getPreferences().getLearningStyle();
        double averageScore = progress.getAverageAssessmentScore();
        
        // Find content that matches learning style and difficulty
        for (Content content : module.getContents()) {
            if (!progress.isContentCompleted(content.getContentId()) && 
                isContentAvailable(student, content, progress)) {
                
                double suitabilityScore = calculateSuitability(content, learningStyle, averageScore);
                String reason = generateRecommendationReason(content, learningStyle, averageScore);
                
                return new ContentRecommendation(content, reason, suitabilityScore);
            }
        }
        
        return null;
    }
    
    private double calculateSuitability(Content content, String learningStyle, double averageScore) {
        double score = 0.5; // Base score
        
        // Adjust based on learning style
        if (learningStyle.equals("Visual") && content.getType() == ContentType.VIDEO) {
            score += 0.3;
        } else if (learningStyle.equals("Auditory") && content.getType() == ContentType.AUDIO) {
            score += 0.3;
        } else if (learningStyle.equals("Kinesthetic") && content.getType() == ContentType.INTERACTIVE) {
            score += 0.3;
        }
        
        // Adjust based on performance
        if (averageScore > 80) {
            score += 0.2; // High performers get challenging content
        } else if (averageScore < 60) {
            score += 0.1; // Struggling students get easier content
        }
        
        return Math.min(1.0, score);
    }
    
    private String generateRecommendationReason(Content content, String learningStyle, double averageScore) {
        StringBuilder reason = new StringBuilder("Recommended based on ");
        
        if (learningStyle.equals("Visual") && content.getType() == ContentType.VIDEO) {
            reason.append("visual learning preference");
        } else if (learningStyle.equals("Auditory") && content.getType() == ContentType.AUDIO) {
            reason.append("auditory learning preference");
        } else {
            reason.append("learning progression");
        }
        
        if (averageScore > 80) {
            reason.append(" and strong performance");
        } else if (averageScore < 60) {
            reason.append(" and need for reinforcement");
        }
        
        return reason.toString();
    }
}

/**
 * Flexible content delivery strategy - allows free navigation
 */
class FlexibleContentDeliveryStrategy implements ContentDeliveryStrategy {
    
    @Override
    public String getStrategyName() {
        return "Flexible Content Delivery";
    }
    
    @Override
    public ContentDeliveryResult deliverContent(Student student, Module module, StudentProgress progress) {
        // Allow access to any content
        for (Content content : module.getContents()) {
            if (!progress.isContentCompleted(content.getContentId())) {
                return new ContentDeliveryResult(true, "Content available for access", content, 
                                               "Free navigation allowed");
            }
        }
        
        return new ContentDeliveryResult(false, "All content completed", null, 
                                       "Module completed");
    }
    
    @Override
    public boolean isContentAvailable(Student student, Content content, StudentProgress progress) {
        // All content is available in flexible mode
        return true;
    }
    
    @Override
    public ContentRecommendation getNextRecommendation(Student student, Module module, StudentProgress progress) {
        // Recommend shortest content first
        return module.getContents().stream()
                .filter(content -> !progress.isContentCompleted(content.getContentId()))
                .min((c1, c2) -> Integer.compare(c1.getDurationMinutes(), c2.getDurationMinutes()))
                .map(content -> new ContentRecommendation(content, "Shortest remaining content", 0.8))
                .orElse(null);
    }
}
