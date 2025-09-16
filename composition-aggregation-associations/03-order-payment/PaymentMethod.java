package composition.order;

/**
 * Payment Method Strategy Interface
 */
public interface PaymentMethod {
    PaymentResult processPayment(double amount, String orderId);
    boolean validatePaymentDetails();
    String getType();
    double getTransactionFee(double amount);
}
