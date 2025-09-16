package composition.restaurant;

import java.util.UUID;

/**
 * Cash Payment Processor Implementation
 */
public class CashPayment implements PaymentProcessor {
    private boolean isAvailable;
    
    public CashPayment() {
        this.isAvailable = true;
    }
    
    @Override
    public PaymentResult processPayment(double amount, String orderId) {
        if (!isAvailable) {
            return new PaymentResult(false, "Cash payment not available", null);
        }
        
        // Simulate cash payment processing
        System.out.println("Processing cash payment of $" + String.format("%.2f", amount));
        System.out.println("Please collect cash from customer...");
        
        // Cash payments are always successful if available
        String transactionId = "CASH_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Cash payment received. Transaction ID: " + transactionId);
        
        return new PaymentResult(true, null, transactionId);
    }
    
    @Override
    public String getType() {
        return "Cash Payment";
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
