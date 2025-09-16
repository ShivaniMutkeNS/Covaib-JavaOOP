package composition.ecommerce;

/**
 * Payment Processor interface for payment processing
 */
public interface PaymentProcessor {
    PaymentResult processPayment(PaymentMethod paymentMethod, double amount, String description);
    String getProcessorName();
}
