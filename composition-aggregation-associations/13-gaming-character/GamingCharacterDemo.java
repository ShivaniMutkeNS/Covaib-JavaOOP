package composition.gaming;

import java.util.*;

/**
 * Comprehensive demo for the Gaming Character System
 * Demonstrates: Strategy Pattern, State Pattern, Observer Pattern, Command Pattern
 */
public class GamingCharacterDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Gaming Character System Demo ===\n");
        
        // Create characters
        GameCharacter warrior = new GameCharacter("CHAR001", "Thorin", CharacterClass.WARRIOR);
        GameCharacter mage = new GameCharacter("CHAR002", "Gandalf", CharacterClass.MAGE);
        GameCharacter rogue = new GameCharacter("CHAR003", "Legolas", CharacterClass.ROGUE);
        
        // Add event listeners
        CharacterEventListener logger = new CharacterEventLogger();
        warrior.addEventListener(logger);
        mage.addEventListener(logger);
        rogue.addEventListener(logger);
        
        System.out.println("1. INITIAL CHARACTER STATUS");
        System.out.println("============================");
        printCharacterStatus(warrior);
        printCharacterStatus(mage);
        printCharacterStatus(rogue);
        
        System.out.println("\n2. EQUIPMENT SYSTEM DEMO");
        System.out.println("========================");
        
        // Create some items
        Item sword = createSword();
        Item armor = createArmor();
        Item staff = createStaff();
        Item robe = createRobe();
        
        // Equip items
        System.out.println("Equipping warrior with sword and armor...");
        warrior.getInventory().addItem(sword);
        warrior.getInventory().addItem(armor);
        EquipResult swordResult = warrior.equipItem(sword);
        EquipResult armorResult = warrior.equipItem(armor);
        
        System.out.println("Sword equip: " + swordResult.getMessage());
        System.out.println("Armor equip: " + armorResult.getMessage());
        
        System.out.println("\\nEquipping mage with staff and robe...");
        mage.getInventory().addItem(staff);
        mage.getInventory().addItem(robe);
        mage.equipItem(staff);
        mage.equipItem(robe);
        
        System.out.println("\n3. COMBAT SYSTEM DEMO");
        System.out.println("=====================");
        
        // Standard combat
        System.out.println("Standard combat between warrior and mage:");
        CombatResult combatResult = warrior.attack(mage);
        System.out.println("Combat result: " + combatResult.getMessage());
        
        // Switch combat strategies
        System.out.println("\\nSwitching warrior to berserker combat strategy:");
        warrior.setCombatStrategy(new BerserkerCombatStrategy());
        
        CombatResult berserkerResult = warrior.attack(mage);
        System.out.println("Berserker attack: " + berserkerResult.getMessage());
        
        // Defensive strategy
        System.out.println("\\nSwitching to defensive combat strategy:");
        warrior.setCombatStrategy(new DefensiveCombatStrategy());
        
        CombatResult defensiveResult = warrior.attack(mage);
        System.out.println("Defensive attack: " + defensiveResult.getMessage());
        
        System.out.println("\n4. SKILL SYSTEM DEMO");
        System.out.println("====================");
        
        // Use skills
        System.out.println("Warrior using skills:");
        SkillResult skillResult1 = warrior.useSkill("Slash", mage);
        System.out.println("Skill result: " + skillResult1.getMessage());
        
        System.out.println("\\nMage using healing skill:");
        SkillResult healResult = mage.useSkill("Heal", mage);
        System.out.println("Heal result: " + healResult.getMessage());
        
        System.out.println("\n5. MOVEMENT SYSTEM DEMO");
        System.out.println("=======================");
        
        Position newPosition = new Position(10, 5, 0);
        System.out.println("Moving warrior to position (10, 5, 0):");
        MovementResult moveResult = warrior.move(newPosition);
        System.out.println("Movement result: " + moveResult.getMessage());
        
        // Switch movement strategies
        System.out.println("\\nSwitching to fast movement strategy:");
        warrior.setMovementStrategy(new FastMovementStrategy());
        
        Position fastPosition = new Position(20, 10, 0);
        MovementResult fastMoveResult = warrior.move(fastPosition);
        System.out.println("Fast movement: " + fastMoveResult.getMessage());
        
        // Teleport strategy
        System.out.println("\\nSwitching to teleport movement strategy:");
        warrior.setMovementStrategy(new TeleportMovementStrategy());
        
        Position teleportPosition = new Position(100, 50, 25);
        MovementResult teleportResult = warrior.move(teleportPosition);
        System.out.println("Teleport result: " + teleportResult.getMessage());
        
        System.out.println("\n6. STATUS EFFECTS DEMO");
        System.out.println("======================");
        
        // Apply status effects
        StatusEffect strengthBuff = new StatusEffect("Strength Boost", StatusEffectType.BUFF, 
                                                    30, AttributeType.STRENGTH, 5);
        StatusEffect poison = new StatusEffect("Poison", StatusEffectType.POISON, 
                                             15, AttributeType.HEALTH, -2);
        
        System.out.println("Applying strength buff to warrior:");
        warrior.addStatusEffect(strengthBuff);
        
        System.out.println("Applying poison to mage:");
        mage.addStatusEffect(poison);
        
        System.out.println("\n7. LEVELING SYSTEM DEMO");
        System.out.println("=======================");
        
        // Gain experience
        System.out.println("Warrior gaining experience:");
        LevelingResult levelResult1 = warrior.gainExperience(150);
        System.out.println("Leveling result: " + levelResult1.getMessage());
        
        // Switch leveling strategies
        System.out.println("\\nSwitching to fast leveling strategy:");
        warrior.setLevelingStrategy(new FastLevelingStrategy());
        
        LevelingResult fastLevelResult = warrior.gainExperience(100);
        System.out.println("Fast leveling: " + fastLevelResult.getMessage());
        
        // Hardcore leveling
        System.out.println("\\nSwitching to hardcore leveling strategy:");
        mage.setLevelingStrategy(new HardcoreLevelingStrategy());
        
        LevelingResult hardcoreResult = mage.gainExperience(300);
        System.out.println("Hardcore leveling: " + hardcoreResult.getMessage());
        
        System.out.println("\n8. INVENTORY MANAGEMENT DEMO");
        System.out.println("============================");
        
        // Add items to inventory
        Item potion = createHealthPotion();
        Item scroll = createMagicScroll();
        
        System.out.println("Adding items to rogue's inventory:");
        boolean addResult1 = rogue.getInventory().addItem(potion);
        boolean addResult2 = rogue.getInventory().addItem(scroll);
        
        System.out.println("Added health potion: " + addResult1);
        System.out.println("Added magic scroll: " + addResult2);
        System.out.println("Inventory slots used: " + rogue.getInventory().getUsedSlots() + 
                         "/" + rogue.getInventory().getMaxSlots());
        
        System.out.println("\n9. CHARACTER ANALYTICS");
        System.out.println("======================");
        
        // Display character statistics
        System.out.println("Warrior stats: " + warrior.getStats());
        System.out.println("Mage stats: " + mage.getStats());
        System.out.println("Rogue stats: " + rogue.getStats());
        
        System.out.println("\n10. FINAL CHARACTER STATUS");
        System.out.println("==========================");
        printCharacterStatus(warrior);
        printCharacterStatus(mage);
        printCharacterStatus(rogue);
        
        System.out.println("\n=== Demo Complete ===");
        
        // Cleanup
        warrior.shutdown();
        mage.shutdown();
        rogue.shutdown();
    }
    
    private static void printCharacterStatus(GameCharacter character) {
        System.out.printf("Character: %s (Level %d %s)\\n", 
                         character.getName(), character.getLevel(), character.getCharacterClass());
        System.out.printf("  Health: %d/%d, Mana: %d/%d\\n",
                         character.getCurrentAttribute(AttributeType.HEALTH),
                         character.getBaseAttribute(AttributeType.HEALTH),
                         character.getCurrentAttribute(AttributeType.MANA),
                         character.getBaseAttribute(AttributeType.MANA));
        System.out.printf("  STR: %d, DEX: %d, INT: %d, CON: %d\\n",
                         character.getCurrentAttribute(AttributeType.STRENGTH),
                         character.getCurrentAttribute(AttributeType.DEXTERITY),
                         character.getCurrentAttribute(AttributeType.INTELLIGENCE),
                         character.getCurrentAttribute(AttributeType.CONSTITUTION));
        System.out.printf("  Position: %s, State: %s\\n",
                         character.getPosition(), character.getState());
        System.out.printf("  Experience: %d, Equipment: %d items\\n",
                         character.getExperience(), character.getEquipment().getAllItems().size());
        System.out.println();
    }
    
    // Helper methods to create items
    private static Item createSword() {
        return new Item.Builder("SWORD001", "Iron Sword", ItemType.WEAPON)
                .description("A sturdy iron sword")
                .equipmentSlot(EquipmentSlot.MAIN_HAND)
                .requiredLevel(1)
                .allowedClass(CharacterClass.WARRIOR)
                .attributeBonus(AttributeType.STRENGTH, 5)
                .weaponDamage(10)
                .value(100)
                .build();
    }
    
    private static Item createArmor() {
        return new Item.Builder("ARMOR001", "Chain Mail", ItemType.ARMOR)
                .description("Protective chain mail armor")
                .equipmentSlot(EquipmentSlot.CHEST)
                .requiredLevel(1)
                .allowedClass(CharacterClass.WARRIOR)
                .attributeBonus(AttributeType.CONSTITUTION, 3)
                .armorValue(5)
                .value(150)
                .build();
    }
    
    private static Item createStaff() {
        return new Item.Builder("STAFF001", "Wizard Staff", ItemType.WEAPON)
                .description("A magical staff imbued with arcane power")
                .equipmentSlot(EquipmentSlot.MAIN_HAND)
                .requiredLevel(1)
                .allowedClass(CharacterClass.MAGE)
                .attributeBonus(AttributeType.INTELLIGENCE, 5)
                .weaponDamage(8)
                .value(120)
                .build();
    }
    
    private static Item createRobe() {
        return new Item.Builder("ROBE001", "Mage Robe", ItemType.ARMOR)
                .description("Flowing robes that enhance magical abilities")
                .equipmentSlot(EquipmentSlot.CHEST)
                .requiredLevel(1)
                .allowedClass(CharacterClass.MAGE)
                .attributeBonus(AttributeType.INTELLIGENCE, 3)
                .armorValue(2)
                .value(80)
                .build();
    }
    
    private static Item createHealthPotion() {
        return new Item.Builder("POTION001", "Health Potion", ItemType.CONSUMABLE)
                .description("Restores health when consumed")
                .value(25)
                .build();
    }
    
    private static Item createMagicScroll() {
        return new Item.Builder("SCROLL001", "Magic Scroll", ItemType.CONSUMABLE)
                .description("A scroll containing magical knowledge")
                .value(50)
                .build();
    }
}

