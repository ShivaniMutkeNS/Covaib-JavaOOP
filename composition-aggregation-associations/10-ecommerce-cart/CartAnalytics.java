package composition.ecommerce;

/**
 * Cart Analytics data class for cart performance metrics
 */
public class CartAnalytics {
    private final CartMetrics metrics;
    private final CartSummary currentSummary;
    private final long sessionDuration;
    private final CartItem mostExpensiveItem;
    private final CartItem cheapestItem;
    private final double averageItemPrice;
    
    public CartAnalytics(CartMetrics metrics, CartSummary currentSummary, long sessionDuration,
                        CartItem mostExpensiveItem, CartItem cheapestItem, double averageItemPrice) {
        this.metrics = metrics;
        this.currentSummary = currentSummary;
        this.sessionDuration = sessionDuration;
        this.mostExpensiveItem = mostExpensiveItem;
        this.cheapestItem = cheapestItem;
        this.averageItemPrice = averageItemPrice;
    }
    
    public CartMetrics getMetrics() { return metrics; }
    public CartSummary getCurrentSummary() { return currentSummary; }
    public long getSessionDuration() { return sessionDuration; }
    public CartItem getMostExpensiveItem() { return mostExpensiveItem; }
    public CartItem getCheapestItem() { return cheapestItem; }
    public double getAverageItemPrice() { return averageItemPrice; }
}
