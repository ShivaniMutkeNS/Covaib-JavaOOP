
import java.time.LocalDateTime;
import java.util.*;

/**
 * Game Character Attributes
 * 
 * This class demonstrates encapsulation by:
 * 1. Encapsulating health, stamina, inventory of a character
 * 2. Preventing invalid states (e.g., health < 0, stamina > 100)
 * 3. Ensuring inventory cannot be directly manipulated externally
 * 4. Implementing proper validation and state management
 */
public class GameCharacter {
    // Encapsulated character attributes
    private int health;
    private int maxHealth;
    private int stamina;
    private int maxStamina;
    private String name;
    private String characterClass;
    private int level;
    private int experience;
    
    // Encapsulated inventory
    private final Map<String, Integer> inventory;
    private final int maxInventorySlots;
    
    // Character stats
    private int strength;
    private int agility;
    private int intelligence;
    private int defense;
    
    // Character state
    private boolean isAlive;
    private boolean isResting;
    private LocalDateTime lastActionTime;
    
    /**
     * Constructor
     */
    public GameCharacter(String name, String characterClass, int level) {
        this.name = name;
        this.characterClass = characterClass;
        this.level = level;
        this.maxHealth = calculateMaxHealth();
        this.health = maxHealth;
        this.maxStamina = calculateMaxStamina();
        this.stamina = maxStamina;
        this.maxInventorySlots = calculateMaxInventorySlots();
        this.inventory = new HashMap<>();
        this.strength = 10;
        this.agility = 10;
        this.intelligence = 10;
        this.defense = 10;
        this.isAlive = true;
        this.isResting = false;
        this.experience = 0;
        this.lastActionTime = LocalDateTime.now();
    }
    
    /**
     * Take damage
     * @param damage Amount of damage to take
     * @return true if character is still alive
     */
    public boolean takeDamage(int damage) {
        if (!isAlive) {
            return false;
        }
        
        if (damage < 0) {
            damage = 0;
        }
        
        // Apply defense
        int actualDamage = Math.max(0, damage - defense);
        
        health = Math.max(0, health - actualDamage);
        
        if (health <= 0) {
            isAlive = false;
            stamina = 0;
        }
        
        lastActionTime = LocalDateTime.now();
        return isAlive;
    }
    
    /**
     * Heal character
     * @param healingAmount Amount to heal
     * @return true if healing was successful
     */
    public boolean heal(int healingAmount) {
        if (!isAlive || healingAmount <= 0) {
            return false;
        }
        
        int oldHealth = health;
        health = Math.min(maxHealth, health + healingAmount);
        
        lastActionTime = LocalDateTime.now();
        return health > oldHealth;
    }
    
