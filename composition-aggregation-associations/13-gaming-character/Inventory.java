package composition.gaming;

import java.util.*;

/**
 * Inventory management system for game characters
 */
public class Inventory {
    private final int maxSlots;
    private final List<Item> items;
    private final Map<String, Integer> itemCounts;
    
    public Inventory(int maxSlots) {
        this.maxSlots = maxSlots;
        this.items = new ArrayList<>();
        this.itemCounts = new HashMap<>();
    }
    
    public boolean addItem(Item item) {
        if (items.size() >= maxSlots && !itemCounts.containsKey(item.getId())) {
            return false; // Inventory full
        }
        
        if (item.isStackable() && itemCounts.containsKey(item.getId())) {
            itemCounts.put(item.getId(), itemCounts.get(item.getId()) + 1);
        } else {
            items.add(item);
            itemCounts.put(item.getId(), itemCounts.getOrDefault(item.getId(), 0) + 1);
        }
        
        return true;
    }
    
    public boolean removeItem(Item item) {
        if (!itemCounts.containsKey(item.getId())) {
            return false;
        }
        
        int count = itemCounts.get(item.getId());
        if (count > 1) {
            itemCounts.put(item.getId(), count - 1);
        } else {
            itemCounts.remove(item.getId());
            items.removeIf(i -> i.getId().equals(item.getId()));
        }
        
        return true;
    }
    
    public boolean removeItem(String itemId, int quantity) {
        if (!itemCounts.containsKey(itemId)) {
            return false;
        }
        
        int currentCount = itemCounts.get(itemId);
        if (currentCount < quantity) {
            return false;
        }
        
        if (currentCount == quantity) {
            itemCounts.remove(itemId);
            items.removeIf(i -> i.getId().equals(itemId));
        } else {
            itemCounts.put(itemId, currentCount - quantity);
        }
        
        return true;
    }
    
    public Item findItem(String itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
    
    public int getItemCount(String itemId) {
        return itemCounts.getOrDefault(itemId, 0);
    }
    
    public boolean hasItem(String itemId) {
        return itemCounts.containsKey(itemId);
    }
    
    public boolean hasSpace() {
        return items.size() < maxSlots;
    }
    
    public int getUsedSlots() {
        return items.size();
    }
    
    public int getMaxSlots() {
        return maxSlots;
    }
    
    public int getFreeSlots() {
        return maxSlots - items.size();
    }
    
    public void clear() {
        items.clear();
        itemCounts.clear();
    }
    
    public Map<String, Integer> getItemCounts() {
        return new HashMap<>(itemCounts);
    }
}
