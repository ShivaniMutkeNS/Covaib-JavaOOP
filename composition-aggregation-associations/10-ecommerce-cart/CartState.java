package composition.ecommerce;

/**
 * Cart State enum for shopping cart lifecycle
 */
public enum CartState {
    ACTIVE,
    CHECKOUT_IN_PROGRESS,
    CHECKED_OUT,
    ABANDONED,
    EXPIRED,
    MERGED
}
