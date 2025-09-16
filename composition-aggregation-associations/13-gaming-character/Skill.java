package composition.gaming;

import java.util.*;

/**
 * Base skill interface for character abilities
 */
public interface Skill {
    String getName();
    String getDescription();
    SkillType getType();
    int getManaCost();
    int getCooldown();
    int getRequiredLevel();
    boolean canUse(GameCharacter character);
    SkillResult use(GameCharacter caster, GameCharacter target);
    String getUsageRequirement();
}

/**
 * Abstract base skill implementation
 */
abstract class BaseSkill implements Skill {
    protected final String name;
    protected final String description;
    protected final SkillType type;
    protected final int manaCost;
    protected final int cooldown;
    protected final int requiredLevel;
    protected long lastUsed;
    
    public BaseSkill(String name, String description, SkillType type, 
                    int manaCost, int cooldown, int requiredLevel) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.requiredLevel = requiredLevel;
        this.lastUsed = 0;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return description; }
    
    @Override
    public SkillType getType() { return type; }
    
    @Override
    public int getManaCost() { return manaCost; }
    
    @Override
    public int getCooldown() { return cooldown; }
    
    @Override
    public int getRequiredLevel() { return requiredLevel; }
    
    @Override
    public boolean canUse(GameCharacter character) {
        // Check level requirement
        if (character.getLevel() < requiredLevel) {
            return false;
        }
        
        // Check mana cost
        if (character.getCurrentAttribute(AttributeType.MANA) < manaCost) {
            return false;
        }
        
        // Check cooldown
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUsed < cooldown * 1000L) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getUsageRequirement() {
        if (!canUse(null)) {
            return "Level " + requiredLevel + ", " + manaCost + " mana, " + cooldown + "s cooldown";
        }
        return "";
    }
    
    protected void consumeResources(GameCharacter character) {
        int currentMana = character.getCurrentAttribute(AttributeType.MANA);
        character.setCurrentAttribute(AttributeType.MANA, currentMana - manaCost);
        lastUsed = System.currentTimeMillis();
    }
}

/**
 * Attack skill implementation
 */
class AttackSkill extends BaseSkill {
    private final int baseDamage;
    private final double damageMultiplier;
    
    public AttackSkill(String name, int manaCost, int cooldown, int requiredLevel) {
        super(name, "Physical attack skill", SkillType.ATTACK, manaCost, cooldown, requiredLevel);
        this.baseDamage = 15;
        this.damageMultiplier = 1.2;
    }
    
    @Override
    public SkillResult use(GameCharacter caster, GameCharacter target) {
        if (!canUse(caster)) {
            return new SkillResult(false, "Cannot use skill", 0);
        }
        
        consumeResources(caster);
        
        int damage = (int) (baseDamage + caster.getCurrentAttribute(AttributeType.STRENGTH) * damageMultiplier);
        target.takeDamage(damage);
        
        return new SkillResult(true, "Used " + name + " for " + damage + " damage", damage);
    }
}

/**
 * Healing skill implementation
 */
class HealingSkill extends BaseSkill {
    private final int baseHealing;
    private final double healingMultiplier;
    
    public HealingSkill(String name, int manaCost, int cooldown, int requiredLevel) {
        super(name, "Healing spell", SkillType.HEALING, manaCost, cooldown, requiredLevel);
        this.baseHealing = 25;
        this.healingMultiplier = 1.0;
    }
    
    @Override
    public SkillResult use(GameCharacter caster, GameCharacter target) {
        if (!canUse(caster)) {
            return new SkillResult(false, "Cannot use skill", 0);
        }
        
        consumeResources(caster);
        
        int healing = (int) (baseHealing + caster.getCurrentAttribute(AttributeType.INTELLIGENCE) * healingMultiplier);
        target.heal(healing);
        
        return new SkillResult(true, "Used " + name + " for " + healing + " healing", healing);
    }
}

/**
 * Buff skill implementation
 */
class BuffSkill extends BaseSkill {
    private final AttributeType buffAttribute;
    private final int buffAmount;
    private final int duration;
    
