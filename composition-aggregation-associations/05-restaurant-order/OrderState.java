package composition.restaurant;

/**
 * Order State enumeration
 */
public enum OrderState {
    CREATED,
    CONFIRMED,
    PROCESSING_PAYMENT,
    PAID,
    PAYMENT_FAILED,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED
}
