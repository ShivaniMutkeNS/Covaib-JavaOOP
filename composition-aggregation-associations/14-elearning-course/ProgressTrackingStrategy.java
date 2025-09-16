package composition.elearning;

import java.util.*;

/**
 * Strategy pattern for progress tracking management
 */
public interface ProgressTrackingStrategy {
    String getStrategyName();
    ProgressSummary calculateProgress(StudentProgress progress, List<Module> modules);
    void updateProgress(StudentProgress progress, Assessment assessment);
    boolean isCourseCompleted(StudentProgress progress, List<Module> modules);
    List<String> getRecommendations(StudentProgress progress, List<Module> modules);
}

/**
 * Detailed progress tracking strategy - tracks everything
 */
class DetailedProgressTrackingStrategy implements ProgressTrackingStrategy {
    
    @Override
    public String getStrategyName() {
        return "Detailed Progress Tracking";
    }
    
    @Override
    public ProgressSummary calculateProgress(StudentProgress progress, List<Module> modules) {
        int totalContent = 0;
        int completedContent = 0;
        int totalAssessments = 0;
        int completedAssessments = 0;
        
        for (Module module : modules) {
            totalContent += module.getTotalContentCount();
            totalAssessments += module.getTotalAssessmentCount();
            
            for (Content content : module.getContents()) {
                if (progress.isContentCompleted(content.getContentId())) {
                    completedContent++;
                }
            }
            
            for (Assessment assessment : module.getAssessments()) {
                if (progress.getAssessmentResults().containsKey(assessment.getAssessmentId())) {
                    completedAssessments++;
                }
            }
        }
        
        double contentProgress = totalContent > 0 ? (double) completedContent / totalContent * 100 : 0;
        double assessmentProgress = totalAssessments > 0 ? (double) completedAssessments / totalAssessments * 100 : 0;
        double overallProgress = (contentProgress + assessmentProgress) / 2;
        
        return new ProgressSummary(
            overallProgress,
            contentProgress,
            assessmentProgress,
            completedContent,
            totalContent,
            completedAssessments,
            totalAssessments,
            progress.getAverageAssessmentScore(),
            progress.getTotalStudyTime()
        );
    }
    
    @Override
    public void updateProgress(StudentProgress progress, Assessment assessment) {
        // Update module progress based on assessment completion
        if (assessment.getModule() != null) {
            String moduleId = assessment.getModule().getModuleId();
            Module module = assessment.getModule();
            
            // Check if all assessments in module are completed
            boolean allAssessmentsCompleted = module.getAssessments().stream()
                    .allMatch(a -> progress.getAssessmentResults().containsKey(a.getAssessmentId()));
            
            // Check if all content in module is completed
            boolean allContentCompleted = module.getContents().stream()
                    .allMatch(c -> progress.isContentCompleted(c.getContentId()));
            
            if (allAssessmentsCompleted && allContentCompleted) {
                progress.updateModuleProgress(moduleId, ProgressStatus.COMPLETED);
            } else {
                progress.updateModuleProgress(moduleId, ProgressStatus.IN_PROGRESS);
            }
        }
    }
    
