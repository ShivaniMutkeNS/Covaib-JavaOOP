package composition.gaming;

/**
 * Strategy pattern for combat systems
 */
public interface CombatStrategy {
    String getStrategyName();
    CombatResult executeAttack(GameCharacter attacker, GameCharacter target);
    double getDamageMultiplier();
    boolean canCriticalHit();
}

/**
 * Standard combat strategy implementation
 */
class StandardCombatStrategy implements CombatStrategy {
    private static final double BASE_DAMAGE_MULTIPLIER = 1.0;
    private static final double CRIT_CHANCE = 0.1; // 10%
    private static final double CRIT_MULTIPLIER = 2.0;
    
    @Override
    public String getStrategyName() {
        return "Standard Combat";
    }
    
    @Override
    public CombatResult executeAttack(GameCharacter attacker, GameCharacter target) {
        int baseAttack = attacker.getCurrentAttribute(AttributeType.STRENGTH);
        int weaponDamage = attacker.getEquipment().getAllItems().stream()
                .mapToInt(Item::getWeaponDamage)
                .sum();
        
        int totalDamage = (int) ((baseAttack + weaponDamage) * getDamageMultiplier());
        
        // Check for critical hit
        boolean isCritical = canCriticalHit() && Math.random() < CRIT_CHANCE;
        if (isCritical) {
            totalDamage = (int) (totalDamage * CRIT_MULTIPLIER);
        }
        
        String message = String.format("Standard attack for %d damage%s", 
                                     totalDamage, isCritical ? " (CRITICAL!)" : "");
        
        return new CombatResult(true, message, totalDamage, isCritical ? "critical" : null);
    }
    
    @Override
    public double getDamageMultiplier() {
        return BASE_DAMAGE_MULTIPLIER;
    }
    
    @Override
    public boolean canCriticalHit() {
        return true;
    }
}

/**
 * Berserker combat strategy - high damage, low accuracy
 */
class BerserkerCombatStrategy implements CombatStrategy {
    private static final double DAMAGE_MULTIPLIER = 1.5;
    private static final double HIT_CHANCE = 0.8; // 80% hit chance
    
    @Override
    public String getStrategyName() {
        return "Berserker Combat";
    }
    
    @Override
    public CombatResult executeAttack(GameCharacter attacker, GameCharacter target) {
        // Check if attack hits
        if (Math.random() > HIT_CHANCE) {
            return new CombatResult(false, "Berserker attack missed!", 0, null);
        }
        
        int baseAttack = attacker.getCurrentAttribute(AttributeType.STRENGTH);
        int weaponDamage = attacker.getEquipment().getAllItems().stream()
                .mapToInt(Item::getWeaponDamage)
                .sum();
        
        int totalDamage = (int) ((baseAttack + weaponDamage) * getDamageMultiplier());
        
        String message = String.format("Berserker attack for %d damage!", totalDamage);
        return new CombatResult(true, message, totalDamage, "berserker");
    }
    
    @Override
    public double getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }
    
    @Override
    public boolean canCriticalHit() {
        return false; // Berserker doesn't crit, but has high base damage
    }
}

/**
 * Defensive combat strategy - lower damage, higher accuracy
 */
class DefensiveCombatStrategy implements CombatStrategy {
    private static final double DAMAGE_MULTIPLIER = 0.8;
    private static final double HIT_CHANCE = 0.95; // 95% hit chance
    
    @Override
    public String getStrategyName() {
        return "Defensive Combat";
    }
    
    @Override
    public CombatResult executeAttack(GameCharacter attacker, GameCharacter target) {
        // Very high hit chance
        if (Math.random() > HIT_CHANCE) {
            return new CombatResult(false, "Defensive attack missed!", 0, null);
        }
        
        int baseAttack = attacker.getCurrentAttribute(AttributeType.STRENGTH);
        int weaponDamage = attacker.getEquipment().getAllItems().stream()
                .mapToInt(Item::getWeaponDamage)
                .sum();
        
        int totalDamage = (int) ((baseAttack + weaponDamage) * getDamageMultiplier());
        
        String message = String.format("Defensive attack for %d damage", totalDamage);
        return new CombatResult(true, message, totalDamage, "defensive");
    }
    
    @Override
    public double getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }
    
    @Override
    public boolean canCriticalHit() {
        return true;
    }
}
