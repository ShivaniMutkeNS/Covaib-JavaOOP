package composition.gaming;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level Gaming Character System using Composition
 * Demonstrates: Strategy Pattern, State Pattern, Observer Pattern, Command Pattern
 */
public class GameCharacter {
    private final String characterId;
    private final String name;
    private final CharacterClass characterClass;
    private int level;
    private int experience;
    private final Map<AttributeType, Integer> baseAttributes;
    private final Map<AttributeType, Integer> currentAttributes;
    private final Equipment equipment;
    private final Inventory inventory;
    private final List<Skill> skills;
    private final List<StatusEffect> statusEffects;
    private final List<CharacterEventListener> listeners;
    private CombatStrategy combatStrategy;
    private MovementStrategy movementStrategy;
    private LevelingStrategy levelingStrategy;
    private CharacterState state;
    private final ExecutorService eventExecutor;
    private final long createdAt;
    private Position position;
    private final CharacterStats stats;
    
    public GameCharacter(String characterId, String name, CharacterClass characterClass) {
        this.characterId = characterId;
        this.name = name;
        this.characterClass = characterClass;
        this.level = 1;
        this.experience = 0;
        this.baseAttributes = initializeBaseAttributes(characterClass);
        this.currentAttributes = new HashMap<>(baseAttributes);
        this.equipment = new Equipment();
        this.inventory = new Inventory(50); // 50 slot inventory
        this.skills = new ArrayList<>();
        this.statusEffects = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.combatStrategy = new StandardCombatStrategy();
        this.movementStrategy = new StandardMovementStrategy();
        this.levelingStrategy = new StandardLevelingStrategy();
        this.state = CharacterState.IDLE;
        this.eventExecutor = Executors.newFixedThreadPool(3);
        this.createdAt = System.currentTimeMillis();
        this.position = new Position(0, 0, 0);
        this.stats = new CharacterStats();
        
        // Initialize class-specific skills
        initializeClassSkills();
        
        // Recalculate attributes with equipment
        recalculateAttributes();
    }
    
