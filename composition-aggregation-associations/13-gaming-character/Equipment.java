package composition.gaming;

import java.util.*;

/**
 * Equipment management system for game characters
 */
public class Equipment {
    private final Map<EquipmentSlot, Item> equippedItems;
    
    public Equipment() {
        this.equippedItems = new HashMap<>();
    }
    
    public Item equipItem(Item item) {
        EquipmentSlot slot = item.getEquipmentSlot();
        Item previousItem = equippedItems.get(slot);
        equippedItems.put(slot, item);
        return previousItem;
    }
    
    public Item unequipItem(EquipmentSlot slot) {
        return equippedItems.remove(slot);
    }
    
    public Item getEquippedItem(EquipmentSlot slot) {
        return equippedItems.get(slot);
    }
    
    public Map<EquipmentSlot, Item> getAllEquippedItems() {
        return new HashMap<>(equippedItems);
    }
    
    public int getTotalArmorValue() {
        return equippedItems.values().stream()
                .mapToInt(Item::getArmorValue)
                .sum();
    }
    
    public int getTotalAttributeBonus(AttributeType attributeType) {
        return equippedItems.values().stream()
                .mapToInt(item -> item.getAttributeBonuses().getOrDefault(attributeType, 0))
                .sum();
    }
    
    public boolean isSlotOccupied(EquipmentSlot slot) {
        return equippedItems.containsKey(slot);
    }
    
    public void clear() {
        equippedItems.clear();
    }
    
    public List<Item> getAllItems() {
        return new ArrayList<>(equippedItems.values());
    }
}
