/**
 * Enumeration of autonomous driving levels based on SAE J3016 standard
 * Defines the level of automation and human intervention required
 */
public enum AutonomyLevel {
    LEVEL_0("No Automation", "Human driver performs all driving tasks", false, true, false),
    LEVEL_1("Driver Assistance", "Single automated system assists driver", false, true, false),
    LEVEL_2("Partial Automation", "Multiple systems work together, driver monitors", false, true, true),
    LEVEL_3("Conditional Automation", "System handles driving, human ready to intervene", true, true, true),
    LEVEL_4("High Automation", "System handles all driving in specific conditions", true, false, true),
    LEVEL_5("Full Automation", "Complete autonomous operation in all conditions", true, false, true);
    
    private final String displayName;
    private final String description;
    private final boolean systemMonitorsEnvironment;
    private final boolean humanSupervisionRequired;
    private final boolean hasAdvancedSensors;
    
    AutonomyLevel(String displayName, String description, boolean systemMonitorsEnvironment,
                  boolean humanSupervisionRequired, boolean hasAdvancedSensors) {
        this.displayName = displayName;
        this.description = description;
        this.systemMonitorsEnvironment = systemMonitorsEnvironment;
        this.humanSupervisionRequired = humanSupervisionRequired;
        this.hasAdvancedSensors = hasAdvancedSensors;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean systemMonitorsEnvironment() {
        return systemMonitorsEnvironment;
    }
    
    public boolean requiresHumanSupervision() {
        return humanSupervisionRequired;
    }
    
    public boolean hasAdvancedSensors() {
        return hasAdvancedSensors;
    }
    
    public boolean isFullyAutonomous() {
        return this == LEVEL_4 || this == LEVEL_5;
    }
    
    public boolean canOperateWithoutDriver() {
        return this == LEVEL_5;
    }
    
    public int getNumericLevel() {
        switch (this) {
            case LEVEL_0: return 0;
            case LEVEL_1: return 1;
            case LEVEL_2: return 2;
            case LEVEL_3: return 3;
            case LEVEL_4: return 4;
            case LEVEL_5: return 5;
            default: return 0;
        }
    }
    
    public String getSafetyRating() {
        switch (this) {
            case LEVEL_0:
            case LEVEL_1: return "Standard";
            case LEVEL_2: return "Enhanced";
            case LEVEL_3: return "Advanced";
            case LEVEL_4: return "Superior";
            case LEVEL_5: return "Ultimate";
            default: return "Unknown";
        }
    }
    
    @Override
    public String toString() {
        return "Level " + getNumericLevel() + ": " + displayName;
    }
}
