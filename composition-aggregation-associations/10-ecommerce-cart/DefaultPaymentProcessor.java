package composition.ecommerce;

/**
 * Default Payment Processor implementation
 */
public class DefaultPaymentProcessor implements PaymentProcessor {
    
    @Override
    public PaymentResult processPayment(PaymentMethod paymentMethod, double amount, String description) {
        System.out.println("💳 Processing payment: $" + String.format("%.2f", amount) + " via " + paymentMethod.getType());
        
        // Simulate payment processing
        try {
            Thread.sleep(1000); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate success/failure based on amount (for demo purposes)
        boolean success = amount <= 10000; // Fail for very large amounts
        
        String transactionId = "TXN_" + System.currentTimeMillis();
        String message = success ? "Payment processed successfully" : "Payment failed - amount too large";
        
        System.out.println("💳 Payment result: " + message + (success ? " (ID: " + transactionId + ")" : ""));
        
        return new PaymentResult(success, message, transactionId, amount);
    }
    
    @Override
    public String getProcessorName() {
        return "Default Payment Processor";
    }
}
