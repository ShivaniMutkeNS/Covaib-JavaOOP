package composition.order;

/**
 * Observer interface for order updates
 */
public interface OrderObserver {
    void onOrderUpdate(String orderId, String message, OrderStatus status);
}
