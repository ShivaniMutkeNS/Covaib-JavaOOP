

import java.util.Map;

/**
 * Demo class to demonstrate Game Character Attributes
 */
public class GameCharacterDemo {
    public static void main(String[] args) {
        System.out.println("=== Game Character Attributes Demo ===\n");
        
        // Test character creation and basic operations
        testCharacterCreation();
        
        // Test health and stamina management
        testHealthAndStamina();
        
        // Test inventory management
        testInventoryManagement();
        
        // Test character progression
        testCharacterProgression();
        
        // Test invalid operations
        testInvalidOperations();
        
        // Test resting functionality
        testResting();
        
        // Test inventory limits
        testInventoryLimits();
    }
    
    private static void testCharacterCreation() {
        System.out.println("=== Character Creation Test ===");
        
        GameCharacter character = new GameCharacter("Hero", "Warrior", 1);
        System.out.println("Character created: " + character);
        System.out.println("Status: " + character.getStatus());
        
        System.out.println();
    }
    
    private static void testHealthAndStamina() {
        System.out.println("=== Health and Stamina Test ===");
        
        GameCharacter character = new GameCharacter("Fighter", "Warrior", 1);
        
        // Test taking damage
        System.out.println("Initial health: " + character.getHealth() + "/" + character.getMaxHealth());
        
        boolean alive = character.takeDamage(30);
        System.out.println("Took 30 damage: " + (alive ? "Still alive" : "Died"));
        System.out.println("Health after damage: " + character.getHealth() + "/" + character.getMaxHealth());
        
        // Test healing
        boolean healed = character.heal(20);
        System.out.println("Healed 20: " + (healed ? "SUCCESS" : "FAILED"));
        System.out.println("Health after healing: " + character.getHealth() + "/" + character.getMaxHealth());
        
        // Test stamina usage
        System.out.println("Initial stamina: " + character.getStamina() + "/" + character.getMaxStamina());
        
        boolean staminaUsed = character.useStamina(50);
        System.out.println("Used 50 stamina: " + (staminaUsed ? "SUCCESS" : "FAILED"));
        System.out.println("Stamina after usage: " + character.getStamina() + "/" + character.getMaxStamina());
        
        // Test stamina restoration
        boolean staminaRestored = character.restoreStamina(30);
        System.out.println("Restored 30 stamina: " + (staminaRestored ? "SUCCESS" : "FAILED"));
        System.out.println("Stamina after restoration: " + character.getStamina() + "/" + character.getMaxStamina());
        
        System.out.println();
    }
    
