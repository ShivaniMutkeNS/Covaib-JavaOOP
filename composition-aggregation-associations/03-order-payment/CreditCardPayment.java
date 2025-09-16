package composition.order;

import java.util.UUID;

/**
 * Credit Card Payment Implementation
 */
public class CreditCardPayment implements PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;
    private final String expiryDate;
    private final String cvv;
    
    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public PaymentResult processPayment(double amount, String orderId) {
        if (!validatePaymentDetails()) {
            return new PaymentResult(false, "Invalid credit card details", null);
        }
        
        // Simulate payment processing
        System.out.println("Processing credit card payment...");
        System.out.println("Card: ****-****-****-" + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Amount: $" + String.format("%.2f", amount + getTransactionFee(amount)));
        
        // Simulate network call delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 95% success rate simulation
        if (Math.random() < 0.95) {
            String transactionId = "CC_" + UUID.randomUUID().toString().substring(0, 8);
            System.out.println("Credit card payment successful. Transaction ID: " + transactionId);
            return new PaymentResult(true, null, transactionId);
        } else {
            return new PaymentResult(false, "Credit card declined by bank", null);
        }
    }
    
    @Override
    public boolean validatePaymentDetails() {
        return cardNumber != null && cardNumber.length() == 16 &&
               cardHolderName != null && !cardHolderName.trim().isEmpty() &&
               expiryDate != null && expiryDate.matches("\\d{2}/\\d{2}") &&
               cvv != null && cvv.length() == 3;
    }
    
    @Override
    public String getType() {
        return "Credit Card";
    }
    
    @Override
    public double getTransactionFee(double amount) {
        return amount * 0.029; // 2.9% transaction fee
    }
}
