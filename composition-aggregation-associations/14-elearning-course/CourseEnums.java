package composition.elearning;

/**
 * Enums for the E-learning Course Management System
 */

public enum CourseCategory {
    PROGRAMMING("Programming"),
    DATA_SCIENCE("Data Science"),
    WEB_DEVELOPMENT("Web Development"),
    MOBILE_DEVELOPMENT("Mobile Development"),
    MACHINE_LEARNING("Machine Learning"),
    CYBERSECURITY("Cybersecurity"),
    CLOUD_COMPUTING("Cloud Computing"),
    DEVOPS("DevOps"),
    BUSINESS("Business"),
    DESIGN("Design"),
    MATHEMATICS("Mathematics"),
    LANGUAGE("Language");
    
    private final String displayName;
    
    CourseCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum CourseDifficulty {
    BEGINNER("Beginner", 1),
    INTERMEDIATE("Intermediate", 2),
    ADVANCED("Advanced", 3),
    EXPERT("Expert", 4);
    
    private final String displayName;
    private final int level;
    
    CourseDifficulty(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
}

public enum CourseState {
    DRAFT("Draft"),
    PUBLISHED("Published"),
    ARCHIVED("Archived"),
    SUSPENDED("Suspended");
    
    private final String displayName;
    
    CourseState(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum ContentType {
    VIDEO("Video"),
    TEXT("Text"),
    AUDIO("Audio"),
    INTERACTIVE("Interactive"),
    QUIZ("Quiz"),
    ASSIGNMENT("Assignment"),
    DOCUMENT("Document"),
    PRESENTATION("Presentation");
    
    private final String displayName;
    
    ContentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum AssessmentType {
    QUIZ("Quiz"),
    ASSIGNMENT("Assignment"),
    PROJECT("Project"),
    EXAM("Exam"),
    PEER_REVIEW("Peer Review"),
    PRACTICAL("Practical");
    
    private final String displayName;
    
    AssessmentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

public enum ProgressStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    SKIPPED("Skipped");
    
    private final String displayName;
    
    ProgressStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
