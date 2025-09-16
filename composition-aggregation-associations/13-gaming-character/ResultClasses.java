package composition.gaming;

/**
 * Result classes for various character operations
 */

/**
 * Result of equipment operations
 */
class EquipResult {
    private final boolean success;
    private final String message;
    private final Item previousItem;
    
    public EquipResult(boolean success, String message, Item previousItem) {
        this.success = success;
        this.message = message;
        this.previousItem = previousItem;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Item getPreviousItem() { return previousItem; }
}

/**
 * Result of unequip operations
 */
class UnequipResult {
    private final boolean success;
    private final String message;
    private final Item unequippedItem;
    
    public UnequipResult(boolean success, String message, Item unequippedItem) {
        this.success = success;
        this.message = message;
        this.unequippedItem = unequippedItem;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Item getUnequippedItem() { return unequippedItem; }
}

/**
 * Result of combat operations
 */
class CombatResult {
    private final boolean success;
    private final String message;
    private final int damage;
    private final String specialEffect;
    
    public CombatResult(boolean success, String message, int damage, String specialEffect) {
        this.success = success;
        this.message = message;
        this.damage = damage;
        this.specialEffect = specialEffect;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getDamage() { return damage; }
    public String getSpecialEffect() { return specialEffect; }
}

/**
 * Result of skill usage
 */
class SkillResult {
    private final boolean success;
    private final String message;
    private final int effect;
    
    public SkillResult(boolean success, String message, int effect) {
        this.success = success;
        this.message = message;
        this.effect = effect;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public int getEffect() { return effect; }
}

/**
 * Result of movement operations
 */
class MovementResult {
    private final boolean success;
    private final String message;
    private final Position newPosition;
    
    public MovementResult(boolean success, String message, Position newPosition) {
        this.success = success;
        this.message = message;
        this.newPosition = newPosition;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Position getNewPosition() { return newPosition; }
}

/**
 * Result of leveling operations
 */
class LevelingResult {
    private final boolean leveledUp;
    private final String message;
    private final int levelsGained;
    private final int newExperience;
    
    public LevelingResult(boolean leveledUp, String message, int levelsGained, int newExperience) {
        this.leveledUp = leveledUp;
        this.message = message;
        this.levelsGained = levelsGained;
        this.newExperience = newExperience;
    }
    
    public boolean isLeveledUp() { return leveledUp; }
    public String getMessage() { return message; }
    public int getLevelsGained() { return levelsGained; }
    public int getNewExperience() { return newExperience; }
}
