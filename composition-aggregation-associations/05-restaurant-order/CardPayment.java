package composition.restaurant;

import java.util.UUID;

/**
 * Card Payment Processor Implementation
 */
public class CardPayment implements PaymentProcessor {
    private final String cardNumber;
    private final String cardType;
    private boolean isAvailable;
    
    public CardPayment(String cardNumber, String cardType) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.isAvailable = true;
    }
    
    @Override
    public PaymentResult processPayment(double amount, String orderId) {
        if (!isAvailable) {
            return new PaymentResult(false, "Card payment terminal unavailable", null);
        }
        
        // Simulate card payment processing
        System.out.println("Processing " + cardType + " payment...");
        System.out.println("Card: ****-****-****-" + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Amount: $" + String.format("%.2f", amount));
        
        // Simulate processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 95% success rate
        if (Math.random() < 0.95) {
            String transactionId = "CARD_" + UUID.randomUUID().toString().substring(0, 8);
            System.out.println("Card payment approved. Transaction ID: " + transactionId);
            return new PaymentResult(true, null, transactionId);
        } else {
            return new PaymentResult(false, "Card declined", null);
        }
    }
    
    @Override
    public String getType() {
        return cardType + " Card Payment";
    }
    
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
