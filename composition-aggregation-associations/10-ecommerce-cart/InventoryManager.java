package composition.ecommerce;

/**
 * Inventory Manager interface for stock management
 */
public interface InventoryManager {
    boolean isAvailable(String productId, int quantity);
    void reserveInventory(String productId, int quantity);
    void releaseReservation(String productId, int quantity);
    void updateInventory(String productId, int quantityChange);
    int getAvailableStock(String productId);
}
