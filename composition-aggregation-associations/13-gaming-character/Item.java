package composition.gaming;

import java.util.*;

/**
 * Item class representing equipment and consumables in the game
 */
public class Item {
    private final String id;
    private final String name;
    private final String description;
    private final ItemType type;
    private final EquipmentSlot equipmentSlot;
    private final int requiredLevel;
    private final Set<CharacterClass> allowedClasses;
    private final Map<AttributeType, Integer> attributeRequirements;
    private final Map<AttributeType, Integer> attributeBonuses;
    private final int armorValue;
    private final int weaponDamage;
    private final boolean stackable;
    private final int maxStackSize;
    private final int value;
    private final ItemRarity rarity;
    
    private Item(String id, String name, String description, ItemType type, 
                EquipmentSlot equipmentSlot, int requiredLevel, Set<CharacterClass> allowedClasses,
                Map<AttributeType, Integer> attributeRequirements, Map<AttributeType, Integer> attributeBonuses,
                int armorValue, int weaponDamage, int value, ItemRarity rarity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.equipmentSlot = equipmentSlot;
        this.requiredLevel = requiredLevel;
        this.allowedClasses = new HashSet<>(allowedClasses);
        this.attributeRequirements = new HashMap<>(attributeRequirements);
        this.attributeBonuses = new HashMap<>(attributeBonuses);
        this.armorValue = armorValue;
        this.weaponDamage = weaponDamage;
        this.stackable = type == ItemType.CONSUMABLE;
        this.maxStackSize = stackable ? 99 : 1;
        this.value = value;
        this.rarity = rarity;
    }
    
    // Builder pattern for complex item creation
    public static class Builder {
        private final String id;
        private final String name;
        private final ItemType type;
        private String description = "";
        private EquipmentSlot equipmentSlot = null;
        private int requiredLevel = 1;
        private final Set<CharacterClass> allowedClasses = new HashSet<>();
        private final Map<AttributeType, Integer> attributeRequirements = new HashMap<>();
        private final Map<AttributeType, Integer> attributeBonuses = new HashMap<>();
        private int armorValue = 0;
        private int weaponDamage = 0;
        private int value = 0;
        private ItemRarity rarity = ItemRarity.COMMON;
        
        public Builder(String id, String name, ItemType type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder equipmentSlot(EquipmentSlot slot) {
            this.equipmentSlot = slot;
            return this;
        }
        
        public Builder requiredLevel(int level) {
            this.requiredLevel = level;
            return this;
        }
        
        public Builder allowedClass(CharacterClass charClass) {
            this.allowedClasses.add(charClass);
            return this;
        }
        
        public Builder attributeRequirement(AttributeType attr, int value) {
            this.attributeRequirements.put(attr, value);
            return this;
        }
        
        public Builder attributeBonus(AttributeType attr, int bonus) {
            this.attributeBonuses.put(attr, bonus);
            return this;
        }
        
        public Builder armorValue(int armor) {
            this.armorValue = armor;
            return this;
        }
        
        public Builder weaponDamage(int damage) {
            this.weaponDamage = damage;
            return this;
        }
        
        public Builder value(int itemValue) {
            this.value = itemValue;
            return this;
        }
        
        public Builder rarity(ItemRarity itemRarity) {
            this.rarity = itemRarity;
            return this;
        }
        
        public Item build() {
            return new Item(id, name, description, type, equipmentSlot, requiredLevel,
                          allowedClasses, attributeRequirements, attributeBonuses,
                          armorValue, weaponDamage, value, rarity);
        }
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ItemType getType() { return type; }
    public EquipmentSlot getEquipmentSlot() { return equipmentSlot; }
    public int getRequiredLevel() { return requiredLevel; }
    public Set<CharacterClass> getAllowedClasses() { return new HashSet<>(allowedClasses); }
    public Map<AttributeType, Integer> getAttributeRequirements() { return new HashMap<>(attributeRequirements); }
    public Map<AttributeType, Integer> getAttributeBonuses() { return new HashMap<>(attributeBonuses); }
    public int getArmorValue() { return armorValue; }
    public int getWeaponDamage() { return weaponDamage; }
    public boolean isStackable() { return stackable; }
    public int getMaxStackSize() { return maxStackSize; }
    public int getValue() { return value; }
    public ItemRarity getRarity() { return rarity; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(id, item.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("%s (Level %d %s)", name, requiredLevel, type);
    }
}

enum ItemType {
    WEAPON, ARMOR, ACCESSORY, CONSUMABLE, QUEST, MISC
}

enum ItemRarity {
    COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
}
