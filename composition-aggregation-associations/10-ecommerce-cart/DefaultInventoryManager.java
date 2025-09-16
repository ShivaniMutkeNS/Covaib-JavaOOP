package composition.ecommerce;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Default Inventory Manager implementation
 */
public class DefaultInventoryManager implements InventoryManager {
    private final Map<String, Integer> inventory;
    private final Map<String, Integer> reservations;
    
    public DefaultInventoryManager() {
        this.inventory = new ConcurrentHashMap<>();
        this.reservations = new ConcurrentHashMap<>();
        
        // Initialize with sample inventory
        inventory.put("LAPTOP001", 50);
        inventory.put("PHONE001", 100);
        inventory.put("HEADPHONES001", 75);
        inventory.put("TABLET001", 30);
        inventory.put("WATCH001", 25);
        inventory.put("CAMERA001", 15);
        inventory.put("SPEAKER001", 40);
        inventory.put("KEYBOARD001", 60);
    }
    
    @Override
    public boolean isAvailable(String productId, int quantity) {
        int available = getAvailableStock(productId);
        return available >= quantity;
    }
    
    @Override
    public void reserveInventory(String productId, int quantity) {
        if (!isAvailable(productId, quantity)) {
            throw new IllegalStateException("Insufficient inventory to reserve");
        }
        
        reservations.merge(productId, quantity, Integer::sum);
        System.out.println("📦 Reserved " + quantity + " units of " + productId);
    }
    
    @Override
    public void releaseReservation(String productId, int quantity) {
        int currentReservation = reservations.getOrDefault(productId, 0);
        int newReservation = Math.max(0, currentReservation - quantity);
        
        if (newReservation == 0) {
            reservations.remove(productId);
        } else {
            reservations.put(productId, newReservation);
        }
        
        System.out.println("📦 Released " + quantity + " units of " + productId + " from reservation");
    }
    
    @Override
    public void updateInventory(String productId, int quantityChange) {
        int currentStock = inventory.getOrDefault(productId, 0);
        int newStock = Math.max(0, currentStock + quantityChange);
        inventory.put(productId, newStock);
        
        if (quantityChange < 0) {
            System.out.println("📦 Inventory decreased: " + productId + " (" + (-quantityChange) + " units sold)");
        } else {
            System.out.println("📦 Inventory increased: " + productId + " (+" + quantityChange + " units restocked)");
        }
    }
    
    @Override
    public int getAvailableStock(String productId) {
        int totalStock = inventory.getOrDefault(productId, 0);
        int reserved = reservations.getOrDefault(productId, 0);
        return Math.max(0, totalStock - reserved);
    }
}