    public BuffSkill(String name, int manaCost, int cooldown, int requiredLevel, 
                    AttributeType buffAttribute, int buffAmount, int duration) {
        super(name, "Buff spell", SkillType.BUFF, manaCost, cooldown, requiredLevel);
        this.buffAttribute = buffAttribute;
        this.buffAmount = buffAmount;
        this.duration = duration;
    }
    
    @Override
    public SkillResult use(GameCharacter caster, GameCharacter target) {
        if (!canUse(caster)) {
            return new SkillResult(false, "Cannot use skill", 0);
        }
        
        consumeResources(caster);
        
        StatusEffect buff = new StatusEffect(name + " Buff", StatusEffectType.BUFF, 
                                           duration, buffAttribute, buffAmount);
        target.addStatusEffect(buff);
        
        return new SkillResult(true, "Applied " + name + " buff", buffAmount);
    }
}

enum SkillType {
    ATTACK, HEALING, BUFF, DEBUFF, UTILITY
}

/**
 * Defense skill implementation
 */
class DefenseSkill extends BaseSkill {
    private final int damageReduction;
    
    public DefenseSkill(String name, int manaCost, int cooldown, int requiredLevel) {
        super(name, "Reduces incoming damage", SkillType.UTILITY, manaCost, cooldown, requiredLevel);
        this.damageReduction = 5;
    }
    
    @Override
    public SkillResult use(GameCharacter caster, GameCharacter target) {
        if (!canUse(caster)) {
            return new SkillResult(false, "Cannot use skill", 0);
        }
        
        consumeResources(caster);
        
        StatusEffect defense = new StatusEffect(name + " Defense", StatusEffectType.BUFF, 
                                              10, AttributeType.CONSTITUTION, damageReduction);
        caster.addStatusEffect(defense);
        
        return new SkillResult(true, "Used " + name + " for defense", damageReduction);
    }
}

/**
 * Magic skill implementation
 */
class MagicSkill extends BaseSkill {
    private final int baseDamage;
    private final double magicMultiplier;
    
    public MagicSkill(String name, int manaCost, int cooldown, int requiredLevel) {
        super(name, "Magical attack spell", SkillType.ATTACK, manaCost, cooldown, requiredLevel);
        this.baseDamage = 20;
        this.magicMultiplier = 1.5;
    }
    
    @Override
    public SkillResult use(GameCharacter caster, GameCharacter target) {
        if (!canUse(caster)) {
            return new SkillResult(false, "Cannot use skill", 0);
        }
        
        consumeResources(caster);
        
        int damage = (int) (baseDamage + caster.getCurrentAttribute(AttributeType.INTELLIGENCE) * magicMultiplier);
        target.takeDamage(damage);
        
        return new SkillResult(true, "Used " + name + " for " + damage + " magic damage", damage);
    }
}

/**
 * Utility skill implementation
 */
class UtilitySkill extends BaseSkill {
    private final String utilityEffect;
    
    public UtilitySkill(String name, int manaCost, int cooldown, int requiredLevel) {
        super(name, "Utility skill with special effects", SkillType.UTILITY, manaCost, cooldown, requiredLevel);
        this.utilityEffect = "Special Effect";
    }
    
    @Override
    public SkillResult use(GameCharacter caster, GameCharacter target) {
        if (!canUse(caster)) {
            return new SkillResult(false, "Cannot use skill", 0);
        }
        
        consumeResources(caster);
        
        // Apply utility effect based on skill name
        if (name.toLowerCase().contains("stealth")) {
            StatusEffect stealth = new StatusEffect("Stealth", StatusEffectType.BUFF, 
                                                  15, AttributeType.DEXTERITY, 10);
            caster.addStatusEffect(stealth);
            return new SkillResult(true, "Used " + name + " - now stealthed", 10);
        } else if (name.toLowerCase().contains("eagle")) {
            StatusEffect eagleEye = new StatusEffect("Eagle Eye", StatusEffectType.BUFF, 
                                                   20, AttributeType.DEXTERITY, 5);
            caster.addStatusEffect(eagleEye);
            return new SkillResult(true, "Used " + name + " - enhanced accuracy", 5);
        }
        
        return new SkillResult(true, "Used " + name, 0);
    }
}
