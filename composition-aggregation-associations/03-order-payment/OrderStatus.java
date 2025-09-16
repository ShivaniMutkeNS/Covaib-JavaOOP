package composition.order;

/**
 * Order Status enumeration
 */
public enum OrderStatus {
    CREATED,
    PROCESSING_PAYMENT,
    PAID,
    PAYMENT_FAILED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