    private static void testInventoryManagement() {
        System.out.println("=== Inventory Management Test ===");
        
        GameCharacter character = new GameCharacter("Adventurer", "Rogue", 1);
        
        System.out.println("Initial inventory size: " + character.getInventorySize() + "/" + character.getMaxInventorySlots());
        
        // Add items
        boolean added1 = character.addItem("Health Potion", 3);
        System.out.println("Added 3 Health Potions: " + (added1 ? "SUCCESS" : "FAILED"));
        
        boolean added2 = character.addItem("Stamina Potion", 2);
        System.out.println("Added 2 Stamina Potions: " + (added2 ? "SUCCESS" : "FAILED"));
        
        boolean added3 = character.addItem("Food", 5);
        System.out.println("Added 5 Food: " + (added3 ? "SUCCESS" : "FAILED"));
        
        System.out.println("Inventory after adding items: " + character.getInventorySize() + "/" + character.getMaxInventorySlots());
        
        // Display inventory
        System.out.println("Inventory contents:");
        for (Map.Entry<String, Integer> entry : character.getInventory().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        
        // Use items
        System.out.println("\nUsing items:");
        boolean used1 = character.useItem("Health Potion");
        System.out.println("Used Health Potion: " + (used1 ? "SUCCESS" : "FAILED"));
        System.out.println("Health after using potion: " + character.getHealth() + "/" + character.getMaxHealth());
        
        boolean used2 = character.useItem("Stamina Potion");
        System.out.println("Used Stamina Potion: " + (used2 ? "SUCCESS" : "FAILED"));
        System.out.println("Stamina after using potion: " + character.getStamina() + "/" + character.getMaxStamina());
        
        // Remove items
        boolean removed = character.removeItem("Food", 2);
        System.out.println("Removed 2 Food: " + (removed ? "SUCCESS" : "FAILED"));
        
        System.out.println("Final inventory:");
        for (Map.Entry<String, Integer> entry : character.getInventory().entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println();
    }
    
    private static void testCharacterProgression() {
        System.out.println("=== Character Progression Test ===");
        
        GameCharacter character = new GameCharacter("Hero", "Mage", 1);
        
        System.out.println("Initial level: " + character.getLevel());
        System.out.println("Initial experience: " + character.getExperience());
        System.out.println("Initial stats: STR=" + character.getStrength() + " AGI=" + character.getAgility() + 
                          " INT=" + character.getIntelligence() + " DEF=" + character.getDefense());
        
        // Add experience
        boolean expAdded = character.addExperience(150);
        System.out.println("Added 150 experience: " + (expAdded ? "SUCCESS" : "FAILED"));
        System.out.println("Level after experience: " + character.getLevel());
        System.out.println("Experience after level up: " + character.getExperience());
        System.out.println("Stats after level up: STR=" + character.getStrength() + " AGI=" + character.getAgility() + 
                          " INT=" + character.getIntelligence() + " DEF=" + character.getDefense());
        System.out.println("Max health after level up: " + character.getMaxHealth());
        System.out.println("Max stamina after level up: " + character.getMaxStamina());
        
        System.out.println();
    }
    
    private static void testInvalidOperations() {
        System.out.println("=== Invalid Operations Test ===");
        
        GameCharacter character = new GameCharacter("Test", "Warrior", 1);
        
        // Test invalid damage
        boolean invalidDamage = character.takeDamage(-10);
        System.out.println("Took negative damage: " + (invalidDamage ? "SUCCESS" : "FAILED"));
        
        // Test invalid healing
        boolean invalidHealing = character.heal(-5);
        System.out.println("Healed negative amount: " + (invalidHealing ? "SUCCESS" : "FAILED"));
        
        // Test invalid stamina usage
        boolean invalidStamina = character.useStamina(-20);
        System.out.println("Used negative stamina: " + (invalidStamina ? "SUCCESS" : "FAILED"));
        
        // Test invalid item operations
        boolean invalidItem1 = character.addItem("", 5);
        System.out.println("Added item with empty name: " + (invalidItem1 ? "SUCCESS" : "FAILED"));
        
        boolean invalidItem2 = character.addItem("Test Item", -3);
        System.out.println("Added item with negative quantity: " + (invalidItem2 ? "SUCCESS" : "FAILED"));
        
        boolean invalidItem3 = character.removeItem("Non-existent Item", 1);
        System.out.println("Removed non-existent item: " + (invalidItem3 ? "SUCCESS" : "FAILED"));
        
        // Test operations on dead character
        character.takeDamage(1000); // Kill the character
        System.out.println("Character is alive: " + character.isAlive());
        
        boolean deadHeal = character.heal(50);
        System.out.println("Healed dead character: " + (deadHeal ? "SUCCESS" : "FAILED"));
        
        boolean deadStamina = character.useStamina(20);
        System.out.println("Used stamina on dead character: " + (deadStamina ? "SUCCESS" : "FAILED"));
        
        boolean deadItem = character.addItem("Test Item", 1);
        System.out.println("Added item to dead character: " + (deadItem ? "SUCCESS" : "FAILED"));
        
        System.out.println();
    }
    
    /**
     * Test resting functionality
     */
    private static void testResting() {
        System.out.println("=== Resting Test ===");
        
        GameCharacter character = new GameCharacter("Resting Hero", "Warrior", 1);
        
        // Use some stamina
        character.useStamina(80);
        System.out.println("Stamina after usage: " + character.getStamina() + "/" + character.getMaxStamina());
        
        // Start resting
        boolean restingStarted = character.startResting();
        System.out.println("Started resting: " + (restingStarted ? "SUCCESS" : "FAILED"));
        System.out.println("Is resting: " + character.isResting());
        
        // Try to start resting again
        boolean restingStartedAgain = character.startResting();
        System.out.println("Started resting again: " + (restingStartedAgain ? "SUCCESS" : "FAILED"));
        
        // Stop resting
        boolean restingStopped = character.stopResting();
        System.out.println("Stopped resting: " + (restingStopped ? "SUCCESS" : "FAILED"));
        System.out.println("Is resting: " + character.isResting());
        
        System.out.println();
    }
    
    /**
     * Test inventory limits
     */
    private static void testInventoryLimits() {
        System.out.println("=== Inventory Limits Test ===");
        
        GameCharacter character = new GameCharacter("Pack Rat", "Rogue", 1);
        
        System.out.println("Max inventory slots: " + character.getMaxInventorySlots());
        
        // Fill inventory
        for (int i = 1; i <= character.getMaxInventorySlots() + 5; i++) {
            boolean added = character.addItem("Item " + i, 1);
            System.out.println("Added Item " + i + ": " + (added ? "SUCCESS" : "FAILED"));
            if (!added) {
                System.out.println("Inventory full at item " + i);
                break;
            }
        }
        
        System.out.println("Final inventory size: " + character.getInventorySize() + "/" + character.getMaxInventorySlots());
        System.out.println("Is inventory full: " + character.isInventoryFull());
        
        System.out.println();
    }
}