/**
 * Event listener implementation for logging character events
 */
class CharacterEventLogger implements CharacterEventListener {
    @Override
    public void onCharacterEvent(String characterId, String eventMessage) {
        System.out.println("[EVENT] " + characterId + ": " + eventMessage);
    }
    
    @Override
    public void onLevelUp(String characterId, int newLevel) {
        System.out.println("[LEVEL UP] " + characterId + " reached level " + newLevel + "!");
    }
    
    @Override
    public void onDeath(String characterId) {
        System.out.println("[DEATH] " + characterId + " has died!");
    }
    
    @Override
    public void onRespawn(String characterId) {
        System.out.println("[RESPAWN] " + characterId + " has respawned!");
    }
    
    @Override
    public void onEquipmentChange(String characterId, String itemName, boolean equipped) {
        String action = equipped ? "equipped" : "unequipped";
        System.out.println("[EQUIPMENT] " + characterId + " " + action + " " + itemName);
    }
    
    @Override
    public void onStatusEffectApplied(String characterId, String effectName) {
        System.out.println("[STATUS] " + characterId + " affected by " + effectName);
    }
    
    @Override
    public void onStatusEffectExpired(String characterId, String effectName) {
        System.out.println("[STATUS] " + characterId + " recovered from " + effectName);
    }
}
