package composition.order;

import java.util.UUID;

/**
 * UPI Payment Implementation
 */
public class UPIPayment implements PaymentMethod {
    private final String upiId;
    private final String pin;
    
    public UPIPayment(String upiId, String pin) {
        this.upiId = upiId;
        this.pin = pin;
    }
    
    @Override
    public PaymentResult processPayment(double amount, String orderId) {
        if (!validatePaymentDetails()) {
            return new PaymentResult(false, "Invalid UPI details", null);
        }
        
        // Simulate UPI payment processing
        System.out.println("Processing UPI payment...");
        System.out.println("UPI ID: " + upiId);
        System.out.println("Amount: $" + String.format("%.2f", amount + getTransactionFee(amount)));
        
        // Simulate faster processing for UPI
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 98% success rate for UPI (higher than credit card)
        if (Math.random() < 0.98) {
            String transactionId = "UPI_" + UUID.randomUUID().toString().substring(0, 8);
            System.out.println("UPI payment successful. Transaction ID: " + transactionId);
            return new PaymentResult(true, null, transactionId);
        } else {
            return new PaymentResult(false, "UPI transaction failed - insufficient balance", null);
        }
    }
    
    @Override
    public boolean validatePaymentDetails() {
        return upiId != null && upiId.contains("@") &&
               pin != null && pin.length() >= 4;
    }
    
    @Override
    public String getType() {
        return "UPI Payment";
    }
    
    @Override
    public double getTransactionFee(double amount) {
        return 0.0; // No transaction fee for UPI
    }
}