    // Runtime strategy swapping - core composition flexibility
    public void setCombatStrategy(CombatStrategy strategy) {
        this.combatStrategy = strategy;
        notifyListeners("Combat strategy updated: " + strategy.getStrategyName());
    }
    
    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
        notifyListeners("Movement strategy updated: " + strategy.getStrategyName());
    }
    
    public void setLevelingStrategy(LevelingStrategy strategy) {
        this.levelingStrategy = strategy;
        notifyListeners("Leveling strategy updated: " + strategy.getStrategyName());
    }
    
    // Equipment management
    public EquipResult equipItem(Item item) {
        if (state == CharacterState.DEAD) {
            return new EquipResult(false, "Cannot equip items while dead", null);
        }
        
        if (!canEquipItem(item)) {
            return new EquipResult(false, "Cannot equip this item", null);
        }
        
        Item previousItem = equipment.equipItem(item);
        inventory.removeItem(item);
        
        if (previousItem != null) {
            inventory.addItem(previousItem);
        }
        
        recalculateAttributes();
        stats.incrementItemsEquipped();
        
        notifyListeners("Equipped: " + item.getName());
        
        return new EquipResult(true, "Item equipped successfully", previousItem);
    }
    
    public UnequipResult unequipItem(EquipmentSlot slot) {
        Item item = equipment.unequipItem(slot);
        if (item == null) {
            return new UnequipResult(false, "No item equipped in slot", null);
        }
        
        if (!inventory.addItem(item)) {
            // If inventory is full, re-equip the item
            equipment.equipItem(item);
            return new UnequipResult(false, "Inventory full", null);
        }
        
        recalculateAttributes();
        notifyListeners("Unequipped: " + item.getName());
        
        return new UnequipResult(true, "Item unequipped successfully", item);
    }
    
    private boolean canEquipItem(Item item) {
        // Check level requirement
        if (level < item.getRequiredLevel()) {
            return false;
        }
        
        // Check class requirement
        if (!item.getAllowedClasses().isEmpty() && 
            !item.getAllowedClasses().contains(characterClass)) {
            return false;
        }
        
        // Check attribute requirements
        for (Map.Entry<AttributeType, Integer> requirement : item.getAttributeRequirements().entrySet()) {
            if (currentAttributes.get(requirement.getKey()) < requirement.getValue()) {
                return false;
            }
        }
        
        return true;
    }
    
    // Combat system
    public CombatResult attack(GameCharacter target) {
        if (state != CharacterState.IDLE && state != CharacterState.COMBAT) {
            return new CombatResult(false, "Cannot attack in current state", 0, null);
        }
        
        if (target.state == CharacterState.DEAD) {
            return new CombatResult(false, "Cannot attack dead target", 0, null);
        }
        
        state = CharacterState.COMBAT;
        target.state = CharacterState.COMBAT;
        
        CombatResult result = combatStrategy.executeAttack(this, target);
        
        if (result.isSuccess()) {
            target.takeDamage(result.getDamage());
            stats.incrementAttacksPerformed();
            stats.addDamageDealt(result.getDamage());
            
            // Gain experience for successful attacks
            gainExperience(result.getDamage() / 10);
        }
        
        // Return to idle if not in sustained combat
        eventExecutor.schedule(() -> {
            if (state == CharacterState.COMBAT) {
                state = CharacterState.IDLE;
            }
        }, 3, TimeUnit.SECONDS);
        
        return result;
    }
    
    public void takeDamage(int damage) {
        // Apply armor reduction
        int armorValue = equipment.getTotalArmorValue();
        int actualDamage = Math.max(1, damage - armorValue);
        
        int currentHealth = currentAttributes.get(AttributeType.HEALTH);
        int newHealth = Math.max(0, currentHealth - actualDamage);
        currentAttributes.put(AttributeType.HEALTH, newHealth);
        
        stats.addDamageTaken(actualDamage);
        
        notifyListeners("Took " + actualDamage + " damage (reduced from " + damage + " by armor)");
        
        if (newHealth <= 0) {
            die();
        }
    }
    
    public void heal(int amount) {
        int currentHealth = currentAttributes.get(AttributeType.HEALTH);
        int maxHealth = baseAttributes.get(AttributeType.HEALTH) + equipment.getTotalAttributeBonus(AttributeType.HEALTH);
        int newHealth = Math.min(maxHealth, currentHealth + amount);
        
        currentAttributes.put(AttributeType.HEALTH, newHealth);
        notifyListeners("Healed for " + (newHealth - currentHealth) + " health");
    }
    
    private void die() {
        state = CharacterState.DEAD;
        stats.incrementDeaths();
        notifyListeners("Character has died!");
        
        // Apply death penalty
        int experienceLoss = experience / 10;
        experience = Math.max(0, experience - experienceLoss);
        
        // Schedule respawn
        eventExecutor.schedule(this::respawn, 10, TimeUnit.SECONDS);
    }
    
    private void respawn() {
        state = CharacterState.IDLE;
        currentAttributes.put(AttributeType.HEALTH, baseAttributes.get(AttributeType.HEALTH));
        currentAttributes.put(AttributeType.MANA, baseAttributes.get(AttributeType.MANA));
        
        notifyListeners("Character has respawned!");
    }
    
    // Skill system
    public SkillResult useSkill(String skillName, GameCharacter target) {
        Skill skill = findSkill(skillName);
        if (skill == null) {
            return new SkillResult(false, "Skill not found", 0);
        }
        
        if (!skill.canUse(this)) {
            return new SkillResult(false, "Cannot use skill: " + skill.getUsageRequirement(), 0);
        }
        
        SkillResult result = skill.use(this, target);
        
        if (result.isSuccess()) {
            stats.incrementSkillsUsed();
            
            // Consume mana
            int currentMana = currentAttributes.get(AttributeType.MANA);
            currentAttributes.put(AttributeType.MANA, Math.max(0, currentMana - skill.getManaCost()));
            
            // Gain experience
            gainExperience(skill.getExperienceGain());
        }
        
        return result;
    }
    
    public void learnSkill(Skill skill) {
        if (!hasSkill(skill.getName())) {
            skills.add(skill);
            notifyListeners("Learned new skill: " + skill.getName());
        }
    }
    
    private Skill findSkill(String skillName) {
        return skills.stream()
                   .filter(skill -> skill.getName().equals(skillName))
                   .findFirst()
                   .orElse(null);
    }
    
    private boolean hasSkill(String skillName) {
        return findSkill(skillName) != null;
    }
    
    // Experience and leveling
    public void gainExperience(int amount) {
        experience += amount;
        stats.addExperienceGained(amount);
        
        int requiredExp = levelingStrategy.getExperienceRequired(level + 1);
        if (experience >= requiredExp) {
            levelUp();
        }
        
        notifyListeners("Gained " + amount + " experience");
    }
    
    private void levelUp() {
        level++;
        stats.incrementLevelsGained();
        
        // Increase base attributes
        Map<AttributeType, Integer> attributeGains = levelingStrategy.getAttributeGains(characterClass, level);
        for (Map.Entry<AttributeType, Integer> gain : attributeGains.entrySet()) {
            baseAttributes.merge(gain.getKey(), gain.getValue(), Integer::sum);
        }
        
        // Learn new skills
        List<Skill> newSkills = levelingStrategy.getNewSkills(characterClass, level);
        for (Skill skill : newSkills) {
            learnSkill(skill);
        }
        
        recalculateAttributes();
        
        // Full heal on level up
        currentAttributes.put(AttributeType.HEALTH, currentAttributes.get(AttributeType.HEALTH));
        currentAttributes.put(AttributeType.MANA, currentAttributes.get(AttributeType.MANA));
        
        notifyListeners("Level up! Now level " + level);
    }
    
    // Movement system
    public MovementResult moveTo(Position targetPosition) {
        if (state == CharacterState.DEAD) {
            return new MovementResult(false, "Cannot move while dead", position);
        }
        
        MovementResult result = movementStrategy.move(this, position, targetPosition);
        
        if (result.isSuccess()) {
            position = result.getNewPosition();
            stats.addDistanceTraveled(calculateDistance(position, targetPosition));
            notifyListeners("Moved to " + position);
        }
        
        return result;
    }
    
    private double calculateDistance(Position from, Position to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    // Status effects
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
        effect.apply(this);
        notifyListeners("Applied status effect: " + effect.getName());
        
        // Schedule removal
        eventExecutor.schedule(() -> removeStatusEffect(effect), effect.getDuration(), TimeUnit.MILLISECONDS);
    }
    
    public void removeStatusEffect(StatusEffect effect) {
        if (statusEffects.remove(effect)) {
            effect.remove(this);
            notifyListeners("Removed status effect: " + effect.getName());
        }
    }
    
    // Attribute management
    private void recalculateAttributes() {
        // Start with base attributes
        currentAttributes.clear();
        currentAttributes.putAll(baseAttributes);
        
        // Add equipment bonuses
        for (AttributeType type : AttributeType.values()) {
            int bonus = equipment.getTotalAttributeBonus(type);
            currentAttributes.merge(type, bonus, Integer::sum);
        }
        
        // Apply status effect modifiers
        for (StatusEffect effect : statusEffects) {
            effect.modifyAttributes(currentAttributes);
        }
        
        // Ensure health and mana don't exceed maximums
        int maxHealth = currentAttributes.get(AttributeType.HEALTH);
        int maxMana = currentAttributes.get(AttributeType.MANA);
        
        currentAttributes.put(AttributeType.HEALTH, 
            Math.min(currentAttributes.get(AttributeType.HEALTH), maxHealth));
        currentAttributes.put(AttributeType.MANA, 
            Math.min(currentAttributes.get(AttributeType.MANA), maxMana));
    }
    
    private Map<AttributeType, Integer> initializeBaseAttributes(CharacterClass characterClass) {
        Map<AttributeType, Integer> attributes = new HashMap<>();
        
        switch (characterClass) {
            case WARRIOR:
                attributes.put(AttributeType.HEALTH, 120);
                attributes.put(AttributeType.MANA, 30);
                attributes.put(AttributeType.STRENGTH, 15);
                attributes.put(AttributeType.DEXTERITY, 10);
                attributes.put(AttributeType.INTELLIGENCE, 8);
                attributes.put(AttributeType.CONSTITUTION, 12);
                break;
            case MAGE:
                attributes.put(AttributeType.HEALTH, 80);
                attributes.put(AttributeType.MANA, 100);
                attributes.put(AttributeType.STRENGTH, 8);
                attributes.put(AttributeType.DEXTERITY, 10);
                attributes.put(AttributeType.INTELLIGENCE, 15);
                attributes.put(AttributeType.CONSTITUTION, 8);
                break;
            case ROGUE:
                attributes.put(AttributeType.HEALTH, 100);
                attributes.put(AttributeType.MANA, 50);
                attributes.put(AttributeType.STRENGTH, 12);
                attributes.put(AttributeType.DEXTERITY, 15);
                attributes.put(AttributeType.INTELLIGENCE, 10);
                attributes.put(AttributeType.CONSTITUTION, 10);
                break;
            case ARCHER:
                attributes.put(AttributeType.HEALTH, 100);
                attributes.put(AttributeType.MANA, 80);
                attributes.put(AttributeType.STRENGTH, 8);
                attributes.put(AttributeType.DEXTERITY, 14);
                attributes.put(AttributeType.INTELLIGENCE, 10);
                attributes.put(AttributeType.CONSTITUTION, 12);
                break;
        }
        
        return attributes;
    }
    
    // ...

    public int getCurrentAttribute(AttributeType type) {
        return currentAttributes.getOrDefault(type, 0);
    }
    
    public void setCurrentAttribute(AttributeType type, int value) {
        currentAttributes.put(type, value);
    }
    
    public void increaseBaseAttribute(AttributeType type, int amount) {
        int current = baseAttributes.getOrDefault(type, 0);
        baseAttributes.put(type, current + amount);
        recalculateAttributes();
    }
    
    public Equipment getEquipment() {
        return equipment;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
        recalculateAttributes();
    }
    
    private Skill findSkill(String skillName) {
        return skills.stream()
                .filter(skill -> skill.getName().equalsIgnoreCase(skillName))
                .findFirst()
                .orElse(null);
    }
    
    private void initializeClassSkills() {
        switch (characterClass) {
            case WARRIOR:
                learnSkill(new AttackSkill("Slash", 10, 20, 5));
                learnSkill(new DefenseSkill("Block", 15, 0, 3));
                break;
            case MAGE:
                learnSkill(new MagicSkill("Fireball", 25, 30, 10));
                learnSkill(new HealingSkill("Heal", 20, 0, 8));
                break;
            case ROGUE:
                learnSkill(new AttackSkill("Backstab", 8, 35, 4));
                learnSkill(new UtilitySkill("Stealth", 12, 0, 6));
                break;
            case ARCHER:
                learnSkill(new AttackSkill("Power Shot", 15, 25, 6));
                learnSkill(new UtilitySkill("Eagle Eye", 10, 0, 4));
                break;
        }
    }
    
    // Event handling
    public void addEventListener(CharacterEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(CharacterEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (CharacterEventListener listener : listeners) {
            eventExecutor.submit(() -> listener.onCharacterEvent(characterId, message));
        }
    }
    
    public void displayCharacterInfo() {
        System.out.println("\n⚔️ Character Information");
        System.out.println("Name: " + name + " (" + characterClass + ")");
        System.out.println("Level: " + level + " (XP: " + experience + ")");
        System.out.println("State: " + state);
        System.out.println("Position: " + position);
        
        System.out.println("\nAttributes:");
        for (Map.Entry<AttributeType, Integer> attr : currentAttributes.entrySet()) {
            int base = baseAttributes.get(attr.getKey());
            int bonus = attr.getValue() - base;
            String bonusStr = bonus > 0 ? " (+" + bonus + ")" : "";
            System.out.printf("   %s: %d%s\n", attr.getKey(), attr.getValue(), bonusStr);
        }
        
        System.out.println("\nEquipment:");
        for (Map.Entry<EquipmentSlot, Item> entry : equipment.getAllEquippedItems().entrySet()) {
            System.out.printf("   %s: %s\n", entry.getKey().getDisplayName(), entry.getValue().getName());
        }
        
        System.out.println("\nSkills (" + skills.size() + "):");
        for (Skill skill : skills) {
            System.out.printf("   • %s (Type: %s)\n", skill.getName(), skill.getType());
        }
        
        if (!statusEffects.isEmpty()) {
            System.out.println("\nStatus Effects:");
            for (StatusEffect effect : statusEffects) {
                System.out.printf("   • %s (%d s remaining)\n", effect.getName(), effect.getRemainingDuration());
            }
        }
        
        System.out.println("\nStatistics:");
        System.out.println("   Attacks Performed: " + stats.getAttacksPerformed());
        System.out.println("   Damage Dealt: " + stats.getDamageDealt());
        System.out.println("   Damage Taken: " + stats.getDamageTaken());
        System.out.println("   Skills Used: " + stats.getSkillsUsed());
        System.out.println("   Levels Gained: " + stats.getLevelsGained());
        System.out.println("   Deaths: " + stats.getDeaths());
        System.out.printf("   Distance Traveled: %.1f\n", stats.getDistanceTraveled());
    }
    
    public void shutdown() {
        eventExecutor.shutdown();
        try {
            if (!eventExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                eventExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            eventExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        notifyListeners("Character services shutdown");
    }
    
    // Additional methods needed for the system
    public void learnSkill(Skill skill) {
        skills.add(skill);
        notifyListeners("Learned new skill: " + skill.getName());
    }
    
    public LevelingResult gainExperience(int amount) {
        stats.addExperienceGained(amount);
        return levelingStrategy.processExperience(this, amount);
    }
    
    public MovementResult move(Position targetPosition) {
        MovementResult result = movementStrategy.move(this, targetPosition);
        if (result.isSuccess()) {
            stats.addDistanceTraveled(position.distanceTo(targetPosition));
        }
        return result;
    }
    
    public int getBaseAttribute(AttributeType type) {
        return baseAttributes.getOrDefault(type, 0);
    }
    
    // Getters
    public String getCharacterId() { return characterId; }
    public String getName() { return name; }
    public CharacterClass getCharacterClass() { return characterClass; }
    public Map<AttributeType, Integer> getCurrentAttributes() { return new HashMap<>(currentAttributes); }
    public Inventory getInventory() { return inventory; }
    public List<Skill> getSkills() { return new ArrayList<>(skills); }
    public CharacterState getState() { return state; }
    public CharacterStats getStats() { return stats; }
    public long getCreatedAt() { return createdAt; }
}
