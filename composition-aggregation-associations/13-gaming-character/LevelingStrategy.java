package composition.gaming;

/**
 * Strategy pattern for character leveling systems
 */
public interface LevelingStrategy {
    String getStrategyName();
    LevelingResult processExperience(GameCharacter character, int experienceGained);
    int getExperienceRequiredForLevel(int level);
    boolean shouldLevelUp(int currentExp, int currentLevel);
}

/**
 * Standard leveling strategy implementation
 */
class StandardLevelingStrategy implements LevelingStrategy {
    private static final int BASE_EXP_REQUIREMENT = 100;
    private static final double EXP_MULTIPLIER = 1.5;
    
    @Override
    public String getStrategyName() {
        return "Standard Leveling";
    }
    
    @Override
    public LevelingResult processExperience(GameCharacter character, int experienceGained) {
        int currentLevel = character.getLevel();
        int currentExp = character.getExperience();
        int newExp = currentExp + experienceGained;
        
        boolean leveledUp = false;
        int levelsGained = 0;
        
        while (shouldLevelUp(newExp, currentLevel + levelsGained)) {
            levelsGained++;
            int expRequired = getExperienceRequiredForLevel(currentLevel + levelsGained);
            newExp -= expRequired;
        }
        
        if (levelsGained > 0) {
            leveledUp = true;
            character.setLevel(currentLevel + levelsGained);
            character.setExperience(newExp);
            
            // Apply level up bonuses
            applyLevelUpBonuses(character, levelsGained);
        } else {
            character.setExperience(newExp);
        }
        
        String message = String.format("Gained %d experience%s", 
                                     experienceGained, 
                                     leveledUp ? " and leveled up " + levelsGained + " time(s)!" : "");
        
        return new LevelingResult(leveledUp, message, levelsGained, newExp);
    }
    
    @Override
    public int getExperienceRequiredForLevel(int level) {
        return (int) (BASE_EXP_REQUIREMENT * Math.pow(EXP_MULTIPLIER, level - 1));
    }
    
    @Override
    public boolean shouldLevelUp(int currentExp, int currentLevel) {
        return currentExp >= getExperienceRequiredForLevel(currentLevel + 1);
    }
    
    private void applyLevelUpBonuses(GameCharacter character, int levels) {
        for (int i = 0; i < levels; i++) {
            // Standard attribute increases per level
            character.increaseBaseAttribute(AttributeType.HEALTH, 10);
            character.increaseBaseAttribute(AttributeType.MANA, 5);
            character.increaseBaseAttribute(AttributeType.STRENGTH, 2);
            character.increaseBaseAttribute(AttributeType.DEXTERITY, 2);
            character.increaseBaseAttribute(AttributeType.INTELLIGENCE, 2);
            character.increaseBaseAttribute(AttributeType.CONSTITUTION, 1);
        }
    }
}

/**
 * Fast leveling strategy - lower experience requirements
 */
class FastLevelingStrategy implements LevelingStrategy {
    private static final int BASE_EXP_REQUIREMENT = 50;
    private static final double EXP_MULTIPLIER = 1.3;
    
    @Override
    public String getStrategyName() {
        return "Fast Leveling";
    }
    
    @Override
    public LevelingResult processExperience(GameCharacter character, int experienceGained) {
        int currentLevel = character.getLevel();
        int currentExp = character.getExperience();
        int newExp = currentExp + experienceGained;
        
        boolean leveledUp = false;
        int levelsGained = 0;
        
        while (shouldLevelUp(newExp, currentLevel + levelsGained)) {
            levelsGained++;
            int expRequired = getExperienceRequiredForLevel(currentLevel + levelsGained);
            newExp -= expRequired;
        }
        
        if (levelsGained > 0) {
            leveledUp = true;
            character.setLevel(currentLevel + levelsGained);
            character.setExperience(newExp);
            
            // Apply smaller level up bonuses for fast leveling
            applyLevelUpBonuses(character, levelsGained);
        } else {
            character.setExperience(newExp);
        }
        
        String message = String.format("Gained %d experience (fast leveling)%s", 
                                     experienceGained, 
                                     leveledUp ? " and leveled up " + levelsGained + " time(s)!" : "");
        
        return new LevelingResult(leveledUp, message, levelsGained, newExp);
    }
    
