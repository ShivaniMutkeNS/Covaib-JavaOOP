package composition.ecommerce;

/**
 * Cart Event Listener interface for observing cart events
 */
public interface CartEventListener {
    void onCartEvent(String cartId, String message, CartSummary summary);
}
