package composition.restaurant;

/**
 * Order State Listener Interface for Observer Pattern
 */
public interface OrderStateListener {
    void onStateChange(String orderId, OrderState newState, String message);
}