    /**
     * Use stamina
     * @param staminaCost Amount of stamina to use
     * @return true if stamina was available
     */
    public boolean useStamina(int staminaCost) {
        if (!isAlive || staminaCost <= 0) {
            return false;
        }
        
        if (stamina < staminaCost) {
            return false;
        }
        
        stamina = Math.max(0, stamina - staminaCost);
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    /**
     * Restore stamina
     * @param staminaAmount Amount of stamina to restore
     * @return true if stamina was restored
     */
    public boolean restoreStamina(int staminaAmount) {
        if (!isAlive || staminaAmount <= 0) {
            return false;
        }
        
        int oldStamina = stamina;
        stamina = Math.min(maxStamina, stamina + staminaAmount);
        
        lastActionTime = LocalDateTime.now();
        return stamina > oldStamina;
    }
    
    /**
     * Start resting
     * @return true if resting started
     */
    public boolean startResting() {
        if (!isAlive || isResting) {
            return false;
        }
        
        isResting = true;
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    /**
     * Stop resting
     * @return true if resting stopped
     */
    public boolean stopResting() {
        if (!isResting) {
            return false;
        }
        
        isResting = false;
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    /**
     * Add item to inventory
     * @param itemName Name of the item
     * @param quantity Quantity to add
     * @return true if item was added
     */
    public boolean addItem(String itemName, int quantity) {
        if (!isAlive || itemName == null || itemName.trim().isEmpty() || quantity <= 0) {
            return false;
        }
        
        if (inventory.size() >= maxInventorySlots && !inventory.containsKey(itemName)) {
            return false; // Inventory full
        }
        
        int currentQuantity = inventory.getOrDefault(itemName, 0);
        inventory.put(itemName, currentQuantity + quantity);
        
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    /**
     * Remove item from inventory
     * @param itemName Name of the item
     * @param quantity Quantity to remove
     * @return true if item was removed
     */
    public boolean removeItem(String itemName, int quantity) {
        if (!isAlive || itemName == null || itemName.trim().isEmpty() || quantity <= 0) {
            return false;
        }
        
        int currentQuantity = inventory.getOrDefault(itemName, 0);
        if (currentQuantity < quantity) {
            return false; // Not enough items
        }
        
        if (currentQuantity == quantity) {
            inventory.remove(itemName);
        } else {
            inventory.put(itemName, currentQuantity - quantity);
        }
        
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    /**
     * Use item
     * @param itemName Name of the item to use
     * @return true if item was used
     */
    public boolean useItem(String itemName) {
        if (!isAlive || itemName == null || itemName.trim().isEmpty()) {
            return false;
        }
        
        int quantity = inventory.getOrDefault(itemName, 0);
        if (quantity <= 0) {
            return false; // Item not in inventory
        }
        
        // Simulate item usage based on item type
        boolean used = false;
        if (itemName.toLowerCase().contains("potion") || itemName.toLowerCase().contains("heal")) {
            used = heal(20);
        } else if (itemName.toLowerCase().contains("stamina") || itemName.toLowerCase().contains("energy")) {
            used = restoreStamina(30);
        } else if (itemName.toLowerCase().contains("food")) {
            used = heal(10) || restoreStamina(15);
        }
        
        if (used) {
            removeItem(itemName, 1);
        }
        
        lastActionTime = LocalDateTime.now();
        return used;
    }
    
    /**
     * Level up character
     * @return true if character leveled up
     */
    public boolean levelUp() {
        if (!isAlive) {
            return false;
        }
        
        int requiredExp = calculateRequiredExperience(level + 1);
        if (experience < requiredExp) {
            return false;
        }
        
        level++;
        experience -= requiredExp;
        
        // Increase stats
        strength += 2;
        agility += 2;
        intelligence += 2;
        defense += 2;
        
        // Recalculate max values
        // int oldMaxHealth = maxHealth; // Removed unused variable
        // int oldMaxStamina = maxStamina; // Removed unused variable
        maxHealth = calculateMaxHealth();
        maxStamina = calculateMaxStamina();
        
        // Restore health and stamina
        health = maxHealth;
        stamina = maxStamina;
        
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    /**
     * Add experience
     * @param expAmount Amount of experience to add
     * @return true if experience was added
     */
    public boolean addExperience(int expAmount) {
        if (!isAlive || expAmount <= 0) {
            return false;
        }
        
        experience += expAmount;
        
        // Check for level up
        while (levelUp()) {
            // Continue leveling up if possible
        }
        
        lastActionTime = LocalDateTime.now();
        return true;
    }
    
    // Getters (read-only access)
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getStamina() { return stamina; }
    public int getMaxStamina() { return maxStamina; }
    public String getName() { return name; }
    public String getCharacterClass() { return characterClass; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getIntelligence() { return intelligence; }
    public int getDefense() { return defense; }
    public boolean isAlive() { return isAlive; }
    public boolean isResting() { return isResting; }
    public LocalDateTime getLastActionTime() { return lastActionTime; }
    
    /**
     * Get inventory (read-only)
     * @return Unmodifiable map of inventory
     */
    public Map<String, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }
    
    /**
     * Get inventory size
     * @return Number of different items in inventory
     */
    public int getInventorySize() {
        return inventory.size();
    }
    
    /**
     * Get max inventory slots
     * @return Maximum number of inventory slots
     */
    public int getMaxInventorySlots() {
        return maxInventorySlots;
    }
    
    /**
     * Check if inventory is full
     * @return true if inventory is full
     */
    public boolean isInventoryFull() {
        return inventory.size() >= maxInventorySlots;
    }
    
    /**
     * Get item quantity
     * @param itemName Name of the item
     * @return Quantity of the item
     */
    public int getItemQuantity(String itemName) {
        return inventory.getOrDefault(itemName, 0);
    }
    
    /**
     * Get character status
     * @return Character status string
     */
    public String getStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Name: ").append(name).append("\n");
        status.append("Class: ").append(characterClass).append("\n");
        status.append("Level: ").append(level).append("\n");
        status.append("Health: ").append(health).append("/").append(maxHealth).append("\n");
        status.append("Stamina: ").append(stamina).append("/").append(maxStamina).append("\n");
        status.append("Experience: ").append(experience).append("\n");
        status.append("Stats: STR=").append(strength).append(" AGI=").append(agility)
               .append(" INT=").append(intelligence).append(" DEF=").append(defense).append("\n");
        status.append("Status: ").append(isAlive ? "Alive" : "Dead").append("\n");
        status.append("Resting: ").append(isResting ? "Yes" : "No").append("\n");
        status.append("Inventory: ").append(inventory.size()).append("/").append(maxInventorySlots).append(" slots");
        
        return status.toString();
    }
    
    /**
     * Calculate max health based on level and stats
     * @return Max health
     */
    private int calculateMaxHealth() {
        return 100 + (level * 10) + (strength * 2);
    }
    
    /**
     * Calculate max stamina based on level and stats
     * @return Max stamina
     */
    private int calculateMaxStamina() {
        return 100 + (level * 5) + (agility * 2);
    }
    
    /**
     * Calculate max inventory slots based on level
     * @return Max inventory slots
     */
    private int calculateMaxInventorySlots() {
        return 20 + (level * 2);
    }
    
    /**
     * Calculate required experience for a level
     * @param targetLevel Target level
     * @return Required experience
     */
    private int calculateRequiredExperience(int targetLevel) {
        return targetLevel * 100;
    }
    
    @Override
    public String toString() {
        return String.format("GameCharacter{name='%s', class='%s', level=%d, health=%d/%d, stamina=%d/%d, alive=%s}", 
            name, characterClass, level, health, maxHealth, stamina, maxStamina, isAlive);
    }
}