    @Override
    public boolean isCourseCompleted(StudentProgress progress, List<Module> modules) {
        for (Module module : modules) {
            if (!module.isRequired()) continue;
            
            // Check all required content is completed
            for (Content content : module.getContents()) {
                if (content.isRequired() && !progress.isContentCompleted(content.getContentId())) {
                    return false;
                }
            }
            
            // Check all required assessments are passed
            for (Assessment assessment : module.getAssessments()) {
                if (assessment.isRequired()) {
                    double score = progress.getAssessmentScore(assessment.getAssessmentId());
                    if (score < assessment.getPassingScore()) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    @Override
    public List<String> getRecommendations(StudentProgress progress, List<Module> modules) {
        List<String> recommendations = new ArrayList<>();
        
        double averageScore = progress.getAverageAssessmentScore();
        if (averageScore < 70) {
            recommendations.add("Consider reviewing completed content to improve understanding");
        }
        
        // Check for incomplete modules
        for (Module module : modules) {
            ProgressStatus status = progress.getModuleProgress(module.getModuleId());
            if (status == ProgressStatus.NOT_STARTED) {
                recommendations.add("Start working on module: " + module.getTitle());
                break; // Only recommend one module at a time
            } else if (status == ProgressStatus.IN_PROGRESS) {
                recommendations.add("Continue working on module: " + module.getTitle());
                break;
            }
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("Great progress! Keep up the excellent work!");
        }
        
        return recommendations;
    }
}

/**
 * Simple progress tracking strategy - basic completion tracking
 */
class SimpleProgressTrackingStrategy implements ProgressTrackingStrategy {
    
    @Override
    public String getStrategyName() {
        return "Simple Progress Tracking";
    }
    
    @Override
    public ProgressSummary calculateProgress(StudentProgress progress, List<Module> modules) {
        int totalItems = 0;
        int completedItems = 0;
        
        for (Module module : modules) {
            totalItems += module.getTotalContentCount() + module.getTotalAssessmentCount();
            
            completedItems += (int) module.getContents().stream()
                    .filter(content -> progress.isContentCompleted(content.getContentId()))
                    .count();
            
            completedItems += (int) module.getAssessments().stream()
                    .filter(assessment -> progress.getAssessmentResults().containsKey(assessment.getAssessmentId()))
                    .count();
        }
        
        double overallProgress = totalItems > 0 ? (double) completedItems / totalItems * 100 : 0;
        
        return new ProgressSummary(
            overallProgress,
            overallProgress, // Same for content and assessment
            overallProgress,
            completedItems,
            totalItems,
            0, 0, // Simplified - don't track separately
            progress.getAverageAssessmentScore(),
            progress.getTotalStudyTime()
        );
    }
    
    @Override
    public void updateProgress(StudentProgress progress, Assessment assessment) {
        // Simple update - just mark module as in progress
        if (assessment.getModule() != null) {
            String moduleId = assessment.getModule().getModuleId();
            if (progress.getModuleProgress(moduleId) == ProgressStatus.NOT_STARTED) {
                progress.updateModuleProgress(moduleId, ProgressStatus.IN_PROGRESS);
            }
        }
    }
    
    @Override
    public boolean isCourseCompleted(StudentProgress progress, List<Module> modules) {
        // Simple completion check - just required assessments
        for (Module module : modules) {
            for (Assessment assessment : module.getAssessments()) {
                if (assessment.isRequired()) {
                    double score = progress.getAssessmentScore(assessment.getAssessmentId());
                    if (score < assessment.getPassingScore()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public List<String> getRecommendations(StudentProgress progress, List<Module> modules) {
        List<String> recommendations = new ArrayList<>();
        recommendations.add("Continue with your learning journey!");
        return recommendations;
    }
}

/**
 * Gamified progress tracking strategy - includes achievements and milestones
 */
class GamifiedProgressTrackingStrategy implements ProgressTrackingStrategy {
    
    @Override
    public String getStrategyName() {
        return "Gamified Progress Tracking";
    }
    
    @Override
    public ProgressSummary calculateProgress(StudentProgress progress, List<Module> modules) {
        DetailedProgressTrackingStrategy detailedStrategy = new DetailedProgressTrackingStrategy();
        ProgressSummary baseProgress = detailedStrategy.calculateProgress(progress, modules);
        
        // Add gamification elements
        List<String> achievements = calculateAchievements(progress, modules);
        int experiencePoints = calculateExperiencePoints(progress);
        int level = calculateLevel(experiencePoints);
        
        // Create enhanced progress summary with gamification
        return new GamifiedProgressSummary(
            baseProgress.getCompletionPercentage(),
            baseProgress.getContentProgress(),
            baseProgress.getAssessmentProgress(),
            baseProgress.getCompletedContent(),
            baseProgress.getTotalContent(),
            baseProgress.getCompletedAssessments(),
            baseProgress.getTotalAssessments(),
            baseProgress.getAverageScore(),
            baseProgress.getStudyTime(),
            achievements,
            experiencePoints,
            level
        );
    }
    
    @Override
    public void updateProgress(StudentProgress progress, Assessment assessment) {
        // Standard update plus achievement checking
        DetailedProgressTrackingStrategy detailedStrategy = new DetailedProgressTrackingStrategy();
        detailedStrategy.updateProgress(progress, assessment);
        
        // Check for new achievements
        checkForAchievements(progress, assessment);
    }
    
    @Override
    public boolean isCourseCompleted(StudentProgress progress, List<Module> modules) {
        DetailedProgressTrackingStrategy detailedStrategy = new DetailedProgressTrackingStrategy();
        return detailedStrategy.isCourseCompleted(progress, modules);
    }
    
    @Override
    public List<String> getRecommendations(StudentProgress progress, List<Module> modules) {
        List<String> recommendations = new ArrayList<>();
        
        int experiencePoints = calculateExperiencePoints(progress);
        int currentLevel = calculateLevel(experiencePoints);
        int nextLevelXP = (currentLevel + 1) * 1000;
        int xpNeeded = nextLevelXP - experiencePoints;
        
        recommendations.add("You're level " + currentLevel + "! " + xpNeeded + " XP to next level.");
        
        double averageScore = progress.getAverageAssessmentScore();
        if (averageScore > 90) {
            recommendations.add("🏆 You're a star performer! Keep up the excellent work!");
        } else if (averageScore > 80) {
            recommendations.add("⭐ Great job! You're doing really well!");
        }
        
        return recommendations;
    }
    
    private List<String> calculateAchievements(StudentProgress progress, List<Module> modules) {
        List<String> achievements = new ArrayList<>();
        
        if (progress.getCompletedContentCount() >= 10) {
            achievements.add("Content Consumer - Completed 10+ content items");
        }
        
        if (progress.getAverageAssessmentScore() > 90) {
            achievements.add("High Achiever - Maintained 90+ average score");
        }
        
        if (progress.getCompletedAssessmentCount() >= 5) {
            achievements.add("Assessment Master - Completed 5+ assessments");
        }
        
        return achievements;
    }
    
    private int calculateExperiencePoints(StudentProgress progress) {
        int xp = 0;
        xp += progress.getCompletedContentCount() * 50; // 50 XP per content
        xp += progress.getCompletedAssessmentCount() * 100; // 100 XP per assessment
        xp += (int) (progress.getAverageAssessmentScore() * 10); // Bonus for high scores
        return xp;
    }
    
    private int calculateLevel(int experiencePoints) {
        return Math.max(1, experiencePoints / 1000); // 1000 XP per level
    }
    
    private void checkForAchievements(StudentProgress progress, Assessment assessment) {
        // This would trigger achievement notifications in a real system
        double score = progress.getAssessmentScore(assessment.getAssessmentId());
        if (score == assessment.getMaxScore()) {
            // Perfect score achievement
        }
    }
}