    @Override
    public int getExperienceRequiredForLevel(int level) {
        return (int) (BASE_EXP_REQUIREMENT * Math.pow(EXP_MULTIPLIER, level - 1));
    }
    
    @Override
    public boolean shouldLevelUp(int currentExp, int currentLevel) {
        return currentExp >= getExperienceRequiredForLevel(currentLevel + 1);
    }
    
    private void applyLevelUpBonuses(GameCharacter character, int levels) {
        for (int i = 0; i < levels; i++) {
            // Smaller bonuses for fast leveling
            character.increaseBaseAttribute(AttributeType.HEALTH, 8);
            character.increaseBaseAttribute(AttributeType.MANA, 4);
            character.increaseBaseAttribute(AttributeType.STRENGTH, 1);
            character.increaseBaseAttribute(AttributeType.DEXTERITY, 1);
            character.increaseBaseAttribute(AttributeType.INTELLIGENCE, 1);
            character.increaseBaseAttribute(AttributeType.CONSTITUTION, 1);
        }
    }
}

/**
 * Hardcore leveling strategy - higher experience requirements, bigger bonuses
 */
class HardcoreLevelingStrategy implements LevelingStrategy {
    private static final int BASE_EXP_REQUIREMENT = 200;
    private static final double EXP_MULTIPLIER = 2.0;
    
    @Override
    public String getStrategyName() {
        return "Hardcore Leveling";
    }
    
    @Override
    public LevelingResult processExperience(GameCharacter character, int experienceGained) {
        int currentLevel = character.getLevel();
        int currentExp = character.getExperience();
        int newExp = currentExp + experienceGained;
        
        boolean leveledUp = false;
        int levelsGained = 0;
        
        while (shouldLevelUp(newExp, currentLevel + levelsGained)) {
            levelsGained++;
            int expRequired = getExperienceRequiredForLevel(currentLevel + levelsGained);
            newExp -= expRequired;
        }
        
        if (levelsGained > 0) {
            leveledUp = true;
            character.setLevel(currentLevel + levelsGained);
            character.setExperience(newExp);
            
            // Apply larger level up bonuses for hardcore leveling
            applyLevelUpBonuses(character, levelsGained);
        } else {
            character.setExperience(newExp);
        }
        
        String message = String.format("Gained %d experience (hardcore mode)%s", 
                                     experienceGained, 
                                     leveledUp ? " and leveled up " + levelsGained + " time(s)!" : "");
        
        return new LevelingResult(leveledUp, message, levelsGained, newExp);
    }
    
    @Override
    public int getExperienceRequiredForLevel(int level) {
        return (int) (BASE_EXP_REQUIREMENT * Math.pow(EXP_MULTIPLIER, level - 1));
    }
    
    @Override
    public boolean shouldLevelUp(int currentExp, int currentLevel) {
        return currentExp >= getExperienceRequiredForLevel(currentLevel + 1);
    }
    
    private void applyLevelUpBonuses(GameCharacter character, int levels) {
        for (int i = 0; i < levels; i++) {
            // Larger bonuses for hardcore leveling
            character.increaseBaseAttribute(AttributeType.HEALTH, 15);
            character.increaseBaseAttribute(AttributeType.MANA, 8);
            character.increaseBaseAttribute(AttributeType.STRENGTH, 3);
            character.increaseBaseAttribute(AttributeType.DEXTERITY, 3);
            character.increaseBaseAttribute(AttributeType.INTELLIGENCE, 3);
            character.increaseBaseAttribute(AttributeType.CONSTITUTION, 2);
        }
    }
}
